
package com.module.utils.permissions.interfac

fun interface OnPermissionsDoNotAskAgain {
    /**
     * 拒绝后，再次请求权限会调用
     * @param doNotAskAgainList List<String>
     * @param onUserResult OnUserResultCallback
     */
    fun onDoNotAskAgain(doNotAskAgainList: List<String>, onUserResult: OnUserResultCallback)
}