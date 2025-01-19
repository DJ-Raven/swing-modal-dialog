package raven.modal.utils;

import com.formdev.flatlaf.util.UIScale;
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

    private DynamicSize initialLocation;

    private Point initialPressed;

    public ModalMouseMovableListener(AbstractModalController modalController) {
        this.modalController = modalController;
        this.layoutOption = modalController.getOption().getLayoutOption();
    }

    private float between(float value, float start, float end) {
        if (value < start) {
            value = start;
        } else if (value > end) {
            value = end;
        }
        return value;
    }

    private boolean isUseOwner() {
        return layoutOption.isRelativeToOwner() && !(modalController.getModalContainer().getOwner() instanceof RootPaneContainer);
    }

    private Point getCurrentPoint(Point point) {
        Insets margin = UIScale.scale(layoutOption.getMargin());
        point.x += margin.left;
        point.y += margin.top;
        if (isUseOwner()) {
            ModalContainer container = modalController.getModalContainer();
            Point convertPoint = SwingUtilities.convertPoint(container.getOwner().getParent(), container.getOwner().getLocation(), container);
            point.x += convertPoint.x;
            point.y += convertPoint.y;
        }
        return point;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            initialLocation = new DynamicSize(layoutOption.getLocation());
            initialPressed = getCurrentPoint(e.getPoint());
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            DynamicSize currentLocation = layoutOption.getLocation();
            boolean doX = currentLocation.getX() != initialLocation.getX() && initialLocation.getX() instanceof Float;
            boolean doY = currentLocation.getY() != initialLocation.getY() && initialLocation.getY() instanceof Float;
            if (doX || doY) {
                Insets margin = UIScale.scale(layoutOption.getMargin());
                Dimension size = isUseOwner() ? modalController.getModalContainer().getOwner().getSize() : modalController.getModalContainer().getSize();
                float width = size.width - (margin.left + margin.right);
                float height = size.height - (margin.top + margin.bottom);

                Number numX = currentLocation.getX();
                Number numY = currentLocation.getY();
                if (doX) {
                    float comWidth = modalController.getWidth() / width / 2f;
                    numX = between(UIScale.scale(numX.floatValue()) / width + comWidth, 0, 1f);
                }
                if (doY) {
                    float comHeight = modalController.getHeight() / height / 2f;
                    numY = between(UIScale.scale(numY.floatValue()) / height + comHeight, 0, 1f);
                }
                layoutOption.setLocation(numX, numY);
                Component parent = modalController.getParent();
                if (parent != null) {
                    parent.doLayout();
                }
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            Point dragged = e.getPoint();
            Point location = modalController.getLocation();
            int x = UIScale.unscale(location.x + (dragged.x - initialPressed.x));
            int y = UIScale.unscale(location.y + (dragged.y - initialPressed.y));

            layoutOption.setLocation(x, y);

            Component parent = modalController.getParent();
            if (parent != null) {
                parent.doLayout();
            }
        }
    }
}
