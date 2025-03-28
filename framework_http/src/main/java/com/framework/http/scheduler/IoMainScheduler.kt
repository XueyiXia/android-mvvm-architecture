package com.framework.http.scheduler

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers


/**
 * @author: xiaxueyi
 * @date: 2022-12-05
 * @time: 13:20
 * @说明:
 */
class IoMainScheduler<T : Any> : BaseScheduler<T>(Schedulers.io(), AndroidSchedulers.mainThread())
