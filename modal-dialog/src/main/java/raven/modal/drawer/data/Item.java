package raven.modal.drawer.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Raven
 */
public class Item implements MenuItem {

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public List<Item> getSubMenu() {
        return subMenu;
    }

    private final String name;
    private String icon;
    private List<Item> subMenu;

    public Item(String name) {
        this.name = name;
    }

    public Item(String name, String icon) {
        this.name = name;
        this.icon = icon;
    }

    public Item subMenu(Item item) {
        addSubMenu(item);
        return this;
    }

    public Item subMenu(String name) {
        addSubMenu(new Item(name));
        return this;
    }

    private void addSubMenu(Item item) {
        if (subMenu == null) {
            subMenu = new ArrayList<>();
        }
        subMenu.add(item);
    }

    public boolean isSubmenuAble() {
        return subMenu != null;
    }

    @Override
    public boolean isMenu() {
        return true;
    }

    public static class Label implements MenuItem {

        public String getName() {
            return name;
        }

        private String name;

        public Label(String name) {
            this.name = name;
        }

        @Override
        public boolean isMenu() {
            return false;
        }
    }
}
