package com.framework.http.scheduler


/**
 * @author: xiaxueyi
 * @date: 2022-12-05
 * @time: 13:11
 * @说明:
 */
object SchedulerUtils {

    /**
     *
     * @return IoMainScheduler<T>
     */
    fun <T : Any> ioToMainScheduler(): IoMainScheduler<T> {
        return IoMainScheduler()
    }

    /**
     *
     * @return NewThreadMainScheduler<T>
     */
    fun <T : Any> newThreadScheduler() : NewThreadMainScheduler<T> {
        return NewThreadMainScheduler()
    }

    /**
     *
     * @return SingleMainScheduler<T>
     */
    fun <T : Any> singleMainScheduler() : SingleMainScheduler<T> {
        return SingleMainScheduler()
    }

    /**
     *
     * @return ComputationMainScheduler<T>
     */
    fun <T : Any> computationScheduler() : ComputationMainScheduler<T> {
        return ComputationMainScheduler()
    }


    fun <T : Any> trampolineMainScheduler() : TrampolineMainScheduler<T> {
        return TrampolineMainScheduler()
    }

}
