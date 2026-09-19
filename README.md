# Swing Modal Dialog

[![Maven Central](https://img.shields.io/maven-central/v/io.github.dj-raven/modal-dialog?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.dj-raven/modal-dialog)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

A Java Swing library styled with [FlatLaf](https://github.com/JFormDesigner/FlatLaf) that adds animated, ready-to-use
components for desktop applications.

- [x] Modal dialog
- [x] Drawer
- [x] Toast Notification

![Simple 1](screenshot/simple%201.png)
![Simple 2](screenshot/simple%202.png)
![Simple 3](screenshot/simple%203.png)

## Installation

Add the dependency

``` xml
<dependency>
    <groupId>io.github.dj-raven</groupId>
    <artifactId>modal-dialog</artifactId>
    <version>2.6.2</version>
</dependency>
```

### Snapshots

To get the latest updates before the release, you can use the snapshot version
from [Sonatype Central](https://central.sonatype.com/service/rest/repository/browse/maven-snapshots/io/github/dj-raven/modal-dialog/)

``` xml
<repositories>
    <repository>
        <name>Central Portal Snapshots</name>
        <id>central-portal-snapshots</id>
        <url>https://central.sonatype.com/repository/maven-snapshots/</url>
    </repository>
</repositories>
```

Add the snapshot version

``` xml
<dependency>
    <groupId>io.github.dj-raven</groupId>
    <artifactId>modal-dialog</artifactId>
    <version>2.6.3-SNAPSHOT</version>
</dependency>
```

## Demo

Get jar file here: [latest-releases](https://github.com/DJ-Raven/swing-modal-dialog/releases/latest)

## Documentation

| Component                            | Description                                                                |
|--------------------------------------|----------------------------------------------------------------------------|
| [Modal Dialog](docs/modal-dialog.md) | Animated dialogs, confirmations and custom in-window modals                |
| [Toast Notification](docs/toast.md)  | Transient status messages, including promise-based and fully custom toasts |
| [Drawer](docs/drawer.md)             | Responsive navigation side-drawer with nested menus, header and footer     |
| [Extras](docs/extras.md)             | Standalone components: `AvatarIcon`, `LightDarkButton`, `SlidePane`        |

## Library Resources

- [FlatLaf](https://github.com/JFormDesigner/FlatLaf) - FlatLaf library for the modern UI design theme
- [MigLayout](https://github.com/mikaelgrev/miglayout) - MigLayout library for flexible layout management

## Changelog

See [CHANGELOG.md](CHANGELOG.md) for release notes.

## License

[MIT](LICENSE) © Raven Laing
