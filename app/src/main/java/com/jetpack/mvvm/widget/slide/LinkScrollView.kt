package com.jetpack.mvvm.widget.slide

import android.content.Context
import android.util.AndroidRuntimeException
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.LinearLayout
import androidx.core.view.NestedScrollingParent3
import androidx.core.view.NestedScrollingParentHelper
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2


/**
 * @author: xueyi.xia
 * @date: 2025-01-02
 * @time: 12:17
 * @说明:
 */
class LinkScrollView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), NestedScrollingParent3 {

    //头部view
    private var topView: View? = null
    //内容view ,viewpage2
    private var contentView: View? = null

    private var topHeight = 0

    private val parentHelper = NestedScrollingParentHelper(this)

    init {
        orientation =VERTICAL;
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        // 获取top_view和content_view
        if(childCount>0){
            topView=getChildAt(0)
        }


        if(childCount>1){
            contentView=getChildAt(1)
        }

        if (topView == null || contentView == null) {
            throw AndroidRuntimeException("容器中至少需要两个子view , topView==$topView ,  contentView==$contentView");
        }
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // 获取top_view的高度
        if (topView!=null){
            topHeight=topView?.measuredHeight?:0
        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 调整contentView的高度为父容器高度，使之填充布局，避免父容器滚动后出现空白
        val lp = contentView?.layoutParams;
        lp?.height = measuredHeight;
        contentView?.layoutParams = lp;
        Log.d("LinkScrollView","measuredHeight==$measuredHeight")
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        if (contentView!=null){
            contentView?.also {
                if (it is RecyclerView){
                    it.stopScroll()
                }else if (it is NestedScrollView){
                    it.stopNestedScroll()
                }else if (it is ViewPager2){
                    it.stopNestedScroll()
                }else if (it is WebView){
                    it.stopNestedScroll()
                }
            }
        }

        topView?.stopNestedScroll()
        // 处理垂直方向的滑动
        val handled = (axes and ViewCompat.SCROLL_AXIS_VERTICAL) != 0
        return handled
    }

    override fun getNestedScrollAxes(): Int {
        return parentHelper.nestedScrollAxes
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {
        parentHelper.onNestedScrollAccepted(child,target,axes,ViewCompat.TYPE_TOUCH)
    }

    override fun onStopNestedScroll(target: View, type: Int) {
        parentHelper.onStopNestedScroll(target,ViewCompat.TYPE_TOUCH)
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        if (dyUnconsumed>0){
            // 由topView发起的向上滑动，继续让contentView滑动剩余的未消耗完的偏移量
            if (target==topView){
                scrollBy(0, dyUnconsumed)
            }
        }
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {

        if (dyUnconsumed>0){
            // 由topView发起的向上滑动，继续让contentView滑动剩余的未消耗完的偏移量
            if (target==topView){
                scrollBy(0, dyUnconsumed)
            }
        }
    }

    /**
     * 主要用于在父视图和子视图之间协调滚动事件
     * target: 进行滚动的视图，通常是子视图。
     * dx: 在水平方向上请求滚动的距离。
     * dy: 在垂直方向上请求滚动的距离。
     * consumed: 一个整数数组，父视图可以将已消耗的滚动距离写入此数组。
     * type: 嵌套滚动的类型，比如触摸滚动或非触摸滚动。
     */
    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        // 向上滑动。若当前topview可见，需要将topview滑动至不可见
        val hideTop=dy>0&&scrollY<topHeight
        // 向下滑动。若contentView滑动至顶，已不可再滑动，且当前topview未完全可见，则将topview滑动至完全可见
        val showTop=dy<0&&scrollY>0&&!target.canScrollVertically(-1)&&!contentView!!.canScrollVertically(-1)
        val pullDown = dy < 0
        val canScrollDown = topView?.canScrollVertically(1)
        val canScrollUp = topView?.canScrollVertically(-1)

        Log.d("onNestedPreScroll","dy==$dy  ,  pullDown==$pullDown , canScrollDown==$canScrollDown, canScrollUp==$canScrollUp")

        if (hideTop||showTop){
            // 若需要滑动topview，则滑动dy偏移量
            scrollBy(0,dy)

            // 将ScrollLayout消耗的偏移量赋值给consumed数组
            consumed[1] = dy;
        }

    }
    var scrollViewY=0
    override fun scrollTo(x: Int, y: Int) {
        // 将ScrollLayout自身的滚动范围限制在0～topHeight（即在topview完全可见至完全不可见的范围内滑动）
        scrollViewY=y
        if (y < 0) {
            scrollViewY=0
        }
        if (y > topHeight) {
            scrollViewY = topHeight;
        }
        super.scrollTo(x, scrollViewY)
    }


    /**
     * view: 需要检查的视图。
     * direction: 滚动的方向。可以是：
     * 1 表示向下滚动（正方向）。
     * -1 表示向上滚动（负方向）。
     * 返回 true 表示视图可以在指定方向上滚动。
     * 返回 false 表示视图在该方向上无法滚动。
     */
//    override fun canScrollVertically(direction: Int): Boolean {
//        return when {
//            nestedScrollAxes != ViewCompat.SCROLL_AXIS_VERTICAL -> false
//            direction > 0 -> {scrollY < maxScroll}
//            direction < 0 -> {scrollY > minScroll}
//            else -> true
//        }
//    }
}