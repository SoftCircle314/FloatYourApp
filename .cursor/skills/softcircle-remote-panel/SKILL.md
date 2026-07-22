---
name: softcircle-remote-panel
description: >-
  Integrate SoftCircle negative-screen remote panels (CardWidget client) into an
  Android app: maven AAR, XML panel declaration, CardWidgetClient.installPanels,
  PanelHost click APIs, and SoftCircle verification. Use when the user wants a
  SoftCircle remote panel/page, CardWidget client setup, FloatYourApp-style
  integration, 负一屏远程面板, or installPanels / card_widget_info_default.xml.
---

# SoftCircle 远程面板接入

在**第三方 Android 工程**中接入 SoftCircle 负一屏远程面板（不是系统 AppWidget，不是 WebView 通用页）。

权威文档（本仓）：
- [docs/cardwidget/quickstart.md](../../../docs/cardwidget/quickstart.md)
- [docs/cardwidget/public-api.md](../../../docs/cardwidget/public-api.md)
- Demo：`demo/`（`DemoApp.kt`、`PromoPanelView`、`StopwatchPanelView`）

## 先问清

1. 目标工程路径 / applicationId / `Application` 类名  
2. 单面板还是多面板；每个面板的 `panelId`（英文 id）与展示名  
3. 点击行为：打开本 App Activity / URI / SoftCircle  
4. 是否需要 `partnerId`（换量；可选）

未说明时：默认**单面板**，`panelId = "default"`，CTA = `setClickOpenApp` 打开主 Activity。

## 硬约束（勿违反）

- 负一屏真机效果必须已安装 **SoftCircle**：[https://www.softcircle.cn](https://www.softcircle.cn)  
- Client 坐标：`com.softcircle.cardwidget:cardwidget-client:0.1.0`（专有 AAR；本仓 Demo/文档 Apache-2.0）  
- 在 `Application.onCreate` 注册，且须在 SoftCircle 首次创建面板之前  
- XML `card:panelId` **必须**与 `panel("…")` 的 id 一致  
- 扫描列表以 XML `<panel>` 为准；代码 `label` 只是辅助  
- 点击跳转用 `PanelHost.setClickUri` / `setClickIntent` / `setClickOpenApp` / `setClickOpenSoftCircle`，勿只靠面板进程里 `startActivity`  
- Manifest **不必**手写 Provider；AAR 会合并  
- 负一屏**一页一个面板**；壳宽约 **320dp**；本地预览按 320dp 宽估高，勿同屏叠两张卡  

## 步骤

### 1. 依赖

```gradle
repositories {
    maven { url = uri("https://raw.githubusercontent.com/SoftCircle314/FloatYourApp/main/maven-repo") }
    // 或：克隆本仓后 maven { url = uri("/path/to/FloatYourApp/maven-repo") }
    google()
    mavenCentral()
}

dependencies {
    implementation "com.softcircle.cardwidget:cardwidget-client:0.1.0"
}
```

### 2. XML（覆盖默认资源名）

新建/覆盖：`app/src/main/res/xml/card_widget_info_default.xml`

单面板示例：

```xml
<?xml version="1.0" encoding="utf-8"?>
<cardwidget-provider xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:card="http://schemas.softcircle.com/cardwidget"
    android:label="@string/card_widget_label"
    android:minWidth="280dp"
    android:minHeight="360dp"
    android:updatePeriodMillis="0">
    <panel
        card:panelId="default"
        android:label="@string/card_widget_label"
        android:minWidth="280dp"
        android:minHeight="360dp" />
</cardwidget-provider>
```

多面板：每个 `<panel card:panelId="…">` 对应一项扫描结果。可选 `android:previewImage`、`card:partnerId`（见 partnership 文档）。

### 3. 注册工厂

`Application.onCreate`：

```kotlin
CardWidgetClient.installPanels(this) {
    panel("default", label = "我的面板") { host ->
        MyPanelView(host.context).also { view ->
            host.setClickOpenApp(view.ctaButton, MainActivity::class.java)
        }
    }
}
```

单面板也可用：`CardWidgetClient.install(this) { host -> MyPanelView(host.context) }`（内部 id 为 `"default"`，XML 须匹配）。

### 4. 面板 View

- 用 `host.context` 创建根 View  
- 布局按约 320dp 宽、高度可滚动/自适应内容区  
- 需要跳转时绑定 `host.setClick*`  
- 参考 Demo：`demo/.../PromoPanelView.kt`（换量卡）、`StopwatchPanelView.kt`（持续刷新）

### 5. 验证清单

1. 已安装 SoftCircle  
2. 安装用户 App（Debug 亦可）  
3. SoftCircle → 负一屏 → **添加远程面板** → 看到 XML 声明的项并添加  
4. 点击 CTA 能打开目标（后台/force-stop 后仍应走宿主代发）

## Agent 输出要求

- 直接改用户工程：`build.gradle` / `settings` 仓库、XML、`Application`、面板 View、必要 strings  
- 改完列出：改了哪些文件、`panelId` 列表、如何验证  
- 不要引入 SoftCircleServer / host-lib；不要发布 sources；不要编造未文档化的 API  
- 合作联系（仅用户问换量时提及）：softcircle@foxmail.com / 13554896856
