# Swing Modal Dialog

Java swing library build with flatlaf look and feel for desktop application. This library include more custom components
and support animation

- [x] Modal dialog
- [x] Drawer
- [x] Toast Notification

<img src="https://github.com/DJ-Raven/swing-modal-dialog/blob/main/screenshot/simple 1.jpg" alt="simple 1"/>
<img src="https://github.com/DJ-Raven/swing-modal-dialog/blob/main/screenshot/simple 2.jpg" alt="simple 2"/>
<img src="https://github.com/DJ-Raven/swing-modal-dialog/blob/main/screenshot/simple 3.jpg" alt="simple 3"/>

## Installation

This project library do not available in maven central. so you can install with the jar library

- Get jar library or demo
  here: [latest-releases](https://github.com/DJ-Raven/swing-modal-dialog/releases/latest)
- Copy jar library file to the root project. exp: `library/modal-dialog-1.0.0.jar`
- Add this code to `pom.xml`

``` xml
<dependency>
    <groupId>raven.modaldialog</groupId>
    <artifactId>modal-dialog</artifactId>
    <version>1.0.0</version>
    <scope>system</scope>
    <systemPath>${basedir}/library/modal-dialog-1.0.0.jar</systemPath>
</dependency>
```

- Other library are use with this library

``` xml
<dependency>
  <groupId>com.formdev</groupId>
  <artifactId>flatlaf</artifactId>
  <version>3.4.1</version>
</dependency>

<dependency>
  <groupId>com.formdev</groupId>
  <artifactId>flatlaf-extras</artifactId>
  <version>3.4.1</version>
</dependency>

<dependency>
    <groupId>com.miglayout</groupId>
    <artifactId>miglayout-swing</artifactId>
    <version>11.3</version>
</dependency>
```

## Document

Not yet

## Library Resources

- [FlatLaf](https://github.com/JFormDesigner/FlatLaf) - FlatLaf library for the modern UI design theme
- [MigLayout](https://github.com/mikaelgrev/miglayout) - MigLayout library for flexible layout management
