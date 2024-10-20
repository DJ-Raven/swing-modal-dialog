package raven.modal.drawer.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Raven
 */
public class Item implements MenuItem {

    public boolean isValidation() {
        return validation;
    }

    public int[] getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public List<Item> getSubMenu() {
        return subMenu;
    }

    public Class<?> getItemClass() {
        return itemClass;
    }

    private final String name;
    private String icon;
    private List<Item> subMenu;
    private Class<?> itemClass;
    private int[] index;
    private boolean validation;

    public Item(String name) {
        this.name = name;
    }

    public Item(String name, Class<?> itemClass) {
        this.name = name;
        this.itemClass = itemClass;
    }

    public Item(String name, String icon) {
        this.name = name;
        this.icon = icon;
    }

    public Item(String name, String icon, Class<?> itemClass) {
        this.name = name;
        this.icon = icon;
        this.itemClass = itemClass;
    }

    public Item subMenu(Item item) {
        addSubMenu(item);
        return this;
    }

    public Item subMenu(String name) {
        addSubMenu(new Item(name));
        return this;
    }

    public Item subMenu(String name, Class<?> itemClass) {
        addSubMenu(new Item(name, itemClass));
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

    public void initIndexOnNull(int[] index, boolean validation) {
        if (this.index == null) {
            this.index = index;
            this.validation = validation;
        }
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

    public static class Separator implements MenuItem {

        @Override
        public boolean isMenu() {
            return false;
        }
    }
}
