
package com.module.utils.permissions.interfac

fun interface OnPermissionsShouldShowRationale {

    /**
     * 显示请求权限理由时调用。
     * @param shouldShowRationaleList List<String>
     * @param onUserResult OnUserResultCallback
     */
    fun onShouldShowRationale(shouldShowRationaleList: List<String>,onUserResult: OnUserResultCallback)
}