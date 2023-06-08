package com.framework.mvvm.base

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.framework.mvvm.utils.getVmClazz
import com.framework.mvvm.utils.notNull
import com.framework.mvvm.viewmodel.BaseViewModel

/**
 * @author: xiaxueyi
 * @date: 2022-11-29
 * @time: 10:20
 * @说明:
 */

abstract class BaseActivity <VM : BaseViewModel> : AppCompatActivity(){
    companion object{
        private const val TAG = "BaseActivity"
    }

    lateinit var mViewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG, "跳转界面--->>" + this.javaClass.simpleName)

        /**
         * 绑定UI
         */
        getDataBinding().notNull({
            setContentView(it)
        })

        /**
         * 实例化创建ViewModel
         */
        mViewModel = createViewModel()

        /**
         * 函数入口
         */
        initView(window.decorView,savedInstanceState)

        /**
         * 创建LiveData数据观察者
         */
        createObserver()
    }

    /**
     * 创建ViewModel
     * @return VM
     */
    private fun createViewModel(): VM {
        return ViewModelProvider(this)[getVmClazz(this)]
    }

    /**
     * 获取绑定的View
     * @return View
     */
    private fun getDataBinding(): View = createDataBinding()

    /**
     * 创建DataBinding
     * @return View
     */
    abstract fun createDataBinding(): View

    /**
     * 创建LiveData数据观察者
     */
    abstract fun createObserver()

    /**
     * 入口函数
     * @param rootView View
     * @param savedInstanceState Bundle?
     */
    abstract fun initView(rootView: View, savedInstanceState: Bundle?)




}