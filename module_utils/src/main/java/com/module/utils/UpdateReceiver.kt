package com.module.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class UpdateReceiver : BroadcastReceiver() {
//
//  @Inject internal lateinit var stocksProvider: StocksProvider
//  @Inject internal lateinit var coroutineScope: CoroutineScope

  override fun onReceive(
      context: Context,
      intent: Intent
  ) {
//    Injector.appComponent().inject(this)
//    val pendingResult = goAsync()
//    coroutineScope.launch(Dispatchers.Main) {
//      stocksProvider.fetch()
//      pendingResult.finish()
//    }
  }
}