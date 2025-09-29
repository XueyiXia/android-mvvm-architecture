
package com.module.utils.permissions

import android.app.Activity
import androidx.fragment.app.Fragment
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.OnPermissionDescription
import com.hjq.permissions.OnPermissionInterceptor
import com.hjq.permissions.XXPermissions
import com.hjq.permissions.fragment.factory.PermissionFragmentFactory
import com.hjq.permissions.permission.base.IPermission
import com.module.utils.permissions.interfac.OnPermissionResult
import com.module.utils.permissions.interfac.OnPermissionsDoNotAskAgain
import com.module.utils.permissions.interfac.OnPermissionsShouldShowRationale

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
    private var permissionList = mutableListOf<IPermission>()
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
    fun permissions(vararg permissions: IPermission): XXPermissionsExt {
        permissionList.addAll(permissions)
        return this
    }

    /**
     * add permissions
     */
    @JvmName("permissionsArray")
    fun permissions(permissions: Array<out IPermission>): XXPermissionsExt {
        permissionList.addAll(permissions)
        return this
    }

    /**
     * add permissions
     */
    fun permissions(permissions: List<IPermission>): XXPermissionsExt {
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
            .permissions(permissionList)
            .interceptor(object : OnPermissionInterceptor {

                override fun onRequestPermissionStart(
                    activity: Activity,
                    requestList: List<IPermission?>,
                    fragmentFactory: PermissionFragmentFactory<*, *>,
                    permissionDescription: OnPermissionDescription,
                    callback: OnPermissionCallback?
                ) {
                    super.onRequestPermissionStart(
                        activity,
                        requestList,
                        fragmentFactory,
                        permissionDescription,
                        callback
                    )
                }

                override fun onRequestPermissionEnd(
                    activity: Activity,
                    skipRequest: Boolean,
                    requestList: List<IPermission?>,
                    grantedList: List<IPermission?>,
                    deniedList: List<IPermission?>,
                    callback: OnPermissionCallback?
                ) {
                    super.onRequestPermissionEnd(
                        activity,
                        skipRequest,
                        requestList,
                        grantedList,
                        deniedList,
                        callback
                    )

                    if (deniedList.isEmpty()) {
                        return
                    }
                    val doNotAskAgain = XXPermissions.isDoNotAskAgainPermissions(activity, deniedList)
                    if (!doNotAskAgain) {
                        // 如果没有勾选不再询问选项，call back给用户
                        onDoNotAskAgain?.onDoNotAskAgain(deniedList){isAgree->
                            if (isAgree) {
                                XXPermissions.startPermissionActivity(
                                    activity,
                                    deniedList,
                                    object : OnPermissionCallback {
                                        override fun onResult(grantedList: List<IPermission?>, deniedList: List<IPermission?>){
                                            val latestDeniedList = XXPermissions.getDeniedPermissions(activity, requestList)
                                            val allGranted = latestDeniedList.isEmpty()
                                            if (!allGranted) {
                                                return
                                            }

                                            if (callback == null) {
                                                return
                                            }
                                            // 用户全部授权了，回调成功给外层监听器，免得用户还要再发起权限申请
                                        }

                                    })

                            }else {
                                // 用户全部授权了，回调成功给外层监听器，免得用户还要再发起权限申请

                            }
                        }
                        return
                    }


                }

                override fun dispatchPermissionRequest(
                    activity: Activity,
                    requestList: List<IPermission?>,
                    fragmentFactory: PermissionFragmentFactory<*, *>,
                    permissionDescription: OnPermissionDescription,
                    callback: OnPermissionCallback?
                ) {
                    super.dispatchPermissionRequest(
                        activity,
                        requestList,
                        fragmentFactory,
                        permissionDescription,
                        callback
                    )
                }
            })
            .request { grantedList, deniedList ->

                /**
                 * 回调请求全选的最后结果
                 * @param allGranted Boolean 授予权限的bool
                 * @param grantedList List<String> 授予权限的列表
                 * @param deniedList List<String> 拒绝权限的列表
                 */
                val allGranted = deniedList.isNotEmpty()
                onResult?.onResult(allGranted, grantedList, deniedList)
            }
    }
}
