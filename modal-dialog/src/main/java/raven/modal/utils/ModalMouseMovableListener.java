package raven.modal.utils;

import raven.modal.component.AbstractModalController;
import raven.modal.component.ModalContainer;
import raven.modal.option.LayoutOption;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This class for move the modal controller
 *
 * @author Raven
 */
public class ModalMouseMovableListener extends MouseAdapter {

    private final AbstractModalController modalController;
    private final LayoutOption layoutOption;

    private Point initialPressed;

    public ModalMouseMovableListener(AbstractModalController modalController) {
        this.modalController = modalController;
        this.layoutOption = modalController.getOption().getLayoutOption();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Insets margin = layoutOption.getMargin();
        initialPressed = e.getPoint();
        initialPressed.x += margin.left;
        initialPressed.y += margin.top;
        if (layoutOption.isRelativeToOwner()) {
            ModalContainer container = modalController.getModalContainer();
            Point point = SwingUtilities.convertPoint(container.getOwner().getParent(), container.getOwner().getLocation(), container);
            initialPressed.x += point.x;
            initialPressed.y += point.y;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point dragged = e.getPoint();
        Point location = modalController.getLocation();
        int x = location.x + (dragged.x - initialPressed.x);
        int y = location.y + (dragged.y - initialPressed.y);

        layoutOption.setLocation(x, y);

        Component parent = modalController.getParent();
        if (parent != null) {
            parent.doLayout();
        }
    }
}
