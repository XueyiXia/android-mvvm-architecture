package com.framework.mvvm.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import com.framework.mvvm.utils.getVmClazz
import com.module.utils.notNull
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
        createViewModel()

        /**
         * 函数入口
         */
        initView(window.decorView,savedInstanceState)

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
        val intent = Intent(this, className)
        startActivity(intent)
    }

    /**
     * activity跳转（有参数）
     * @param className
     */
    open fun startActivity(className: Class<out Activity>, bundle: Bundle) {
        val intent = Intent(this, className)
        intent.putExtras(bundle)
        startActivity(intent)
    }



    /**
     * activity结果跳转（没有参数），自定义回调
     * @param className Class<out Activity>
     * @param activityResultCallback ActivityResultCallback<ActivityResult>?
     */
    open fun startActivityForResult(className: Class<out Activity>, activityResultCallback: ActivityResultCallback<ActivityResult>?) {
        this.activityResultCallback=activityResultCallback
        val intent = Intent(this, className)
        resultLauncher.launch(intent)
    }



    /**
     * activity结果跳转（有参数），自定义回调
     * @param className Class<out Activity>
     * @param bundle Bundle
     * @param activityResultCallback ActivityResultCallback<ActivityResult>?
     */
    open fun startActivityForResult(className: Class<out Activity>, bundle: Bundle,activityResultCallback: ActivityResultCallback<ActivityResult>?) {
        this.activityResultCallback=activityResultCallback
        val intent = Intent(this, className)
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
     * 注册页面回调
     */
    private fun initRegisterForActivityResult(){
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),launcherCallback)
    }


    /**
     * 获取绑定的View
     * @return View
     */
    private fun getDataBinding(): View = createDataBinding()




    /**
     * hide System UI
     */
    private fun hideSystemUI(rootView: View) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, rootView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    /**
     * show System UI
     */
    private fun showSystemUI(rootView: View) {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(window,  rootView).show(WindowInsetsCompat.Type.systemBars())
    }


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