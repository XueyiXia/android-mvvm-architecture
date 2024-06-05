package com.framework.mvvm.utils

import com.framework.mvvm.viewmodel.BaseViewModel
import com.module.utils.ParameterizedTypeUtils
import java.lang.reflect.ParameterizedType



/**
 * 获取当前类绑定的泛型ViewModel-clazz
 * @param obj Any
 * @return VM
 */
//@Suppress("UNCHECKED_CAST")
//fun <VM> getVmClazz(obj: Any): VM {
//    return (obj.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as VM
//}


fun <VM> getVmClazz(obj: Any): Class<VM> {
    val type = obj.javaClass.genericSuperclass?.let {
        ParameterizedTypeUtils.findParameterizedType(it)
    }
    val modelClass :Class<VM> = if (type is ParameterizedType) {
        type.actualTypeArguments[1] as Class<VM>
    } else {
        BaseViewModel::class.java as Class<VM>
    }
    return modelClass
}






