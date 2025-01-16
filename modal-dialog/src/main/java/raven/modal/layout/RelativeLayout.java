package raven.modal.layout;

import raven.modal.component.RelativeLayerPane;

import javax.swing.*;
import java.awt.*;

/**
 * @author Raven
 */
public class RelativeLayout implements LayoutManager {

    public RelativeLayout() {
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
            return new Dimension(0, 0);
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
            int x = insets.left;
            int y = insets.top;
            int width = parent.getWidth() - (insets.left + insets.right);
            int height = parent.getHeight() - (insets.top + insets.bottom);
            int count = parent.getComponentCount();
            for (int i = 0; i < count; i++) {
                Component com = parent.getComponent(i);
                if (com instanceof RelativeLayerPane) {
                    RelativeLayerPane layer = (RelativeLayerPane) com;
                    Rectangle bound;
                    if (layer.isFixedLayout()) {
                        bound = new Rectangle(x, y, width, height);
                    } else {
                        bound = getOwnerBounds(parent, layer);
                    }
                    layer.setBounds(bound);
                }
            }
        }
    }

    private Rectangle getOwnerBounds(Container parent, RelativeLayerPane container) {
        Component owner = container.getOwner();
        Point ownerLocation = SwingUtilities.convertPoint(owner.getParent(), owner.getLocation(), parent);
        return new Rectangle(ownerLocation, owner.getSize());
    }
}
