package com.jetpack.mvvm

import dagger.hilt.EntryPoints

/**
 * Created by premnirmal on 2/26/16.
 */
object Injector {

  private lateinit var app: AppLoader

  fun init(app: AppLoader) {
    this.app = app
  }

  fun appComponent(): AppEntryPoint {
    return EntryPoints.get(app, AppEntryPoint::class.java)
  }
}