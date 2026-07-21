# 换量合作

面板发现**不设白名单**（与系统 AppWidget 一致）：接入 SDK 并声明 XML 后，用户安装你的 App，即可在 SoftCircle 负一屏「添加远程面板」里看到并添加。

## 可选：partnerId（换量 / 统计）

若要做互推结算，可在面板 XML 声明 `partnerId`（不填也能被扫描添加）：

```xml
<cardwidget-provider
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:card="http://schemas.softcircle.com/cardwidget"
    android:label="@string/card_widget_default_label"
    android:minWidth="280dp"
    android:minHeight="360dp"
    card:partnerId="your-partner-id">
    <!-- 多面板时声明 panel；扫描只读 XML，不会冷启动你的进程 -->
    <panel card:panelId="promo" android:label="运营卡" android:minHeight="360dp" />
</cardwidget-provider>
```

运营卡建议结构：**标题 + 配图 + 文案 + 一个跳转本 App**（见 Demo `PromoPanelView`）。

覆盖路径：`res/xml/card_widget_info_default.xml`（与 SDK 默认资源同名即可）。

## 面板内 CTA 互推

```kotlin
host.setClickOpenApp(openAppBtn, MainActivity::class.java)
host.setClickOpenSoftCircle(softCircleBtn)
```

合作与商务联系：

- 邮箱：softcircle@foxmail.com
- 电话：13554896856
