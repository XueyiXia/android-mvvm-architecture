package com.framework.http.exception


/**
 * @author: xiaxueyi
 * @date: 2022-08-18
 * @time: 15:28
 * @说明: 整个事件链错误类型
 */
class EventException : Exception {

    private var code: Any //错误码

    private var msg: String//错误信息


    /**
     *
     * @param code Any
     * @param msg String
     * @constructor
     */
    constructor(code: Any, msg: String) {
        this.code = code
        this.msg = msg
    }

    /**
     *
     * @param throwable Throwable?
     * @param code Any
     * @param msg String
     * @constructor
     */
    constructor(throwable: Throwable?, code: Any, msg: String) : super(throwable) {
        this.code = code
        this.msg = msg
    }
}