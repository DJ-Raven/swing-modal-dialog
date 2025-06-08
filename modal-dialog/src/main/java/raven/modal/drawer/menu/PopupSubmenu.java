package raven.modal.drawer.menu;

import com.formdev.flatlaf.ui.FlatPopupMenuBorder;
import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.UIScale;
import raven.modal.drawer.item.Item;
import raven.modal.drawer.renderer.AbstractDrawerLineStyleRenderer;
import raven.modal.utils.FlatLafStyleUtils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.awt.*;

/**
 * @author Raven
 */
public class PopupSubmenu {

    private JPopupMenu popupMenu;
    private final DrawerMenu.SubMenuItem subMenuItem;
    private final DrawerMenu drawerMenu;
    private final Item item;

    public PopupSubmenu(DrawerMenu drawerMenu, DrawerMenu.SubMenuItem subMenuItem, Item item) {
        this.drawerMenu = drawerMenu;
        this.subMenuItem = subMenuItem;
        this.item = item;
    }

    private void init(boolean ltr) {
        if (popupMenu == null) {
            popupMenu = new JPopupMenu();
            applyBorder(popupMenu);
            for (Item i : item.getSubMenu()) {
                if (i.isValidation()) {
                    if (i.isSubmenuAble()) {
                        popupMenu.add(createMenu(i, ltr));
                    } else {
                        popupMenu.add(createMenuItem(i, ltr));
                    }
                }
            }
        }
    }

    private JMenuItem createMenu(Item item, boolean ltr) {
        boolean isMainItem = true;
        Menu menu = new Menu(item, isMainItem);
        applyMenuEvent(menu, item, isMainItem);
        defaultStyled(menu, item.getIndex(), isMainItem, ltr);
        for (Item i : item.getSubMenu()) {
            if (i.isValidation()) {
                if (i.isSubmenuAble()) {
                    menu.add(createMenu(i, ltr));
                } else {
                    menu.add(createMenuItem(i, ltr));
                }
            }
        }
        return menu;
    }

    private JMenuItem createMenuItem(Item item, boolean ltr) {
        boolean isMainItem = false;
        MenuItem menuItem = new MenuItem(item, isMainItem);
        applyMenuEvent(menuItem, item, isMainItem);
        defaultStyled(menuItem, item.getIndex(), isMainItem, ltr);
        return menuItem;
    }

    private void defaultStyled(JMenuItem menuItem, int[] index, boolean isMainItem, boolean ltr) {
        if (drawerMenu.getMenuOption().menuStyle != null) {
            drawerMenu.getMenuOption().menuStyle.styleCompactMenuItem(menuItem, index, isMainItem);
        }
        String insets = ltr ? "0,25,0,5" : "0,5,0,25";
        Insets margin = new Insets(5, 10, 5, 10);
        FlatLafStyleUtils.appendStyleIfAbsent(menuItem, "" +
                "selectionInsets:" + insets + ";" +
                "selectionArc:10;" +
                "minimumWidth:170;");
        FlatLafStyleUtils.appendStyle(menuItem, "" +
                "margin:" + FlatLafStyleUtils.appendMargin(menuItem, margin));
    }

    private void applyBorder(JPopupMenu popupMenu) {
        if (!(popupMenu.getBorder() instanceof CompoundBorder)) {
            if (popupMenu.getBorder() instanceof FlatPopupMenuBorder) {
                FlatPopupMenuBorder flatPopupMenuBorder = (FlatPopupMenuBorder) popupMenu.getBorder();
                flatPopupMenuBorder.applyStyleProperty(new Insets(6, 1, 6, 1));
            }
            popupMenu.setBorder(new CompoundBorder(new PopupBorder(popupMenu), popupMenu.getBorder()));
        }
    }

    public void show(Component com) {
        boolean ltr = subMenuItem.getComponentOrientation().isLeftToRight();
        init(ltr);
        if (popupMenu.getComponentOrientation().isLeftToRight() != ltr) {
            popupMenu.applyComponentOrientation(subMenuItem.getComponentOrientation());
        }

        int x;
        int y = com.getHeight() + UIScale.scale(5);
        if (ltr) {
            x = com.getWidth();
        } else {
            x = -popupMenu.getPreferredSize().width;
        }
        popupMenu.show(com, x, y);
    }

    protected void applyMenuEvent(JMenuItem menuItem, Item item, boolean isMainItem) {
        menuItem.addActionListener(e -> {
            MenuAction action = drawerMenu.runEvent(item, item.getIndex());
            if (action != null) {
                if (!action.getConsume()) {
                    if (drawerMenu.isMenuAutoSelection(isMainItem)) {
                        drawerMenu.setMenuSelectedIndex(item.getIndex());
                    }
                }
            }
        });
    }

    private class PopupBorder implements Border {

        private final JPopupMenu popupMenu;

        public PopupBorder(JPopupMenu popupMenu) {
            this.popupMenu = popupMenu;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int lx, int ly, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            FlatUIUtils.setRenderingHints(g2);
            if (drawerMenu.getMenuOption() != null && drawerMenu.getMenuOption().menuStyle != null) {
                AbstractDrawerLineStyleRenderer lineStyleRenderer = drawerMenu.getMenuOption().menuStyle.getDrawerLineStyleRenderer();
                if (lineStyleRenderer != null) {
                    boolean ltr = popupMenu.getComponentOrientation().isLeftToRight();
                    int menuHeight = UIScale.scale(6);
                    int last = getLastLocation();
                    int gap = UIScale.scale(12);
                    int x = ltr ? gap : width - gap;
                    int count = popupMenu.getComponentCount();
                    int[] subMenuLocation = new int[count];
                    int selectedIndex = -1;
                    for (int i = 0; i < count; i++) {
                        Component com = popupMenu.getComponent(i);
                        int y;
                        if (com instanceof MainItem) {
                            if (((MainItem) com).isItemSelected()) {
                                selectedIndex = i;
                            }
                        }
                        y = com.getY() + (com.getHeight() / 2);
                        subMenuLocation[i] = y;
                    }
                    lineStyleRenderer.draw(g2, popupMenu, x, menuHeight, x, last, subMenuLocation, selectedIndex, ltr, null);
                }
            }
            g2.dispose();
        }

        private int getLastLocation() {
            Component com = popupMenu.getComponent(popupMenu.getComponentCount() - 1);
            return com.getY() + com.getHeight() / 2;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(0, 0, 0, 0);
        }

        @Override
        public boolean isBorderOpaque() {
            return true;
        }
    }

    private interface MainItem {

        boolean isItemSelected();
    }

    private class MenuItem extends JMenuItem implements MainItem {

        public Item getItem() {
            return item;
        }

        public boolean isMainItem() {
            return isMainItem;
        }

        private final Item item;
        private final boolean isMainItem;

        public MenuItem(Item item, boolean isMainItem) {
            super(item.getName());
            this.item = item;
            this.isMainItem = isMainItem;
        }

        @Override
        public boolean isItemSelected() {
            return drawerMenu.isMenuSelected(item.getIndex());
        }
    }

    protected class Menu extends JMenu implements MainItem {

        public Item getItem() {
            return item;
        }

        public boolean isMainItem() {
            return isMainItem;
        }

        private final Item item;
        private final boolean isMainItem;

        public Menu(Item item, boolean isMainItem) {
            super(item.getName());
            this.item = item;
            this.isMainItem = isMainItem;
        }

        @Override
        public JPopupMenu getPopupMenu() {
            JPopupMenu jPopupMenu = super.getPopupMenu();
            applyBorder(jPopupMenu);
            return jPopupMenu;
        }

        @Override
        public boolean isItemSelected() {
            return drawerMenu.isMenuSelected(item.getIndex());
        }
    }
}
