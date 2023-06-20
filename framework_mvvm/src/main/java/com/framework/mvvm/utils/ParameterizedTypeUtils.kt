package com.framework.mvvm.utils

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * @author: xiaxueyi
 * @date: 2023-06-20
 * @time: 16:49
 * @说明:
 */
object ParameterizedTypeUtils {

    fun findParameterizedType(type: Type): Type?{
        return when(type){
            is ParameterizedType -> type
            is Class<*> ->{
                type.genericSuperclass?.let {
                    findParameterizedType(it)
                }
            }
            else ->{
                null
            }
        }
    }
}