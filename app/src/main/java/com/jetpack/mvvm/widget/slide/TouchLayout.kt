package com.jetpack.mvvm.widget.slide

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.FrameLayout

/**
 * @author: xiaxueyi
 * @date: 2024-12-27
 * @time: 16:08
 * @说明:
 */

class TouchLayout : FrameLayout {
    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet?): super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    companion object{
        private const val TAG="TouchLayout"
    }

    override fun dispatchTouchEvent(e: MotionEvent): Boolean{
        Log.d(TAG,"dispatchTouchEvent ")
        when(e.actionMasked){
            MotionEvent.ACTION_DOWN->{

            }

            MotionEvent.ACTION_MOVE->{

            }

            MotionEvent.ACTION_UP->{

            }
        }
        return super.dispatchTouchEvent(e)
//        return true
    }

    override fun onInterceptTouchEvent(e: MotionEvent): Boolean{
        Log.d(TAG,"onInterceptTouchEvent  ")
        return super.onInterceptTouchEvent(e)
    }

    override fun onTouchEvent(e: MotionEvent): Boolean{
        Log.d(TAG,"onTouchEvent  ")
        return super.onTouchEvent(e)
    }
}