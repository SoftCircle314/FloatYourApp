package com.softcircle.cardwidget.clientdemo

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * 单面板预览页：壳宽 320dp（对齐 SoftCircle 浮层），高度吃满内容区
 *（对齐负一屏 ViewPager 一页，而不是 XML minHeight）。
 */
class PanelPreviewActivity : AppCompatActivity() {

    private var stopwatch: StopwatchPanelView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val panelId = intent.getStringExtra(EXTRA_PANEL_ID).orEmpty()
        val density = resources.displayMetrics.density
        val pad = (16 * density).toInt()
        val shellW = (SOFTCIRCLE_SHELL_WIDTH_DP * density).toInt()

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(pad, pad, pad, pad)
            setBackgroundColor(Color.parseColor("#F5F5F5"))
        }

        val panelTitle = when (panelId) {
            PANEL_PROMO -> getString(R.string.panel_promo_label)
            else -> getString(R.string.panel_stopwatch_label)
        }
        setTitle(panelTitle)
        root.addView(TextView(this).apply {
            text = getString(R.string.preview_page_hint, panelTitle)
            textSize = 13f
            setTextColor(Color.parseColor("#616161"))
            setPadding(0, 0, 0, pad / 2)
        })
        // 建议尺寸仅给开发者对照 XML，不出现在 SoftCircle 用户添加弹框
        root.addView(TextView(this).apply {
            text = getString(
                R.string.preview_dev_meta,
                XML_MIN_WIDTH_DP,
                XML_MIN_HEIGHT_DP,
                panelId.ifBlank { PANEL_STOPWATCH }
            )
            textSize = 12f
            setTextColor(Color.parseColor("#455A64"))
            setPadding(0, 0, 0, pad)
        })

        val shell = FrameLayout(this).apply {
            background = GradientDrawable().apply {
                setColor(Color.WHITE)
                setStroke((1 * density).toInt(), Color.parseColor("#BDBDBD"))
                cornerRadius = 8 * density
            }
            clipToOutline = true
        }
        root.addView(
            shell,
            LinearLayout.LayoutParams(shellW, 0, 1f).apply {
                gravity = Gravity.CENTER_HORIZONTAL
            }
        )

        when (panelId) {
            PANEL_PROMO -> {
                val promo = PromoPanelView(this)
                promo.openAppButton.setOnClickListener { /* 已在本 App */ }
                shell.addView(
                    promo,
                    FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                    )
                )
            }
            else -> {
                val sw = StopwatchPanelView(this).also { stopwatch = it }
                sw.bind(sourceLabel = "本地预览 · 对齐负一屏")
                sw.wireLocalBrowserClick()
                shell.addView(
                    sw,
                    FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                    )
                )
            }
        }

        setContentView(root)

        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                pad + bars.left,
                pad + bars.top,
                pad + bars.right,
                pad + bars.bottom
            )
            insets
        }
    }

    override fun onDestroy() {
        stopwatch?.stopTicker()
        stopwatch = null
        super.onDestroy()
    }

    companion object {
        const val EXTRA_PANEL_ID = "panel_id"
        const val PANEL_STOPWATCH = "stopwatch"
        const val PANEL_PROMO = "promo"
        /** SoftCircle float_control_decor 壳宽 */
        const val SOFTCIRCLE_SHELL_WIDTH_DP = 320
        /** 与 res/xml/card_widget_info_default.xml 中 minWidth/minHeight 对齐 */
        const val XML_MIN_WIDTH_DP = 280
        const val XML_MIN_HEIGHT_DP = 360

        fun intent(context: Context, panelId: String): Intent {
            return Intent(context, PanelPreviewActivity::class.java)
                .putExtra(EXTRA_PANEL_ID, panelId)
        }
    }
}
