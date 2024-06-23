package raven.modal.layout;

import java.awt.*;

/**
 * @author Raven
 */
public class FrameToastLayout implements LayoutManager {

    public Component getComponent() {
        return component;
    }

    private final Component component;

    public FrameToastLayout(Component component) {
        this.component = component;
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
            int height = parent.getHeight() - (insets.bottom + insets.top);
            component.setBounds(x, y, width, height);
        }
    }
}
