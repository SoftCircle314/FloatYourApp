package com.softcircle.cardwidget.clientdemo

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.Gravity
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView

/**
 * 秒表面板：Demo 主界面与 SoftCircle 远程面板共用。
 * 按钮：重置秒表、打开浏览器。
 */
class StopwatchPanelView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val titleView = TextView(context).apply {
        textSize = 13f
        setTextColor(Color.parseColor("#FF666666"))
        gravity = Gravity.CENTER
        setPadding(0, 0, 0, dp(8))
    }
    private val timeView = TextView(context).apply {
        text = "00:00.0"
        textSize = 36f
        typeface = Typeface.MONOSPACE
        setTextColor(Color.parseColor("#FF2E7D32"))
        gravity = Gravity.CENTER
        setPadding(0, dp(8), 0, dp(8))
    }
    private val hintView = TextView(context).apply {
        textSize = 11f
        setTextColor(Color.parseColor("#FF888888"))
        gravity = Gravity.CENTER
        setPadding(0, 0, 0, dp(12))
    }
    private val resetButton = Button(context).apply {
        text = "重置秒表"
        setAllCaps(false)
    }
    val browserButton = Button(context).apply {
        text = "打开浏览器"
        setAllCaps(false)
    }

    private val mainHandler = Handler(Looper.getMainLooper())
    private var ticker: Runnable? = null
    private var uiTicking = false

    init {
        orientation = VERTICAL
        setBackgroundColor(Color.WHITE)
        setPadding(dp(16), dp(12), dp(16), dp(12))
        gravity = Gravity.CENTER_HORIZONTAL
        layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        resetButton.setOnClickListener {
            StopwatchState.resetAndStart()
            refreshTimeText()
        }
        addView(titleView)
        addView(timeView)
        addView(hintView)
        addView(resetButton)
        addView(browserButton)
    }

    /**
     * @param sourceLabel 如「App 主界面」或「远程面板」
     * @param cardWidgetId 远程面板 ID；主界面传 -1
     */
    fun bind(sourceLabel: String, cardWidgetId: Int = -1) {
        titleView.text = if (cardWidgetId >= 0) {
            "$sourceLabel  ·  ID $cardWidgetId"
        } else {
            sourceLabel
        }
        hintView.text = "共享计时 · 主界面与远程面板同一状态\n重建面板不会重置（点重置才会清零）"
        StopwatchState.ensureRunning()
        startUiTicker()
    }

    /** 主界面内直接打开浏览器（不经宿主 PendingIntent） */
    fun wireLocalBrowserClick(url: String = "https://www.baidu.com") {
        browserButton.setOnClickListener {
            context.startActivity(
                android.content.Intent(
                    android.content.Intent.ACTION_VIEW,
                    android.net.Uri.parse(url)
                )
            )
        }
    }

    fun stopTicker() {
        ticker?.let { mainHandler.removeCallbacks(it) }
        ticker = null
        uiTicking = false
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        StopwatchState.ensureRunning()
        startUiTicker()
    }

    override fun onDetachedFromWindow() {
        stopTicker()
        super.onDetachedFromWindow()
    }

    private fun startUiTicker() {
        if (uiTicking) {
            refreshTimeText()
            return
        }
        uiTicking = true
        val r = object : Runnable {
            override fun run() {
                refreshTimeText()
                mainHandler.postDelayed(this, 100L)
            }
        }
        ticker = r
        mainHandler.post(r)
    }

    private fun refreshTimeText() {
        timeView.text = formatElapsed(StopwatchState.elapsedMs())
    }

    private fun formatElapsed(elapsedMs: Long): String {
        val totalTenths = elapsedMs / 100
        val tenths = (totalTenths % 10).toInt()
        val totalSeconds = (totalTenths / 10).toInt()
        val seconds = totalSeconds % 60
        val minutes = (totalSeconds / 60) % 60
        val hours = totalSeconds / 3600
        return if (hours > 0) {
            String.format("%d:%02d:%02d.%d", hours, minutes, seconds, tenths)
        } else {
            String.format("%02d:%02d.%d", minutes, seconds, tenths)
        }
    }

    private fun dp(v: Int): Int = (v * resources.displayMetrics.density).toInt()
}
