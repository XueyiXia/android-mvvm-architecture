package com.framework.mvvm.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.ParameterizedType

/**
 * @author: xiaxueyi
 * @date: 2023-05-16
 * @time: 13:50
 * @说明:
 */


/**
 *
 * @receiver AppCompatActivity
 * @param layoutInflater LayoutInflater
 * @return VB
 */
@JvmName("inflateWithGeneric")
fun <VB : ViewBinding> AppCompatActivity
        .inflateBindingWithGeneric(layoutInflater: LayoutInflater): VB
= withGenericBindingClass<VB>(this) { clazz -> clazz.getMethod("inflate", LayoutInflater::class.java).invoke(null, layoutInflater) as VB }.also { binding ->
        if (binding is ViewDataBinding) {
            binding.lifecycleOwner = this
        }
    }


/**
 *
 * @receiver Fragment
 * @param layoutInflater LayoutInflater
 * @param parent ViewGroup?
 * @param attachToParent Boolean
 * @return VB
 */
@JvmName("inflateWithGeneric")
fun <VB : ViewBinding> Fragment.inflateBindingWithGeneric(layoutInflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean): VB =
    withGenericBindingClass<VB>(this) { clazz ->
        clazz.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
            .invoke(null, layoutInflater, parent, attachToParent) as VB
    }.also { binding ->
        if (binding is ViewDataBinding) {
            binding.lifecycleOwner = viewLifecycleOwner
        }
    }


/**
 *
 * @param any Any
 * @param block Function1<Class<VB>, VB>
 * @return VB
 */
private fun <VB : ViewBinding> withGenericBindingClass(any: Any, block: (Class<VB>) -> VB): VB {
    var genericSuperclass = any.javaClass.genericSuperclass
    var superclass = any.javaClass.superclass
    while (superclass != null) {
        if (genericSuperclass is ParameterizedType) {
                try {
                    return block.invoke(genericSuperclass.actualTypeArguments[0] as Class<VB>)
                } catch (_: NoSuchMethodException) {
                } catch (_: ClassCastException) {
                } catch (e: InvocationTargetException) {
                    throw e.targetException
                }
        }
        genericSuperclass = superclass.genericSuperclass
        superclass = superclass.superclass
    }
    throw IllegalArgumentException("There is no generic of ViewBinding.")
}