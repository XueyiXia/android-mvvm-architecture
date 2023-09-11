package com.framework.mvvm.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.framework.mvvm.utils.ParameterizedTypeUtils
import com.framework.mvvm.utils.getVmClazz
import com.framework.mvvm.viewmodel.BaseViewModel
import java.lang.reflect.ParameterizedType

/**
 * @author: xiaxueyi
 * @date: 2022-11-29
 * @time: 10:20
 * @说明:
 */

abstract class BaseFragment<VM : BaseViewModel> :Fragment() {

    private lateinit var mActivity: AppCompatActivity

    private val mHandler = Handler(Looper.getMainLooper())

    lateinit var mViewModel: VM

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>  // registerForActivityResult

    private var activityResultCallback: ActivityResultCallback<ActivityResult>?=null  //暴露给子页面的回调


    /**
     * 当前页面回调数据处理
     */
    private val launcherCallback = ActivityResultCallback<ActivityResult> { result ->
        val code = result.resultCode
        val data = result.data
        val msgContent = "code = $code msg = $data "
        activityResultCallback?.onActivityResult(result)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
         * 实例化创建ViewModel
         */
        createViewModel()

        /**
         * 函数入口
         */
        initView(view,savedInstanceState)


        /**
         * 创建LiveData数据观察者
         */
        createObserver()


        /**
         * 实例化跳转页面回调
         */
        initRegisterForActivityResult()

    }



    /**
     * activity跳转（无参数）
     * @param className
     */
    open fun startActivity(className: Class<out Activity>) {
        val intent = Intent(requireContext(), className)
        startActivity(intent)
    }

    /**
     * activity跳转（有参数）
     * @param className
     */
    open fun startActivity(className: Class<out Activity>, bundle: Bundle) {
        val intent = Intent(requireContext(), className)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    /**
     * activity结果跳转（没有参数）
     * @param className
     * @param requestCode
     */
    open fun startActivityForResult(className: Class<out Activity>, activityResultCallback: ActivityResultCallback<ActivityResult>?) {
        this.activityResultCallback=activityResultCallback
        val intent = Intent(requireContext(), className)
        resultLauncher.launch(intent)
    }




    /**
     * activity结果跳转（有参数）
     * @param className
     * @param bundle
     * @param requestCode
     */
    open fun startActivityForResult(className: Class<out Activity>, bundle: Bundle, activityResultCallback: ActivityResultCallback<ActivityResult>?) {
        this.activityResultCallback=activityResultCallback
        val intent = Intent(requireContext(), className)
        intent.putExtras(bundle)
        resultLauncher.launch(intent)
    }


    /**
     * 注册页面回调
     */
    private fun initRegisterForActivityResult(){
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),launcherCallback)
    }


    /**
     * 创建ViewModel
     * @return VM
     */
    private fun createViewModel() {
        val modelClass :Class<VM> = getVmClazz(this)
        mViewModel= ViewModelProvider(this)[modelClass]
    }

    /**
     * 获取绑定的View
     * @return View
     */
    private fun getDataBinding(): View = createDataBinding()


    /**
     * 上下文对象
     * @return AppCompatActivity
     */
    fun activity()=mActivity



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