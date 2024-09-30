package raven.modal.drawer.menu;

import raven.modal.drawer.data.Item;
import raven.modal.utils.FlatLafStyleUtils;

import javax.swing.*;
import java.awt.*;

/**
 * @author Raven
 */
public class PopupSubmenu {

    private JPopupMenu popupMenu;
    private final DrawerMenu drawerMenu;
    private final Item item;

    public PopupSubmenu(DrawerMenu drawerMenu, Item item) {
        this.drawerMenu = drawerMenu;
        this.item = item;
    }

    private void init() {
        if (popupMenu == null) {
            popupMenu = new JPopupMenu();
            defaultStyledPopup(popupMenu);
            for (Item i : item.getSubMenu()) {
                if (i.isValidation()) {
                    if (i.isSubmenuAble()) {
                        popupMenu.add(createMenu(i));
                    } else {
                        popupMenu.add(createMenuItem(i));
                    }
                }
            }
        }
    }

    private JMenuItem createMenu(Item item) {
        boolean isMainItem = true;
        Menu menu = new Menu(item, isMainItem);
        applyMenuEvent(menu, item, isMainItem);
        defaultStyled(menu);
        defaultStyledPopup(menu.getPopupMenu());
        for (Item i : item.getSubMenu()) {
            if (i.isValidation()) {
                if (i.isSubmenuAble()) {
                    menu.add(createMenu(i));
                } else {
                    menu.add(createMenuItem(i));
                }
            }
        }
        return menu;
    }

    private JMenuItem createMenuItem(Item item) {
        boolean isMainItem = false;
        MenuItem menuItem = new MenuItem(item, isMainItem);
        applyMenuEvent(menuItem, item, isMainItem);
        defaultStyled(menuItem);
        return menuItem;
    }

    private void defaultStyled(JMenuItem menuItem) {
        FlatLafStyleUtils.appendStyleIfAbsent(menuItem, "" +
                "selectionInsets:0,5,0,5;" +
                "margin:5,5,5,10;" +
                "selectionArc:10;" +
                "minimumWidth:150;");
    }

    public void defaultStyledPopup(JPopupMenu popupMenu) {
        FlatLafStyleUtils.appendStyleIfAbsent(popupMenu, "" +
                "borderInsets:5,0,5,0;");
    }

    public void show(Component com) {
        init();
        popupMenu.show(com, com.getWidth(), 0);
    }

    protected void applyMenuEvent(JMenuItem menuItem, Item item, boolean isMainItem) {
        menuItem.addActionListener(e -> {
            MenuAction action = drawerMenu.runEvent(item, item.getIndex());
            if (action != null) {
                if (action.getConsume() == false) {
                    if (drawerMenu.isMenuAutoSelection(isMainItem)) {
                        drawerMenu.setMenuSelectedIndex(item.getIndex());
                    }
                }
            }
        });
    }

    private class MenuItem extends JMenuItem {

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
    }

    protected class Menu extends JMenu {
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
    }
}
