# Changelog

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