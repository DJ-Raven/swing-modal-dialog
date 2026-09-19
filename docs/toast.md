# Toast

`Toast` shows small, transient notification messages on top of a window (a `JFrame`, `JDialog` or `JInternalFrame`). It
is commonly used for brief, non-intrusive feedback such as "saved successfully" or "connection failed". Toasts stack,
animate in/out, and can auto-close after a delay, show a loading/result "promise" sequence, or host completely custom
content.

All entry points are static methods on `raven.modal.Toast`.

## Contents

- [Basic usage](#basic-usage)
- [Toast location](#toast-location)
- [ToastOption](#toastoption)
- [Layout option](#layout-option)
- [Style](#style)
- [Promise toast](#promise-toast)
- [Custom toast](#custom-toast)
- [Events / listener](#events--listener)
- [Managing toasts](#managing-toasts)
- [Heavy-weight mode](#heavy-weight-mode)

## Basic usage

``` java
import raven.modal.Toast;

Toast.show(this, Toast.Type.SUCCESS, "Saved successfully");
```

`owner` (the first argument) is any component inside the window the toast should appear over — it's used to resolve the
containing `JFrame`/`JDialog`/`JInternalFrame`.

`Toast.Type` controls the icon and accent color:

| Type      | Meaning                          |
|-----------|----------------------------------|
| `DEFAULT` | Neutral message, no themed color |
| `SUCCESS` | Green, success confirmation      |
| `INFO`    | Blue, informational              |
| `WARNING` | Amber/orange, warning            |
| `ERROR`   | Red, error/failure               |

`Toast.show(...)` returns a `String` id that can later be used with [`close(id)`](#managing-toasts).

## Toast location

Show a toast at a specific corner/edge with a `ToastLocation`:

``` java
Toast.show(this, Toast.Type.INFO, "Message", ToastLocation.BOTTOM_TRAILING);
```

Available locations (`raven.modal.toast.option.ToastLocation`):

| Location          | Position                               |
|-------------------|----------------------------------------|
| `TOP_LEADING`     | Top edge, leading side (left in LTR)   |
| `TOP_CENTER`      | Top edge, centered — **default**       |
| `TOP_TRAILING`    | Top edge, trailing side (right in LTR) |
| `BOTTOM_LEADING`  | Bottom edge, leading side              |
| `BOTTOM_CENTER`   | Bottom edge, centered                  |
| `BOTTOM_TRAILING` | Bottom edge, trailing side             |

`ToastLocation` is orientation-aware (leading/trailing respect `ComponentOrientation`, e.g. right-to-left locales). Each
location has an associated default `ToastDirection` (the direction new toasts stack/animate towards), which you can
override — see [Layout option](#layout-option).

For a location that isn't one of the six presets, use `ToastLayoutOption.setLocation(Number x, Number y)` (see below).

## ToastOption

Every `show*` overload accepts a `ToastOption`. Get a mutable copy of the library defaults with `Toast.createOption()`,
or read/mutate the shared defaults with `Toast.getDefaultOption()`:

``` java
// one-off option for a single toast
ToastOption option = Toast.createOption();
option.setDuration(1000)
        .setAutoClose(false)
        .setCloseOnClick(true);
Toast.show(this, Toast.Type.WARNING, "Careful!", option);

// change the defaults used by every future Toast.show(...) call that
// doesn't pass its own option
Toast.getDefaultOption()
        .setHeavyWeight(true)
        .getLayoutOption()
        .setRelativeToOwner(true);
```

There is no `setDefaultOption(...)` replace method for `Toast` (unlike `ModalDialog`) — mutate the object returned by
`getDefaultOption()` in place.

`ToastOption` properties:

| Setter                               | Type                | Default                          | Description                                                                                                                 |
|--------------------------------------|---------------------|----------------------------------|-----------------------------------------------------------------------------------------------------------------------------|
| `setEvent(ToastEvent)`               | `ToastEvent`        | new `ToastEvent`                 | Mouse listeners for the toast, see [Events / listener](#events--listener)                                                   |
| `setLayoutOption(ToastLayoutOption)` | `ToastLayoutOption` | `ToastLayoutOption.getDefault()` | Position, direction, margins — see [Layout option](#layout-option)                                                          |
| `setStyle(ToastStyle)`               | `ToastStyle`        | `ToastStyle.getDefault()`        | Visual appearance — see [Style](#style)                                                                                     |
| `setInterpolator(ToastInterpolator)` | `ToastInterpolator` | `null` (linear)                  | Custom easing function for the open/close animation, e.g. `toastOption.setInterpolator(EasingInterpolator::easeOutBack)`    |
| `setAnimationEnabled(boolean)`       | `boolean`           | `true`                           | Whether the toast animates in/out or appears/disappears instantly                                                           |
| `setHeavyWeight(boolean)`            | `boolean`           | `false`                          | Render in a heavyweight top-level window instead of the window's layered pane — see [Heavy-weight mode](#heavy-weight-mode) |
| `setPauseDelayOnHover(boolean)`      | `boolean`           | `true`                           | Pause the auto-close countdown while the mouse hovers the toast                                                             |
| `setAutoClose(boolean)`              | `boolean`           | `true`                           | Automatically close after `delay` milliseconds                                                                              |
| `setCloseOnClick(boolean)`           | `boolean`           | `false`                          | Close the toast when it's clicked                                                                                           |
| `setHtmlEnabled(boolean)`            | `boolean`           | `false`                          | Render `message` as HTML (wrap it in `<html>...</html>`)                                                                    |
| `setDuration(int)`                   | `int` (ms)          | `350`                            | Open/close animation duration, used when `openDuration`/`closeDuration` are not set                                         |
| `setOpenDuration(int)`               | `int` (ms)          | `-1` (falls back to `duration`)  | Duration of the open animation only                                                                                         |
| `setCloseDuration(int)`              | `int` (ms)          | `-1` (falls back to `duration`)  | Duration of the close animation only                                                                                        |
| `setDelay(int)`                      | `int` (ms)          | `3000`                           | How long the toast stays visible before auto-closing (when `autoClose` is `true`)                                           |

## Layout option

`ToastOption.getLayoutOption()` returns a `ToastLayoutOption` controlling where and how the toast is positioned:

| Setter                                                                   | Description                                                                                                                                                                                                                                            |
|--------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `setLocation(ToastLocation)`                                             | One of the six preset locations (default `TOP_CENTER`)                                                                                                                                                                                                 |
| `setLocation(Number x, Number y)`                                        | Arbitrary position; each of `x`/`y` is a fraction (`0f`–`1f`) of the container size, or a fixed pixel value depending on the underlying `DynamicSize` — see the six presets in `ToastLocation` for reference values                                    |
| `setDirection(ToastDirection)`                                           | Overrides the open/close slide axis and/or stacking growth direction (see below); defaults to the direction paired with the chosen `ToastLocation`                                                                                                     |
| `setRelativeToOwnerType(RelativeToOwnerType)`                            | `RELATIVE_CONTAINED` (default, confined to the owner's bounds, tracks owner visibility) / `RELATIVE_GLOBAL` (spans the whole window, ignores owner visibility) / `RELATIVE_BOUNDLESS` (can extend outside the owner's bounds, tracks owner visibility) |
| `setRelativeToOwner(boolean)`                                            | `false` by default. When `true`, the toast is positioned relative to the `owner` component passed to `Toast.show(...)` instead of the whole window                                                                                                     |
| `setOverflowAlignmentAuto(boolean)`                                      | `false` by default; when `true`, adjusts alignment automatically if the toast would overflow the container                                                                                                                                             |
| `setGap(int)`                                                            | Pixel gap between stacked toasts, default `5`                                                                                                                                                                                                          |
| `setMargin(int)` / `setMargin(int top, int left, int bottom, int right)` | Margin from the container edges, default `Insets(7, 7, 7, 7)`                                                                                                                                                                                          |

`ToastDirection` controls two things at once for the toast(s) shown at a given location: the axis/edge a toast slides
in from when it opens (and slides back out toward when it closes), and — once more than one toast is stacked at that
location — which way each newly-added toast grows relative to the one(s) already showing.

| Value                  | Open/close slide axis             | New toasts stack                   |
|------------------------|-----------------------------------|------------------------------------|
| `TOP_TO_BOTTOM`        | vertical, enters from the top     | downward (away from the top edge)  |
| `BOTTOM_TO_TOP`        | vertical, enters from the bottom  | upward (away from the bottom edge) |
| `LEFT_TO_RIGHT_TOP`    | horizontal, enters from the left  | upward                             |
| `LEFT_TO_RIGHT_BOTTOM` | horizontal, enters from the left  | downward                           |
| `RIGHT_TO_LEFT_TOP`    | horizontal, enters from the right | upward                             |
| `RIGHT_TO_LEFT_BOTTOM` | horizontal, enters from the right | downward                           |

For the four horizontal variants, the `_TOP`/`_BOTTOM` suffix only affects stacking growth — the open/close animation
itself always slides horizontally for those four; it doesn't change with the suffix. That's why each corner
`ToastLocation` pairs a horizontal slide with whichever suffix matches its own anchor edge: `TOP_LEADING`, for
example, defaults to `LEFT_TO_RIGHT_BOTTOM` — it slides in from the left, and since it's anchored at the *top* edge,
additional stacked toasts grow *downward*, away from that edge (the mirror image of `BOTTOM_LEADING`'s
`LEFT_TO_RIGHT_TOP`, anchored at the bottom and growing upward).

``` java
ToastOption option = Toast.createOption();
option.getLayoutOption()
        .setLocation(ToastLocation.BOTTOM_TRAILING)
        .setRelativeToOwner(true)
        .setGap(8)
        .setMargin(12);
```

## Style

`ToastOption.getStyle()` returns a `ToastStyle`:

| Setter                              | Type                            | Default                                              | Description                                                               |
|-------------------------------------|---------------------------------|------------------------------------------------------|---------------------------------------------------------------------------|
| `setBorderStyle(ToastBorderStyle)`  | `ToastBorderStyle`              | `ToastBorderStyle.getDefault()`                      | Border/shadow appearance, see below                                       |
| `setBackgroundType(BackgroundType)` | `DEFAULT` / `GRADIENT` / `NONE` | `DEFAULT`                                            | Background paint style                                                    |
| `setShowIcon(boolean)`              | `boolean`                       | `true`                                               | Show the type icon                                                        |
| `setShowLabel(boolean)`             | `boolean`                       | `false`                                              | Show a bold label above the message (e.g. "Success")                      |
| `setIconSeparateLine(boolean)`      | `boolean`                       | `false`                                              | Put the icon on its own line/column separate from the text                |
| `setShowCloseButton(boolean)`       | `boolean`                       | `true`                                               | Show a close (×) button                                                   |
| `setPaintTextColor(boolean)`        | `boolean`                       | `false`                                              | Tint the message text with the type color instead of just the icon/border |
| `setPromiseLabel(String)`           | `String`                        | `"Loading"`                                          | Label shown while a [promise toast](#promise-toast) is pending            |
| `setLabelText(String)`              | `String`                        | `null` (derived from `Toast.Type`, e.g. `"Success"`) | Override the label text                                                   |
| `setCustomIcon(Icon)`               | `Icon`                          | `null`                                               | Override the type icon                                                    |
| `setCloseIcon(Icon)`                | `Icon`                          | `null`                                               | Override the close-button icon                                            |

`ToastStyle.getBorderStyle()` returns a `ToastBorderStyle`:

| Setter                                                                     | Type                                                                               | Default                | Description                                            |
|----------------------------------------------------------------------------|------------------------------------------------------------------------------------|------------------------|--------------------------------------------------------|
| `setBorderType(BorderType)`                                                | `OUTLINE` / `TRAILING_LINE` / `LEADING_LINE` / `TOP_LINE` / `BOTTOM_LINE` / `NONE` | `NONE`                 | Border style                                           |
| `setRound(int)`                                                            | `int`                                                                              | `10`                   | Corner radius                                          |
| `setShadowSize(int)` / `setShadowSize(Insets)`                             | `Insets`                                                                           | `Insets(0, 2, 10, 2)`  | Drop-shadow size (must be `>= 0`)                      |
| `setShadow(Shadow)`                                                        | `NONE` / `SMALL` / `MEDIUM` / `LARGE` / `EXTRA_LARGE`                              | —                      | Convenience preset that sets `shadowSize` for you      |
| `setShadowColor(Color)`                                                    | `Color`                                                                            | `null` (theme default) | Shadow color                                           |
| `setShadowOpacity(float)`                                                  | `float`                                                                            | `-1` (theme default)   | Shadow opacity                                         |
| `setLineSize(int)`                                                         | `int`                                                                              | `3`                    | Thickness of the accent line for `*_LINE` border types |
| `setBorderWidth(int)`                                                      | `int`                                                                              | `1`                    | Thickness for `OUTLINE`                                |
| `setLineColor(Color)`                                                      | `Color`                                                                            | `null` (type color)    | Accent line/outline color                              |
| `setPadding(int)` / `setPadding(int top, int left, int bottom, int right)` | `Insets`                                                                           | `Insets(0, 0, 0, 0)`   | Inner content padding                                  |

``` java
ToastOption option = Toast.createOption();
option.getStyle()
        .setBackgroundType(ToastStyle.BackgroundType.GRADIENT)
        .setShowLabel(true)
        .setIconSeparateLine(true)
        .getBorderStyle()
        .setBorderType(ToastBorderStyle.BorderType.LEADING_LINE)
        .setShadow(ToastBorderStyle.Shadow.MEDIUM);
```

## Promise toast

A promise toast shows a "loading" state, runs work in the background, then resolves into a normal toast with a result
type and message. Extend `ToastPromise` and implement `execute(...)`:

``` java
Toast.showPromise(this, "Uploading file, please wait", new ToastPromise() {
    @Override
    public void execute(ToastPromise.PromiseCallback callback) {
        try {
            // update the loading label while work is in progress (optional)
            callback.update("Uploading 42%");
            Thread.sleep(2000);
            callback.done(Toast.Type.SUCCESS, "Upload complete");
        } catch (InterruptedException e) {
            // rejected — see rejectAble()
        }
    }
});
```

- `execute(PromiseCallback callback)` runs on a background `Thread` by default (`useThread()` returns `true`); override
  `useThread()` to return `false` to run synchronously on the EDT instead (only safe for non-blocking work).
- `callback.update(String message)` updates the message shown while pending.
- `callback.done(Toast.Type type, String message)` resolves the promise into a regular toast of that type.
- `rejectAble()` returns `false` by default; override it to allow the promise's background thread to be interrupted (
  e.g. if the toast is closed before it resolves).
- `ToastPromise(String id)` lets you give the promise a stable id you can check with `Toast.checkPromiseId(id)` before
  showing another one, to avoid duplicate concurrent promises:

``` java
if (!Toast.checkPromiseId("upload")) {
    Toast.showPromise(this, "Uploading...", new ToastPromise("upload") {
        @Override
        public void execute(PromiseCallback callback) {
            // ...
            callback.done(Toast.Type.SUCCESS, "Done");
        }
    });
}
```

`Toast.showPromise` has the same overloads as `Toast.show` — with a `ToastLocation` and/or a `ToastOption`.
`ToastStyle.setPromiseLabel(String)` controls the pending-state label (default `"Loading"`).

## Custom toast

To show arbitrary content instead of an icon/message, extend `ToastCustomPanel` (a `JPanel` that implements
`ToastCustom` for you) and call `Toast.showCustom`:

``` java
public class MyToast extends ToastCustomPanel {
    public MyToast() {
        setLayout(new MigLayout());
        JButton close = new JButton("Dismiss");
        close.addActionListener(e -> toastAction.close());
        add(new JLabel("Custom content"));
        add(close);
    }
}
```

``` java
ToastOption option = Toast.createOption();
option.setAnimationEnabled(false)
        .setAutoClose(false)
        .getLayoutOption()
        .setLocation(ToastLocation.BOTTOM_TRAILING);
option.getStyle()
        .setBackgroundType(ToastStyle.BackgroundType.NONE)
        .getBorderStyle().setBorderType(ToastBorderStyle.BorderType.OUTLINE);

Toast.showCustom(this, new MyToast(), option);
```

`ToastCustomPanel` exposes a protected `toastAction` field (`ToastCustom.Action`) with a single `close()` method — call
it to close the toast programmatically, e.g. from a close button. Custom toasts are typically shown with
`setAutoClose(false)` since there's no default message/duration semantics to auto-dismiss by.

If you don't want to extend `ToastCustomPanel`, implement the `ToastCustom` interface directly on any `JComponent` and
store the `Action` passed to `initToastAction(Action action)` yourself.

## Events / listener

`ToastOption.getEvent()` returns a `ToastEvent`, which lets you listen for mouse presses/releases on a toast:

``` java
ToastOption option = Toast.createOption();
option.getEvent().addListener(new ToastAdapter() {
    @Override
    public void mousePressed(MouseEvent event, ToastCallback callback) {
        // callback.close() closes just this toast
        // callback.closeAllImmediately() closes every toast without animation
        // callback.isCurrenPromise() reports whether this toast is still a pending promise
    }
});
Toast.show(this, Toast.Type.INFO, "Click me", option);
```

`ToastAdapter` is a no-op convenience base for `ToastListener` so you only need to override the method(s) you care
about.

## Managing toasts

``` java
String id = Toast.show(this, Toast.Type.INFO, "Message");

Toast.close(id);                 // close one toast (animated)
Toast.closeImmediately(id);      // close one toast without animation
Toast.closeAll();                // close every toast in every window
Toast.closeAll(ToastLocation.TOP_CENTER); // close every toast at a given location
Toast.closeAllImmediately();     // close everything without animation

Toast.isToastAvailable(id);      // whether a toast with this id is currently showing
Toast.checkPromiseId(id);        // whether a promise toast with this id is currently pending

Toast.setReverseOrder(true);     // reverse the stacking order of new toasts
Toast.isReverseOrder();
```

## Heavy-weight mode

`ToastOption.setHeavyWeight(true)` renders the toast in its own heavyweight top-level window instead of inside the
owning `JFrame`'s layered pane. Use it when a toast needs to visually escape the bounds of its owner window, or needs to
appear above other heavyweight components that a lightweight (layered-pane) toast can't paint over.

Animation is not supported in this mode: `setAnimationEnabled(boolean)` and the `duration`/`openDuration`/
`closeDuration` settings have no effect, and the toast simply appears/disappears instantly.
