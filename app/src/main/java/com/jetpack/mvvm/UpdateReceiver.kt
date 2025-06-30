package com.jetpack.mvvm

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.jetpack.mvvm.model.StocksProvider
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpdateReceiver : BroadcastReceiver() {
//
  @Inject internal lateinit var stocksProvider: StocksProvider
  @Inject internal lateinit var coroutineScope: CoroutineScope

  @SuppressLint("UnsafeProtectedBroadcastReceiver")
  override fun onReceive(
      context: Context,
      intent: Intent
  ) {
    Injector.appComponent().inject(this)
    val pendingResult = goAsync()
    coroutineScope.launch(Dispatchers.Main) {
      stocksProvider.fetch()
      pendingResult.finish()
    }
  }
}