package raven.modal.layout;

import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.UIScale;
import raven.modal.drawer.menu.AbstractMenuElement;
import raven.modal.drawer.menu.MenuItemLayoutOption;
import raven.modal.drawer.menu.MenuOption;

import javax.swing.*;
import java.awt.*;

/**
 * @author Raven
 */
public class DrawerMenuLayout implements LayoutManager {

    private final AbstractMenuElement parent;
    private final MenuOption menuOption;

    public DrawerMenuLayout(AbstractMenuElement parent, MenuOption menuOption) {
        this.parent = parent;
        this.menuOption = menuOption;
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
            // use parent width to avoid the issues right-to-left scroll pane
            int width = parent.getParent().getWidth();
            int height = insets.top + insets.bottom;
            int count = parent.getComponentCount();
            for (int i = 0; i < count; i++) {
                Component com = parent.getComponent(i);
                if (com.isVisible()) {
                    Insets in = getComponentInsets(com);
                    height += com.getPreferredSize().height + (in.top + in.bottom);
                    width = Math.min(width, com.getPreferredSize().width + (in.left + in.right));
                }
            }
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
            int width = parent.getWidth() - (insets.left + insets.right);
            int x = insets.left;
            int y = insets.top;
            int count = parent.getComponentCount();
            for (int i = 0; i < count; i++) {
                Component com = parent.getComponent(i);
                if (com.isVisible()) {
                    int h = com.getPreferredSize().height;
                    Insets in = getComponentInsets(com);
                    com.setBounds(x + in.left, y + in.top, width - (in.left + in.right), h);
                    y += (h + in.top + in.bottom);
                }
            }
        }
    }

    private Insets getComponentInsets(Component com) {
        boolean full = this.parent.getMenuOpenMode() == MenuOption.MenuOpenMode.FULL;
        MenuItemLayoutOption layoutOption = full ? menuOption.getMenuItemLayoutOption() : menuOption.getCompactMenuItemLayoutOption();

        if (com instanceof JSeparator) {
            Insets separatorInsets = ((JSeparator) com).getInsets();
            return FlatUIUtils.addInsets(separatorInsets, UIScale.scale(layoutOption.getSeparatorMargin()));
        } else if (com instanceof JLabel) {
            return UIScale.scale(layoutOption.getLabelMargin());
        } else {
            Point point = layoutOption.getMenuHorizontalMargin();
            int x = UIScale.scale(point.x);
            int y = UIScale.scale(point.y);

            // menu item not support the top and bottom margin
            return new Insets(0, x, 0, y);
        }
    }
}
