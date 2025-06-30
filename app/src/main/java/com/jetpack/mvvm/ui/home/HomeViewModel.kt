package com.jetpack.mvvm.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.framework.mvvm.livedata.ListLiveData
import com.jetpack.mvvm.R
import com.jetpack.mvvm.model.Menu
import com.jetpack.mvvm.model.News
import com.jetpack.mvvm.model.StocksProvider
import com.jetpack.mvvm.network.data.Quote
import com.jetpack.mvvm.ui.home.HomeViewModel.SettingsUiState.Loading
import com.jetpack.mvvm.ui.home.HomeViewModel.SettingsUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class HomeViewModel @Inject constructor(  private val stocksProvider: StocksProvider) : ViewModel() {

    val portfolio: LiveData<List<Quote>> by lazy {
        stocksProvider.portfolio.asLiveData()
    }

    private val listMenu = ListLiveData<Menu>()
    private val listNews = ListLiveData<News>()



    val settingsUiState: StateFlow<SettingsUiState> =
        listMenu.map { userData ->
                Success(
                    "Success"
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = WhileSubscribed(5.seconds.inWholeMilliseconds),
                initialValue = Loading,
            )

    sealed interface SettingsUiState {
        data object Loading : SettingsUiState
        data class Success(val settings: Any) : SettingsUiState
    }


    /********************************************************************************************************/

    fun fetchPortfolioInRealTime() {
        viewModelScope.launch(Dispatchers.Default) {
            do {
                var isMarketOpen = false
                val result = stocksProvider.fetch(false)
                if (result.wasSuccessful) {
                    isMarketOpen = result.data.any { it.isMarketOpen }
                }
                delay(StocksProvider.DEFAULT_INTERVAL_MS)
            } while (result.wasSuccessful && isMarketOpen)
        }
    }

    fun getListMenu(): ListLiveData<Menu> {
        listMenu.value =  listOf(
            Menu(id = 1, name = R.string.menu_item_1, color = R.color.lightTeal),
            Menu(id = 1, name = R.string.menu_item_2, color = R.color.lightRed),
            Menu(id = 1, name = R.string.menu_item_3, color = R.color.lightBlue),
            Menu(id = 1, name = R.string.menu_item_4, color = R.color.lightYellow),
            Menu(id = 1, name = R.string.menu_item_5, color = R.color.lightPurple),
            Menu(id = 1, name = R.string.menu_item_6, color = R.color.lightBrown)
        )
        return listMenu
    }

    fun getListNews(): LiveData<List<News>> {
        listNews.value =  listOf(
            News(),
            News(),
            News(),
            News(),
            News(),
            News(),
            News(),
            News()
        )
        return listNews
    }
}
