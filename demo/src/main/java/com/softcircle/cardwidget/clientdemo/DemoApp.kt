package com.softcircle.cardwidget.clientdemo

import android.app.Application
import com.softcircle.cardwidget.client.CardWidgetClient

class DemoApp : Application() {
    override fun onCreate() {
        super.onCreate()
        CardWidgetClient.installPanels(this) {
            panel("stopwatch", label = "秒表演示") { host ->
                StopwatchPanelView(host.context).also { panel ->
                    panel.bind(sourceLabel = "远程面板", cardWidgetId = host.cardWidgetId)
                    host.setClickUri(panel.browserButton, "https://www.baidu.com")
                }
            }
            panel(
                id = "promo",
                label = "运营演示",
                partnerId = "softcircle-widget-demo"
            ) { host ->
                PromoPanelView(host.context).also { panel ->
                    host.setClickOpenApp(panel.openAppButton, MainActivity::class.java)
                }
            }
        }
    }
}
