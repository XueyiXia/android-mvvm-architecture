package com.jetpack.mvvm

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.framework.http.config.RxHttpConfigure
import com.framework.http.utils.HttpConstants
import com.jetpack.mvvm.di.appComponent
import com.rxjava_retrofit.HttpApi
import me.jessyan.autosize.AutoSize
import me.jessyan.autosize.AutoSizeConfig
import me.jessyan.autosize.onAdaptListener
import me.jessyan.autosize.utils.AutoSizeLog
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.util.Locale
import java.util.concurrent.TimeUnit


/**
 * @author: xiaxueyi
 * @date: 2022-12-30
 * @time: 10:46
 * @说明:
 */

class AppLoader : Application() {


    /**
     * +++++++++++++++++++++++多dex模式测试-开始+++++++++++++++++++++
     */
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }


    override fun onCreate() {
        super.onCreate()

        configureDI()

        initAutoSizeConfig()

        initRHttp()
    }


    private fun configureDI() = startKoin {
        androidContext(this@AppLoader)
        modules(appComponent)
    }


    private fun initAutoSizeConfig(){
        //跳转的三方库中的 DefaultErrorActivity 就是在另外一个进程中, 所以要想适配这个 Activity 就需要调用 initCompatMultiProcess()
        AutoSize.initCompatMultiProcess(this)

        //如果在某些特殊情况下出现 InitProvider 未能正常实例化, 导致 AndroidAutoSize 未能完成初始化
        //可以主动调用 AutoSize.checkAndInit(this) 方法, 完成 AndroidAutoSize 的初始化后即可正常使用
        AutoSize.checkAndInit(this);

//        如何控制 AndroidAutoSize 的初始化，让 AndroidAutoSize 在某些设备上不自动启动？https://github.com/JessYanCoding/AndroidAutoSize/issues/249
        /**
         * 以下是 AndroidAutoSize 可以自定义的参数, [AutoSizeConfig] 的每个方法的注释都写的很详细
         * 使用前请一定记得跳进源码，查看方法的注释, 下面的注释只是简单描述!!!
         */
        AutoSizeConfig.getInstance() //
            //是否让框架支持自定义 Fragment 的适配参数, 由于这个需求是比较少见的, 所以须要使用者手动开启 ,如果没有这个需求建议不开启
            .setCustomFragment(false) //
            //是否屏蔽系统字体大小对 AndroidAutoSize 的影响, 如果为 true, App 内的字体的大小将不会跟随系统设置中字体大小的改变
            //如果为 false, 则会跟随系统设置中字体大小的改变, 默认为 false
            .setExcludeFontScale(true)
            //区别于系统字体大小的放大比例, AndroidAutoSize 允许 APP 内部可以独立于系统字体大小之外，独自拥有全局调节 APP 字体大小的能力
            //当然, 在 APP 内您必须使用 sp 来作为字体的单位, 否则此功能无效, 不设置或将此值设为 0 则取消此功能
            //.setPrivateFontScale(0.8f)
            //屏幕适配监听器
            .setOnAdaptListener(object : onAdaptListener {
                override fun onAdaptBefore(target: Any, activity: Activity) {
                    AutoSizeLog.d(
                        kotlin.String.format(Locale.ENGLISH, "%s onAdaptBefore!", target.javaClass.getName())
                    )
                }

                override fun onAdaptAfter(target: Any, activity: Activity) {
                    AutoSizeLog.d(
                        kotlin.String.format(Locale.ENGLISH, "%s onAdaptAfter!", target.javaClass.getName())
                    )
                }
            }) //是否打印 AutoSize 的内部日志, 默认为 true, 如果您不想 AutoSize 打印日志, 则请设置为 false
            .setLog(true)
    }



    /**
     * 初始化网络请求
     */
    private fun initRHttp() {
        val headerMap :MutableMap<kotlin.String, Any> = mutableMapOf()
        headerMap["Content-Type"] = "application/x-www-form-urlencoded" //默认的编码方式
        headerMap["Connection"] = "Keep-Alive"
        headerMap["Accept-Language"] = "zh-cn"
        headerMap["Accept"] = "Application/Json"

        //必须初始化
        RxHttpConfigure.getInstance()
            .setBaseUrl(HttpApi.BASE_URL)
            .setBaseHeader(headerMap)
            .setTimeout(HttpConstants.TIME_OUT)
            .setTimeUnit(TimeUnit.MILLISECONDS)
            .showLog(true)
            .init(this)
    }
}