package raven.modal.layout;

import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.UIScale;
import raven.modal.component.ModalController;
import raven.modal.option.LayoutOption;

import javax.swing.*;
import java.awt.*;

/**
 * @author Raven
 */
public class ModalLayout implements LayoutManager {

    public void setAnimate(float animate) {
        this.animate = animate;
    }

    public void setWindow(JWindow window) {
        this.window = window;
    }

    private JWindow window;
    private final ModalController component;
    private final LayoutOption layoutOption;
    private float animate;

    public ModalLayout(ModalController component, LayoutOption layoutOption) {
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
                Rectangle rec = OptionLayoutUtils.getLayoutLocation(parent, component.getModalContainer().getOwner(), component, animate, layoutOption);
                if (window != null) {
                    Point point = window.getParent().getLocation();
                    Point p = SwingUtilities.convertPoint(parent.getParent(), point, null);
                    int x = p.x + rec.x;
                    int y = p.y + rec.y;
                    window.setBounds(x, y, rec.width, rec.height);
                } else {
                    component.setBounds(rec.x, rec.y, rec.width, rec.height);
                }
            }
        }
    }

    public Dimension getComponentSize(Container parent, Component component, Insets margin) {
        Insets insets = FlatUIUtils.addInsets(parent.getInsets(), UIScale.scale(layoutOption.getMargin()));
        int width = parent.getWidth() - (insets.left + insets.right + margin.left + margin.right);
        int height = parent.getHeight() - (insets.top + insets.bottom + margin.top + margin.bottom);
        return OptionLayoutUtils.getComponentSize(component, width, height, 1f, layoutOption, null, false);
    }
}
