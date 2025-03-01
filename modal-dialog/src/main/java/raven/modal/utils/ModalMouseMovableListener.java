package raven.modal.utils;

import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.UIScale;
import raven.modal.component.AbstractModalController;
import raven.modal.component.HeavyWeightModalController;
import raven.modal.component.HeavyWeightRelativeLayout;
import raven.modal.layout.OptionLayoutUtils;
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
public abstract class ModalMouseMovableListener extends MouseAdapter {

    private final AbstractModalController modalController;
    private final LayoutOption layoutOption;

    private DynamicSize initialLocation;
    private Point initialPoint;
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

    private Dimension getModalSize() {
        Dimension size = modalController.getSize();
        if (modalController instanceof HeavyWeightModalController) {
            Rectangle rec = HeavyWeightRelativeLayout.getModalBorderSize(((HeavyWeightModalController) modalController).getModalWindow());
            if (rec != null) {
                size.width += rec.width;
                size.height += rec.height;
            }
        }
        return size;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            initialLocation = new DynamicSize(layoutOption.getLocation());
            initialPoint = OptionLayoutUtils.convertToLocation(getParent(), getOwner(), layoutOption, getModalSize(), null);
            initialPressed = e.getLocationOnScreen();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            DynamicSize currentLocation = layoutOption.getLocation();
            boolean doX = currentLocation.getX() != initialLocation.getX() && initialLocation.getX() instanceof Float;
            boolean doY = currentLocation.getY() != initialLocation.getY() && initialLocation.getY() instanceof Float;
            if (doX || doY) {
                Container parent = getParent();
                Component owner = getOwner();
                boolean useOwner = isUseOwner();
                Insets margin = UIScale.scale(layoutOption.getMargin());
                Insets parentMargin = parent.getInsets();
                margin = FlatUIUtils.addInsets(margin, parentMargin);
                Dimension size = useOwner ? owner.getSize() : parent.getSize();
                float width = size.width - (margin.left + margin.right);
                float height = size.height - (margin.top + margin.bottom);

                Number numX = currentLocation.getX();
                Number numY = currentLocation.getY();
                Dimension modalSize = getModalSize();
                if (doX) {
                    float comWidth = modalSize.width / width / 2f;
                    numX = between(UIScale.scale(numX.floatValue()) / width + comWidth, 0, 1f);
                }
                if (doY) {
                    float comHeight = modalSize.height / height / 2f;
                    numY = between(UIScale.scale(numY.floatValue()) / height + comHeight, 0, 1f);
                }
                layoutOption.setLocation(numX, numY);
                updateLayout();
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            Component owner = isUseOwner() ? getOwner() : getParent();
            Point draggedLocation = e.getLocationOnScreen();
            int maxWidth = owner.getWidth() - modalController.getWidth();
            int maxHeight = owner.getHeight() - modalController.getHeight();
            int x = initialPoint.x + (draggedLocation.x - initialPressed.x);
            int y = initialPoint.y + (draggedLocation.y - initialPressed.y);
            if (x <= 0) {
                x = 0;
            } else if (x >= maxWidth) {
                x = maxWidth;
            }
            if (y <= 0) {
                y = 0;
            } else if (y > maxHeight) {
                y = maxHeight;
            }
            layoutOption.setLocation(UIScale.unscale(x), UIScale.unscale(y));
            updateLayout();
        }
    }

    protected abstract Container getParent();

    protected abstract Component getOwner();

    protected abstract void updateLayout();

    private boolean isUseOwner() {
        return layoutOption.isRelativeToOwner() && !(getOwner() instanceof RootPaneContainer);
    }
}
