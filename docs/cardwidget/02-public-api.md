# 公开 API（第三方只需这些）

## 约定

- **调用时机**：在 `Application.onCreate` 中注册，且须在 SoftCircle 首次创建面板之前。
- **`panelId`**：`panel("…")` 的 id 须与 `res/xml/card_widget_info_default.xml` 里 `card:panelId` **一致**。
- **扫描列表**：以 XML `<panel>`（及 label / 预览等）为准；代码里的 `label` 等为辅助或回退，不替代 XML 声明。

示例流程见 [快速接入](01-quickstart.md)。

## CardWidgetClient

| API | 说明 |
|-----|------|
| `installPanels(app) { panel(...) }` | 多面板工厂注册（**推荐**） |
| `install(app) { host -> View }` | 单面板；内部 `panelId` 为 `DEFAULT_PANEL_ID`（`"default"`），XML 须对应 |
| `isInstalled()` | 是否已注册（调试用） |
| `PACKAGE_SOFTCIRCLE` | SoftCircle 包名；一般不用，供 `setClickOpenSoftCircle` |
| `DEFAULT_PANEL_ID` | 单面板默认 id：`"default"` |

## Registry.panel

`installPanels` 的 DSL：

| 参数 | 说明 |
|------|------|
| `id` | 必填，与 XML `card:panelId` 一致 |
| `label` | 辅助显示名；列表仍以 XML 为准 |
| `minWidthDp` / `minHeightDp` | 可选，默认 `280` / `200` |
| `partnerId` | 可选，换量归因；亦可只在 XML 声明（见 [换量合作](04-partnership.md)） |
| `factory` | `(PanelHost) -> View`，返回面板根 View |

## PanelHost

工厂回调里拿到的宿主能力：

| API | 说明 |
|-----|------|
| `context` | 用于创建 View 的 Context |
| `cardWidgetId` | 当前面板实例 id |
| `panelId` | 当前面板逻辑 id |
| `widthPx` / `heightPx` | 宿主给出的内容区尺寸 |
| `setClickUri(view, uri)` | 点击后由**宿主**代发 `ACTION_VIEW`（客户端被杀也能跳） |
| `setClickIntent(view, intent)` | 点击后由**宿主**代发 Intent |
| `setClickOpenApp(view, Activity::class.java)` | 打开本 App 指定 Activity |
| `setClickOpenSoftCircle(view)` | 打开 SoftCircle |

点击跳转请用上述 API，勿在面板进程里直接 `startActivity` 充当唯一路径（后台/force-stop 时不可靠）。

## Manifest

`CardWidgetEntryProvider` / `WakeActivity` 等由 Client AAR **自动合并**，业务无需再声明。
