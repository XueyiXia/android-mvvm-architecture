package com.framework.mvvm.lifecycle

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.framework.mvvm.livedata.BooleanLiveData



/**
 * @author: xiaxueyi
 * @date: 2022-12-30
 * @time: 13:22
 * @说明:
 */

object KotlinAppLifeObserver : DefaultLifecycleObserver {
    private const val TAG = "KotlinAppLifeObserver"

    var isForeground = BooleanLiveData()


    override fun onStart(owner: LifecycleOwner) {
        Log.e(TAG, "LifecycleObserver-> onStart :${owner}")
        super.onStart(owner)
        //在前台
        isForeground.value = true
    }

    override fun onStop(owner: LifecycleOwner) {
        Log.e(TAG, "LifecycleObserver-> onStop :${owner.lifecycle}")
        super.onStop(owner)
        //在后台
        isForeground.value = false
    }

}