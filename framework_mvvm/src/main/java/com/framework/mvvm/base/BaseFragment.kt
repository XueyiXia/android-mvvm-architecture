package com.framework.mvvm.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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

    companion object{
        private const val TAG: String = "BaseFragment"
    }

    private lateinit var mActivity: AppCompatActivity

    private val mHandler = Handler(Looper.getMainLooper())

    lateinit var mViewModel: VM

    /**
     * 当前页面回调数据处理
     */

    private var launcherCallback: ((ActivityResult) -> Unit)? = null
    private val resultLauncher: ActivityResultLauncher<Intent> =  registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val code = result.resultCode
        val data = result.data
        val msgContent = "code = $code  ,  msg = $data "
        Log.e("launcherCallback","msgContent: ->> $msgContent")
        launcherCallback?.invoke(result)
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e(TAG, "跳转界面--->>" + this.javaClass.simpleName)
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
     *  activity结果跳转（没有参数）
     * @param className Class<out Activity>
     * @param activityResultCallback ActivityResultCallback<ActivityResult>?
     */
    open fun startActivityForResult(className: Class<out Activity>, activityResultCallback: (ActivityResult) -> Unit) {
        launcherCallback=activityResultCallback
        val intent = Intent(requireContext(), className)
        resultLauncher.launch(intent)
    }


    /**
     * activity结果跳转（有参数）
     * @param className Class<out Activity>
     * @param bundle Bundle
     * @param activityResultCallback ActivityResultCallback<ActivityResult>?
     */
    open fun startActivityForResult(className: Class<out Activity>, bundle: Bundle, activityResultCallback: (ActivityResult) -> Unit) {
        launcherCallback=activityResultCallback
        val intent = Intent(requireContext(), className)
        intent.putExtras(bundle)
        resultLauncher.launch(intent)
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
        resultLauncher.unregister()
    }
}