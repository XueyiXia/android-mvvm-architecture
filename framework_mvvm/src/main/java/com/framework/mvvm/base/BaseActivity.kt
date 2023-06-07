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

abstract class BaseActivity <viewDataBinding: ViewDataBinding,viewMode: BaseViewModel> : AppCompatActivity(){

    companion object{
        private const val TAG = "BaseActivity"
    }

    lateinit var mViewDataBinding:viewDataBinding

    lateinit var mViewModel:viewMode

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.e(TAG, "跳转界面--->>" + this.javaClass.simpleName)

        mViewDataBinding = inflateBindingWithGeneric(layoutInflater)

        /**
         * 绑定Ui
         */
        setContentView(mViewDataBinding.root)

        /**
         * 创建ViewModel
         */
        mViewModel = createViewModel()


        /**
         * 函数入口
         */
        initView(mViewDataBinding.root, savedInstanceState)


        /**
         * 创建LiveData数据观察者
         */
        createObserver()
    }


    /**
     * 创建ViewModel
     * @return VM
     */
    private fun createViewModel(): viewMode {
        return ViewModelProvider(this)[getVmClazz(this)]
    }


    /**
     * 创建LiveData数据观察者
     */
    abstract fun createObserver()

    /**
     * 入口函数
     */
    abstract fun initView(rootView: View, savedInstanceState: Bundle?)
}