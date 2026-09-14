# Drawer

A responsive, animated navigation side-drawer for Swing. It installs into the layered pane of a `JFrame`/`JDialog`/`JInternalFrame` and is typically used as an application's main navigation menu, with support for nested submenus, role-based item validation, and automatic collapse into an overlay on small windows.

The drawer is driven by a `raven.modal.drawer.DrawerBuilder` — an interface that supplies the header/menu/footer components and layout options. Most applications don't implement `DrawerBuilder` directly; they extend `raven.modal.drawer.simple.SimpleDrawerBuilder`, a ready-made implementation with a header, a scrollable nested menu, and a footer.

## Quick start

```java
public class MyDrawerBuilder extends SimpleDrawerBuilder {

    private static MyDrawerBuilder instance;

    public static MyDrawerBuilder getInstance() {
        if (instance == null) {
            instance = new MyDrawerBuilder();
        }
        return instance;
    }

    private MyDrawerBuilder() {
        super(createSimpleMenuOption());
    }

    private static MenuOption createSimpleMenuOption() {
        MenuOption menuOption = new MenuOption();
        menuOption.setMenus(new MenuItem[]{
                new Item.Label("MAIN"),
                new Item("Dashboard", "dashboard.svg", FormDashboard.class),
                new Item("Components", "components.svg")
                        .subMenu("Modal", FormModal.class)
                        .subMenu("Toast", FormToast.class),
                new Item("Setting", "setting.svg", FormSetting.class),
        });
        menuOption.setBaseIconPath("path/to/icons")
                .setIconScale(0.45f);

        menuOption.addMenuEvent((action, index) -> {
            Class<?> itemClass = action.getItem().getItemClass();
            if (itemClass != null) {
                // e.g. FormManager.showForm(itemClass)
            }
        });
        return menuOption;
    }

    @Override
    public SimpleHeaderData getSimpleHeaderData() {
        return new SimpleHeaderData()
                .setTitle("My App")
                .setDescription("user@example.com");
    }

    @Override
    public SimpleFooterData getSimpleFooterData() {
        return new SimpleFooterData()
                .setTitle("My App")
                .setDescription("Version 1.0.0");
    }
}
```

Install it into your top-level window:

```java
Drawer.installDrawer(this, MyDrawerBuilder.getInstance());
```

`Drawer.installDrawer(Component parentComponent, DrawerBuilder drawerBuilder)` walks up from `parentComponent` to find its enclosing `JFrame`/`JDialog`/`JInternalFrame`, builds the drawer panel into that window's layered pane, and — if `drawerBuilder.getOpenDrawerAt() >= 0` — wires up responsive show/hide behavior (see [Responsive behavior](#responsive-behavior)).

`SimpleDrawerBuilder` requires only two abstract methods to be implemented: `getSimpleHeaderData()` and `getSimpleFooterData()`. Everything else (`getDrawerWidth()`, `getDrawerCompactWidth()`, `getOpenDrawerAt()`, `openDrawerAtScale()`, `createOption()`, `createHeader()`, `createFooter()`, `createHeaderSeparator()`, `build(DrawerPanel)`) has a sensible default and can be overridden as needed.

## Showing and hiding the drawer

```java
Drawer.showDrawer();
Drawer.closeDrawer();
Drawer.setVisible(false);   // hide the drawer entirely
Drawer.setVisible(true);
boolean visible = Drawer.isVisible();
boolean open = Drawer.isOpen();
```

- `Drawer.showDrawer()` — when the drawer is currently in overlay mode (see [Responsive behavior](#responsive-behavior)), shows it as a modal overlay; also makes the drawer visible if it was hidden.
- `Drawer.closeDrawer()` — closes the modal overlay (if currently shown as one). Internally it closes the modal registered under `Drawer.DRAWER_ID` (`"drawer"`).
- `Drawer.setVisible(boolean)` — shows/hides the drawer completely (embedded or overlay).
- `Drawer.isVisible()` / `Drawer.isOpen()` — query current state; both return `true` when the drawer isn't in responsive mode (no `DrawerLayoutResponsive` installed).

## Menu items

Menu content is built from arrays of `raven.modal.drawer.item.MenuItem`. The concrete types are:

- **`Item`** — a clickable menu entry.
  ```java
  new Item("Dashboard")                                  // name only
  new Item("Dashboard", FormDashboard.class)              // name + target class
  new Item("Dashboard", "dashboard.svg")                  // name + icon path
  new Item("Dashboard", "dashboard.svg", FormDashboard.class) // name + icon + target class
  ```
  The `Class<?>` argument (`getItemClass()`) is optional and is used purely for the [active-item selection](#selecting-the-active-menu-item) and validation helpers — it isn't required for the menu to render or fire events.

  Nested submenus are built fluently:
  ```java
  new Item("Components", "components.svg")
          .subMenu("Modal", FormModal.class)
          .subMenu("Toast", FormToast.class)
          .subMenu(new Item("Nested").subMenu("Deeper"));
  ```
  `item.isSubmenuAble()` reports whether an item has children, `item.getSubMenu()` returns the child `List<Item>`, and `item.getIndex()` returns the item's position as an `int[]` path (auto-assigned when the menu is built; also what `MenuEvent`/`MenuValidation` receive).

- **`Item.Label`** — a non-clickable section header (`new Item.Label("MAIN")`).
- **`Item.Separator`** — a horizontal divider (`new Item.Separator()`).

Attach the array with `MenuOption.setMenus(MenuItem[])` (see below).

## Menu open mode

`MenuOption.MenuOpenMode` is an enum with two constants:

- `FULL` — icons and text labels are shown (the default).
- `COMPACT` — only icons are shown (a slimmer rail); clicking an item with children opens its submenu as a popup instead of expanding inline.

```java
Drawer.setDrawerOpenMode(MenuOption.MenuOpenMode.COMPACT);
MenuOption.MenuOpenMode mode = Drawer.getDrawerOpenMode();
Drawer.toggleMenuOpenMode();
```

These three `Drawer` methods only work when the installed `DrawerBuilder` is a `SimpleDrawerBuilder` — `getDrawerOpenMode()` throws a `RuntimeException` ("Error drawer not support open mode") otherwise, and `setDrawerOpenMode`/`toggleMenuOpenMode` are no-ops for a non-`SimpleDrawerBuilder`.

## Menu style

`MenuStyle` is a set of overridable no-op hooks for styling each visual part of the menu — override the ones you need:

```java
menuOption.setMenuStyle(new MenuStyle() {
    @Override
    public void styleMenu(JComponent component) {
        component.putClientProperty(FlatClientProperties.STYLE, "background:$Menu.background;");
    }

    @Override
    public void styleMenuItem(JButton menu, int[] index, boolean isMainItem) {
        // per-item styling, e.g. adjust margins for top-level items
    }
});
```

Available hooks: `styleMenu(JComponent)`, `styleMenuPanel(JPanel, int[] index)`, `styleMenuItem(JButton, int[] index, boolean isMainItem)`, `styleCompactMenuItem(JMenuItem, int[] index, boolean isMainItem)`, `styleLabel(JLabel)`, `styleSeparator(JSeparator)`.

`MenuStyle` also owns the submenu connector-line renderer, described next.

## Line style renderers

When a top-level menu item has nested children (in `FULL` open mode), the drawer draws a connecting line/arrow between the parent and its children. Which style is drawn is controlled by an `AbstractDrawerLineStyleRenderer`, set via `MenuStyle`:

```java
menuOption.getMenuStyle().setDrawerLineStyleRenderer(new DrawerStraightDotLineStyle());
```

Available renderers (all in `raven.modal.drawer.renderer`):

- **`DrawerCurvedLineStyle`** — a smooth, rounded connector line (the default set by `MenuStyle`). Constructors let you toggle rounded corners, whether the line paints in the accent/selected color, and a custom line color: `DrawerCurvedLineStyle()`, `DrawerCurvedLineStyle(boolean useRound)`, `DrawerCurvedLineStyle(boolean useRound, boolean lineSelectedPaint)`, `DrawerCurvedLineStyle(boolean useRound, boolean lineSelectedPaint, Color lineColor)`.
- **`DrawerStraightDotLineStyle`** — a straight line with dotted connector points. Same constructor overloads as above.
- **`DrawerNoneLineStyle`** — draws no connector line at all. Constructors: `DrawerNoneLineStyle()`, `DrawerNoneLineStyle(Color lineColor)` (color is unused since nothing is drawn).

You can also subclass `AbstractDrawerLineStyleRenderer` directly for a fully custom look by implementing `draw(Graphics2D, JComponent, int startX, int startY, int endX, int endY, int[] subMenuLocation, int selectedIndex, boolean isLeftToRight, AbstractMenuElement menuElement)` (and optionally overriding `drawArrow(...)`).

## Menu validation

`MenuValidation` gates which menu items and labels are rendered — the canonical use case is hiding menu entries a signed-in user isn't permitted to see. Override `menuValidation(int[] index)` (called with an item's index path) and/or `labelValidation(int index)`, both of which default to `true`:

```java
public class MyMenuValidation extends MenuValidation {

    public static ModelUser user;

    @Override
    public boolean menuValidation(int[] index) {
        if (user == null) return false;
        if (user.getRole() == ModelUser.Role.ADMIN) return true;
        // hide specific items for non-admins, e.g. index {2, 0} = Components -> Modal
        return !Arrays.equals(index, new int[]{2, 0});
    }
}
```

```java
menuOption.setMenuValidation(new MyMenuValidation());
```

Two constructor flags on `MenuValidation` (default `MenuValidation()` sets both `true`) affect index numbering when items are hidden:

- `keepMenuValidationIndex` — when `true`, a hidden item's slot still consumes an index (so remaining item indices don't shift); when `false`, indices compact around hidden items.
- `removeLabelWhenEmptyMenu` — when `true`, a section `Item.Label` is itself hidden if every menu item following it (until the next label/separator) is invalidated.

Combine with class-based lookup for permission checks outside the menu itself:

```java
public static boolean validation(Class<? extends Form> itemClass) {
    int[] index = Drawer.getMenuIndexClass(itemClass);
    return index != null && validation(index);
}
```

## Header and footer

`SimpleDrawerBuilder` renders a `SimpleHeader` at the top and a footer (by default a `LightDarkButtonFooter`) at the bottom, each configured via a small data object.

**Header** — `SimpleHeaderData` (fluent setters, all optional): `setIcon(Icon)`, `setTitle(String)`, `setDescription(String)`, `setHeaderStyle(SimpleHeaderStyle)`.

```java
@Override
public SimpleHeaderData getSimpleHeaderData() {
    AvatarIcon icon = new AvatarIcon(new FlatSVGIcon("path/avatar.svg", 100, 100), 50, 50, 3.5f);
    return new SimpleHeaderData()
            .setIcon(icon)
            .setTitle("Ra Ven")
            .setDescription("raven@gmail.com");
}
```

To restyle the header (fonts/colors/spacing), subclass `SimpleHeaderStyle` and override `styleHeader(JComponent)`, `styleComponent(JComponent, int styleType)`, and/or `getTextGap()` (default `3`).

**Footer** — `SimpleFooterData`: `setTitle(String)`, `setDescription(String)`, `setFooterStyle(SimpleFooterStyle)`.

```java
@Override
public SimpleFooterData getSimpleFooterData() {
    return new SimpleFooterData()
            .setTitle("My App")
            .setDescription("Version 1.0.0");
}
```

The default footer implementation, `LightDarkButtonFooter`, adds a light/dark theme toggle button below the title/description:

```java
LightDarkButtonFooter footer = (LightDarkButtonFooter) getFooter();
footer.addModeChangeListener(isDarkMode -> {
    // react to theme toggle
});
```

`SimpleFooter` exposes style-type constants for use in a custom `SimpleFooterStyle#styleComponent`: `LABEL_TITLE_STYLE = 0`, `LABEL_DESCRIPTION_STYLE = 1`, and `LightDarkButtonFooter` adds `LIGHT_DARK_BUTTON_STYLE = 2`. To use a plain footer without the theme toggle, override `createFooter()` in your builder to return a plain `SimpleFooter` instead.

## Selecting the active menu item

```java
Drawer.setSelectedItemClass(FormDashboard.class);
int[] index = Drawer.getMenuIndexClass(FormDashboard.class);
```

- `Drawer.setSelectedItemClass(Class<?> itemClass)` — finds the menu `Item` whose `getItemClass()` matches and highlights it as selected (also runs any registered `MenuEvent`s for that item's index, honoring `MenuAction.consume()`). Pass `null` to clear the selection.
- `Drawer.getMenuIndexClass(Class<?> itemClass)` — returns the `int[]` index path for the item bound to `itemClass`, or `null` if not found. Both methods require the installed builder to be a `SimpleDrawerBuilder`.

This is the pattern used to keep the drawer's highlighted item in sync with whatever "page"/form is currently shown — call `setSelectedItemClass` whenever you navigate programmatically (not via a menu click).

Note on menu events and selection: use `MenuAction.consume()` inside a `MenuEvent` for items that trigger an action rather than navigate to a page (e.g. "Logout", "About" in the quick-start example) — consuming the action prevents that item from being highlighted as the active selection.

## Responsive behavior

`DrawerBuilder.getOpenDrawerAt()` returns a pixel width threshold for the drawer's parent window (`SimpleDrawerBuilder`'s default is `-1`, meaning "never responsive" — the drawer stays permanently embedded in the layout). Set it to a non-negative value to enable responsive behavior:

```java
@Override
public int getOpenDrawerAt() {
    return 1000; // switch to overlay mode when the window is <= 1000px wide
}

@Override
public boolean openDrawerAtScale() {
    return false; // treat 1000 as a raw pixel value instead of scaling it with the UI scale factor
}
```

When the parent window's width drops to or below this threshold, the drawer detaches from the normal layout and becomes a modal overlay (shown/hidden via `Drawer.showDrawer()` / `Drawer.closeDrawer()`, e.g. from a menu button), rather than an always-visible embedded panel. `openDrawerAtScale()` (default `true`) controls whether the threshold is scaled by FlatLaf's UI scale factor. As a special case, if `MenuOpenMode.COMPACT` is active while the drawer is laid out along the top/bottom edge (a "horizontal" drawer) rather than the leading/trailing edge, compact mode isn't supported inline and the drawer is forced into overlay mode regardless of window width.

## See also

- [Modal Dialog](./modal-dialog.md)
- [Toast](./toast.md)
