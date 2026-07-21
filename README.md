# FloatYourApp（开源）

SoftCircle **负一屏远程面板** 的接入 Demo + 文档（不是系统桌面 AppWidget，也不是 SDK 源码）。

本仓示例代码与文档采用 **Apache-2.0**。依赖的 Client AAR（`com.softcircle.cardwidget:cardwidget-client`）为 SoftCircle **专有** 二进制，源码不在本仓。

## 依赖

仓库已内嵌 `maven-repo/`（免 token）。也可在克隆后直接构建：

```gradle
repositories {
    maven { url = uri("${rootProject.projectDir}/maven-repo") }
    // 公开 clone 后亦可用：
    // maven { url = uri("https://raw.githubusercontent.com/SoftCircle314/FloatYourApp/main/maven-repo") }
    google()
    mavenCentral()
}

dependencies {
    implementation "com.softcircle.cardwidget:cardwidget-client:0.1.0"
}
```

## 三步接入（多面板）

```kotlin
CardWidgetClient.installPanels(this) {
    panel("stopwatch", label = "秒表演示") { host ->
        StopwatchPanelView(host.context).also {
            it.bind("远程面板", host.cardWidgetId)
            host.setClickUri(it.browserButton, "https://www.baidu.com")
        }
    }
    panel("promo", label = "运营演示") { host ->
        PromoPanelView(host.context).also {
            host.setClickOpenApp(it.openAppButton, MainActivity::class.java)
        }
    }
}
```

Manifest 不用改。SoftCircle 扫描列表会出现 **两个** 面板。

## Demo 说明

| 面板 | 作用 |
|------|------|
| 秒表演示 | 持续刷新 + reattach 状态 + 浏览器跳转 |
| 运营演示 | 典型换量：文案 + 打开本 App |

## 验证

```bash
./gradlew :demo:installDebug
```

装 SoftCircle → 添加「秒表演示」/「运营演示」。

## 更多

- [快速接入](docs/cardwidget/quickstart.md)
- [公开 API](docs/cardwidget/public-api.md)
- [换量合作](docs/cardwidget/partnership.md)
