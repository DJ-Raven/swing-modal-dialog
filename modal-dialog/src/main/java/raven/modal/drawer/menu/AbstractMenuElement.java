package raven.modal.drawer.menu;

import javax.swing.*;

/**
 * This class for menu element it's provide method to update layout between FULL and COMPACT
 *
 * @author Raven
 */
public abstract class AbstractMenuElement extends JPanel {

    public MenuOption.MenuOpenMode getMenuOpenMode() {
        return menuOpenMode;
    }

    protected MenuOption.MenuOpenMode menuOpenMode = MenuOption.MenuOpenMode.FULL;

    public void setMenuOpenMode(MenuOption.MenuOpenMode menuOpenMode) {
        if (this.menuOpenMode != menuOpenMode) {
            this.menuOpenMode = menuOpenMode;
            layoutOptionChanged(menuOpenMode);
        }
    }

    protected abstract void layoutOptionChanged(MenuOption.MenuOpenMode menuOpenMode);
}
