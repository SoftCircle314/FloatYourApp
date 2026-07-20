package com.softcircle.cardwidget.clientdemo

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat

/**
 * 运营卡样例：标题 + 配图 + 文案 + 一个跳转本 App（典型换量接入）。
 */
class PromoPanelView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    val openAppButton = Button(context).apply {
        text = context.getString(R.string.promo_cta)
        setAllCaps(false)
    }

    init {
        orientation = VERTICAL
        gravity = Gravity.CENTER_HORIZONTAL
        setBackgroundColor(Color.WHITE)
        val pad = dp(16)
        setPadding(pad, pad, pad, pad)

        addView(TextView(context).apply {
            text = context.getString(R.string.promo_title)
            textSize = 20f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.parseColor("#212121"))
            gravity = Gravity.CENTER
        })

        addView(
            ImageView(context).apply {
                setImageDrawable(ContextCompat.getDrawable(context, R.drawable.promo_banner))
                scaleType = ImageView.ScaleType.CENTER_CROP
                adjustViewBounds = true
                contentDescription = context.getString(R.string.promo_title)
            },
            LayoutParams(LayoutParams.MATCH_PARENT, dp(120)).apply {
                topMargin = dp(12)
                bottomMargin = dp(12)
            }
        )

        addView(TextView(context).apply {
            text = context.getString(R.string.promo_body)
            textSize = 14f
            setTextColor(Color.parseColor("#616161"))
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, pad)
        })

        addView(
            openAppButton,
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        )
    }

    private fun dp(v: Int): Int = (v * resources.displayMetrics.density).toInt()
}
