package com.softcircle.cardwidget.clientdemo

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * Demo 入口：负一屏是一页一个 Widget，本地也按「主界面 → 独立预览页」进入，
 * 避免在同一屏叠两张卡误判高度。
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val density = resources.displayMetrics.density
        val pad = (16 * density).toInt()
        val gap = (12 * density).toInt()

        val column = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(pad, pad, pad, pad)
            setBackgroundColor(Color.parseColor("#F5F5F5"))
        }

        column.addView(TextView(this).apply {
            text = getString(R.string.home_hint)
            textSize = 14f
            setTextColor(Color.parseColor("#616161"))
            setPadding(0, 0, 0, pad)
        })

        addPanelEntry(
            column,
            label = getString(R.string.panel_stopwatch_label),
            minWidthDp = 280,
            minHeightDp = 360,
            panelId = PanelPreviewActivity.PANEL_STOPWATCH,
            gapAfter = gap
        ) {
            startActivity(
                PanelPreviewActivity.intent(this, PanelPreviewActivity.PANEL_STOPWATCH)
            )
        }

        addPanelEntry(
            column,
            label = getString(R.string.panel_promo_label),
            minWidthDp = 280,
            minHeightDp = 360,
            panelId = PanelPreviewActivity.PANEL_PROMO,
            gapAfter = 0
        ) {
            startActivity(
                PanelPreviewActivity.intent(this, PanelPreviewActivity.PANEL_PROMO)
            )
        }

        val scroll = ScrollView(this).apply {
            setBackgroundColor(Color.parseColor("#F5F5F5"))
            addView(column)
        }
        setContentView(scroll)

        ViewCompat.setOnApplyWindowInsetsListener(scroll) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }
    }

    private fun addPanelEntry(
        column: LinearLayout,
        label: String,
        minWidthDp: Int,
        minHeightDp: Int,
        panelId: String,
        gapAfter: Int,
        onClick: () -> Unit
    ) {
        column.addView(
            entryButton(label, onClick),
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )
        column.addView(
            TextView(this).apply {
                text = getString(R.string.home_entry_meta, minWidthDp, minHeightDp, panelId)
                textSize = 12f
                setTextColor(Color.parseColor("#757575"))
                setPadding(0, (4 * resources.displayMetrics.density).toInt(), 0, gapAfter)
            },
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )
    }

    private fun entryButton(label: String, onClick: () -> Unit): Button {
        return Button(this).apply {
            text = label
            setAllCaps(false)
            setOnClickListener { onClick() }
        }
    }
}
