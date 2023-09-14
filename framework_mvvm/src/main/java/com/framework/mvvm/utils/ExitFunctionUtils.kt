package com.framework.mvvm.utils

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * @author: xiaxueyi
 * @date: 2023-09-14
 * @time: 09:21
 * @说明:
 */

inline fun AppCompatActivity.loadFragment(isAddToBackStack: Boolean = false,
                                          transitionPairs: Map<String, View> = mapOf(),
                                          transaction: FragmentTransaction.() -> Unit) {
    val beginTransaction = supportFragmentManager.beginTransaction()
    beginTransaction.transaction()
    for ((name, view) in transitionPairs) {
        ViewCompat.setTransitionName(view, name)
        beginTransaction.addSharedElement(view, name)
    }

    if (isAddToBackStack) beginTransaction.addToBackStack(null)
    beginTransaction.commit()
}

fun Activity.isPortrait() = requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

fun Activity.getDeviceWidth() = with(this) {
    val displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    displayMetrics.widthPixels
}

fun Activity.getDeviceHeight() = with(this) {
    val displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    displayMetrics.heightPixels
}

fun Fragment.toast(message: String, isLong: Boolean = false) {
    Toast.makeText(this.activity, message, if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.removeFragmentByTag(tag: String): Boolean {
    return removeFragment(supportFragmentManager.findFragmentByTag(tag))
}

fun AppCompatActivity.removeFragmentByID(@IdRes containerID: Int): Boolean {
    return removeFragment(supportFragmentManager.findFragmentById(containerID))
}

fun AppCompatActivity.removeFragment(fragment: Fragment?): Boolean {
    fragment?.let {
        val commit = supportFragmentManager.beginTransaction().remove(fragment).commit()
        return true
    } ?: return false
}

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}


fun Context.toast(message: String, isLong: Boolean = false) {
    Toast.makeText(this, message, if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show()
}

//fun ImageView.loadFromUrl(imageUrl: String, placeHolder: Int = R.drawable.img_placeholder) {
//
//    val requestOptions = RequestOptions()
//    requestOptions.placeholder(R.drawable.img_placeholder)
//
//    Glide.with(this.context)
//        .load(imageUrl)
//        .apply(requestOptions)
//        .into(this)
//}

inline fun ConstraintLayout.updateParams(constraintSet: ConstraintSet = ConstraintSet(), updates: ConstraintSet.() -> Unit) {
    constraintSet.clone(this)
    constraintSet.updates()
    constraintSet.applyTo(this)
}

inline fun <reified T : ViewModel> FragmentActivity.getViewModel() = ViewModelProvider(this)[T::class.java]

inline fun <reified T : ViewModel> Fragment.getViewModel() = ViewModelProvider(this)[T::class.java]

inline fun <reified T : ViewModel> Fragment.getActivityViewModel() = ViewModelProvider(this)[T::class.java]

inline fun <reified T : ViewGroup.LayoutParams> View.getParams() = this.layoutParams as T