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
    protected MenuStyle menuStyle;
    protected MenuItem menus[];
    protected float iconScale[] = {1f};

    protected String baseIconPath;
    protected boolean menuItemAutoSelect = true;

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

    public MenuOption setMenuItemAutoSelect(boolean menuItemAutoSelect) {
        this.menuItemAutoSelect = menuItemAutoSelect;
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
}
