package com.framework.http.utils

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
/**
 * @author: xiaxueyi
 * @date: 2023-02-22
 * @time: 14:02
 * @说明:
 */
object TypeUtils {

    /**
     *
     * @param subclass Class<*>
     * @return Type
     */
    fun getType(subclass: Class<*>): Type {
        val superclass = subclass.genericSuperclass
        return if (superclass is Class<*>) {
            subclass
        } else {
            val parameterized = superclass as ParameterizedType
            parameterized.actualTypeArguments[0]
        }
    }


    /**
     * 获取泛型 Type
     *
     * @param subclass
     * @param <T>
     * @return
    </T> */
    fun <T> getGenericType(subclass: Class<*>): Type {
        val types: Array<Type> = subclass.javaClass.genericInterfaces
        val params = (types[0] as ParameterizedType).actualTypeArguments
        return params[0]
    }
}