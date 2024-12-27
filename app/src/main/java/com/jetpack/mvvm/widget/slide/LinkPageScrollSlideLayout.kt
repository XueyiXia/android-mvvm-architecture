package com.jetpack.mvvm.widget.slide

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.VelocityTracker
import android.view.View
import android.view.animation.PathInterpolator
import android.widget.FrameLayout
import androidx.core.animation.doOnCancel
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.view.NestedScrollingParent2
import androidx.core.view.NestedScrollingParentHelper
import androidx.core.view.ViewCompat
import androidx.core.view.isInvisible

import com.aa.cat.ui.watchlistQuoteGraph.slide.PullToRefreshHeaderPresenter
import com.jetpack.mvvm.R
import com.jetpack.mvvm.widget.slide.refresh.TestLayout
import java.lang.ref.WeakReference
import kotlin.math.abs


class LinkPageScrollSlideLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), NestedScrollingParent2  {

    companion object {
        private const val NESTED_SCROLL_TYPE_TOUCH = 1 shl 0
        private const val NESTED_SCROLL_TYPE_NON_TOUCH = 1 shl 1
        private const val FLING_SLOP = 60 //惯性率
        private const val SLIDER_DURATION = 400L
        private val SLIDER_INTERPOLATOR = PathInterpolator(0.25f, 0.77f, 0.30f, 0.98f)
    }

    private var headerTop = 0
    private var sliderTop = 0

    private var nestedParentHelper = NestedScrollingParentHelper(this)

    private var sliderAnimator: ValueAnimator? = null
    private var headerAnimator: ValueAnimator? = null

    private var handlingScrollType = 0

    private var sliderVelocityY = 0f

    private var handlingNonTouchChildRef: WeakReference<View>? = null

    private var lastGesture: SlideGesture? = null

    private val pullToRefreshHeaderPresenter = PullToRefreshHeaderPresenter({ refreshView() as? TestLayout }, { updateHeaderSliderForRefresh() })

    private var sliderOffsetHeaderWhenRefresh = 0

    private var headerTarget: View? = null

    private var onHeaderUpdateListener: ((Int) -> Unit)? = null
    private var onSliderExpandChangeListener: ((Boolean) -> Unit)? = null
    private var sliderExpand = false
    private var onSliderOffsetChangeListener: ((Int) -> Unit)? = null

    private var headerVisibleRange: IntRange? = null
    private var onHeaderVisibleRangeChangeListener: ((IntRange) -> Unit)? = null

    private var overlapDistance = 0 //重叠距离，就是刷新控件合布局的重叠

    private var disableLayout = false

    private var disableSliderRefresh = false //是否需要刷新，true 禁用滑块刷新,因为页有刷新，所以这个一个入口，防止计算smark 刷新有bug

    /**
     * 上次触摸事件的 x 值，用于判断是否拦截事件
     */
    private var lastX = 0F

    /**
     * 上次触摸事件的 y 值，用于处理自身的滑动事件
     */
    private var lastY = 0F

    /**
     * y 轴的最大滚动范围 = 顶部视图高度 + 底部视图高度 - 自身的高度
     */
    private var maxScrollY = 0

    /**
     * 用于计算自身时的 y 轴速度，处理自身的 fling
     */
    private val velocityTracker = VelocityTracker.obtain()

    init {
        useAttrs(attrs, R.styleable.LinkPageScrollSlideLayout) {
            overlapDistance = getDimensionPixelSize(R.styleable.LinkPageScrollSlideLayout_slide_overlay_distance, 0)
            disableSliderRefresh = getBoolean(R.styleable.LinkPageScrollSlideLayout_disable_slider_refresh, false)
        }

    }

    private fun headerTarget(): View? {
        return headerTarget ?: findChildTypeHeader().also { headerTarget = it }
    }

    private fun headerView(): View? {
        return getChildAt(0)
    }

    private fun sliderView(): View? {
        return getChildAt(1)
    }

    private fun refreshView(): View? {
        return getChildAt(2)
    }

    private fun canRefreshBySlider(targetView: View?, dy: Int): Boolean {
        return disableSliderRefresh.not()
                && targetView?.let { it.isSlideChildTypeSlider() && it.canScrollVertically(-1).not() } == true
                && isSliderExpanded()
                && dy < 0
    }

    private fun isSliderExpanded(): Boolean = sliderTop == (headerMinHeight() ?: 0)

    private fun headerMinHeight() = headerView()?.run { minimumHeight }

    private fun headerHeight() =
            headerView()?.let { header -> (header.height - overlapDistance).coerceAtLeast(header.minimumHeight) }

    private fun headerRange() = headerView()?.run {
        val min = headerMinHeight()!!
        val max = headerHeight()!!
        if (max < min) {
            null
        } else {
            IntRange(min, max)
        }
    }

    private fun headerMeasureHeight(): Int? {
        return headerView()?.let { header ->
            header.measure(
                MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(measuredHeight, MeasureSpec.AT_MOST)
            )
            (header.measuredHeight - overlapDistance).coerceAtLeast(header.minimumHeight)
        }
    }

    private fun headerVisibleRange(): IntRange? {
        return headerView()?.let { IntRange(it.top + headerMinHeight()!!, it.top + headerHeight()!!) }
    }

    private fun getGesture(targetView: View? = null, dy: Int = 0): SlideGesture {
        val gesture = when {
            pullToRefreshHeaderPresenter.isVisible() -> SlideGesture.REFRESH
            headerTop < 0 -> {
                if (canRefreshBySlider(targetView, dy)) {
                    SlideGesture.REFRESH
                } else {
                    SlideGesture.SCROLL
                }
            }
            sliderTop < (headerHeight() ?: 0) -> {
                if (canRefreshBySlider(targetView, dy)) {
                    SlideGesture.REFRESH
                } else {
                    SlideGesture.SLIDE
                }
            }
            else -> {
                // headerTop == 0 && sliderTop == headerHeight
                var result: SlideGesture? = null
                val pullDown = dy < 0
                headerTarget()?.apply {
                    val canScrollDown = canScrollVertically(1)
                    val canScrollUp = canScrollVertically(-1)
                    if (canScrollDown.not()) {
                        val headerScrollUp = targetView?.isSlideChildTypeHeader() == true
                                && canScrollUp && pullDown
                        if (headerScrollUp.not()) {
                            result = if (canScrollUp.not() && pullDown) {
                                SlideGesture.REFRESH
                            } else {
                                SlideGesture.SCROLL
                            }
                        }
                    }
                }
                result ?: run {
                    if (targetView?.let { it.isSlideChildTypeHeader() && it.canScrollVertically(-1).not() } == true
                            && pullDown) {
                        SlideGesture.REFRESH
                    } else {
                        SlideGesture.SLIDE
                    }
                }
            }
        }
        if (lastGesture != gesture) {
            when (lastGesture) {
                SlideGesture.REFRESH -> sliderOffsetHeaderWhenRefresh = 0
                SlideGesture.SCROLL -> cancelHeaderAnimator()
                SlideGesture.SLIDE -> cancelSliderAnimator()
                null -> {}
            }
            val isRefresh = gesture == SlideGesture.REFRESH
            if (isRefresh) {
                if (isSliderExpanded() || headerTarget()?.canScrollVertically(1) == false) {
                    sliderOffsetHeaderWhenRefresh = sliderTop - headerTop
                }
            }
            headerView()?.isInvisible = isSliderExpanded() && isRefresh
            lastGesture = gesture
        }
        return gesture
    }

    private fun changeHeaderTop(top: Int, disableCheck: Boolean = false) {
        if (headerTop != top) {
            headerTop = top
            onHeaderUpdateListener?.invoke(headerTop)
            dispatchHeaderVisibleRangeChange()
        }
        headerView()?.also { header ->
            header.offsetTopAndBottom(headerTop - header.top)
            if (disableCheck.not()) {
                val validSliderTop = sliderTop.coerceIn(headerVisibleRange()!!)
                if (validSliderTop != sliderTop) {
                    changeSliderTop(validSliderTop)
                }
            }
        }
    }

    private fun changeSliderTop(top: Int, disableCheck: Boolean = false) {
        headerVisibleRange()?.also {
            val t = if (disableCheck) top else top.coerceIn(it)
            if (sliderTop != t) {
                sliderTop = t
                onSliderOffsetChangeListener?.invoke(t)
                dispatchHeaderVisibleRangeChange()
                val expand = isSliderExpanded()
                if (sliderExpand != expand) {
                    sliderExpand = expand
                    onSliderExpandChangeListener?.invoke(sliderExpand)
                }
            }
            sliderView()?.also { slider ->
                slider.offsetTopAndBottom(sliderTop - slider.top)
                slider.viewTreeObserver.dispatchOnGlobalLayout()
            }
        }
    }

    private fun updateHeaderSliderForRefresh() {
        val headerTopForRefresh = headerTop + pullToRefreshHeaderPresenter.height
        val sliderTopForRefresh = sliderOffsetHeaderWhenRefresh.takeIf { it > 0 }
                ?.let { it + headerTopForRefresh }
                ?: sliderTop
        headerView()?.apply { offsetTopAndBottom(headerTopForRefresh - top) }
        sliderView()?.apply { offsetTopAndBottom(sliderTopForRefresh - top) }
    }

    /* nested scroll end */
    private fun stopNonTouchScrollingChild() {
        handlingNonTouchChildRef?.get()?.also {
            ViewCompat.stopNestedScroll(it, ViewCompat.TYPE_NON_TOUCH)
            handlingNonTouchChildRef = null
        }
    }

    private fun isNotExpandSliderScrolling(target: View) = target.isSlideChildTypeSlider() && isSliderExpanded().not()

    /**
     * called only when gesture is scroll
     */
    private fun onScrollHeader(dy: Int): Int {
        var consumed = 0
        headerView()?.takeIf { headerHeight()!! > 0 }?.also { header ->
            val top = (header.top - dy).coerceIn(-(headerHeight()!! - headerMinHeight()!!), 0)
            consumed = header.top - top
            changeHeaderTop(top)
            syncSliderWithHeader()
        }
        return consumed
    }

    private fun onScrollSlider(dy: Int): Int {
        var consumed = 0
        headerRange()?.takeIf { it.last > 0 }?.also { range ->
            sliderView()?.also { slider ->
                val top = (slider.top - dy).coerceIn(range)
                consumed = slider.top - top
                changeSliderTop(top)
            }
        }
        return consumed
    }

    private fun settleSlider() {
        headerRange()?.takeIf { it.last > 0 }?.also { range ->
            val slideUp = sliderTop < range.last / 2
            val targetTop = if (slideUp) range.first else range.last
            animateSlider(targetTop)
        }
    }

    private fun animateSlider(top: Int) {
        sliderView()?.also { slider ->
            if (top != slider.top) {
                cancelSliderAnimator()
                sliderAnimator = ValueAnimator.ofInt(slider.top, top).apply {
                    doOnUpdate { _: Animator, t: Int ->
                        changeSliderTop(t)
                    }
                    interpolator = SLIDER_INTERPOLATOR
                    this.duration = SLIDER_DURATION
                    start()
                }
            }
        }
    }

    private fun cancelSliderAnimator() {
        sliderAnimator?.apply {
            cancel(true)
            sliderAnimator = null
        }
    }

    private fun syncSliderWithHeader() {
        headerView()?.also { header ->
            changeSliderTop(header.bottom)
        }
    }

    private fun animateHeader(top: Int) {
        headerView()?.also { header ->
            if (top != header.top) {
                cancelHeaderAnimator()
                sliderAnimator = ValueAnimator.ofInt(header.top, top).apply {
                    doOnUpdate { _: Animator, t: Int ->
                        changeHeaderTop(t)
                        syncSliderWithHeader()
                    }
                    interpolator = SLIDER_INTERPOLATOR
                    this.duration = SLIDER_DURATION
                    start()
                }
            }
        }
    }

    private fun cancelHeaderAnimator() {
        headerAnimator?.apply {
            cancel(true)
            headerAnimator = null
        }
    }

    private fun dispatchHeaderVisibleRangeChange(force: Boolean = false) {
        headerHeight()?.also { headerHeight ->
            val top = if (headerTop < 0) -headerTop else 0
            val bottom = (sliderTop - headerTop).coerceAtMost(headerHeight)
            if (top in 0 until bottom) {
                val changed = headerVisibleRange?.run { start == top && endInclusive == bottom } != true
                if (force || changed) {
                    (IntRange(top, bottom)).also {
                        headerVisibleRange = it
                        onHeaderVisibleRangeChangeListener?.invoke(it)
                    }
                }
            }
        }
    }

    private fun tryStopNonTouchNestedScroll(gesture: SlideGesture, target: View, dy: Int): Boolean {
        val stop = when (gesture) {
            SlideGesture.REFRESH -> pullToRefreshHeaderPresenter.canStopNonTouch(dy)
            SlideGesture.SCROLL -> {
                when {
                    // pull down
                    dy < 0 -> headerTop >= 0
                    // pull up
                    dy > 0 -> headerView()?.let { headerTop <= -(headerHeight()!! - headerMinHeight()!!) } ?: false
                    else -> false
                }
            }
            SlideGesture.SLIDE -> true
        }
        return stop.also {
            if (it) {
                ViewCompat.stopNestedScroll(target, ViewCompat.TYPE_NON_TOUCH)
            }
        }
    }



    fun finishRefresh() {
        pullToRefreshHeaderPresenter.finishRefresh()
    }

    fun refresh() {
        pullToRefreshHeaderPresenter.refresh()
    }


    fun quickReturn() {
        stopNonTouchScrollingChild()
        when (getGesture()) {
            SlideGesture.SCROLL -> {
                animateHeader(0)
            }
            SlideGesture.SLIDE -> {
                headerHeight()?.let { animateSlider(it) }
            }
            else -> {
            }
        }
    }

    fun scrollAroundSlider(distance: Int, onEndListener: () -> Unit) {
        cancelSliderAnimator()
        sliderView()?.also { slider ->
            val startTop = slider.top
            sliderAnimator = ValueAnimator.ofInt(startTop, startTop - distance, startTop).apply {
                doOnUpdate { _: Animator, t: Int ->
                    changeSliderTop(t)
                }
                doOnStart {
                    disableLayout = true
                }
                val endOrCancel = {
                    onEndListener()
                    disableLayout = false
                    requestLayout()
                }
                doOnEnd { endOrCancel() }
                doOnCancel { endOrCancel() }
                this.interpolator = SLIDER_INTERPOLATOR
                this.duration = 1000
                start()
            }
        }
    }

    fun expandHeader(offset: Int? = null) {
        headerMeasureHeight()?.also { h ->
            var sliderTargetTop = h
            offset?.also { o ->
                changeHeaderTop(-o, true)
                sliderTargetTop -= o
            }
            changeSliderTop(sliderTargetTop, true)
        }
    }

    fun expandSlider(slideMode: Boolean) {
        headerMinHeight()?.also { minHeight ->
            headerMeasureHeight()?.also { height ->
                if (slideMode.not()) {
                    changeHeaderTop(minHeight - height, true)
                }
                changeSliderTop(minHeight, true)
            }
        }
    }

    fun slideExpandSlider() {
        if (isSliderCollapsed()) {
            headerMinHeight()?.also { animateSlider(it) }
        }
    }

    fun isSliderCollapsed(): Boolean {
        return getGesture() == SlideGesture.SLIDE && headerHeight() == sliderTop
    }


    fun setRefreshOffset(offset: Int) {
        pullToRefreshHeaderPresenter.setOffset(offset)
    }

    /**
     * 分发滚动Y 轴
     * @param dScrollY Int
     * @param child View?
     * @param target View?
     */

    private fun dispatchScrollY(dScrollY: Int, child: View?, target: View?) {
        if (dScrollY == 0) {
            return
        }
        // 滚动所处的位置没有在子 view，或者子 view 没有完全显示出来
        // 或者子 view 中没有要处理滚动的 target，或者 target 不在能够滚动
        if (child == null || !isChildTotallyShowing(child)
            || target == null || !target.canScrollVertically(dScrollY)) {
            // 优先自己处理，处理不了再根据滚动方向交给顶部或底部的 view 处理
            when {
                canScrollVertically(dScrollY) -> scrollBy(0, dScrollY)
            }
        } else {
            target.scrollBy(0, dScrollY)
        }
    }

    /**
     * 子view 是否全部可见
     * @param c View
     * @return Boolean
     */
    private fun isChildTotallyShowing(c: View): Boolean {
        val relativeY = c.y - scrollY
        return relativeY >= 0 && relativeY + c.height <= height
    }

    /**
     * 滚动范围是[0, [maxScrollY]]，根据方向判断垂直方向是否可以滚动
     */
//    override fun canScrollVertically(direction: Int): Boolean {
//        return if (direction > 0) {
//            scrollY < maxScrollY
//        } else {
//            scrollY > 0
//        }
//    }


//    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
//        Log.d("触摸日志输出","onInterceptTouchEvent--> action.rawY ==${e.rawY}    ,   action.y ==${e.y}  ,  headerHeight()== ${headerHeight()}   ,  scrollX==$scrollX")
//       return when (e.actionMasked) {
//            MotionEvent.ACTION_DOWN -> {
//                Log.d("触摸日志输出","onInterceptTouchEvent--> ACTION_DOWN 手势按下")
//                lastX = e.x
//                lastY = e.y
//               return false
//            }
//
//            MotionEvent.ACTION_MOVE -> {
//                Log.d("触摸日志输出","onInterceptTouchEvent--> ACTION_MOVE 手势移动")
//                if (abs(lastX - e.x) < abs(lastY - e.y)) {
//                    return true
//                } else {
//                    lastX = e.x
//                    lastY = e.y
//                    return false
//                }
//            }
//
//           else -> super.onInterceptTouchEvent(e)
//        }
//
//    }
//
//    @SuppressLint("ClickableViewAccessibility")
//    override fun onTouchEvent(e: MotionEvent): Boolean {
//       return when (e.actionMasked) {
//            MotionEvent.ACTION_DOWN -> {
//                Log.d("触摸日志输出","onTouchEvent--> ACTION_DOWN 手势按下")
//                // 手指按下时记录 y 轴初始位置
//                lastY = e.y
//                velocityTracker.clear()
//                velocityTracker.addMovement(e)
//                true
//            }
//
//            MotionEvent.ACTION_MOVE -> {
//                // 移动时分发滚动量
//                val dScrollY = (lastY - e.y).toInt()
//
//                lastY = e.y
//                velocityTracker.addMovement(e)
//
//                Log.d("触摸日志输出","onTouchEvent--> ACTION_MOVE 手势移动   dScrollY==$dScrollY")
//                true
//            }
//
//            MotionEvent.ACTION_UP -> {
//                Log.d("触摸日志输出","onTouchEvent--> ACTION_UP 手势抬起")
//                // 手指抬起时计算 y 轴速度，然后自身处理 fling
//                velocityTracker.addMovement(e)
//                velocityTracker.computeCurrentVelocity(1000)
//                val yv = -velocityTracker.yVelocity.toInt()
//                true
//            }
//
//            else -> super.onTouchEvent(e)
//        }
//
//    }


    /**
     * TODO 开始滚动
     * nested scroll start
     * @param child View
     * @param target View
     * @param axes Int
     * @param type Int
     * @return Boolean
     */
    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        when (getGesture(target)) {
            SlideGesture.REFRESH -> {
                pullToRefreshHeaderPresenter.onStartScroll()
            }
            SlideGesture.SCROLL -> {
                cancelHeaderAnimator()
            }

            SlideGesture.SLIDE -> {
                if (target.isSlideChildTypeBar() || isNotExpandSliderScrolling(target)) {
                    cancelSliderAnimator()
                }
            }
        }
        sliderVelocityY = 0f
        return axes and ViewCompat.SCROLL_AXIS_VERTICAL > 0
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {
        nestedParentHelper.onNestedScrollAccepted(child, target, axes, type)
        when (type) {
            ViewCompat.TYPE_TOUCH -> handlingScrollType = NESTED_SCROLL_TYPE_TOUCH
            ViewCompat.TYPE_NON_TOUCH -> handlingScrollType = handlingScrollType or NESTED_SCROLL_TYPE_NON_TOUCH
        }
        stopNonTouchScrollingChild()
        if (type == ViewCompat.TYPE_NON_TOUCH) {
            handlingNonTouchChildRef = WeakReference(target)
        }
    }

    /**
     * TODO  停止嵌套滚动
     * @param target View
     * @param type Int
     */
    override fun onStopNestedScroll(target: View, type: Int) {
        nestedParentHelper.onStopNestedScroll(target, type)
        when (type) {
            ViewCompat.TYPE_TOUCH -> handlingScrollType = handlingScrollType and NESTED_SCROLL_TYPE_TOUCH.inv()
            ViewCompat.TYPE_NON_TOUCH -> handlingScrollType = handlingScrollType and NESTED_SCROLL_TYPE_NON_TOUCH.inv()
        }
        if (handlingScrollType == 0) {
            when (getGesture(target)) {
                SlideGesture.REFRESH -> {
                    pullToRefreshHeaderPresenter.onStopScroll()
                }
                SlideGesture.SLIDE -> {
                    if (abs(sliderVelocityY) > FLING_SLOP) {
                        headerRange()?.also { range -> animateSlider(if (sliderVelocityY > 0) range.first else range.last) }
                    } else {
                        settleSlider()
                    }
                }
                else -> {
                }
            }
        }
        handlingNonTouchChildRef?.get()?.takeIf { type == ViewCompat.TYPE_NON_TOUCH && it == target }
            ?.apply { handlingNonTouchChildRef = null }
    }

    /**
     *  TODO 处理嵌套滑动前的逻辑
     * @param target View
     * @param dx Int
     * @param dy Int
     * @param consumed IntArray
     * @param type Int
     */
    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        var remainingDy = dy
        var gesture = getGesture(target, remainingDy)
        while (remainingDy != 0) {
            var consumedDy = 0
            when (gesture) {
                SlideGesture.REFRESH -> {
                    consumedDy = pullToRefreshHeaderPresenter.onScroll(remainingDy, type)
                }
                SlideGesture.SCROLL -> {
                    // slideBar doesn't have pre scroll, no need care
                    val sliderScrollUp = remainingDy > 0 && target.isSlideChildTypeSlider()
                    val headerScrollDown = remainingDy < 0 && target.isSlideChildTypeHeader()
                    if (sliderScrollUp || headerScrollDown) {
                        consumedDy = onScrollHeader(remainingDy)
                    }
                }
                SlideGesture.SLIDE -> {
                    if (isNotExpandSliderScrolling(target) && type == ViewCompat.TYPE_TOUCH) {
                        consumedDy = onScrollSlider(remainingDy)
                    }
                }
            }
            remainingDy -= consumedDy
            consumed[1] += consumedDy
            val nextGesture = getGesture(target, remainingDy)
            if (nextGesture == gesture) {
                break
            }
            gesture = nextGesture
        }
    }

    /**
     *   TODO 处理嵌套滑动后的逻辑
     * @param target View
     * @param dxConsumed Int
     * @param dyConsumed Int
     * @param dxUnconsumed Int
     * @param dyUnconsumed Int
     * @param type Int
     */
    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {
        var remainingDy = dyUnconsumed
        var gesture = getGesture(target, remainingDy)
        if (type == ViewCompat.TYPE_NON_TOUCH && tryStopNonTouchNestedScroll(gesture, target, dyUnconsumed)) {
            return
        }
        while (remainingDy != 0) {
            var consumedDy = 0
            when (gesture) {
                SlideGesture.REFRESH -> {
                    consumedDy = pullToRefreshHeaderPresenter.onScroll(remainingDy, type)
                }
                SlideGesture.SCROLL -> {
                    consumedDy = onScrollHeader(remainingDy)
                }
                SlideGesture.SLIDE -> {
                    if ((target.isSlideChildTypeBar() || target.isSlideChildTypeSlider())
                        && type == ViewCompat.TYPE_TOUCH
                    ) {
                        consumedDy = onScrollSlider(remainingDy)
                    }
                }
            }
            remainingDy -= consumedDy
            val nextGesture = getGesture(target, remainingDy)
            if (nextGesture == gesture) {
                break
            }
            gesture = nextGesture
        }

    }

    override fun onNestedFling(target: View, velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
        if (getGesture(target) == SlideGesture.SLIDE && target.isSlideChildTypeSlider()) {
            this.sliderVelocityY = velocityY
            return true
        }
        return false
    }

    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        if (
            getGesture(target) == SlideGesture.SLIDE
            && (target.isSlideChildTypeBar() || isNotExpandSliderScrolling(target))

        ) {
            this.sliderVelocityY = velocityY
            return true
        }
        return false
    }



    /***********************************************************************分离窗口************************************************************/

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val lastHeaderHeight = headerHeight()
        super.onLayout(changed, left, top, right, bottom)

        maxScrollY= (sliderView()?.measuredHeight ?: 0)+(headerView()?.measuredHeight?:0)- height
        Log.d("maxScrollY","==$maxScrollY")
        pullToRefreshHeaderPresenter.onLayout()
        if (lastGesture == SlideGesture.REFRESH) {
            updateHeaderSliderForRefresh()
        } else {
            if (disableLayout) {
                return
            }
            changeHeaderTop(headerTop)
            val curHeaderHeight = headerHeight()
            val overrideSliderTop =
                if (lastGesture == SlideGesture.SCROLL && lastHeaderHeight != curHeaderHeight) curHeaderHeight else null
            changeSliderTop(overrideSliderTop ?: sliderTop)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        pullToRefreshHeaderPresenter.onContainerHeightReady(h)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        pullToRefreshHeaderPresenter.onDetach()
        cancelHeaderAnimator()
        cancelSliderAnimator()
    }

    /*****************************************************************function******************************************************************/

    fun doOnHeaderVisibleRangeChange(listener: (headerVisibleRange: IntRange) -> Unit) {
        this.onHeaderVisibleRangeChangeListener = listener
        dispatchHeaderVisibleRangeChange(true)
    }

    fun doOnHeaderUpdate(listener: (headerTop: Int) -> Unit) {
        this.onHeaderUpdateListener = listener
        listener(headerTop)
    }

    fun doOnSliderExpandChange(listener: (expand: Boolean) -> Unit) {
        this.onSliderExpandChangeListener = listener
        listener(sliderExpand)
    }

    fun doOnSliderOffsetChange(listener: (sliderTop: Int) -> Unit) {
        this.onSliderOffsetChangeListener = listener
    }

    fun setOnRefreshListener(listener: (byPull: Boolean, isSliderExpand: Boolean) -> Unit) {
        pullToRefreshHeaderPresenter.setOnRefreshListener {
            listener(it, isSliderExpanded())
        }
    }
}