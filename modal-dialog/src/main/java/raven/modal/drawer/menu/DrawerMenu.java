package raven.modal.drawer.menu;

import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.UIScale;
import raven.modal.drawer.data.Item;
import raven.modal.drawer.data.MenuItem;
import raven.modal.drawer.renderer.AbstractDrawerLineStyleRenderer;
import raven.modal.layout.DrawerMenuLayout;
import raven.modal.utils.FlatLafStyleUtils;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

/**
 * @author Raven
 */
public class DrawerMenu extends JPanel {

    private final MenuOption menuOption;

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
    }

    private void init() {
        setLayout(new DrawerMenuLayout());
        if (menuOption.menuStyle != null) {
            menuOption.menuStyle.styleMenu(this);
        }
        buildMenu();
    }

    private void buildMenu() {
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
                            add(createSubmenuItem(item, arrIndex, arrValidationIndex, 0));
                        }
                        if (validation || menuOption.menuValidation.keepMenuValidationIndex) {
                            index++;
                        }
                    } else {
                        // create single menu item
                        int[] arrValidationIndex = {++validationIndex};
                        boolean validation = menuOption.menuValidation.menuValidation(arrValidationIndex);
                        if (validation) {
                            JButton button = createMenuItem(item.getName(), item.getIcon(), arrIndex, 0);
                            applyMenuEvent(button, arrIndex);
                            add(button);
                        }
                        if (validation || menuOption.menuValidation.keepMenuValidationIndex) {
                            index++;
                        }
                    }
                } else {
                    // create label
                    if (checkLabelValidation(i, index)) {
                        Item.Label label = (Item.Label) menuItem;
                        add(createLabel(label.getName()));
                    }
                }
            }
        }
    }

    private int[] copyArray(int[] arr) {
        return Arrays.copyOf(arr, arr.length);
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
            Icon iconObject = menuOption.buildMenuIcon(path + icon, iconScale);
            return iconObject;
        } else {
            return null;
        }
    }

    protected JButton createMenuItem(String name, String icon, int[] index, int menuLevel) {
        JButton button = new JButton(name);
        Icon iconObject = getIcon(icon, menuLevel);
        if (iconObject != null) {
            button.setIcon(iconObject);
        }
        button.setHorizontalAlignment(JButton.LEADING);
        if (menuOption.menuStyle != null) {
            menuOption.menuStyle.styleMenuItem(button, copyArray(index));
        }
        FlatLafStyleUtils.appendStyleIfAbsent(button, "" +
                "arc:0;" +
                "margin:6,20,6,20;" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0;" +
                "background:null;" +
                "iconTextGap:5;");
        return button;
    }

    protected void applyMenuEvent(JButton button, int[] index) {
        button.addActionListener(e -> {
            MenuAction action = runEvent(index);
            if (action != null) {
                // next update on action menu selected
            }
        });
    }

    private MenuAction runEvent(int[] index) {
        if (!menuOption.events.isEmpty()) {
            MenuAction action = new MenuAction();
            if (menuOption.menuItemAutoSelect) {
                action.selected();
            }
            for (MenuEvent event : menuOption.events) {
                event.selected(action, copyArray(index));
            }
            return action;
        }
        return null;
    }

    protected Component createSubmenuItem(Item menu, int index[], int[] validationIndex, int menuLevel) {
        JPanel panelItem = new SubMenuItem(menu, index, validationIndex, menuLevel);
        return panelItem;
    }

    protected boolean checkLabelValidation(int labelIndex, int menuIndex) {
        if (menuOption.menuValidation.labelValidation(labelIndex)) {
            if (menuOption.menuValidation.removeLabelWhenEmptyMenu) {
                boolean fondMenu = false;
                for (int i = labelIndex + 1; i < menuOption.menus.length; i++) {
                    MenuItem menuItem = menuOption.menus[i];
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
                "border:8,10,8,10;" +
                "foreground:$Label.disabledForeground;");
        return label;
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

    protected class SubMenuItem extends JPanel {

        private int menuLevel;
        private int levelSpace = 18;
        private SubmenuLayout menuLayout;
        private boolean menuShow;
        private final Item menu;
        private final int[] index;
        private final int[] validationIndex;
        private int iconWidth;

        public void setAnimate(float animate) {
            menuLayout.setAnimate(animate);
        }

        public SubMenuItem(Item menu, int[] index, int[] validationIndex, int menuLevel) {
            this.menu = menu;
            this.index = index;
            this.validationIndex = validationIndex;
            this.menuLevel = menuLevel;
            init();
        }

        private void init() {
            menuLayout = new SubmenuLayout();
            setLayout(menuLayout);
            // use opaque true on the first submenu panel to fix g2d draw arrow line
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
            JButton mainButton;
            if (menuLevel == 0) {
                // create first level menu item
                mainButton = createMenuItem(menu.getName(), menu.getIcon(), this.index, menuLevel);
            } else {
                int addSpace = menuLevel > 1 ? (menuLevel - 1) * levelSpace : 0;
                mainButton = createSubMenuItem(menu.getName(), this.index, iconWidth + addSpace);
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
                if (validation) {
                    Item item = menu.getSubMenu().get(i);
                    if (item.isSubmenuAble()) {
                        add(createSubmenuItem(item, arrIndex, arrValidationIndex, nextMenuLevel));
                    } else {
                        // create single menu item
                        int addSpace = menuLevel * levelSpace;
                        JButton button = createSubMenuItem(item.getName(), arrIndex, iconWidth + addSpace);
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
                menuShow = !menuShow;
                new MenuAnimation(this).run(menuShow);
            });
        }


        protected JButton createSubMenuItem(String name, int[] index, int gap) {
            JButton button = new JButton(name);
            button.setHorizontalAlignment(JButton.LEADING);
            if (menuOption.menuStyle != null) {
                menuOption.menuStyle.styleMenuItem(button, copyArray(index));
            }
            boolean isRightToLeft = !DrawerMenu.this.getComponentOrientation().isLeftToRight();
            String margin = isRightToLeft ? ("7,30,7," + (gap + 25)) : ("7," + (gap + 25) + ",7,30");
            FlatLafStyleUtils.appendStyleIfAbsent(button, "" +
                    "arc:0;" +
                    "margin:" + margin + ";" +
                    "borderWidth:0;" +
                    "focusWidth:0;" +
                    "innerFocusWidth:0;" +
                    "background:null");
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
                        int gap = UIScale.scale((20 + (iconWidth / 2)) + (levelSpace * menuLevel));
                        int x = ltr ? gap : width - gap;
                        int count = getComponentCount();
                        int subMenuLocation[] = new int[count - 1];
                        for (int i = 1; i < count; i++) {
                            Component com = getComponent(i);
                            int y;
                            if (com instanceof SubMenuItem) {
                                y = com.getY() + ((SubMenuItem) com).getFirstItemLocation();
                            } else {
                                y = com.getY() + (com.getHeight() / 2);
                            }
                            subMenuLocation[i - 1] = y;
                        }
                        lineStyleRenderer.draw(g2, this, x, menuHeight, x, last, subMenuLocation, ltr);
                        // create arrow
                        lineStyleRenderer.drawArrow(g2, this, width, menuHeight, menuLayout.getAnimate(), ltr);
                        g2.dispose();
                    }
                }
            }
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
