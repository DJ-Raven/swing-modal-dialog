package raven.modal.layout;

import raven.modal.slider.PanelSlider;

import java.awt.*;

/**
 * @author Raven
 */
public class AnimatedLayout extends CardLayout {

    public float getAnimate() {
        return animate;
    }

    public void setAnimate(float animate) {
        this.animate = animate;
    }

    public void reset() {
        animate = 0;
        from = null;
        target = null;
    }

    public void addAnimateSize(Dimension from, Dimension target) {
        animate = 0;
        if (from.width != target.width || from.height != target.height) {
            this.from = from;
            this.target = target;
        }
    }

    private final PanelSlider.PaneSliderLayoutSize paneSliderLayoutSize;
    private Dimension from;
    private Dimension target;
    private float animate;

    public AnimatedLayout(PanelSlider.PaneSliderLayoutSize paneSliderLayoutSize) {
        this.paneSliderLayoutSize = paneSliderLayoutSize;
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
            Dimension size = super.preferredLayoutSize(parent);
            if (from == null || target == null) {
                return size;
            }
            return getPreferredLayoutSize(parent);
        }
    }

    private Dimension getPreferredLayoutSize(Container parent) {
        Insets insets = parent.getInsets();
        int width = from.width + (insets.left + insets.right);
        int height = from.height + (insets.top + insets.bottom);
        int diffWidth = target.width - from.width;
        int diffHeight = target.height - from.height;
        height += (diffHeight * animate);
        width += (diffWidth * animate);
        return new Dimension(width, height);
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
            return new Dimension();
        }
    }

    @Override
    public void layoutContainer(Container parent) {
        if (from == null || target == null) {
            super.layoutContainer(parent);
        } else {
            // custom code from super `layoutContainer(Container parent)` method
            synchronized (parent.getTreeLock()) {
                Insets insets = parent.getInsets();
                int count = parent.getComponentCount();
                int width = parent.getWidth() - (insets.left + insets.right);
                int height = parent.getHeight() - (insets.top + insets.bottom);
                Component comp;
                boolean currentFound = false;
                for (int i = 0; i < count; i++) {
                    comp = parent.getComponent(i);
                    Dimension size = getComponentSize(parent, comp, width, height);
                    int w = size.width;
                    int h = size.height;
                    comp.setBounds(insets.left, insets.top, w, h);
                    if (comp.isVisible()) {
                        currentFound = true;
                    }
                }
                if (!currentFound && count > 0) {
                    parent.getComponent(0).setVisible(true);
                }
            }
        }
    }

    private Dimension getComponentSize(Container parent, Component com, int width, int height) {
        if (com instanceof PanelSlider.PanelSnapshot) {
            return new Dimension(width, height);
        }
        return paneSliderLayoutSize.getComponentSize(parent, com);
    }

    public boolean isUpdateAble() {
        return from != null && target != null;
    }
}
