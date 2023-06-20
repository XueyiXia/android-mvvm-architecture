package com.framework.mvvm.lifecycle

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner


/**
 * @author: xiaxueyi
 * @date: 2022-12-30
 * @time: 13:22
 * @说明:
 */

class KotlinHandler(lifecycleOwner: LifecycleOwner, callback: Callback) : Handler(Looper.getMainLooper(),callback),
    LifecycleEventObserver {

    private val mLifecycleOwner: LifecycleOwner = lifecycleOwner

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
       if (event==Lifecycle.Event.ON_DESTROY){
           removeCallbacksAndMessages(null)
           mLifecycleOwner.lifecycle.removeObserver(this)
       }
    }
}