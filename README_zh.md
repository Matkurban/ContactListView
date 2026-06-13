[English](README.md) | [中文](README_zh.md)

# ContactListView

[![Maven Central](https://img.shields.io/maven-central/v/io.github.matkurban/contactlistview)](https://central.sonatype.com/artifact/io.github.matkurban/contactlistview)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)

Compose Multiplatform 版联系人列表库，移植自 [contact_list_view](https://github.com/Matkurban/contact_list_view) Flutter 包。提供 A-Z 分组列表、粘性分组头、右侧字母索引条，以及拖拽索引时的浮动游标。

仓库地址：[github.com/Matkurban/ContactListView](https://github.com/Matkurban/ContactListView)

## 截图

|                         截图                         |                                                  截图                                                   |
|:--------------------------------------------------:|:-----------------------------------------------------------------------------------------------------:|
| ![截图 1](doc/images/Screenshot_20260202_222935.jpg) | [![视频预览图](doc/images/Screenshot_20260202_224751.jpg)](doc/videos/Screenrecording_20260202_222939.mp4) |

## 特性

- **A-Z 分组** — 按标签分组，`#` 排在最后
- **粘性分组头** — 滚动时可选固定顶部分组头
- **字母索引条** — 点击或拖拽快速跳转到对应分组
- **浮动游标** — 拖拽索引条时显示当前字母提示
- **自定义构建器** — `stickyHeaderBuilder`、`cursorBuilder`、索引条样式构建器
- **`startContent` / `endContent`** — 列表头部与尾部的扩展插槽
- **100% commonMain** — 所有平台共享同一套源码

## 支持的平台

| 平台      | 目标                    |
|---------|-----------------------|
| Android | minSdk 24             |
| iOS     | Arm64、Simulator Arm64 |
| Desktop | JVM                   |
| Web     | JS、Wasm               |

## 安装

### Version Catalog（`gradle/libs.versions.toml`）

在 `gradle/libs.versions.toml` 中添加：

```toml
[versions]
contactlistview = "1.0.0"

[libraries]
contactlistview = { module = "io.github.matkurban:contactlistview", version.ref = "contactlistview" }
```

然后在 `build.gradle.kts` 中引用：

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.contactlistview)
        }
    }
}
```

### 直接依赖

或在 `commonMain` 中直接添加依赖：

```kotlin
// build.gradle.kts
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation("io.github.matkurban:contactlistview:1.0.0")
        }
    }
}
```

从 `io.github.matkurban.contactlistview.*` 导入 API。

## 快速开始

```kotlin
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import io.github.matkurban.contactlistview.ContactListView

data class Contact(val name: String)

ContactListView(
    contactsList = contacts,
    tag = { contact ->
        val first = contact.name.firstOrNull()?.uppercaseChar() ?: '#'
        if (first in 'A'..'Z') first.toString() else "#"
    },
    sticky = true,
    startContent = {
        item { ListItem(headlineContent = { Text("新的朋友") }) }
    },
    endContent = {
        item { ListItem(headlineContent = { Text("总共 ${contacts.size} 位好友") }) }
    },
    itemBuilder = { contact ->
        ListItem(headlineContent = { Text(contact.name) })
    },
)
```

## 自定义

| API                                | 说明                            |
|------------------------------------|-------------------------------|
| `stickyHeaderBuilder`              | 自定义分组头；参数为 `tag` 和 `isPinned` |
| `cursorBuilder`                    | 自定义拖拽索引条时的浮动游标                |
| `indexBarBoxDecorationBuilder`     | 按选中状态定制索引条背景                  |
| `indexBarTextStyleBuilder`         | 按选中状态定制索引条文字样式                |
| `stickyHeaderBoxDecorationBuilder` | 按固定状态定制分组头背景                  |
| `stickyHeaderTextStyleBuilder`     | 按固定状态定制分组头文字样式                |
| `startContent` / `endContent`      | 在分组列表前后插入额外列表项                |

## 示例应用

本仓库包含各平台 Demo：

| 模块           | 说明                                  |
|--------------|-------------------------------------|
| `sample`     | 共享 Demo UI（`App`、`ContactScreen` 等） |
| `androidApp` | Android 示例壳工程                       |
| `desktopApp` | Compose Desktop 桌面应用                |
| `webApp`     | 浏览器应用（Wasm）                         |

运行示例：

```bash
./gradlew :androidApp:assembleDebug
./gradlew :desktopApp:run
./gradlew :webApp:wasmJsBrowserDevelopmentRun
./gradlew :contactlistview:jvmTest
```

## 环境要求

- Kotlin 2.4.0+
- Compose Multiplatform 1.11.1+
- JDK 17+

## 项目结构

- `contactlistview` — 发布的库模块（100% `commonMain`）
- `sample` — Demo UI（不发布）
- `androidApp` / `desktopApp` / `webApp` — 各平台示例应用

## 相关链接

- [原版 Flutter 包](https://github.com/Matkurban/contact_list_view)

## 许可证

本项目采用 [MIT License](https://opensource.org/licenses/MIT)。
