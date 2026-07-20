# 快速接入

## 1. 依赖

先加仓库（本 Demo 仓已内嵌 `maven-repo/`；第三方工程可指向本仓 raw 地址或复制该目录）：

```gradle
repositories {
    maven { url = uri("https://raw.githubusercontent.com/SoftCircle314/FloatYourApp/main/maven-repo") }
    // 或本地：maven { url = uri("/path/to/FloatYourApp/maven-repo") }
    google()
    mavenCentral()
}
```

```gradle
implementation "com.softcircle.cardwidget:cardwidget-client:0.1.0"
```

Client AAR 为专有二进制；本仓 Demo/文档为 Apache-2.0。

## 2. XML 声明面板（宿主扫描只读 XML，不拉起进程）

多面板时在 `res/xml/card_widget_info_default.xml` 写 `<panel>`；`card:panelId` 须与代码一致：

```xml
<cardwidget-provider ... android:minWidth="280dp" android:minHeight="360dp"
    card:partnerId="your-partner-id">
    <panel card:panelId="stopwatch" android:label="@string/panel_stopwatch_label"
        android:minHeight="360dp" />
    <panel card:panelId="promo" android:label="@string/panel_promo_label"
        android:minHeight="360dp" />
</cardwidget-provider>
```

负一屏是**一页一个面板**，壳宽约 **320dp**，高度为浮层内容区。Demo：主界面入口 → 各自预览页（320dp 宽、高度铺满），勿在同一屏叠两张卡估高。

## 3. 注册工厂

```kotlin
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        CardWidgetClient.installPanels(this) {
            panel("stopwatch", label = "秒表演示") { host ->
                // 能力样例
                StopwatchPanelView(host.context).also { ... }
            }
            panel("promo", label = "运营演示") { host ->
                // 换量：标题 + 图 + 文案 + 跳转本 App
                PromoPanelView(host.context).also {
                    host.setClickOpenApp(it.openAppButton, MainActivity::class.java)
                }
            }
        }
    }
}
```

单面板也可：`CardWidgetClient.install(this) { host -> MyView(host.context) }`

点击跳转请用 `setClickUri` / `setClickOpenApp` / `setClickOpenSoftCircle`（宿主代发）。

## 4. 验证

装 SoftCircle → 添加面板。列表中每个 XML `<panel>` 各占一项（首次添加不应再弹「打开 Demo」类关联启动提示）。
