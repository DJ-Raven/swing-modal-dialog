# Swing Modal Dialog

Java swing library build with flatlaf look and feel for desktop application. This library include more custom components
and support animation

- [x] Modal dialog
- [x] Drawer
- [x] Toast Notification

![Simple 1](screenshot/simple%201.png)
![Simple 2](screenshot/simple%202.png)
![Simple 3](screenshot/simple%203.png)

## Installation

[![Maven Central](https://img.shields.io/maven-central/v/io.github.dj-raven/modal-dialog?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.dj-raven/modal-dialog)

Add the dependency
``` xml
<dependency>
    <groupId>io.github.dj-raven</groupId>
    <artifactId>modal-dialog</artifactId>
    <version>2.5.2</version>
</dependency>
```

### Snapshots
To get the latest updates before the release, you can use the snapshot version from [Sonatype Central](https://central.sonatype.com/service/rest/repository/browse/maven-snapshots/io/github/dj-raven/modal-dialog/)

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
    <version>2.6.0-SNAPSHOT</version>
</dependency>
```

## Demo
Get jar file here: [latest-releases](https://github.com/DJ-Raven/swing-modal-dialog/releases/latest)

## Documentation

### Modal Dialog

Modal dialogs are overlay windows that require user interaction before continuing with the main application.

#### Basic Usage

```java
import raven.modal.ModalDialog;
import raven.modal.component.SimpleModalBorder;

// Create a simple modal with content
JPanel content = new JPanel();
content.add(new JLabel("Hello, World!"));

// Show modal dialog
ModalDialog.showModal(parentComponent, new SimpleModalBorder(
    content, 
    "Dialog Title", 
    SimpleModalBorder.OK_CANCEL_OPTION, 
    null
));
```

#### Advanced Modal with Options

```java
import raven.modal.option.Option;
import raven.modal.option.Location;

// Create custom options
Option option = ModalDialog.createOption()
    .setAnimationEnabled(true)
    .setCloseOnPressedEscape(true)
    .setBackgroundClickType(Option.BackgroundClickType.CLOSE_MODAL)
    .setOpacity(0.7f);

// Set position and behavior
option.getLayoutOption()
    .setLocation(Location.CENTER, Location.CENTER)
    .setMovable(true);

// Show modal with custom options
ModalDialog.showModal(parentComponent, modal, option, "modal-id");
```

#### Nested Modals

```java
ModalDialog.showModal(this, new SimpleModalBorder(
    content, "Main Modal", SimpleModalBorder.YES_NO_CANCEL_OPTION,
    (controller, action) -> {
        if (action == SimpleModalBorder.YES_OPTION) {
            // Show another modal
            ModalDialog.pushModal(new SimpleModalBorder(
                new JLabel("Nested Content"),
                "Nested Modal", 
                SimpleModalBorder.OK_OPTION,
                null
            ), "nested-modal");
        }
    }
), "main-modal");
```

### Toast Notifications

Toast notifications are transient messages that appear briefly to provide feedback to users.

#### Basic Toast

```java
import raven.modal.Toast;
import raven.modal.toast.option.ToastStyle;

// Simple toast message
Toast.show(this, "Operation completed successfully!");
```

#### Styled Toast

```java
import raven.modal.toast.option.ToastOption;

// Create toast with custom options
ToastOption option = Toast.createOption()
    .setToastStyle(ToastStyle.SUCCESS)
    .setLocation(ToastLocation.TOP_CENTER)
    .setAutoClose(true)
    .setDuration(3000);

Toast.show(this, "Data saved successfully!", option);
```

#### Custom Toast Content

```java
import raven.modal.toast.ToastCustom;

// Custom toast with panel content
JPanel customContent = new JPanel();
customContent.add(new JLabel("Custom content with buttons"));

ToastCustom.show(this, customContent, ToastLocation.BOTTOM_RIGHT);
```

### Drawer Navigation

Drawers provide slide-in panels for navigation or additional content.

#### Simple Drawer

```java
import raven.modal.drawer.DrawerPanel;
import raven.modal.drawer.SimpleDrawerBuilder;
import raven.modal.drawer.menu.MenuItem;
import java.awt.Color;

SimpleDrawerBuilder drawerBuilder = new SimpleDrawerBuilder() {
    @Override
    public void build(DrawerPanel drawerPanel) {
        // Create menu items
        drawerPanel.addMenu(createMenuItems());
    }
    
    private MenuItem[] createMenuItems() {
        return new MenuItem[]{
            new MenuItem("Dashboard", () -> System.out.println("Dashboard clicked")),
            new MenuItem("Settings", () -> System.out.println("Settings clicked")),
            new MenuItem("Profile", () -> System.out.println("Profile clicked"))
        };
    }
};

// Add drawer to frame (assuming you have a JFrame)
DrawerPanel.addDrawer(frame, drawerBuilder);
```

#### Advanced Drawer with Custom Styling

```java
SimpleDrawerBuilder drawerBuilder = new SimpleDrawerBuilder() {
    @Override
    public void build(DrawerPanel drawerPanel) {
        // Set header
        drawerPanel.setHeader(createHeader());
        
        // Create menu with icons
        drawerPanel.addMenu(createStyledMenuItems());
    }
    
    private Component createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.add(new JLabel("My Application"), BorderLayout.WEST);
        header.setBackground(Color.GRAY);
        return header;
    }
};
```

### Configuration Options

#### Modal Options

```java
Option option = ModalDialog.createOption();

// Animation settings
option.setAnimationEnabled(true);

// Behavior settings
option.setCloseOnPressedEscape(true);
option.setBackgroundClickType(Option.BackgroundClickType.CLOSE_MODAL);

// Visual settings
option.setOpacity(0.8f);
option.setHeavyWeight(false); // Use heavyweight window for better integration

// Border and shadow
option.getBorderOption()
    .setBorderWidth(1)
    .setShadow(BorderOption.Shadow.MEDIUM);

// Positioning
option.getLayoutOption()
    .setLocation(Location.CENTER, Location.CENTER)
    .setRelativeToOwner(true)
    .setMovable(true)
    .setAnimateScale(0.1f);
```

#### Toast Options

```java
ToastOption toastOption = Toast.createOption();

// Style configuration
toastOption.setToastStyle(ToastStyle.INFO);
toastOption.setLocation(ToastLocation.TOP_RIGHT);

// Timing
toastOption.setAutoClose(true);
toastOption.setDuration(5000);

// Animation
toastOption.setAnimationEnabled(true);
```

### Event Handling

#### Modal Events

```java
ModalDialog.showModal(this, new SimpleModalBorder(
    content, 
    "Title", 
    SimpleModalBorder.YES_NO_CANCEL_OPTION,
    (controller, action) -> {
        switch (action) {
            case SimpleModalBorder.OPENED:
                // Modal is opened
                break;
            case SimpleModalBorder.YES_OPTION:
                // User clicked YES
                break;
            case SimpleModalBorder.NO_OPTION:
                // User clicked NO
                break;
            case SimpleModalBorder.CANCEL_OPTION:
                // User clicked CANCEL or closed modal
                break;
        }
        
        // Consume the event to prevent modal from closing
        if (needValidation) {
            controller.consume();
        }
    }
));
```

#### Toast Events

```java
ToastOption option = Toast.createOption()
    .setCallback(new ToastCallback() {
        @Override
        public void onToastAction(ToastEvent evt) {
            switch (evt.getAction()) {
                case ToastEvent.SHOWN:
                    // Toast is shown
                    break;
                case ToastEvent.DISMISSED:
                    // Toast was dismissed
                    break;
                case ToastEvent.ACTION_PERFORMED:
                    // User clicked on toast
                    break;
            }
        }
    });
```

### Best Practices

1. **Modal Dialogs:**
   - Use modals for important actions that require user confirmation
   - Keep modal content concise and focused
   - Provide clear action buttons (YES/NO/CANCEL)
   - Use appropriate positioning and sizing

2. **Toast Notifications:**
   - Use for non-critical feedback messages
   - Keep messages short and clear
   - Don't show too many toasts simultaneously
   - Use appropriate styles (success, warning, error)

3. **Drawers:**
   - Use for navigation or secondary content
   - Keep menu items concise and well-organized
   - Consider responsive behavior for different screen sizes
   - Provide clear visual hierarchy

4. **Performance:**
   - Reuse modal instances when possible
   - Clean up resources when modals are closed
   - Use lightweight options for better performance
   - Consider heavyweight windows for complex modals

### Component Reference

#### Key Classes

- `ModalDialog` - Main class for modal dialogs
- `Toast` - Main class for toast notifications
- `DrawerPanel` - Main class for drawer navigation
- `Option` - Configuration options for modals
- `ToastOption` - Configuration options for toasts
- `SimpleModalBorder` - Pre-built modal border component
- `SimpleDrawerBuilder` - Builder for simple drawer implementations

#### Enums and Types

- `Location` - Positioning options (LEFT, CENTER, RIGHT, TOP, BOTTOM, etc.)
- `ToastStyle` - Toast styles (SUCCESS, WARNING, ERROR, INFO)
- `ToastLocation` - Toast display positions
- `SimpleModalBorder` - Pre-built modal options

## Library Resources

- [FlatLaf](https://github.com/JFormDesigner/FlatLaf) - FlatLaf library for the modern UI design theme
- [MigLayout](https://github.com/mikaelgrev/miglayout) - MigLayout library for flexible layout management
