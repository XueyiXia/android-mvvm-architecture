package com.jetpack.mvvm.model

import android.content.Context
import android.content.SharedPreferences
import com.github.premnirmal.ticker.model.FetchException
import com.jetpack.mvvm.AppPreferences
import com.jetpack.mvvm.network.StocksApi
import com.jetpack.mvvm.network.data.Holding
import com.jetpack.mvvm.network.data.Position
import com.jetpack.mvvm.network.data.Quote
import com.jetpack.mvvm.repo.StocksStorage
import com.module.utils.AppClock
import com.module.utils.AppClock.AppClockImpl
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.threeten.bp.DayOfWeek
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.TextStyle.SHORT
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by premnirmal on 2/28/16.
 */
@Singleton
class StocksProvider @Inject constructor(
    @ApplicationContext private val context: Context,
    private val api: StocksApi,
    private val preferences: SharedPreferences,
    private val clock: AppClock,
    private val appPreferences: AppPreferences,
    private val alarmScheduler: AlarmScheduler,
    private val storage: StocksStorage,
    private val coroutineScope: CoroutineScope
) {

  companion object {
    private const val LAST_FETCHED = "LAST_FETCHED"
    private const val NEXT_FETCH = "NEXT_FETCH"
    private val DEFAULT_STOCKS = arrayOf("^GSPC", "^DJI", "GOOG", "AAPL", "MSFT")
    const val DEFAULT_INTERVAL_MS: Long = 15_000L
  }

  private val tickerSet: MutableSet<String> = HashSet()
  private val quoteMap: MutableMap<String, Quote> = HashMap()
  private val _fetchState = MutableStateFlow<FetchState>(FetchState.NotFetched)
  private val _nextFetch = MutableStateFlow<Long>(0)
  private var lastFetched = 0L
  private val _tickers = MutableStateFlow<List<String>>(emptyList())
  private val _portfolio = MutableStateFlow<List<Quote>>(emptyList())

  init {
    val tickers = storage.readTickers()
    this.tickerSet.addAll(tickers)
    if (this.tickerSet.isEmpty()) {
      this.tickerSet.addAll(DEFAULT_STOCKS)
    }
    _tickers.tryEmit(tickerSet.toList())
    val lastFetchedLoaded = preferences.getLong(LAST_FETCHED, 0L)
    lastFetched = lastFetchedLoaded
    val nextFetch = preferences.getLong(NEXT_FETCH, 0L)
    _nextFetch.tryEmit(nextFetch)
    runBlocking { fetchLocal() }
    if (lastFetched == 0L) {
      coroutineScope.launch {
        fetch()
      }
    } else {
      _fetchState.tryEmit(FetchState.Success(lastFetched))
    }
    schedule()
  }

  private suspend fun fetchLocal() = withContext(Dispatchers.IO) {
      try {
        val quotes = storage.readQuotes()
        synchronized(quoteMap) {
          quotes.forEach { quoteMap[it.symbol] = it }
        }
        _portfolio.emit(quoteMap.values.filter { tickerSet.contains(it.symbol) }.toList())
      } catch (e: Exception) {
        Timber.w(e)
      }
  }

  private fun saveLastFetched() {
    preferences.edit()
        .putLong(LAST_FETCHED, lastFetched)
        .apply()
  }

  private fun saveTickers() {
    storage.saveTickers(tickerSet)
  }

  private fun scheduleUpdate() {
    scheduleUpdateWithMs(alarmScheduler.msToNextAlarm(lastFetched))
  }

  private fun scheduleUpdateWithMs(
    msToNextAlarm: Long
  ) {
    val updateTime = alarmScheduler.scheduleUpdate(msToNextAlarm, context)
    _nextFetch.tryEmit(updateTime.toInstant().toEpochMilli())
    preferences.edit()
        .putLong(NEXT_FETCH, updateTime.toInstant().toEpochMilli())
        .apply()
    appPreferences.setRefreshing(false)
  }

  private suspend fun fetchStockInternal(ticker: String, allowCache: Boolean): FetchResult<Quote> = withContext(Dispatchers.IO){
      val quote = if (allowCache) quoteMap[ticker] else null
      return@withContext quote?.let { FetchResult.Companion.success(quote) } ?: run {
        try {
          val res = api.getStock(ticker)
          if (res.wasSuccessful) {
            val data = res.data
            val quoteFromStorage = storage.readQuote(ticker)
            val quote = quoteFromStorage?.let {
              it.copyValues(data)
              quoteFromStorage
            } ?: data
            quoteMap[ticker] = quote
            FetchResult.Companion.success(quote)
          } else {
            FetchResult.Companion.failure<Quote>(FetchException("Failed to fetch", res.error))
          }
        } catch (ex: CancellationException) {
          // ignore
          FetchResult.Companion.failure<Quote>(FetchException("Failed to fetch", ex))
        } catch (ex: Exception) {
          Timber.w(ex)
          FetchResult.Companion.failure<Quote>(FetchException("Failed to fetch", ex))
        }
      }
    }

  /////////////////////
  // public api
  /////////////////////

  fun hasTicker(ticker: String): Boolean {
    return tickerSet.contains(ticker)
  }

  suspend fun fetch(allowScheduling: Boolean = true): FetchResult<List<Quote>> = withContext(Dispatchers.IO) {
    if (tickerSet.isEmpty()) {
      if (allowScheduling) {
        _fetchState.emit(FetchState.Failure(FetchException("No symbols in portfolio")))
      }
      FetchResult.Companion.failure<List<Quote>>(FetchException("No symbols in portfolio"))
    } else {
      return@withContext try {
        if (allowScheduling) {
          appPreferences.setRefreshing(true)
        }
        val fr = api.getStocks(tickerSet.toList())
        if (fr.hasError) {
          throw fr.error
        }
        val fetchedStocks = fr.data
        if (fetchedStocks.isEmpty()) {
          return@withContext FetchResult.Companion.failure<List<Quote>>(FetchException("Refresh failed"))
        } else {
          synchronized(tickerSet) {
            tickerSet.addAll(fetchedStocks.map { it.symbol })
          }
          _tickers.emit(tickerSet.toList())
          // clean up existing tickers
          tickerSet.toSet().forEach { ticker ->
          }
          storage.saveQuotes(fetchedStocks)
          fetchLocal()
          if (allowScheduling) {
            lastFetched = clock.currentTimeMillis()
            _fetchState.emit(FetchState.Success(lastFetched))
            saveLastFetched()
            scheduleUpdate()
          }
          FetchResult.Companion.success(quoteMap.values.filter { tickerSet.contains(it.symbol) }.toList())
        }
      } catch(ex: CancellationException) {
        FetchResult.Companion.failure<List<Quote>>(FetchException("Failed to fetch", ex))
      } catch (ex: Throwable) {
        Timber.w(ex)
        FetchResult.Companion.failure<List<Quote>>(FetchException("Failed to fetch", ex))
      } finally {
        appPreferences.setRefreshing(false)
      }
    }
  }

  fun schedule() {
    scheduleUpdate()
    alarmScheduler.enqueuePeriodicRefresh()
  }

  fun addStock(ticker: String): Collection<String> {
    synchronized(quoteMap) {
      if (!tickerSet.contains(ticker)) {
        tickerSet.add(ticker)
        val quote = Quote(symbol = ticker)
        quoteMap[ticker] = quote
        saveTickers()
      }
    }
    _tickers.tryEmit(tickerSet.toList())
    _portfolio.tryEmit(quoteMap.values.filter { tickerSet.contains(it.symbol) }.toList())
    coroutineScope.launch {
      val result = fetchStockInternal(ticker, false)
      if (result.wasSuccessful) {
        val data = result.data
        quoteMap[ticker] = data
        storage.saveQuote(result.data)
        _portfolio.emit(quoteMap.values.filter { tickerSet.contains(it.symbol) }.toList())
      }
    }
    return tickerSet
  }

  fun hasPositions(): Boolean = quoteMap.filter { it.value.hasPositions() }.isNotEmpty()

  fun hasPosition(ticker: String): Boolean = quoteMap[ticker]?.hasPositions() ?: false

  fun getPosition(ticker: String): Position? = quoteMap[ticker]?.position

  suspend fun addHolding(
    ticker: String,
    shares: Float,
    price: Float
  ): Holding {
    val quote: Quote?
    var position: Position
    synchronized(quoteMap) {
      quote = quoteMap[ticker]
      position = getPosition(ticker) ?: Position(ticker)
      if (!tickerSet.contains(ticker)) {
        tickerSet.add(ticker)
      }
    }
    _tickers.emit(tickerSet.toList())
    saveTickers()
    val holding = Holding(ticker, shares, price)
    position.add(holding)
    quote?.position = position
    val id = storage.addHolding(holding)
    holding.id = id
    _portfolio.emit(quoteMap.values.filter { tickerSet.contains(it.symbol) }.toList())
    return holding
  }

  suspend fun removePosition(
    ticker: String,
    holding: Holding
  ) {
    synchronized(quoteMap) {
      val position = getPosition(ticker)
      val quote = quoteMap[ticker]
      position?.remove(holding)
      quote?.position = position
    }
    storage.removeHolding(ticker, holding)
    _portfolio.emit(quoteMap.values.filter { tickerSet.contains(it.symbol) }.toList())
  }

  fun addStocks(symbols: Collection<String>): Collection<String> {
    synchronized(this.tickerSet) {
      val filterNot = symbols.filterNot { this.tickerSet.contains(it) }
      filterNot.forEach { this.tickerSet.add(it) }
      saveTickers()
      if (filterNot.isNotEmpty()) {
        coroutineScope.launch {
          fetch()
        }
      }
    }
    _tickers.tryEmit(tickerSet.toList())
    _portfolio.tryEmit(quoteMap.values.filter { tickerSet.contains(it.symbol) }.toList())
    return this.tickerSet
  }

  suspend fun removeStock(ticker: String): Collection<String> {
    synchronized(quoteMap) {
      tickerSet.remove(ticker)
      saveTickers()
      quoteMap.remove(ticker)
    }
    storage.removeQuoteBySymbol(ticker)
    _tickers.emit(tickerSet.toList())
    _portfolio.emit(quoteMap.values.filter { tickerSet.contains(it.symbol) }.toList())
    return tickerSet
  }

  suspend fun removeStocks(symbols: Collection<String>) {
    synchronized(quoteMap) {
      symbols.forEach {
        tickerSet.remove(it)
        quoteMap.remove(it)
      }
    }
    storage.removeQuotesBySymbol(symbols.toList())
    _tickers.emit(tickerSet.toList())
    _portfolio.emit(quoteMap.values.filter { tickerSet.contains(it.symbol) }.toList())
    saveTickers()
  }

  suspend fun fetchStock(ticker: String, allowCache: Boolean = true): FetchResult<Quote> {
    return fetchStockInternal(ticker, allowCache)
  }

  fun getStock(ticker: String): Quote? = quoteMap[ticker]

  val tickers: StateFlow<List<String>>
    get() = _tickers

  val portfolio: StateFlow<List<Quote>>
    get() = _portfolio//quoteMap.filter { widgetDataProvider.containsTicker(it.key) }.map { it.value }

  fun addPortfolio(portfolio: List<Quote>) {
    synchronized(quoteMap) {
      portfolio.forEach {
        val symbol = it.symbol
        if (!tickerSet.contains(symbol)) tickerSet.add(symbol)
        quoteMap[symbol] = it
      }
    }
    saveTickers()
    coroutineScope.launch {
      storage.saveQuotes(portfolio)
      fetchLocal()
      fetch()
    }
  }

  val fetchState: StateFlow<FetchState>
    get() = _fetchState

  val nextFetchMs: StateFlow<Long>
    get() = _nextFetch

  sealed class FetchState {
    abstract val displayString: String

    object NotFetched : FetchState() {
      override val displayString: String = "--"
    }

    class Success(val fetchTime: Long) : FetchState() {
      override val displayString: String by lazy {
        val instant = Instant.ofEpochMilli(fetchTime)
        val time = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault())
        time.createTimeString()
      }
    }

    class Failure(val exception: Throwable) : FetchState() {
      override val displayString: String by lazy {
        exception.message.orEmpty()
      }
    }
  }
}


fun ZonedDateTime.createTimeString(): String {
  val fetched: String
  val fetchedDayOfWeek = dayOfWeek.value
  val today = AppClockImpl.todayZoned().dayOfWeek.value
  fetched = if (today == fetchedDayOfWeek) {
    AppPreferences.TIME_FORMATTER.format(this)
  } else {
    val day: String = DayOfWeek.from(this)
      .getDisplayName(SHORT, Locale.getDefault())
    val timeStr: String = AppPreferences.TIME_FORMATTER.format(this)
    "$timeStr $day"
  }
  return fetched
}
