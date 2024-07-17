
package com.module.utils.permissions.interfac


fun interface OnUserResultCallback {
    /**
     * 当用户在设置中同意或拒绝允许这些权限时调用它。
     * @param isAgree Boolean
     */
    fun onResult(isAgree: Boolean)
}