package raven.modal.layout;

import raven.modal.option.LayoutOption;

import java.awt.*;

/**
 * @author Raven
 */
public class ModalLayout implements LayoutManager {

    public void setAnimate(float animate) {
        this.animate = animate;
    }

    private Component component;
    private final LayoutOption layoutOption;
    private float animate;

    public ModalLayout(Component component, LayoutOption layoutOption) {
        this.component = component;
        this.layoutOption = layoutOption;
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
            if (component != null && component.isVisible()) {
                Rectangle rec = OptionLayoutUtils.getLayoutLocation(parent, component, animate, layoutOption);
                component.setBounds(rec.x, rec.y, rec.width, rec.height);
            }
        }
    }
}
