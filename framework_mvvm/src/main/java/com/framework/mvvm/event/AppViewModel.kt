package com.framework.mvvm.event

import com.framework.mvvm.viewmodel.BaseViewModel



/**
 * @author: xiaxueyi
 * @date: 2022-11-29
 * @time: 10:20
 * @说明: APP全局的ViewModel，存放公共数据，当他数据改变时，所有监听他的地方都会收到回调,这样可以达到更新数据，比如全局可使用的 地理位置信息，账户信息,App的基本配置等等，
 *
 */
class AppViewModel : BaseViewModel() {


}