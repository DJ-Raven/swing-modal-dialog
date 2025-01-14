package raven.modal.layout;

import raven.modal.component.ModalContainer;
import raven.modal.option.LayoutOption;

import javax.swing.*;
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
                    if (isUseOwnerBounds(container)) {
                        container.setBounds(getOwnerBounds(parent, container));
                    } else {
                        container.setBounds(x, y, width, height);
                    }
                }
            }
        }
    }

    private boolean isUseOwnerBounds(ModalContainer container) {
        LayoutOption option = container.getController().getOption().getLayoutOption();
        Component owner = container.getOwner();
        return option.isRelativeToOwner()
                && option.getRelativeToOwnerType() == LayoutOption.RelativeToOwnerType.RELATIVE_CONTAINED
                && !(owner instanceof RootPaneContainer);
    }

    private Rectangle getOwnerBounds(Container parent, ModalContainer container) {
        Component owner = container.getOwner();
        Point ownerLocation = SwingUtilities.convertPoint(owner.getParent(), owner.getLocation(), parent);
        return new Rectangle(ownerLocation, owner.getSize());
    }
}
