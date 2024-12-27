package com.jetpack.mvvm.widget.slide

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.Px
import androidx.core.view.updatePadding
import com.jetpack.mvvm.R
import com.jetpack.mvvm.widget.slide.useAttrs

class LinkPagerVerticalMarginFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): FrameLayout(context, attrs, defStyleAttr) {

    private var minVerticalMargin = 0

    private var paddingBottom: Int =0
        private set

    init {
        useAttrs(attrs, R.styleable.LinkPagerVerticalMarginFrameLayout) {
            minVerticalMargin = getDimensionPixelSize(R.styleable.LinkPagerVerticalMarginFrameLayout_min_vertical_margin, 0)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (minVerticalMargin > 0) {
            super.onMeasure(
                widthMeasureSpec,
                MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec) - minVerticalMargin,
                 MeasureSpec.getMode(heightMeasureSpec))
            )
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    fun setMinVerticalMargin(margin: Int) {
        minVerticalMargin = margin
        requestLayout()
    }


    fun updatePaddingBottom(@Px bottom: Float ){
        this.paddingBottom= bottom.toInt()
        updatePadding(0,0,0, bottom.toInt())
    }
}
