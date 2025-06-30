package com.jetpack.mvvm

import com.github.premnirmal.ticker.model.RefreshWorker
import com.google.gson.Gson
import com.jetpack.mvvm.UpdateReceiver
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Created by premnirmal on 3/3/16.
 */

interface LegacyComponent {
  fun gson(): Gson




  fun inject(receiver: RefreshReceiver)
  fun inject(receiver: UpdateReceiver)
  fun inject(refreshWorker: RefreshWorker)
}

@InstallIn(SingletonComponent::class)
@EntryPoint
interface AppEntryPoint : LegacyComponent
