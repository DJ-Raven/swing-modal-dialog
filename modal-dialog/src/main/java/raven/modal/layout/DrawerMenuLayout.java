package raven.modal.layout;

import javax.swing.*;
import java.awt.*;

/**
 * @author Raven
 */
public class DrawerMenuLayout implements LayoutManager {

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
                    height += com.getPreferredSize().height;
                    width = Math.max(width, com.getPreferredSize().width);
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
                    if (com instanceof JSeparator) {
                        Insets in = ((JSeparator) com).getInsets();
                        h += in.top + in.bottom;
                        com.setBounds(x + in.left, y, width - (in.left + in.right), h);
                    } else {
                        com.setBounds(x, y, width, h);
                    }
                    y += h;
                }
            }
        }
    }
}
