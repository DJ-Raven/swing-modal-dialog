package raven.modal.drawer.menu;

import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.UIScale;
import raven.modal.drawer.item.Item;
import raven.modal.drawer.item.MenuItem;
import raven.modal.drawer.renderer.AbstractDrawerLineStyleRenderer;
import raven.modal.layout.DrawerMenuLayout;
import raven.modal.utils.CustomAnimation;
import raven.modal.utils.FlatLafStyleUtils;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

/**
 * @author Raven
 */
public class DrawerMenu extends AbstractMenuElement {

    public int[] getMenuSelectedIndex() {
        return menuSelectedIndex == null ? null : copyArray(menuSelectedIndex);
    }

    protected void setMenuSelectedIndex(int[] menuSelectedIndex) {
        if (menuSelectedIndex == null) {
            this.menuSelectedIndex = null;
        } else {
            this.menuSelectedIndex = copyArray(menuSelectedIndex);
        }
        repaint();
    }

    public void setMenuSelectedClass(Class<?> itemClass) {
        if (itemClass == null) {
            setMenuSelectedIndex(null);
            return;
        }
        for (MenuItem item : menuOption.menus) {
            if (item.isMenu()) {
                Item t = getMenuItemOf((Item) item, itemClass);
                if (t != null) {
                    MenuAction action = runEvent(t, t.getIndex());
                    if (action != null) {
                        if (!action.getConsume()) {
                            if (isMenuAutoSelection(t.isMenu())) {
                                setMenuSelectedIndex(t.getIndex());
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    public int[] getMenuIndexClass(Class<?> itemClass) {
        if (itemClass == null) {
            return null;
        }
        for (MenuItem item : menuOption.menus) {
            if (item.isMenu()) {
                Item t = getMenuItemOf((Item) item, itemClass);
                if (t != null) {
                    return t.getIndex();
                }
            }
        }
        return null;
    }

    private Item getMenuItemOf(Item item, Class<?> itemClass) {
        if (item.getItemClass() == itemClass) {
            return item;
        }
        if (!item.isSubmenuAble()) {
            return null;
        }
        for (Item t : item.getSubMenu()) {
            Item i = getMenuItemOf(t, itemClass);
            if (i != null) {
                return i;
            }
        }
        return null;
    }

    private final int menuItemPadding = 10;
    private final int iconTextGap = 10;
    private final MenuOption menuOption;
    private int[] menuSelectedIndex;

    public DrawerMenu(MenuOption menuOption) {
        this.menuOption = menuOption;
        init();
    }

    public void rebuildMenu() {
        removeAll();
        boolean isRightToLeft = !getComponentOrientation().isLeftToRight();
        buildMenu();
        if (isRightToLeft) {
            super.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }
        layoutOptionChanged(menuOpenMode);
    }

    private void init() {
        setLayout(new DrawerMenuLayout(this, menuOption));
        if (menuOption.menuStyle != null) {
            menuOption.menuStyle.styleMenu(this);
        }
        buildMenu();
    }

    private void buildMenu() {
        menuSelectedIndex = null;
        MenuItem[] menus = menuOption.menus;
        if (menus != null) {
            int index = 0;
            int validationIndex = -1;
            for (int i = 0; i < menus.length; i++) {
                MenuItem menuItem = menus[i];
                if (menuItem.isMenu()) {
                    Item item = (Item) menuItem;
                    int[] arrIndex = {index};

                    if (item.isSubmenuAble()) {
                        // create submenu
                        int[] arrValidationIndex = {++validationIndex};
                        boolean validation = menuOption.menuValidation.menuValidation(copyArray(arrValidationIndex));
                        if (validation) {
                            add(createSubmenuItem(item, arrIndex, arrValidationIndex, 0, 0));
                        }
                        if (validation || menuOption.menuValidation.keepMenuValidationIndex) {
                            index++;
                        }
                        item.initIndexOnNull(arrIndex, validation);
                    } else {
                        // create single menu item
                        int[] arrValidationIndex = {++validationIndex};
                        boolean validation = menuOption.menuValidation.menuValidation(arrValidationIndex);
                        if (validation) {
                            ButtonItem button = createMenuItem(item, arrIndex, 0, false);
                            applyMenuEvent(button, arrIndex);
                            add(button);
                        }
                        if (validation || menuOption.menuValidation.keepMenuValidationIndex) {
                            index++;
                        }
                        item.initIndexOnNull(arrIndex, validation);
                    }
                } else {
                    // create label and separator
                    if (checkLabelValidation(i, index)) {
                        if (menuItem instanceof Item.Label) {
                            Item.Label label = (Item.Label) menuItem;
                            add(createLabel(label.getName()));
                        } else if (menuItem instanceof Item.Separator) {
                            add(createSeparator());
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void layoutOptionChanged(MenuOption.MenuOpenMode menuOpenMode) {
        boolean isFull = menuOpenMode == MenuOption.MenuOpenMode.FULL;
        for (Component com : getComponents()) {
            ButtonItem item = getButtonItem(com);
            if (item != null) {
                if (isFull) {
                    item.setText(item.getItem().getName());
                    item.setHorizontalAlignment(SwingConstants.LEADING);
                } else {
                    item.setText("");
                    item.setHorizontalAlignment(SwingConstants.CENTER);
                }
                applySelectedButtonStyle(item);
            } else if (com instanceof JLabel) {
                JLabel label = (JLabel) com;
                if (isFull) {
                    label.setHorizontalAlignment(SwingConstants.LEADING);
                } else {
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                }
            }

            // hide and show submenu item
            if (com instanceof SubMenuItem) {
                SubMenuItem subMenuItem = (SubMenuItem) com;
                float animate = isFull && subMenuItem.menuShow ? 1 : 0;
                subMenuItem.setAnimate(animate);
            }
        }
    }

    private ButtonItem getButtonItem(Component com) {
        if (com instanceof ButtonItem) {
            return (ButtonItem) com;
        } else if (com instanceof JPanel) {
            JPanel panel = (JPanel) com;
            if (panel.getComponentCount() > 0) {
                if (panel.getComponent(0) instanceof ButtonItem) {
                    return (ButtonItem) panel.getComponent(0);
                }
            }
        }
        return null;
    }

    private void applySelectedButtonStyle(ButtonItem item) {
        if (item.isMainItem) {
            boolean isFull = menuOpenMode == MenuOption.MenuOpenMode.FULL;
            if (isFull) {
                FlatLafStyleUtils.appendStyle(item, "" +
                        "selectedBackground:null;" +
                        "selectedForeground:$Component.accentColor;");
            } else {
                FlatLafStyleUtils.appendStyle(item, "" +
                        "selectedBackground:$Button.selectedBackground;");
            }
        }
    }

    private int[] copyArray(int[] arr) {
        return Arrays.copyOf(arr, arr.length);
    }

    protected boolean isMenuSelected(int[] itemIndex) {
        if (menuSelectedIndex == null) {
            return false;
        }
        if (itemIndex.length > menuSelectedIndex.length) {
            return false;
        }
        for (int i = 0; i < itemIndex.length; i++) {
            if (itemIndex[i] != menuSelectedIndex[i]) {
                return false;
            }
        }
        return true;
    }

    private String getBasePath() {
        if (menuOption.baseIconPath == null) {
            return "";
        }
        if (menuOption.baseIconPath.endsWith("/")) {
            return menuOption.baseIconPath;
        } else {
            return menuOption.baseIconPath + "/";
        }
    }

    protected Icon getIcon(String icon, int menuLevel) {
        if (icon != null) {
            String path = getBasePath();
            float iconScale;
            if (menuLevel < menuOption.iconScale.length) {
                iconScale = menuOption.iconScale[menuLevel];
            } else {
                iconScale = menuOption.iconScale[menuOption.iconScale.length - 1];
            }
            return menuOption.buildMenuIcon(path + icon, iconScale);
        } else {
            return null;
        }
    }

    protected ButtonItem createMenuItem(Item item, int[] index, int menuLevel, boolean isMainItem) {
        ButtonItem button = new ButtonItem(item, index, isMainItem);
        Icon iconObject = getIcon(item.getIcon(), menuLevel);
        if (iconObject != null) {
            button.setIcon(iconObject);
        }
        button.setHorizontalAlignment(JButton.LEADING);
        if (menuOption.menuStyle != null) {
            menuOption.menuStyle.styleMenuItem(button, copyArray(index), isMainItem);
        }
        Insets margin = new Insets(7, menuItemPadding, 7, menuItemPadding);
        FlatLafStyleUtils.appendStyleIfAbsent(button, "" +
                "arc:15;" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0;" +
                "background:null;" +
                "iconTextGap:" + iconTextGap + ";");
        FlatLafStyleUtils.appendStyle(button, "" +
                "margin:" + FlatLafStyleUtils.appendMargin(button, margin));
        applySelectedButtonStyle(button);
        return button;
    }

    protected void applyMenuEvent(ButtonItem button, int[] index) {
        button.addActionListener(e -> {
            MenuAction action = runEvent(button.getItem(), index);
            if (action != null) {
                if (!action.getConsume()) {
                    if (isMenuAutoSelection(button.isMainItem())) {
                        menuSelectedIndex = index;
                        repaint();
                    }
                }
            }
        });
    }

    protected boolean isMenuAutoSelection(boolean isMainMenu) {
        MenuOption.MenuItemAutoSelectionMode mode = menuOption.menuItemAutoSelectionMode;
        if (mode == null || mode == MenuOption.MenuItemAutoSelectionMode.NONE) {
            return false;
        }
        if (mode == MenuOption.MenuItemAutoSelectionMode.SELECT_ALL) {
            return true;
        }
        if (mode == MenuOption.MenuItemAutoSelectionMode.SELECT_MAIN_MENU_LEVEL && isMainMenu) {
            return true;
        }
        if (mode == MenuOption.MenuItemAutoSelectionMode.SELECT_SUB_MENU_LEVEL && !isMainMenu) {
            return true;
        }
        return false;
    }

    protected MenuAction runEvent(Item menuItem, int[] index) {
        if (!menuOption.events.isEmpty()) {
            MenuAction action = new MenuAction(menuItem);
            for (MenuEvent event : menuOption.events) {
                event.selected(action, copyArray(index));
            }
            return action;
        }
        return null;
    }

    protected Component createSubmenuItem(Item menu, int[] index, int[] validationIndex, int menuLevel, int iconTextGap) {
        return new SubMenuItem(menu, index, validationIndex, menuLevel, iconTextGap);
    }

    protected boolean checkLabelValidation(int labelIndex, int menuIndex) {
        if (menuOption.menuValidation.labelValidation(labelIndex)) {
            if (menuOption.menuValidation.removeLabelWhenEmptyMenu) {
                boolean fondMenu = false;
                for (int i = labelIndex + 1; i < menuOption.menus.length; i++) {
                    MenuItem menuItem = menuOption.menus[i];
                    if (menuItem instanceof Item.Separator) {
                        continue;
                    }
                    if (menuItem.isMenu()) {
                        if (menuOption.menuValidation.menuValidation(new int[]{menuIndex})) {
                            fondMenu = true;
                            break;
                        }
                    } else {
                        break;
                    }
                    menuIndex++;
                }
                return fondMenu;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    protected Component createLabel(String name) {
        JLabel label = new JLabel(name);
        if (menuOption.menuStyle != null) {
            menuOption.menuStyle.styleLabel(label);
        }
        FlatLafStyleUtils.appendStyleIfAbsent(label, "" +
                "foreground:$Label.disabledForeground;");
        return label;
    }

    protected Component createSeparator() {
        JSeparator separator = new JSeparator();
        if (menuOption.menuStyle != null) {
            menuOption.menuStyle.styleSeparator(separator);
        }
        FlatLafStyleUtils.appendStyleIfAbsent(separator, "" +
                "height:11;" +
                "stripeIndent:5;");
        return separator;
    }

    public MenuOption getMenuOption() {
        return menuOption;
    }

    @Override
    public void applyComponentOrientation(ComponentOrientation o) {
        boolean change = o.isLeftToRight() != getComponentOrientation().isLeftToRight();
        super.applyComponentOrientation(o);
        if (change) {
            rebuildMenu();
        }
    }

    protected class ButtonItem extends JButton {

        public Item getItem() {
            return item;
        }

        public boolean isMainItem() {
            return isMainItem;
        }

        private final Item item;
        private final int[] itemIndex;
        private final boolean isMainItem;

        public ButtonItem(Item item, int[] itemIndex, boolean isMainItem) {
            super(item.getName());
            this.item = item;
            this.itemIndex = itemIndex;
            this.isMainItem = isMainItem;
        }

        @Override
        public boolean isSelected() {
            return isMenuSelected(itemIndex);
        }
    }

    public class SubMenuItem extends JPanel {

        private final int menuLevel;
        private int iconTextGap;
        private int levelSpace = 13;
        private int submenuSpace = 5;
        private SubmenuLayout menuLayout;
        private boolean menuShow;
        private final Item menu;
        private final int[] index;
        private final int[] validationIndex;
        private int iconWidth;
        private CustomAnimation animation;

        public void setAnimate(float animate) {
            menuLayout.setAnimate(animate);
        }

        public SubMenuItem(Item menu, int[] index, int[] validationIndex, int menuLevel, int iconTextGap) {
            this.menu = menu;
            this.index = index;
            this.validationIndex = validationIndex;
            this.menuLevel = menuLevel;
            this.iconTextGap = iconTextGap;
            init();
        }

        private void init() {
            menuLayout = new SubmenuLayout();
            setLayout(menuLayout);
            // use opaque false on the submenu panel level >0 to fix g2d draw arrow line
            setOpaque(menuLevel == 0);
            if (menuOption.menuStyle != null) {
                menuOption.menuStyle.styleMenuPanel(this, copyArray(this.index));
            }
            FlatLafStyleUtils.appendStyleIfAbsent(this, "" +
                    "background:null");

            iconWidth = 22;
            int index = 0;
            int validationIndex = -1;
            int nextMenuLevel = menuLevel + 1;

            // create menu item
            ButtonItem mainButton;
            if (menuLevel == 0) {
                // create first level menu item
                mainButton = createMenuItem(menu, this.index, menuLevel, true);
                if (mainButton.getIcon() != null) {
                    iconTextGap = UIScale.unscale(mainButton.getIconTextGap());
                }
                levelSpace += iconTextGap;
            } else {
                levelSpace += iconTextGap;
                int addSpace = menuLevel > 1 ? (menuLevel - 1) * levelSpace : 0;
                int gap = iconWidth + addSpace + iconTextGap;
                mainButton = createSubMenuItem(menu, this.index, gap, true);
            }
            if (mainButton.getIcon() != null) {
                iconWidth = UIScale.unscale(mainButton.getIcon().getIconWidth());
            }
            createMainMenuEvent(mainButton);
            applyMenuEvent(mainButton, this.index);
            add(mainButton);
            for (int i = 0; i < menu.getSubMenu().size(); i++) {
                int[] arrIndex = createArrayIndex(this.index, index);
                int[] arrValidationIndex = createArrayIndex(this.validationIndex, ++validationIndex);
                boolean validation = menuOption.menuValidation.menuValidation(copyArray(arrValidationIndex));
                Item item = menu.getSubMenu().get(i);
                item.initIndexOnNull(arrIndex, validation);
                if (validation) {
                    if (item.isSubmenuAble()) {
                        add(createSubmenuItem(item, arrIndex, arrValidationIndex, nextMenuLevel, iconTextGap));
                    } else {
                        // create single menu item
                        int addSpace = menuLevel * levelSpace;
                        int gap = iconWidth + addSpace + iconTextGap;
                        ButtonItem button = createSubMenuItem(item, arrIndex, gap, false);
                        applyMenuEvent(button, arrIndex);
                        add(button);
                    }
                }
                if (validation || menuOption.menuValidation.keepMenuValidationIndex) {
                    index++;
                }
            }
        }

        private int[] createArrayIndex(int[] index, int subIndex) {
            int[] newArr = new int[index.length + 1];
            for (int i = 0; i < index.length; i++) {
                newArr[i] = index[i];
            }
            newArr[newArr.length - 1] = subIndex;
            return newArr;
        }

        private void createMainMenuEvent(JButton button) {
            button.addActionListener(e -> {
                if (getMenuOpenMode() == MenuOption.MenuOpenMode.FULL) {
                    menuShow = !menuShow;
                    if (animation == null) {
                        animation = new CustomAnimation(f -> setAnimate(f));
                    }
                    animation.run(menuShow);
                } else {
                    new PopupSubmenu(DrawerMenu.this, this, menu).show(button);
                }
            });
        }

        protected ButtonItem createSubMenuItem(Item item, int[] index, int gap, boolean isMainItem) {
            ButtonItem button = new ButtonItem(item, index, isMainItem);
            button.setHorizontalAlignment(JButton.LEADING);
            if (menuOption.menuStyle != null) {
                menuOption.menuStyle.styleMenuItem(button, copyArray(index), isMainItem);
            }
            boolean isRightToLeft = !DrawerMenu.this.getComponentOrientation().isLeftToRight();
            Insets margin = isRightToLeft ? new Insets(7, menuItemPadding, 7, gap + menuItemPadding)
                    : new Insets(7, gap + menuItemPadding, 7, menuItemPadding);
            FlatLafStyleUtils.appendStyleIfAbsent(button, "" +
                    "arc:15;" +
                    "borderWidth:0;" +
                    "focusWidth:0;" +
                    "innerFocusWidth:0;" +
                    "background:null;");
            FlatLafStyleUtils.appendStyle(button, "" +
                    "margin:" + FlatLafStyleUtils.appendMargin(button, margin) + ";");
            applySelectedButtonStyle(button);
            return button;
        }

        @Override
        protected void paintChildren(Graphics g) {
            super.paintChildren(g);
            if (getComponentCount() > 0) {
                if (menuOption != null && menuOption.menuStyle != null) {
                    AbstractDrawerLineStyleRenderer lineStyleRenderer = menuOption.menuStyle.getDrawerLineStyleRenderer();
                    if (lineStyleRenderer != null) {
                        boolean ltr = getComponentOrientation().isLeftToRight();
                        int menuHeight = getComponent(0).getHeight();
                        int width = getWidth();
                        Graphics2D g2 = (Graphics2D) g.create();
                        FlatUIUtils.setRenderingHints(g2);

                        // create submenu line
                        int last = getLastLocation();
                        int gap = UIScale.scale((menuItemPadding + (iconWidth / 2)) + ((levelSpace + submenuSpace - 5) * menuLevel));
                        int x = ltr ? gap : width - gap;
                        int count = getComponentCount();
                        int[] subMenuLocation = new int[count - 1];
                        int selectedIndex = -1;
                        for (int i = 1; i < count; i++) {
                            Component com = getComponent(i);
                            int y;
                            if (com instanceof SubMenuItem) {
                                y = com.getY() + ((SubMenuItem) com).getFirstItemLocation();
                                if (((SubMenuItem) com).isHasMenuSelected()) {
                                    selectedIndex = i - 1;
                                }
                            } else {
                                if (com instanceof ButtonItem) {
                                    if (((ButtonItem) com).isSelected()) {
                                        selectedIndex = i - 1;
                                    }
                                }
                                y = com.getY() + (com.getHeight() / 2);
                            }
                            subMenuLocation[i - 1] = y;
                        }
                        lineStyleRenderer.draw(g2, this, x, menuHeight, x, last, subMenuLocation, selectedIndex, ltr, DrawerMenu.this);
                        // create arrow
                        lineStyleRenderer.drawArrow(g2, this, width, menuHeight, menuLayout.getAnimate(), isHasMenuSelected(), ltr, DrawerMenu.this);
                        g2.dispose();
                    }
                }
            }
        }

        private boolean isHasMenuSelected() {
            return isMenuSelected(index);
        }

        private int getLastLocation() {
            Component com = getComponent(getComponentCount() - 1);
            if (com instanceof SubMenuItem) {
                SubMenuItem subMenuItem = (SubMenuItem) com;
                return com.getY() + subMenuItem.getFirstItemLocation();
            } else {
                return com.getY() + com.getHeight() / 2;
            }
        }

        private int getFirstItemLocation() {
            if (getComponentCount() == 0) {
                return 0;
            }
            return getComponent(0).getHeight() / 2;
        }

        protected class SubmenuLayout implements LayoutManager {

            private float animate;

            public void setAnimate(float animate) {
                this.animate = animate;
                revalidate();
            }

            public float getAnimate() {
                return animate;
            }

            @Override
            public void addLayoutComponent(String name, Component comp) {
            }

            @Override
            public void removeLayoutComponent(Component comp) {
            }

            @Override
            public Dimension preferredLayoutSize(Container parent) {
                synchronized (parent.getTreeLock()) {
                    Insets insets = parent.getInsets();
                    int width = insets.left + insets.right;
                    int height = insets.top + insets.bottom;
                    int count = parent.getComponentCount();
                    int first = -1;
                    for (int i = 0; i < count; i++) {
                        Component com = parent.getComponent(i);
                        if (com.isVisible()) {
                            if (first == -1) {
                                first = com.getPreferredSize().height;
                            }
                            height += com.getPreferredSize().height;
                        }
                    }
                    int space = height - first;
                    height = (int) (first + space * animate);
                    return new Dimension(width, height);
                }
            }

            @Override
            public Dimension minimumLayoutSize(Container parent) {
                synchronized (parent.getTreeLock()) {
                    return new Dimension(0, 0);
                }
            }

            @Override
            public void layoutContainer(Container parent) {
                synchronized (parent.getTreeLock()) {
                    Insets insets = parent.getInsets();
                    int x = insets.left;
                    int y = insets.top;
                    int width = parent.getWidth() - (insets.left + insets.right);
                    int count = parent.getComponentCount();
                    for (int i = 0; i < count; i++) {
                        Component com = parent.getComponent(i);
                        if (com.isVisible()) {
                            int h = com.getPreferredSize().height;
                            com.setBounds(x, y, width, h);
                            y += h;
                        }
                    }
                }
            }
        }
    }
}
