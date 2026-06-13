# ContactListView

[![Maven Central](https://img.shields.io/maven-central/v/io.github.matkurban/contactlistview)](https://central.sonatype.com/artifact/io.github.matkurban/contactlistview)

Kotlin Multiplatform (Compose Multiplatform) port of the Flutter [`contact_list_view`](https://github.com/Matkurban/contact_list_view) package.

Repository: [github.com/Matkurban/ContactListView](https://github.com/Matkurban/ContactListView)

## Features

- A-Z grouped contact list with `#` sorted last
- Sticky section headers (optional)
- Right-side alphabet index bar with tap/drag navigation
- Floating letter cursor while dragging the index bar
- Customizable header, cursor, and index bar builders
- `startContent` / `endContent` slots for leading and trailing list content

## Requirements

- JDK 17+
- Kotlin 2.4.0
- Compose Multiplatform 1.11.1

## Usage

```kotlin
import io.github.matkurban.contactlistview.ContactListView

ContactListView(
    contactsList = contacts,
    tag = { contact -> contact.name.first().uppercase() },
    sticky = true,
    itemBuilder = { contact ->
        ListItem(headlineContent = { Text(contact.name) })
    },
)
```

## Sample

```bash
cd ContactListView
./gradlew :androidApp:assembleDebug          # Android
./gradlew :desktopApp:run                    # Desktop
./gradlew :webApp:wasmJsBrowserDevelopmentRun  # Web
./gradlew :contactlistview:jvmTest
```

## Modules

- `contactlistview` — reusable CMP library
- `sample` — shared demo UI
- `androidApp` — Android sample shell
- `desktopApp` — Desktop sample shell
- `webApp` — Web sample shell

## Publishing

See [BUILD.md](BUILD.md) for Maven Central publishing instructions.
