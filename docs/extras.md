# Extras

`raven.extras` is a small set of standalone Swing components the library ships alongside its modal/toast/drawer system.
They have no dependency on `ModalDialog`, `Toast` or `Drawer` and can be used on their own in any FlatLaf-based Swing
app.

| Class                                 | Purpose                                                                                |
|---------------------------------------|----------------------------------------------------------------------------------------|
| [`AvatarIcon`](#avataricon)           | An `Icon` that clips an image into a circle or squircle, with optional border/gradient |
| [`LightDarkButton`](#lightdarkbutton) | A light/dark theme-switcher button (dual-button or single toggle style)                |
| [`SlidePane`](#slidepane)             | A container that animates a slide/crossfade transition when its content is swapped     |

## Contents

- [AvatarIcon](#avataricon)
- [LightDarkButton](#lightdarkbutton)
- [SlidePane](#slidepane)

## AvatarIcon

`raven.extras.AvatarIcon` implements `javax.swing.Icon`. It loads an image, scales it to fit, and masks it into a
rounded shape — typically used as the icon of a `JLabel` or `JButton` to render a user avatar/profile picture.

``` java
JLabel labelAvatar = new JLabel();
AvatarIcon avatarIcon = new AvatarIcon(
        getClass().getResource("/path/profile.jpg"),
        50, 50,  // icon width, height
        999      // round — 999 = perfect circle
);
avatarIcon.setBorder(3, 0);
avatarIcon.setBorderColor(new AvatarIcon.BorderColor(Color.decode("#3B82F6")));
labelAvatar.setIcon(avatarIcon);
```

### Constructors

- `AvatarIcon(String filename, int iconWidth, int iconHeight, float round)`
- `AvatarIcon(URL location, int iconWidth, int iconHeight, float round)`
- `AvatarIcon(ImageIcon imageIcon, int iconWidth, int iconHeight, float round)`

`round` is the corner radius of the mask, in pixels. For the default `Type.ROUND` mask, a value of `999` is treated
specially and produces a perfect ellipse/circle instead of a rounded rectangle; for `Type.MASK_SQUIRCLE` it's passed
straight through to the superellipse shape with no special-casing.

### Properties

| Setter                                             | Description                                                                                                                     |
|----------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------|
| `setIcon(String \| URL \| ImageIcon)`              | replace the source image                                                                                                        |
| `setType(AvatarIcon.Type)`                         | `ROUND` *(default)* — rounded-rectangle/circle mask, or `MASK_SQUIRCLE` — a superellipse ("squircle") mask via `SuperEllipse2D` |
| `setBorderWidth(int)`                              | outer border thickness in px, default `0`                                                                                       |
| `setInnerBorderWidth(int)`                         | additional gap between the image and the border ("ring" effect), default `0`                                                    |
| `setBorder(int borderWidth, int innerBorderWidth)` | convenience setter for both at once                                                                                             |
| `setBorderColor(AvatarIcon.BorderColor)`           | border paint — solid or gradient, see below; `null` falls back to the theme's `Component.borderColor`                           |

`getRound()`, `getBorderWidth()`, `getInnerBorderWidth()`, `getBorderColor()`, `getType()`, `getImageIcon()` are the
matching getters. The icon re-renders (and re-scales for the current HiDPI/UI scale factor) lazily the next time it's
painted; any setter above invalidates the cached rendering.

### AvatarIcon.BorderColor

A small paint descriptor used by `setBorderColor(...)`, supporting a solid color or a linear gradient:

``` java
new AvatarIcon.BorderColor(Color.decode("#3B82F6"));                         // solid
new AvatarIcon.BorderColor(59, 130, 246);                                    // solid, RGB ints
new AvatarIcon.BorderColor(Color.decode("#FF7E5F"), Color.decode("#FEB47B")); // gradient
new AvatarIcon.BorderColor(startColor, endColor, 0.5f);                      // gradient + opacity
```

Constructors accept RGB ints or `Color`, an optional end `Color` for a gradient, optional gradient `startPoint`/
`endPoint` (fractions along the icon's height, default `0f`/`0f`), and an optional `opacity` (default `1f`). All fields
also have getters/setters.

## LightDarkButton

`raven.extras.LightDarkButton` is a ready-made `JPanel` that switches the app's FlatLaf theme between light and dark.
It's the same component used in the [Drawer](drawer.md#header-and-footer)'s default footer (`LightDarkButtonFooter`),
but it can be dropped anywhere on its own.

``` java
LightDarkButton lightDarkButton = new LightDarkButton();
lightDarkButton.installAutoLafChangeListener();
add(lightDarkButton);
```

`installAutoLafChangeListener()` wires the button to actually change `UIManager`'s look and feel (with FlatLaf's
animated transition) whenever its mode changes, and keeps the button's selected state in sync if the LaF changes from
elsewhere. Without calling it, the button only tracks its own selection state and fires `ModeChangeListener`s — you'd
apply the theme change yourself.

### Constructors

- `LightDarkButton()` — `ButtonStyle.DUAL_BUTTON`, arc `15`
- `LightDarkButton(int arc)`
- `LightDarkButton(ButtonStyle buttonStyle)`
- `LightDarkButton(ButtonStyle buttonStyle, int arc)`

`ButtonStyle`:

- `DUAL_BUTTON` *(default)* — two toggle buttons side by side, labeled "Light" / "Dark".
- `TOGGLE_BUTTON` — a single button that swaps its icon between light/dark on each click.

`arc` is the corner radius (FlatLaf `arc` style property) of the button(s).

### Properties

| Setter                                                                                 | Description                                                                                                                                                                                                            |
|----------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `setButtonStyle(ButtonStyle)`                                                          | switch between `DUAL_BUTTON`/`TOGGLE_BUTTON` at runtime (rebuilds the internal buttons)                                                                                                                                |
| `setSelectedMode(boolean dark)`                                                        | select light/dark and notify listeners                                                                                                                                                                                 |
| `setSelectedMode(boolean dark, boolean notify)`                                        | same, optionally without firing `ModeChangeListener`s                                                                                                                                                                  |
| `setLightButtonText(String)` / `setDarkButtonText(String)`                             | button labels, default `"Light"` / `"Dark"` (only shown in `DUAL_BUTTON` style)                                                                                                                                        |
| `setLightButtonIcon(Icon)` / `setDarkButtonIcon(Icon)`                                 | override the default sun/moon SVG icons                                                                                                                                                                                |
| `addSupportLookAndFeel(Class<? extends FlatLaf> light, Class<? extends FlatLaf> dark)` | register an additional light/dark FlatLaf theme pair (FlatLaf, IntelliJ/Darcula, and Mac Light/Dark are registered by default) so `installAutoLafChangeListener()` knows which paired theme to switch to when toggling |
| `setSupportLookAndFeel(List<LookAndFeelLightDark>)`                                    | **replaces** the entire list of registered pairs (including the 3 built-in defaults above) instead of adding to it — use `addSupportLookAndFeel` unless you intend to drop the defaults                                |

`autoSelectedMode()` re-reads `FlatLaf.isLafDark()` and updates the button's selection without notifying listeners —
useful after the theme was changed from somewhere else in the app.

### Listening for mode changes

``` java
lightDarkButton.addModeChangeListener(isDarkMode -> {
    // e.g. persist the preference, or apply your own theme switch
});
```

`addModeChangeListener(ModeChangeListener)` / `removeModeChangeListener(ModeChangeListener)` — `ModeChangeListener` is a
single-method functional interface: `void onModeChanged(boolean isDarkMode)`.

## SlidePane

`raven.extras.SlidePane` (extends `raven.modal.slider.PanelSlider`) is a container for exactly one "current" component
at a time; calling `addSlide(...)` swaps in a new component with an animated transition instead of an abrupt
`removeAll()`/`add()`. It's useful for wizard-style flows, master/detail navigation, or any place you'd otherwise swap
panels in a `CardLayout` but want a slide/zoom animation.

``` java
SlidePane slidePane = new SlidePane();
slidePane.addSlide(new StepOnePanel()); // first panel: shown immediately, no animation

button.addActionListener(e -> {
    Component next = new StepTwoPanel();
    slidePane.addSlide(next, SlidePaneTransition.Type.FORWARD);
});

add(slidePane);
```

The very first `addSlide(...)` call just displays the component (nothing to transition from). Every subsequent call
animates from the previously-shown component to the new one, using a snapshot image of each.

### Constructors

- `SlidePane()` — sizes the container to its own current size (`container.getSize()`).
- `SlidePane(PanelSlider.PaneSliderLayoutSize paneSliderLayoutSize)` — supply a custom sizing strategy, e.g. size to the
  incoming component's preferred size instead:
  ```java
  SlidePane slidePane = new SlidePane((container, component) -> component.getPreferredSize());
  ```

### Adding slides

``` java
slidePane.addSlide(component);                                    // no transition (instant swap)
slidePane.addSlide(component, SlidePaneTransition.Type.FORWARD);   // preset transition, default 400ms
slidePane.addSlide(component, SlidePaneTransition.Type.BACK, 250); // preset transition, custom duration (ms)
slidePane.addSlide(component, myCustomTransition);                 // custom SlidePaneTransition
slidePane.addSlide(component, myCustomTransition, 250);
```

`SlidePaneTransition.Type` presets (`raven.extras.SlidePaneTransition`), resolved via
`SlidePaneTransition.create(type)`:

- `DEFAULT` — cross-fade.
- `FORWARD` — new content slides in from the trailing edge, old content slides out (a "push forward" navigation feel).
- `BACK` — the reverse of `FORWARD`, for "navigate back" transitions.
- `ZOOM_IN` / `ZOOM_OUT` — new content scales in/out.
- `TOP_DOWN` / `DOWN_TOP` — vertical slide transitions.

### Custom transitions

Extend `SliderTransition` (from `raven.modal.slider`, the base class `SlidePaneTransition` also extends) and implement
how the outgoing and incoming snapshots are painted:

``` java
public class MyTransition extends SliderTransition {
    @Override
    public void renderImageOld(Graphics g, Image image, int width, int height, float animate) {
        // paint the outgoing snapshot as `animate` goes 0 -> 1
    }

    @Override
    public void renderImageNew(Graphics g, Image image, int width, int height, float animate) {
        // paint the incoming snapshot as `animate` goes 0 -> 1
    }
}
```

### Other properties

| Setter                               | Description                                                                                                       |
|--------------------------------------|-------------------------------------------------------------------------------------------------------------------|
| `setRequestFocusAfterSlide(boolean)` | request focus on the new component once its slide-in animation finishes, default `false`                          |
| `setUseSlideAsBackground(boolean)`   | make `getBackground()` return the current slide component's background instead of the pane's own, default `false` |
