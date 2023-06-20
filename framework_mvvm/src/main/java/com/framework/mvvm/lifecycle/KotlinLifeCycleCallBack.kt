package com.framework.mvvm.lifecycle

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log


/**
 * @author: xiaxueyi
 * @date: 2022-12-30
 * @time: 13:22
 * @说明:
 */

class KotlinLifeCycleCallBack : Application.ActivityLifecycleCallbacks {
    companion object{
        private const val TAG = "KotlinLifeCycleCallBack"
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        KotlinActivityManger.pushActivity(activity)
        Log.e(TAG, "onActivityCreated--->>   Activity : $activity")
    }
    override fun onActivityStarted(activity: Activity) {
        Log.e(TAG, "onActivityStarted--->>   Activity : $activity")
    }

    override fun onActivityResumed(activity: Activity) {
        Log.e(TAG, "onActivityResumed--->>   Activity : $activity")
    }

    override fun onActivityPaused(activity: Activity) {
        Log.e(TAG, "onActivityPaused--->>   Activity : $activity")
    }


    override fun onActivityDestroyed(activity: Activity) {
        Log.e(TAG, "onActivityDestroyed--->>   Activity : $activity")
        KotlinActivityManger.popActivity(activity)
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        Log.e(TAG, "onActivitySaveInstanceState--->>   Activity : $activity")
    }

    override fun onActivityStopped(activity: Activity) {
        Log.e(TAG, "onActivityStopped--->>   Activity : $activity")
    }


}