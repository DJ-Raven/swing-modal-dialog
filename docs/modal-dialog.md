# Modal Dialog

An animated, in-window modal dialog system built on top of `JLayeredPane`. By default a modal renders *inside* the owning window (lightweight), tracks the window's position/resizing, and animates open/close — no separate native window is created. An optional heavyweight mode renders the modal in its own top-level `Window` instead, for cases where content needs to escape the owner window's bounds.

A modal is any `JPanel` subclass extending `raven.modal.component.Modal`. The library ships a ready-made implementation, `SimpleModalBorder`, that adds a title bar, close button and option buttons (Yes/No, OK/Cancel, ...) around any `JComponent`, which covers most use cases without writing a custom `Modal`.

## Creating a modal

The simplest way to show a dialog is to wrap any component with `SimpleModalBorder` — see [SimpleModalBorder](#simplemodalborder) below.

To build a fully custom modal, extend `Modal` directly:

```java
public class MyModal extends Modal {

    public MyModal(Component content) {
        setLayout(new MigLayout("fill,insets 8", "[fill]", "[fill]"));
        add(content);
    }

    @Override
    protected void modalOpened() {
        // called after the modal has finished its open animation and gained focus
    }

    public void close() {
        getController().closeModal();
    }
}
```

`Modal` exposes these hooks/members:

- `installComponent()` — called once, the first time the modal is shown, to build/attach its UI. Optional to override; you can also build the UI directly in the constructor (as above).
- `controllerInit()` — called right after the controller is attached (i.e. right after `setController(...)` is invoked internally), before the modal is shown.
- `modalOpened()` — called after the open animation finishes and the modal has requested focus. Good place to focus an input field.
- `getController()` — returns a `ControllerAction` with `closeModal()` and `popModal()`, used to close the modal (or pop back to the previous one in a push/pop stack) from inside the modal itself.
- `getId()` / `setId(String)`, `isInstalled()` / `setInstalled(boolean)` — managed by the library; you normally don't need to call the setters yourself.

## Showing a modal

```java
ModalDialog.showModal(owner, modal);
ModalDialog.showModal(owner, modal, "my-modal-id");
ModalDialog.showModal(owner, modal, option);
ModalDialog.showModal(owner, modal, option, "my-modal-id");
```

- `owner` — any `Component` inside the target window; the library walks up to find the enclosing `JFrame`/`JDialog`/`JInternalFrame`.
- `option` — an `Option` controlling layout, border, animation, etc. (defaults to `ModalDialog.getDefaultOption()` when omitted).
- `id` — an optional identifier used later to close/push/pop this modal (`ModalDialog.closeModal(id)`, `pushModal(...)`, `popModal(id)`). If you pass an `id` that is already in use, `showModal` throws `IllegalArgumentException`. Check first with `ModalDialog.isIdExist(id)`.

## Closing a modal

```java
ModalDialog.closeModal(id);              // animated close
ModalDialog.closeModalImmediately(id);   // no animation
ModalDialog.closeAllModal();             // close every open modal (all windows)
ModalDialog.closeAllModalImmediately();
ModalDialog.isIdExist(id);
```

From inside the modal itself (no `id` needed), use the controller handed to it:

```java
getController().closeModal();
```

`SimpleModalBorder`-based modals get this for free — see [SimpleModalBorder](#simplemodalborder).

## Stacking modals (push / pop)

`ModalDialog.pushModal(modal, id)` slides a new modal on top of the one currently shown under `id`, keeping the same window/container and `id`; `ModalDialog.popModal(id)` slides back to the previous one. This is meant for wizard-like flows (e.g. "New Country" sub-form opened from within an input form) where you want a back-navigation animation instead of opening an unrelated second modal:

```java
final String id = "input";
SimpleInputForms form = new SimpleInputForms();
ModalDialog.showModal(this, new SimpleModalBorder(
        form, "Sample Input Forms", SimpleModalBorder.YES_NO_CANCEL_OPTION,
        (controller, action) -> {
            if (action == SimpleInputForms.NEW_COUNTRY) {
                controller.consume(); // keep the current modal open
                SimpleInputFormsCountry sub = new SimpleInputFormsCountry();
                ModalDialog.pushModal(new SimpleModalBorder(sub, "New Country", SimpleModalBorder.YES_NO_OPTION,
                        (subController, subAction) -> {
                            if (subAction == SimpleModalBorder.YES_OPTION) {
                                subController.consume();
                                form.newCountryCreated(sub.getInputData());
                                ModalDialog.popModal(id); // slide back to the parent form
                            }
                        }), id);
            }
        }), option, id);
```

`controller.consume()` tells the library the callback already handled the action, so the modal should *not* auto-close — useful when validation fails, or (as above) when you're about to push a child modal instead of closing.

## Option

`Option` controls window-level behavior: layout, border, background, animation.

```java
Option option = ModalDialog.createOption();   // copy of the current default option
```

- `ModalDialog.getDefaultOption()` — the shared default `Option` instance; mutate it directly to change defaults for every modal that doesn't pass its own option.
- `ModalDialog.setDefaultOption(Option)` — replace the default outright.
- `ModalDialog.createOption()` — returns a **copy** of the default option, safe to customize per-modal without affecting the default.

Properties (all setters return `this` for chaining):

| Setter | Type | Default | Description |
|---|---|---|---|
| `setLayoutOption(LayoutOption)` | `LayoutOption` | see [Layout option](#layout-option) | positioning/animation of the modal |
| `setBackgroundClickType(BackgroundClickType)` | enum | `CLOSE_MODAL` | what clicking the dimmed background does: `CLOSE_MODAL`, `BLOCK` (swallow the click), `NONE` (click passes through) |
| `setAnimationEnabled(boolean)` | `boolean` | `true` | enable/disable open animation |
| `setAnimationOnClose(boolean)` | `boolean` | `true` | enable/disable close animation |
| `setCloseOnPressedEscape(boolean)` | `boolean` | `true` | close the modal when `Esc` is pressed |
| `setHeavyWeight(boolean)` | `boolean` | `false` | render in a real top-level `Window` — see [Heavy-weight mode](#heavy-weight-mode) |
| `setBackground(Color)` / `setBackground(Color light, Color dark)` | `Color` | `null` (theme default) | background dim color, optionally different per FlatLaf light/dark theme |
| `setOpacity(float)` | `float` | `0.5f` | opacity of the dimmed background |
| `setDuration(int)` | `int` (ms) | `200` | open/close fade duration |
| `setSliderDuration(int)` | `int` (ms) | `400` | duration of the push/pop slide transition |

```java
Option option = ModalDialog.createOption();
option.setHeavyWeight(true)
      .setOpacity(0.4f)
      .setBackgroundClickType(Option.BackgroundClickType.BLOCK);
option.getBorderOption().setShadow(BorderOption.Shadow.MEDIUM).setBorderWidth(1);
option.getLayoutOption().setMovable(true).setRelativeToOwner(true);
ModalDialog.showModal(owner, modal, option);
```

## Layout option

`option.getLayoutOption()` returns a `LayoutOption`:

| Setter | Description |
|---|---|
| `setLocation(Location h, Location v)` | position using named locations (`TOP`, `CENTER`, `BOTTOM`, `LEFT`, `RIGHT`, `LEADING`, `TRAILING`) — default `CENTER`/`CENTER` |
| `setLocation(Location h, Number y)` / `setLocation(Number x, Number y)` | position with a fixed pixel/fraction offset instead of (or combined with) a named location |
| `setMargin(int)` / `setMargin(int top, int left, int bottom, int right)` | gap kept between the modal and the window edges — default `7,7,7,7` |
| `setBackgroundPadding(int)` / `setBackgroundPadding(top, left, bottom, right)` | shrinks the dimmed background rectangle relative to the owner (used with `RELATIVE_BOUNDLESS`) — default `0,0,0,0` |
| `setSize(Number width, Number height)` | fixed/preferred size, `-1` means "use the modal's preferred size" (the default) |
| `setAnimateDistance(Number x, Number y)` | distance the modal travels during the open/close slide animation — default `(0, 20)` |
| `setAnimateScale(float)` | 0–1 scale-in amount used together with the slide animation (throws `IllegalArgumentException` outside `0..1`) — default `0` (disabled) |
| `setRelativeToOwner(boolean)` | position/clip the modal relative to the `owner` component passed to `showModal`, instead of the whole window — default `false` |
| `setRelativeToOwnerType(RelativeToOwnerType)` | how "relative to owner" behaves — see below — default `RELATIVE_CONTAINED` |
| `setOverflowAlignmentAuto(boolean)` | automatically re-align the modal if it would overflow the window bounds — default `true` |
| `setMovable(boolean)` | let the user drag the modal by its body — default `false` |
| `setOnTop(boolean)` | keep this modal above others when several are shown — default `false` |

`RelativeToOwnerType` (only meaningful when `relativeToOwner` is `true`):

- `RELATIVE_CONTAINED` *(default)* — modal and dimmed background are confined to the owner's bounds and track the owner's visibility.
- `RELATIVE_GLOBAL` — background spans the entire window and does not track the owner's visibility.
- `RELATIVE_BOUNDLESS` — background covers the owner, but the modal itself can extend outside the owner's bounds. Tracks the owner's visibility. Requires `heavyWeight = true`.

## Border option

`option.getBorderOption()` returns a `BorderOption`:

| Setter | Description |
|---|---|
| `setRound(int)` | corner radius — default `10` |
| `setBorderWidth(int)` | outline border width in px — default `0` (no outline) |
| `setBorderColor(Color)` | outline border color — default `null` (theme default) |
| `setShadow(BorderOption.Shadow)` | apply one of the built-in shadow presets (sets shadow insets for you) |
| `setShadowSize(int)` / `setShadowSize(Insets)` | explicit shadow insets instead of a preset |
| `setShadowColor(Color)` | shadow color — default `null` (theme default) |
| `setShadowOpacity(float)` | shadow opacity — default `-1` (theme default) |

`BorderOption.Shadow` presets: `NONE`, `SMALL`, `MEDIUM`, `LARGE`, `EXTRA_LARGE`, `DOUBLE_EXTRA_LARGE` (each maps to a fixed shadow `Insets`).

## SimpleModalBorder

`SimpleModalBorder` is a ready-made `Modal` that wraps any `Component` with a title bar (title + close button) and an optional row of option buttons at the bottom.

```java
ModalDialog.showModal(owner,
    new SimpleModalBorder(myFormPanel, "Sample Input Forms", SimpleModalBorder.YES_NO_CANCEL_OPTION,
        (controller, action) -> {
            if (action == SimpleModalBorder.YES_OPTION) {
                // handle confirm
            }
        }));
```

Constructors range from `new SimpleModalBorder(component, title)` (title + close button only) up to `new SimpleModalBorder(component, title, ModalBorderOption, int optionType, ModalCallback)`. Built-in `optionType` constants:

- `SimpleModalBorder.DEFAULT_OPTION` — no button row (or pass your own `Option[]` via the `Option[] optionsType` constructor overload).
- `SimpleModalBorder.YES_NO_OPTION` — Yes / No buttons.
- `SimpleModalBorder.YES_NO_CANCEL_OPTION` — Yes / No / Cancel buttons.
- `SimpleModalBorder.OK_CANCEL_OPTION` — Ok / Cancel buttons.

The callback is a `raven.modal.listener.ModalCallback`: `void action(ModalController controller, int action)`. It fires with one of the return codes below (plus `SimpleModalBorder.OPENED` right after the modal finishes opening, and whatever `action` you pass when clicking a custom button/close):

- `YES_OPTION` (`0`), `NO_OPTION` (`1`), `CANCEL_OPTION` (`2`)
- `OK_OPTION` (`0`, same value as `YES_OPTION` since only one of the two option sets is active at a time)
- `CLOSE_OPTION` (`-1`) — fired when the title bar's close (×) button is clicked
- `OPENED` (`20`) — fired once the modal has opened

By default, after the callback runs, the modal auto-closes. Call `controller.consume()` inside the callback to keep it open (e.g. to show a validation error, or to `ModalDialog.pushModal(...)` a follow-up modal instead — see [Stacking modals](#stacking-modals-push--pop)).

`ModalBorderOption` (passed into the constructor, separate from the window-level `Option`) controls the wrapper itself:

| Setter | Description |
|---|---|
| `setUseScroll(boolean)` | wrap the content in a `JScrollPane` — default `false` |
| `setPadding(int)` / `setPadding(top, left, bottom, right)` / `setPadding(PaddingType)` | inner padding around the content — default `PaddingType.EXTRA_LARGE` |

`ModalBorderOption.PaddingType` presets: `SMALL`, `MEDIUM`, `LARGE`, `EXTRA_LARGE`.

Custom option buttons can be added directly instead of a preset `optionType`, via the `Option[] optionsType` constructors, where each `SimpleModalBorder.Option` is a simple `(String text, int type)` pair whose `type` is delivered back through the callback's `action` parameter.

To build your own title-bar style, subclass `SimpleModalBorder` and override `createHeader()`, `createTitleComponent(String)`, `createOptionButton(Option[])` and/or `createButtonOption(Option)` — see `SimpleMessageModal` in the demo module for a themed (success/info/warning/error) example.

## Heavy-weight mode

`option.setHeavyWeight(true)` renders the modal in a real, separate top-level `Window` positioned over the owner, instead of inside the owner window's `JLayeredPane`. Reach for it when a modal needs to render outside the bounds of its owner window (for example `RelativeToOwnerType.RELATIVE_BOUNDLESS`, which requires it), or otherwise needs real top-level-window behavior that an in-window layered pane can't provide. Sliding/push-pop transitions are effectively instantaneous in this mode (`sliderDuration` is treated as `0`).

## See also

- [Toast](./toast.md)
- [Drawer](./drawer.md)
