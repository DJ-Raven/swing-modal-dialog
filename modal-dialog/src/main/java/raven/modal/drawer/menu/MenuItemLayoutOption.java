package raven.modal.drawer.menu;

import java.awt.*;

/**
 * @author Raven
 */
public class MenuItemLayoutOption {

    public static MenuItemLayoutOption getDefault(MenuOption.MenuOpenMode menuOpenMode) {
        if (menuOpenMode == MenuOption.MenuOpenMode.FULL) {
            return new MenuItemLayoutOption()
                    .setMenuHorizontalMargin(new Point(10, 10))
                    .setLabelMargin(new Insets(8, 20, 8, 20))
                    .setSeparatorMargin(new Insets(0, 10, 0, 10));
        } else {
            return new MenuItemLayoutOption()
                    .setMenuHorizontalMargin(new Point(20, 20))
                    .setLabelMargin(new Insets(8, 5, 8, 5))
                    .setSeparatorMargin(new Insets(0, 20, 0, 20));
        }
    }

    public Point getMenuHorizontalMargin() {
        return menuHorizontalMargin;
    }

    public Insets getLabelMargin() {
        return labelMargin;
    }

    public Insets getSeparatorMargin() {
        return separatorMargin;
    }

    protected Point menuHorizontalMargin = new Point(0, 0);
    protected Insets labelMargin = new Insets(0, 0, 0, 0);
    protected Insets separatorMargin = new Insets(0, 0, 0, 0);

    public MenuItemLayoutOption setMenuHorizontalMargin(Point margin) {
        this.menuHorizontalMargin = margin;
        return this;
    }

    public MenuItemLayoutOption setLabelMargin(Insets margin) {
        this.labelMargin = margin;
        return this;
    }

    public MenuItemLayoutOption setSeparatorMargin(Insets margin) {
        this.separatorMargin = margin;
        return this;
    }
}
