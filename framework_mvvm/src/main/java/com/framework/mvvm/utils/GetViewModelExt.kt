package com.framework.mvvm.utils

import java.lang.reflect.ParameterizedType



/**
 * 获取当前类绑定的泛型ViewModel-clazz
 * @param obj Any
 * @return VM
 */
@Suppress("UNCHECKED_CAST")
fun <VM> getVmClazz(obj: Any): VM {
    return (obj.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as VM
}







