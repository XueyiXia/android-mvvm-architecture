package com.framework.mvvm.utils

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.FileUtils
import android.provider.OpenableColumns
import android.provider.Settings
import android.text.Html
import android.text.Spanned
import android.text.TextUtils
import android.view.View
import androidx.annotation.RequiresApi
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.math.roundToInt

/**
 * 获取屏幕宽度
 */
val Context.screenWidth
    get() = resources.displayMetrics.widthPixels

/**
 * 获取屏幕高度
 */
val Context.screenHeight
    get() = resources.displayMetrics.heightPixels


/**
 * 判断是否为空 并传入相关操作
 * @receiver T?
 * @param notNullAction Function1<T, Unit>
 * @param nullAction Function0<Unit>
 */
inline fun <reified T> T?.notNull(notNullAction: (T) -> Unit, nullAction: () -> Unit = {}) {
    if (this != null) {
        notNullAction.invoke(this)
    } else {
        nullAction.invoke()
    }
}

/**
 * dp值转换为px
 * @receiver Context
 * @param dp Int
 * @return Int
 */
fun Context.dp2px(dp: Int): Int {
    val scale = resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}

/**
 *  px值转换成dp
 * @receiver Context
 * @param px Int
 * @return Int
 */
fun Context.px2dp(px: Int): Int {
    val scale = resources.displayMetrics.density
    return (px / scale + 0.5f).toInt()
}

/**
 * dp值转换为px
 * @receiver View
 * @param dp Int
 * @return Int
 */
fun View.dp2px(dp: Int): Int {
    val scale = resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}

/**
 * px值转换成dp
 * @receiver View
 * @param px Int
 * @return Int
 */
fun View.px2dp(px: Int): Int {
    val scale = resources.displayMetrics.density
    return (px / scale + 0.5f).toInt()
}


/**
 * 复制文本到粘贴板
 * @receiver Context
 * @param text String
 * @param label String
 */
fun Context.copyToClipboard(text: String, label: String = "") {
    val clipData = ClipData.newPlainText(label, text)
    clipboardManager?.setPrimaryClip(clipData)
}


/**
 * 检查是否启用无障碍服务
 * @receiver Context
 * @param serviceName String
 * @return Boolean
 */
fun Context.checkAccessibilityServiceEnabled(serviceName: String): Boolean {
    val settingValue =
        Settings.Secure.getString(
            applicationContext.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        )
    var result = false
    val splitter = TextUtils.SimpleStringSplitter(':')
    while (splitter.hasNext()) {
        if (splitter.next().equals(serviceName, true)) {
            result = true
            break
        }
    }
    return result
}

/**
 * 设置点击事件
 * @param views 需要设置点击事件的view
 * @param onClick 点击触发的方法
 */
fun setOnclick(vararg views: View?, onClick: (View) -> Unit) {
    views.forEach {
        it?.setOnClickListener { view ->
            onClick.invoke(view)
        }
    }
}

/**
 * 设置防止重复点击事件
 * @param views 需要设置点击事件的view集合
 * @param interval 时间间隔 默认0.5秒
 * @param onClick 点击触发的方法
 */
fun setOnclickNoRepeat(vararg views: View?, interval: Long = 500, onClick: (View) -> Unit) {
    views.forEach {
        it?.clickNoRepeat(interval = interval) { view ->
            onClick.invoke(view)
        }
    }
}

/**
 *
 * @receiver String
 * @param flag Int
 * @return Spanned
 */
fun String.toHtml(flag: Int = Html.FROM_HTML_MODE_LEGACY): Spanned {
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        Html.fromHtml(this, flag)
    } else {
        Html.fromHtml(this)
    }
}

var lastClickTime = 0L

/**
 * 防止重复点击事件 默认0.5秒内不可重复点击
 * @receiver View
 * @param interval Long 时间间隔 默认0.5秒
 * @param action Function1<[@kotlin.ParameterName] View, Unit>
 */
fun View.clickNoRepeat(interval: Long = 500, action: (view: View) -> Unit) {
    setOnClickListener {
        val currentTime = System.currentTimeMillis()
        if (lastClickTime != 0L && (currentTime - lastClickTime < interval)) {
            return@setOnClickListener
        }
        lastClickTime = currentTime
        action(it)
    }
}



/**
 * Android 10 以上适配
 * @param context
 * @param uri
 * @return
 */
@SuppressLint("Recycle", "Range")
@RequiresApi(api = Build.VERSION_CODES.Q)
 fun uriToFileApiQ(context: Context, uri: Uri): String? {
    var file: File? = null
    //android10以上转换
    if (uri.scheme == ContentResolver.SCHEME_FILE) {
        file = uri.path?.let { File(it) }
    } else if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
        //把文件复制到沙盒目录
        val contentResolver = context.contentResolver
        val cursor = contentResolver.query(uri, null, null, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val displayName =
                    cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                try {
                    val openInputStream = contentResolver.openInputStream(uri)
                    val cache = File(
                        context.externalCacheDir!!.absolutePath,
                        ((Math.random() + 1) * 1000).roundToInt().toString() + displayName
                    )
                    val fos = FileOutputStream(cache)
                    if (openInputStream != null) {
                        FileUtils.copy(openInputStream, fos)
                    }
                    file = cache
                    fos.close()
                    openInputStream?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
    return file!!.absolutePath
}



