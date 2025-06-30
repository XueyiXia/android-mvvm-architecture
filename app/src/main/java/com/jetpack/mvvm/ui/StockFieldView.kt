package com.jetpack.mvvm.ui

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.robinhood.ticker.TickerUtils
import com.robinhood.ticker.TickerView
import kotlin.let
import kotlin.toString
import androidx.core.content.withStyledAttributes
import com.jetpack.mvvm.R

class StockFieldView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

  companion object {
    const val ORIENTATION_HORIZONTAL = 0
    const val ORIENTATION_VERTICAL = 1

    const val GRAVITY_LEFT = 0
    const val GRAVITY_RIGHT = 1
    const val GRAVITY_CENTER = 2
  }

  private val fieldname: TextView
  private val fieldvalue: TickerView

  init {
    LayoutInflater.from(context).inflate(R.layout.stock_field_view, this, true)
    fieldname = findViewById(R.id.fieldname)
    fieldvalue = findViewById(R.id.fieldvalue)
    fieldvalue.setCharacterLists(TickerUtils.provideNumberList())
    attrs?.let {
        context.withStyledAttributes(it, R.styleable.StockFieldView) {
            val orientation = getInt(R.styleable.StockFieldView_or, 0)
            if (orientation == ORIENTATION_HORIZONTAL) {
                setOrientation(HORIZONTAL)
                fieldname.layoutParams =
                    LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.5f)
                fieldvalue.layoutParams =
                    LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.5f)
                fieldvalue.gravity = Gravity.END
            } else {
                setOrientation(VERTICAL)
                fieldname.layoutParams = LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT
                )
                fieldvalue.layoutParams = LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT
                )
                fieldvalue.gravity = Gravity.START
            }
            weightSum = 1f
            val name = getStringValue(context, this, R.styleable.StockFieldView_name)
            fieldname.text = name
            val textSize = getDimensionPixelSize(R.styleable.StockFieldView_size, 20)
                .toFloat()
            fieldname.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
            fieldvalue.textSize = textSize * 0.9f
            val centerText = getBoolean(R.styleable.StockFieldView_center_text, false)
            when {
                centerText -> {
                    fieldname.gravity = Gravity.CENTER
                    fieldvalue.gravity = Gravity.CENTER
                }

                orientation == ORIENTATION_HORIZONTAL -> {
                    fieldname.gravity = Gravity.START
                    fieldvalue.gravity = Gravity.END
                }

                orientation == ORIENTATION_VERTICAL -> {
                    val textGravity = getInt(R.styleable.StockFieldView_text_gravity, 0)
                    when (textGravity) {
                        GRAVITY_LEFT -> {
                            fieldname.gravity = Gravity.START
                            fieldvalue.gravity = Gravity.START
                        }

                        GRAVITY_RIGHT -> {
                            fieldname.gravity = Gravity.END
                            fieldvalue.gravity = Gravity.END
                        }

                        GRAVITY_CENTER -> {
                            fieldname.gravity = Gravity.CENTER
                            fieldvalue.gravity = Gravity.CENTER
                        }
                    }
                }
            }
        }
    }
  }

  constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int
  ) : this(context, attrs)

  constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int,
    defStyleRes: Int
  ) : this(
      context, attrs
  )

  fun setLabel(text: CharSequence?) {
    fieldname.text = text
  }

  fun setText(text: CharSequence?) {
    fieldvalue.text = text.toString()
  }

  fun setTextColor(color: Int) {
    fieldvalue.textColor = color
  }

  private fun getStringValue(
    context: Context,
    array: TypedArray,
    stylelable: Int
  ): String {
    var name = array.getString(stylelable)
    if (name == null) {
      val stringId = array.getResourceId(stylelable, -1)
      name = if (stringId > 0) {
        context.getString(stringId)
      } else {
        ""
      }
    }
    return name
  }

}