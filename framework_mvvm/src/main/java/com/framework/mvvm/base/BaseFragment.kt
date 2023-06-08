package com.framework.mvvm.base

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.framework.mvvm.utils.getVmClazz
import com.framework.mvvm.viewmodel.BaseViewModel

/**
 * @author: xiaxueyi
 * @date: 2022-11-29
 * @time: 10:20
 * @说明:
 */

abstract class BaseFragment<VM : BaseViewModel> :Fragment() {

    lateinit var mActivity: AppCompatActivity

    private val mHandler = Handler(Looper.getMainLooper())

    lateinit var mViewModel: VM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
         * 实例化创建ViewModel
         */
        mViewModel = createViewModel()

        /**
         * 函数入口
         */
        initView(view,savedInstanceState)

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


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as AppCompatActivity
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
    }
}