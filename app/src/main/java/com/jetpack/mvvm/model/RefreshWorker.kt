package com.github.premnirmal.ticker.model

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.jetpack.mvvm.Injector
import com.jetpack.mvvm.model.StocksProvider
import com.module.utils.isNetworkOnline
import javax.inject.Inject

class RefreshWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

  companion object {
    const val TAG = "RefreshWorker"
    const val TAG_PERIODIC = "RefreshWorker_Periodic"
  }

  @Inject internal lateinit var stocksProvider: StocksProvider

  override suspend fun doWork(): Result {
    return if (applicationContext.isNetworkOnline()) {
      Injector.appComponent().inject(this)
      val result = stocksProvider.fetch()
      if (result.hasError) {
        Result.retry()
      } else {
        Result.success()
      }
    } else {
      Result.retry()
    }
  }
}