package com.framework.mvvm.base

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.ViewDataBinding
import com.framework.mvvm.R
import com.google.android.material.snackbar.Snackbar
import kotlin.system.exitProcess


/**
 * @author: xiaxueyi
 * @date: 2022-11-29
 * @time: 10:20
 * @说明:
 */

abstract class BaseActivity <VB : ViewDataBinding> : AppCompatActivity(){
    companion object{
        private const val TAG = "BaseActivity"
        var exitTime: Long = 0 //退出程序的时间
    }

    private var _binding: VB? = null
    val mBinding: VB get() = _binding ?: throw IllegalStateException("it is not initialized or null.")


    /**
     * 当前页面回调数据处理
     */
    private var launcherCallback: ((ActivityResult) -> Unit)? = null
    private val resultLauncher: ActivityResultLauncher<Intent> =  registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val code = result.resultCode
        val data = result.data
        val msgContent = "result = $result   ，  code = $code  ,  msg = $data  "
        Log.e("launcherCallback","msgContent: ->> $msgContent")
        launcherCallback?.invoke(result)
    }


    // The permissions we need for the app to work properly
    private val permissions = mutableListOf(
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.READ_EXTERNAL_STORAGE,
    ).apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            add(Manifest.permission.ACCESS_MEDIA_LOCATION)
        }
    }

    private val permissionRequest = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        if (permissions.all { it.value }) {
            onPermissionGranted()
        } else {
            Snackbar.make(findViewById(android.R.id.content), R.string.message_no_permissions, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.label_ok) {
//                    ActivityCompat.finishAffinity(this)
                }
                .show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG, "跳转界面（BaseActivityNew）--->>" + this.javaClass.simpleName)
        /**
         * 绑定UI
         */
        _binding = bindDataBinding()
        mBinding.lifecycleOwner = this
        setContentView(mBinding.root)


        /**
         * 函数入口
         */
        initView(mBinding.root,savedInstanceState)


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
    open fun startActivityForResult(className: Class<out Activity>, activityResultCallback: (ActivityResult) -> Unit) {
        this.launcherCallback=activityResultCallback
        val intent = Intent(this, className)
        resultLauncher.launch(intent)
    }



    /**
     * activity结果跳转（有参数），自定义回调
     * @param className Class<out Activity>
     * @param bundle Bundle
     * @param activityResultCallback ActivityResultCallback<ActivityResult>?
     */
    open fun startActivityForResult(className: Class<out Activity>, bundle: Bundle,activityResultCallback: (ActivityResult) -> Unit) {
        this.launcherCallback=activityResultCallback
        val intent = Intent(this, className)
        intent.putExtras(bundle)
        resultLauncher.launch(intent)
    }

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
     * 检查权限
     */
    private fun checkPermissionGranted(){
        if (allPermissionsGranted()) {
            onPermissionGranted()
        } else {
            permissionRequest.launch(permissions.toTypedArray())
        }
    }


    /**
     * Check for the permissions
     */
    private fun allPermissionsGranted() = permissions.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * 退出应用
     */
   open fun exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            exitTime = System.currentTimeMillis()
        } else {
            try {
                System.gc()
                finish()
                exitProcess(0)
            } catch (e: RuntimeException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * A function which will be called after the permission check
     * */
    open fun onPermissionGranted() = Unit


    // 显示 Snackbar 的方法
    protected fun showSnackbar(message: String, anchorView: View? = null) {
        val view = anchorView ?: findViewById(android.R.id.content)
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }


    /*****************************************************************华丽的分割线***************************************************************************/

    protected abstract fun bindDataBinding(): VB

    /**
     * 入口函数
     * @param rootView View
     * @param savedInstanceState Bundle?
     */
    protected abstract fun initView(rootView: View, savedInstanceState: Bundle?)


    override fun onDestroy() {
        super.onDestroy()
        resultLauncher.unregister()
        mBinding.unbind()
        _binding?.unbind()
        System.gc()
    }

}