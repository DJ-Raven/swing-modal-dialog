package raven.modal.drawer.menu;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import raven.modal.drawer.data.MenuItem;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Raven
 */
public class MenuOption {

    protected List<MenuEvent> events = new ArrayList<>();
    protected MenuValidation menuValidation = new MenuValidation();
    protected MenuStyle menuStyle = new MenuStyle();
    protected MenuOpenMode menuOpenMode = MenuOpenMode.FULL;
    protected MenuItem menus[];
    protected float iconScale[] = {1f};

    protected String baseIconPath;
    protected MenuItemAutoSelectionMode menuItemAutoSelectionMode = MenuItemAutoSelectionMode.SELECT_ALL;
    protected MenuItemLayoutOption menuItemLayoutOption = MenuItemLayoutOption.getDefault(MenuOpenMode.FULL);
    protected MenuItemLayoutOption compactMenuItemLayoutOption = MenuItemLayoutOption.getDefault(MenuOpenMode.COMPACT);

    public MenuOption setMenus(MenuItem menus[]) {
        this.menus = menus;
        return this;
    }

    public MenuOption setIconScale(float... iconScale) {
        this.iconScale = iconScale;
        return this;
    }

    public MenuOption setBaseIconPath(String baseIconPath) {
        this.baseIconPath = baseIconPath;
        return this;
    }

    public MenuOption setMenuItemAutoSelectionMode(MenuItemAutoSelectionMode menuItemAutoSelectionMode) {
        this.menuItemAutoSelectionMode = menuItemAutoSelectionMode;
        return this;
    }

    public MenuOption setMenuItemLayoutOption(MenuItemLayoutOption menuItemLayoutOption) {
        this.menuItemLayoutOption = menuItemLayoutOption;
        return this;
    }

    public MenuOption setMenuValidation(MenuValidation menuValidation) {
        this.menuValidation = menuValidation;
        return this;
    }

    public MenuOption setMenuStyle(MenuStyle menuStyle) {
        this.menuStyle = menuStyle;
        return this;
    }

    public MenuOption setMenuOpenMode(MenuOpenMode menuOpenMode) {
        this.menuOpenMode = menuOpenMode;
        return this;
    }

    public MenuOption addMenuEvent(MenuEvent event) {
        events.add(event);
        return this;
    }

    public Icon buildMenuIcon(String path, float scale) {
        FlatSVGIcon icon = new FlatSVGIcon(path, scale);
        return icon;
    }

    public MenuEvent[] getEvents() {
        return events.toArray(new MenuEvent[events.size()]);
    }

    public MenuValidation getMenuValidation() {
        return menuValidation;
    }

    public MenuStyle getMenuStyle() {
        return menuStyle;
    }

    public MenuOpenMode getMenuOpenMode() {
        return menuOpenMode;
    }

    public MenuItem[] getMenus() {
        return menus;
    }

    public float[] getIconScale() {
        return iconScale;
    }

    public String getBaseIconPath() {
        return baseIconPath;
    }

    public MenuItemAutoSelectionMode getMenuItemAutoSelectionMode() {
        return menuItemAutoSelectionMode;
    }

    public MenuItemLayoutOption getMenuItemLayoutOption() {
        return menuItemLayoutOption;
    }

    public MenuItemLayoutOption getCompactMenuItemLayoutOption() {
        return compactMenuItemLayoutOption;
    }

    public enum MenuItemAutoSelectionMode {
        NONE, SELECT_ALL, SELECT_MAIN_MENU_LEVEL, SELECT_SUB_MENU_LEVEL
    }

    public enum MenuOpenMode {
        FULL, COMPACT;

        public MenuOpenMode toggle() {
            if (this == FULL) {
                return COMPACT;
            } else {
                return FULL;
            }
        }
    }
}
