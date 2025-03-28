package com.framework.http.manager

import io.reactivex.rxjava3.disposables.Disposable
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class RxHttpTagManager private constructor(){


    companion object {
        fun getInstance()= RxHttpTagManager.holder

        fun generateRandomTag(): String {
            return UUID.randomUUID().toString()
        }
    }

    private object RxHttpTagManager {
        val holder = RxHttpTagManager()
    }

    private val map: MutableMap<Any, Disposable> = ConcurrentHashMap(32)


    /**
     *
     * @param tag Any?
     * @param disposable Disposable
     */
    fun addTag(tag: Any?, disposable: Disposable) {
        if (tag != null) {
            this.map[tag] = disposable
        }
    }

    /**
     *
     * @param tag Any?
     */
    fun removeTag(tag: Any?) {
        if (tag != null) {
            map.remove(tag)
        }
    }

    /**
     *
     * @param tag Any?
     */
    fun cancelTag(tag: Any?) {
        if (tag != null) {
            if (map.containsKey(tag)) {
                dispose(map[tag])
                removeTag(tag)
            }
        } else {
            for ((_, value) in map) {
                dispose(value)
            }
            map.clear()
        }
    }

    private fun dispose(disposable: Disposable?) {
        if (disposable != null && !disposable.isDisposed) {
            disposable.dispose()
        }
    }

}