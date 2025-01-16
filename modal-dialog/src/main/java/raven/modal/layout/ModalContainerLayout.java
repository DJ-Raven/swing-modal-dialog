package raven.modal.layout;

import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.UIScale;
import raven.modal.component.ModalContainer;

import java.awt.*;

/**
 * @author Raven
 */
public class ModalContainerLayout implements LayoutManager {

    public ModalContainerLayout() {
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
                if (com instanceof ModalContainer) {
                    ModalContainer container = (ModalContainer) com;
                    Rectangle bound = new Rectangle(x, y, width, height);
                    Insets backgroundPadding = container.getOption().getLayoutOption().getBackgroundPadding();
                    if (!FlatUIUtils.isInsetsEmpty(backgroundPadding)) {
                        bound = FlatUIUtils.subtractInsets(bound, UIScale.scale(backgroundPadding));
                    }
                    container.setBounds(bound);
                }
            }
        }
    }
}
