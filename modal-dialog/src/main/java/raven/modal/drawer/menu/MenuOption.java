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
    protected MenuItem menus[];
    protected float iconScale[] = {1f};

    protected String baseIconPath;
    protected MenuItemAutoSelectionMode menuItemAutoSelectionMode = MenuItemAutoSelectionMode.SELECT_ALL;

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

    public MenuOption setMenuValidation(MenuValidation menuValidation) {
        this.menuValidation = menuValidation;
        return this;
    }

    public MenuOption setMenuStyle(MenuStyle menuStyle) {
        this.menuStyle = menuStyle;
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

    public MenuStyle getMenuStyle() {
        return menuStyle;
    }

    public enum MenuItemAutoSelectionMode {
        NONE, SELECT_ALL, SELECT_MAIN_MENU_LEVEL, SELECT_SUB_MENU_LEVEL
    }
}
