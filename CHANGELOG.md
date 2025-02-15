# Changelog

## [2.4.0]-SNAPSHOT

### New features and improvements

- Toast
    - Add new `ToastBorderStyle`
        - `borderType` default `BorderType.NONE`
        - `round` default `10`
        - `shadowSize` default `insets(0,0,10,10)`
        - `shadowColor` default `null` (use `Popup.dropShadowColor` as the value)
        - `shadowOpactity` default `-1` (use `Popup.dropShadowOpacity` as the value)
        - `lineSize` default `3` (use when`borderType` as `TRAILING_LINE`, `LEADING_LINE`,`TOP_LINE` and `BOTTOM_LINE`)
        - `borderWidth` default `1` (use when `borderType` as `OUTLINE`)
        - `padding` default `insets(0,0,0,0)`
    - Add new layout option `gap` by default `5`
- Modal dialog
    - SimpleModalBorder: modalBorderOption add padding
        - `SMALL`
        - `MEDIUM`
        - `LARGE`
        - `EXTRA_LARGE` (default)

### Changed

- Toast
    - option `borderType` move from `ToastStyle` to `ToastBorderStyle`
    - option `lineSize` move from `ToastStyle` to `ToastBorderStyle`
- Other
    - Changed `raven.modal.drawer.menu.MenuAnimation.java`
      to `raven.modal.utils.CustomAnimation.java` for reusable class

### Fixed bugs

- Drawer
    - Fixed animation menu items

### Demo

- Dashboard form
    - `TimeSeriesChart` add new `ChartStackedXYBarRenderer`

## [2.3.0] - 2025-01-21

### New features and improvements

- Modal dialog and Toast
    - Add new option `heavyWeight` by default `false` if set to true, the modal and toast will show by create `JWindow`
    - Add new layout option `relativeToOwner` by default `false` if set to true, the location modal and toast will show
      relative to the owner component
    - Add new option `overflowAlignmentAuto` work when component overflow the container
        - if `ture` the component adjust to center of container
        - if `false` the location value `<0.5f` adjust to the left, location value `>0.5f` adjust to the right, location
          value `0.5f` adjust center
            - modal: `true` by default
            - toast: `false` by default
- Modal dialog
    - Add new layout option `relativeToOwnerType` (requires `relativeToOwner` = `true`) with 3 types:
        - `RELATIVE_CONTAINED`: modal and background are confined to the owner's bounds and track the owner's
          visibility (`default`)
        - `RELATIVE_GLOBAL`: background spans the entire window and does not track the owner's visibility
        - `RELATIVE_BOUNDLESS`: background covers the owner, but the modal can extend outside the owner. Tracks owner's
          visibility. (requires `heavyWeight` = `true`)
    - Add new layout option `backgroundPadding` to padding the background. by default `insets(0,0,0,0)`
    - Add new layout option `movable` by default `false` if set to true, the modal can move by mouse drag
- Toast
    - Add new layout option `relativeToOwnerType` (requires `relativeToOwner` = `true`) with 3 types:
        - `RELATIVE_CONTAINED`: toast is confined to the owner's bounds and tracks the owner's visibility. (`default`)
        - `RELATIVE_GLOBAL`: toast spans the entire window and does not track the owner's visibility
        - `RELATIVE_BOUNDLESS`: toast can extend outside the owner's bounds and tracks the owner's visibility

### Fixed bugs

- Modal dialog
    - Fixed `borderWidth`
        - fixed border not paint when value `1` and round border is active
        - fixed border not paint when value `0.5f`

## Demo

- Create dashboard form using `JFreeChart`
- Update improvements the UI `FormSearchButton`

## [2.2.0] - 2024-12-31

### New features and improvements

- Modal dialog
    - Add custom location (PR #22)
    - Add new option `animationOnClose` by default `true` use to enable or disabled animation while closing
- Toast
    - Add custom location (PR #22)
    - Add new style option `paintTextColor` by default `false`. When set to true, the message text will be painted using
      the color corresponding to the toast type

### Changed

- FlatLaf: update to `v3.5.4`

### Fixed bugs

- Toast
    - Fixed paint border toast background when animation fade in fade out

### Demo

- DateTimePicker: library update to `v2.0.0`
- Login: password field add `PasswordRevealIcon`
- Form Table: remove table header separator

## [2.1.0] - 2024-11-07

### New features and improvements

- Modal dialog
    - Add new callback action `SimpleModalBorder.OPENED` work when modal has opened
    - Add new layout option `animateScale` to scale the modal when open and close
- Extras
    - LightDarkButton: add list support l&f `light` and `dark` to auto install themes when switch
    - Add new `SlidePane` component (PR #19)

### Changed

- Modal dialog
    - Update drop shadow border use `insets` instead of `shadowSize` and `shadowTopSize` (PR #16)
    - Default animation duration decreased from `350` to `200`
- Drawer
    - Embed drawer menu now apply border option
    - Add new method `Drawer.getDrawerOption()` and use it instead of `Drawer.getDrawerBuilder().getOption()`
    - Default animation duration set to `300`
    - Menu item horizontal margin change from `7,7` to `10,10`

### Demo

- Use `JTextPane` instead of `JTextArea` to avoid error layout when component is RTL (issues with layout)
- Drawer now use shadow border
- Update form layout
- Quick search update layout and add new favorite option (PR #17)
- Add new frame footer and create new `MemoryBar`
- Update form DateTimePicker

## [2.0.0] - 2024-10-20

### New features and improvements

- Modal dialog
    - Add new border option `borderWidth` and `borderColor` (PR #14)
    - Add drop shadow border
    - Slider layout changed with animation when `push` or `pop`
- Drawer
    - Support menu two mode `full` and `compact`
    - Add new drawer footer `LightDark Button`
- Extras
    - Add new `LightDarkButton`

### Changed

- Modal dialog
    - Add new option `sliderDuration`
    - Add new `BorderOption` class
    - Move `round` option in class `Option` to `BorderOption`
    - Move `menuOption` instance to class SimpleDrawerBuilder and init in constructor
    - Modal background now paint as the component background and not allow `null` background
- Drawer
    - Removed menu `headerSeparator` in default
    - Use `LightDarkButtonFooter` in default
    - Changed menu `header` layout to horizontal view

### Fixed bugs

- Modal dialog
    - Fixed component not install when push modal
    - Fixed layout
- Toast
    - Fixed `ToastPanel` border `null` when changed themes or UI updated

### Demo

- Add `about` information
- Updated select drawer menu when undo and redo form
- Fixed component not install when push modal
- Fixed component orientation `RTL` not update when show form

## [1.2.0] - 2024-09-21

### New features and improvements

- Drawer
    - add line style and menu selected color (PR #7)
    - support for invoke menu event using `class` (PR #13)

### Demo

- Fixed form search inconsistent search case-insensitive (issues #9) (PR #10)
- Updated change selected drawer menu item when open form by `quick-form-search`
- Datetime picker library update to v1.4.0

## [1.1.0] - 2024-07-27

### New features and improvements

- Modal dialog
    - SimpleModalBorder
        - add constructor to custom action type
        - add modal action (PR #1)
- Toast
    - ToastStyle
        - add new option `showCloseButton`
        - add new `customIcon`
    - add method to update the message in toast promise callback (PR #2)
- Extras
    - AvatarIcon: now support border option (PR #5)

### Changed

- Modal dialog
    - Modal
        - change method `init()` to `installComponent()` and it called when modal show
- Drawer
    - Method `closeDrawer()` now close with animation

### Fixed bugs

- Modal dialog
    - SimpleModalBorder: fixed some code to customizable
- Extras
    - AvatarIcon: fixed image not painted center and size not affect when round value is 0

### Demo

- Add custom modal message (PR #3)
- Add responsive layout (PR #4)
- Add form avatar icon (PR #5)
- Add quick form search (PR #6)

## [1.0.0] - 2024-06-23

- Initial release
