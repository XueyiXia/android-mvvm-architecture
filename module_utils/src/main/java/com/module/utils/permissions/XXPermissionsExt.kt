
package com.module.utils.permissions

import android.app.Activity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.module.utils.permissions.interfac.OnPermissionResult
import com.hjq.permissions.IPermissionInterceptor
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.module.utils.permissions.interfac.OnPermissionsDoNotAskAgain
import com.module.utils.permissions.interfac.OnPermissionsShouldShowRationale
import com.hjq.permissions.OnPermissionPageCallback
import com.module.utils.permissions.interfac.OnUserResultCallback

/**
 *
 * @property activity Activity
 * @property permissionList MutableList<String>
 * @property onResult OnPermissionResult?
 * @property onShouldShowRationale OnPermissionsShouldShowRationale?
 * @property onDoNotAskAgain OnPermissionsDoNotAskAgain?
 * @constructor
 */
class XXPermissionsExt private constructor(private val activity: Activity) {
    private val permissionList = mutableListOf<String>()
    private var onResult: OnPermissionResult? = null
    private var onShouldShowRationale: OnPermissionsShouldShowRationale? = null
    private var onDoNotAskAgain: OnPermissionsDoNotAskAgain? = null


    companion object {
        @JvmStatic
        fun with(activity: Activity): XXPermissionsExt {
            return XXPermissionsExt(activity)
        }

        @JvmStatic
        fun with(fragment: Fragment): XXPermissionsExt {
            return with(fragment.requireActivity())
        }
    }

    /**
     * add permissions
     */
    fun permissions(vararg permissions: String): XXPermissionsExt {
        permissionList.addAll(permissions)
        return this
    }

    /**
     * add permissions
     */
    @JvmName("permissionsArray")
    fun permissions(permissions: Array<out String>): XXPermissionsExt {
        permissionList.addAll(permissions)
        return this
    }

    /**
     * add permissions
     */
    fun permissions(permissions: List<String>): XXPermissionsExt {
        permissionList.addAll(permissions)
        return this
    }



    /**
     * 拒绝权限后，回调此方法
     * @param onDoNotAskAgain OnPermissionsDoNotAskAgain
     * @return XXPermissionsExt
     */
    fun onDoNotAskAgain(onDoNotAskAgain: OnPermissionsDoNotAskAgain): XXPermissionsExt {
        this.onDoNotAskAgain = onDoNotAskAgain
        return this
    }

    /**
     * 需要显示请求权限理由时调用
     * @param onShouldShowRationale OnPermissionsShouldShowRationale
     * @return XXPermissionsExtensionsKt
     */
    fun onShouldShowRationale(onShouldShowRationale: OnPermissionsShouldShowRationale): XXPermissionsExt {
        this.onShouldShowRationale = onShouldShowRationale
        return this
    }

    /**
     * Callback for the permissions request result.
     */
    fun onResult(onResult: OnPermissionResult): XXPermissionsExt {
        this.onResult = onResult
        return this
    }


    /**
     * 发起权限请求
     */
    fun request() {
        return XXPermissions.with(activity)
            .permission(permissionList)
            .interceptor(object : IPermissionInterceptor {

                override fun launchPermissionRequest(
                    activity: Activity,
                    allList: List<String>,
                    callback: OnPermissionCallback?
                ) {
                    val showRationale = onShouldShowRationale ?: return super.launchPermissionRequest(activity, allList, callback)
                    val rationalePermissions = allList.filter {
                        try {
                            ActivityCompat.shouldShowRequestPermissionRationale(activity, it)
                        } catch (e: Exception) {
                            false
                        }
                    }
                    if (rationalePermissions.isEmpty()) return super.launchPermissionRequest(activity, allList, callback)

                    showRationale.onShouldShowRationale(rationalePermissions) { isAgree ->
                        if (isAgree) {
                            super.launchPermissionRequest(activity, allList, callback)
                        } else {
                            val newDenied = XXPermissions.getDenied(activity, allList)
                            callback?.onGranted(allList - newDenied.toSet(), false)
                            callback?.onDenied(newDenied, false)
                        }
                    }
                }

                override fun deniedPermissionRequest(
                        activity: Activity,
                        allList: List<String>,
                        deniedList: List<String>,
                        doNotAskAgain: Boolean,
                        callback: OnPermissionCallback?
                    ) {
                        if (doNotAskAgain) {
                            showPermissionSettingDialog(activity, allList, deniedList, callback)
                        } else {
                            val newDenied = XXPermissions.getDenied(activity, allList)
                            callback?.onGranted(allList - newDenied.toSet(), false)
                            callback?.onDenied(deniedList, false)
                        }
                    }

                    private fun showPermissionSettingDialog(
                        activity: Activity,
                        allList: List<String>,
                        deniedList: List<String>,
                        callback: OnPermissionCallback?
                    ) {

                        val onDoNotAskAgain = onDoNotAskAgain ?: return super.deniedPermissionRequest(activity, allList, deniedList, true, callback)
                        val doNotAskAgainList = deniedList.filter {
                            XXPermissions.isDoNotAskAgainPermissions(activity, it)
                        }
                        onDoNotAskAgain.onDoNotAskAgain(doNotAskAgainList) { isAgree ->
                            if (isAgree) {
                                XXPermissions.startPermissionActivity(activity, doNotAskAgainList, object :
                                    OnPermissionPageCallback {
                                    override fun onGranted() {
                                        callback?.onGranted(allList, true)
                                    }

                                    override fun onDenied() {
                                        val newDenied = XXPermissions.getDenied(activity, allList)
                                        callback?.onGranted(allList - newDenied.toSet(), false)
                                        callback?.onDenied(newDenied, true)
                                    }
                                })
                            } else {
                                val newDenied = XXPermissions.getDenied(activity, allList)
                                callback?.onGranted(allList - newDenied.toSet(), false)
                                callback?.onDenied(deniedList, true)
                            }
                        }
                    }
                })
            .request(object : OnPermissionCallback {

                private var grantedList: List<String>? = null

                override fun onGranted(permissions: List<String>, allGranted: Boolean) {
                    if (allGranted) {
                        onResult?.onResult(true, permissions, emptyList())
                    } else {
                        grantedList = permissions
                    }
                }

                override fun onDenied(permissions: List<String>, doNotAskAgain: Boolean) {
                    onResult?.onResult(false, grantedList.orEmpty(), permissions)
                }
            })
    }
}
