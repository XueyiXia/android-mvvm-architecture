package com.framework.mvvm.base

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.framework.mvvm.utils.getVmClazz
import com.framework.mvvm.utils.inflateBindingWithGeneric
import com.framework.mvvm.viewmodel.BaseViewModel

/**
 * @author: xiaxueyi
 * @date: 2022-11-29
 * @time: 10:20
 * @说明:
 */

abstract class BaseMvvmActivity <DB: ViewDataBinding,VM: BaseViewModel> : BaseActivity<DB,VM>(){

    companion object{
        private const val TAG = "BaseMvvmActivity"
    }

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        Log.e(TAG, "BaseMvvmActivity--->>" + this.javaClass.simpleName)
    }

    override fun createObserver() {
        Log.e(TAG, "createObserver--->>${mViewModel}" )
    }

}