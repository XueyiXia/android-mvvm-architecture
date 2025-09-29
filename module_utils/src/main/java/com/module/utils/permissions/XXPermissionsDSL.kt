
package com.module.utils.permissions

import android.app.Activity
import androidx.fragment.app.Fragment
import com.hjq.permissions.permission.base.IPermission
import com.module.utils.permissions.interfac.OnPermissionResult
import com.module.utils.permissions.interfac.OnPermissionsDoNotAskAgain
import com.module.utils.permissions.interfac.OnPermissionsShouldShowRationale

/**
 * Activity 中调用
 * @receiver Activity
 * @param block [@kotlin.ExtensionFunctionType] Function1<XXPermissionsDSL, Unit>
 */
inline fun Activity.requestPermissions(block: XXPermissionsDSL.() -> Unit) =
    XXPermissionsDSL(
        XXPermissionsExt.with(this)
    ).apply {
        block(this)
    }.xxPermissions.request()


/**
 * 在fragment 调用
 * @receiver Fragment
 * @param block [@kotlin.ExtensionFunctionType] Function1<XXPermissionsDSL, Unit>
 */
inline fun Fragment.requestPermissions(block: XXPermissionsDSL.() -> Unit) =
    XXPermissionsDSL(
        XXPermissionsExt.with(requireActivity())
    ).apply {
        block(this)
    }.xxPermissions.request()


class XXPermissionsDSL(
    @PublishedApi internal val xxPermissions: XXPermissionsExt) {



    /**
     * 增加权限
     * @param permissions Array<out String>
     */
//    fun permissions(vararg permissions: IPermission) {
//        xxPermissions.permission(permissions)
//    }

    /**
     * 增加权限，数组形式
     * @param permissions Array<out String>
     */
    @JvmName("permissionsArray")
    fun permissions(permissions: Array<out IPermission>) {
        xxPermissions.permissions(permissions)
    }

    /**
     * 增加权限，
     * @param permissions List<String>
     */
    fun permissions(permissions: List<IPermission>) {
        xxPermissions.permissions(permissions)
    }


    /**
     * 告诉用户在设置中允许这些权限时调用。
     * @param onDoNotAskAgain OnPermissionsDoNotAskAgain
     */
    fun onDoNotAskAgain(onDoNotAskAgain: OnPermissionsDoNotAskAgain) {
        xxPermissions.onDoNotAskAgain(onDoNotAskAgain)
    }

    /**
     * 需要显示请求权限时调用。
     * @param onShouldShowRationale OnPermissionsShouldShowRationale
     */
    fun onShouldShowRationale(onShouldShowRationale: OnPermissionsShouldShowRationale) {
        xxPermissions.onShouldShowRationale(onShouldShowRationale)
    }

    /**
     *
     * 权限请求结果的回调。
     */
    fun onResult(onResult: OnPermissionResult) {
        xxPermissions.onResult(onResult)
    }
}