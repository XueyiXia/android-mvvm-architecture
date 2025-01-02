package com.jetpack.mvvm.widget.slide

import android.R.attr
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.ViewConfiguration
import android.widget.LinearLayout
import android.widget.Scroller
import androidx.core.view.NestedScrollingChild3
import androidx.core.view.NestedScrollingChildHelper
import androidx.core.view.ViewCompat
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


/**
 * @author: xueyi.xia
 * @date: 2025-01-02
 * @time: 15:07
 * @说明:
 */
class LinkScrollViewChild3  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), NestedScrollingChild3 {
    private val childHelper = NestedScrollingChildHelper(this)

    // touch滑动相关参数
    private var lastX = -1  // touch滑动相关参数
    private var lastY = -1
    private val offset = IntArray(2)
    private val consumed = IntArray(2)

    // fling滑动相关参数
    private var isFling = false
    private var minFlingVelocity = 0
    private var maxFlingVelocity=0
    private val scroller: Scroller = Scroller(context)
    private var velocityTracker: VelocityTracker? = null
    private var lastFlingX = 0
    private var lastFlingY:Int = 0
    private val flingConsumed = IntArray(2)


    init {
        childHelper.isNestedScrollingEnabled = true
        orientation= VERTICAL


        // 获取当前页面配置信息
        val config: ViewConfiguration = ViewConfiguration.get(context)

        // 设置系统默认最小和最大加速度
        minFlingVelocity = config.scaledMinimumFlingVelocity
        maxFlingVelocity = config.scaledMaximumFlingVelocity
    }

    override fun startNestedScroll(axes: Int, type: Int): Boolean {
        return childHelper.startNestedScroll(axes,type)
    }

    override fun stopNestedScroll(type: Int) {
        childHelper.startNestedScroll(type)
    }

    /**
     * Android 中用于检查当前视图是否有嵌套滚动的父视图的
     */
    override fun hasNestedScrollingParent(type: Int): Boolean {

        //返回 true 表示当前视图有嵌套滚动的父视图。
        //返回 false 表示当前视图没有嵌套滚动的父视图。
        return childHelper.hasNestedScrollingParent(type);
    }

    override fun dispatchNestedScroll(
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        offsetInWindow: IntArray?,
        type: Int,
        consumed: IntArray
    ) {
        childHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type,consumed);
    }

    override fun dispatchNestedScroll(
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        offsetInWindow: IntArray?,
        type: Int
    ): Boolean {

        return childHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type);
    }

    override fun dispatchNestedPreScroll(
        dx: Int,
        dy: Int,
        consumed: IntArray?,
        offsetInWindow: IntArray?,
        type: Int
    ): Boolean {
        return childHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type);
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        /**
         * 退出惯性滑动
         */
        cancelFling()

        if (velocityTracker==null){
            velocityTracker=VelocityTracker.obtain();
        }
        // 追踪触摸点移动加速度
        velocityTracker?.addMovement(event);

        when(event?.actionMasked){
            MotionEvent.ACTION_DOWN->{
                consumed[0]=0
                consumed[1]=0

                offset[0] = 0;
                offset[1] = 0;

                lastX = event.x.toInt()
                lastY = event.y.toInt()

                // 通知parent根据滑动方向和滑动类型进行启用嵌套滑动
                if (orientation == VERTICAL) {
                    startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, ViewCompat.TYPE_TOUCH);
                } else {
                    startNestedScroll(ViewCompat.SCROLL_AXIS_HORIZONTAL, ViewCompat.TYPE_TOUCH);
                }
            }

            MotionEvent.ACTION_MOVE->{
                //获取当前坐标
                val curX = event.x.toInt()
                val curY = event.y.toInt()

                // 计算滑动偏移量，起始坐标-当前坐标
                var dx = lastX - curX
                var dy = lastY - curY

                // 优先将滑动偏移量交由parent处理，
                val isCanScroll=dispatchNestedPreScroll(dx = dx, dy = dy,consumed, offset, ViewCompat.TYPE_TOUCH)
                //true 是可以滚动，父类
                if (isCanScroll){
                    // 滑动偏移量减去parent消耗的量
                    dx -= consumed[0];
                    dy -= consumed[1];
                }

                var consumedX = 0
                var consumedY = 0

                // 自身或child处理滑动偏移
                if (orientation == VERTICAL) {
                    consumedY = childConsumedY(dy)
                } else {
                    consumedX = childConsumedX(dx)
                }


                // 滑动偏移量减去自身或child消耗的量，然后再交由parent处理
                dispatchNestedScroll(
                    consumedX,
                    consumedY,
                    dx - consumedX,
                    dy - consumedY,
                    null,
                    ViewCompat.TYPE_TOUCH
                )

                lastX = curX
                lastY = curY

            }


            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL->{
                // 通知parent滑动结束
                stopNestedScroll(ViewCompat.TYPE_TOUCH);

                if (velocityTracker != null) {
                    // 计算触摸点加速度
                    velocityTracker?.computeCurrentVelocity(1000, maxFlingVelocity.toFloat())
                    // 获取xy轴加速度
                    val vx = velocityTracker!!.xVelocity.toInt()
                    val vy = velocityTracker!!.yVelocity.toInt()
                    fling(vx, vy)
                    velocityTracker!!.clear()
                }

                lastX = -1
                lastY = -1
            }
        }
        return true
    }


    override fun computeScroll() {
        if (isFling && scroller.computeScrollOffset()) {
            // 获取scroller计算出的当前滚动距离
            val x = scroller.currX
            val y = scroller.currY
            // 计算滚动偏移量，起始坐标-当前坐标
            var dx = lastFlingX - x
            var dy = lastFlingY - y
            lastFlingX = x
            lastFlingY = y

            // 处理消耗滚动偏移量逻辑同ACTION_MOVE（触摸类型为非用户触摸）
            if (dispatchNestedPreScroll(dx, dy, flingConsumed, null, ViewCompat.TYPE_NON_TOUCH)) {
                dx -= flingConsumed[0]
                dy -= flingConsumed[1]
            }

            var flingX = 0
            var flingY = 0
            // 自身或子view处理fling
            if (orientation == VERTICAL) {
                flingX = childFlingX(dx)
            } else {
                flingY = childFlingY(dy)
            }

            dispatchNestedScroll(
                flingX,
                flingY,
                dx - flingX,
                dy - flingY,
                null,
                ViewCompat.TYPE_NON_TOUCH
            )

            // 触发再次执行computeScroll()
            postInvalidate()
        } else {
            stopNestedScroll(ViewCompat.TYPE_NON_TOUCH)
            cancelFling()
        }
    }

    private fun cancelFling() {
        isFling = false
        lastFlingX = 0
        lastFlingY = 0
    }

    private fun fling(velocityX: Int, velocityY: Int): Boolean {
        var velocityX = velocityX
        var velocityY = velocityY
        if (abs(velocityX.toDouble()) < minFlingVelocity && abs(velocityY.toDouble()) < minFlingVelocity) {
            // 加速度过小，则不进行fling
            return false
        }

        // 通知parent根据滑动方向和滑动类型进行启用嵌套滑动
        if (orientation == VERTICAL) {
            startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, ViewCompat.TYPE_NON_TOUCH)
        } else {
            startNestedScroll(ViewCompat.SCROLL_AXIS_HORIZONTAL, ViewCompat.TYPE_NON_TOUCH)
        }

        // 限制加速度值范围不超过maxFlingVelocity
        velocityX = max(
            -maxFlingVelocity.toDouble(),
            min(velocityX.toDouble(), maxFlingVelocity.toDouble())
        ).toInt()
        velocityY = max(
            -maxFlingVelocity.toDouble(),
            min(velocityY.toDouble(), maxFlingVelocity.toDouble())
        ) .toInt()
        doFling(velocityX, velocityY)
        return true
    }

    private fun doFling(velocityX: Int, velocityY: Int) {
        isFling = true
        // 将加速度值交由scroller计算
        scroller.fling(
            0,
            0,
            velocityX,
            velocityY,
            Int.MIN_VALUE,
            Int.MAX_VALUE,
            Int.MIN_VALUE,
            Int.MAX_VALUE
        )
        // 触发执行computeScroll()
        postInvalidate()
    }


    /**
     * 进行fling
     * @param dx 可以滚动的偏移量
     * @return 实际滚动消耗的偏移量
     */
    protected fun childFlingX(dx: Int): Int {
        return dx
    }

    /**
     * 进行fling
     * @param dy 可以滚动的偏移量
     * @return 实际滚动消耗的偏移量
     */
    protected fun childFlingY(dy: Int): Int {
        return dy
    }

    /**
     * 进行滚动
     * @param dx 可以滚动的偏移量
     * @return 实际滚动消耗的偏移量
     */
    protected fun childConsumedX(dx: Int): Int {
        return dx
    }

    /**
     * 进行滚动
     * @param dy 可以滚动的偏移量
     * @return 实际滚动消耗的偏移量
     */
    protected fun childConsumedY(dy: Int): Int {
        return dy
    }
}