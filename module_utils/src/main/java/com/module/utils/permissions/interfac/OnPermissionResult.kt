

package com.module.utils.permissions.interfac

import com.hjq.permissions.permission.base.IPermission

fun interface OnPermissionResult {

    /**
     * 回调请求全选的最后结果
     * @param allGranted Boolean 授予权限的bool
     * @param grantedList List<String> 授予权限的列表
     * @param deniedList List<String> 拒绝权限的列表
     */
    fun onResult(allGranted: Boolean, grantedList: List<IPermission?>, deniedList: List<IPermission?>)
}