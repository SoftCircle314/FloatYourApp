# 公开 API（第三方只需这些）

## CardWidgetClient

| API | 说明 |
|-----|------|
| `installPanels(app) { panel(...) }` | 多面板工厂注册（推荐）；列表展示以 XML `<panel>` 为准 |
| `install(app) { host -> View }` | 单面板注册 |
| `isInstalled()` | 是否已注册 |
| `PACKAGE_SOFTCIRCLE` | SoftCircle 包名常量 |

## PanelHost

| API | 说明 |
|-----|------|
| `context` / `cardWidgetId` / `widthPx` / `heightPx` | 创建上下文 |
| `setClickOpenApp(view, Activity::class.java)` | 打开本 App |
| `setClickOpenSoftCircle(view)` | 打开 SoftCircle |
| `setClickUri(view, uri)` | 打开链接 |
| `setClickIntent(view, intent)` | 自定义 Intent（由宿主代发） |

Manifest / Provider / WakeActivity：AAR 自动合并，业务无需声明。
