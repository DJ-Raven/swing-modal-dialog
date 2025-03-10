package raven.modal.component;

import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.SystemInfo;
import raven.modal.option.Option;
import raven.modal.utils.ModalUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

/**
 * @author Raven
 */
public class ModalBackground extends JComponent {

    private final HeavyWeightModalController modalController;
    private Window parentWindow;
    private MouseListener mouseListener;

    public ModalBackground(HeavyWeightModalController modalController, Container parent) {
        this.modalController = modalController;
        if (parent instanceof Window) {
            parentWindow = (Window) parent;
        }
        install();
    }

    private void install() {
        Option.BackgroundClickType clickType = modalController.getOption().getBackgroundClickType();
        mouseListener = new MouseAdapter() {

            private boolean hover;

            @Override
            public void mouseEntered(MouseEvent e) {
                hover = true;
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hover = false;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (hover && clickType == Option.BackgroundClickType.CLOSE_MODAL) {
                    modalController.closeModal();
                }
            }
        };
        addMouseListener(mouseListener);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Option option = modalController.getOption();
        float opacity = Math.max(option.getOpacity(), 0.01f);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(AlphaComposite.SrcOver.derive(opacity));
        g2.setColor(getBackgroundColor());
        Shape shape = getBackgroundShape();
        if (shape == null) {
            g2.fill(new Rectangle2D.Double(0, 0, getWidth(), getHeight()));
        } else {
            // fill background with round border
            FlatUIUtils.setRenderingHints(g2);
            Point point = getLocationOnScreen();
            g2.translate(-point.x, -point.y);
            g2.fill(shape);
        }
        g2.dispose();
        super.paintComponent(g);
    }

    protected Color getBackgroundColor() {
        return ModalUtils.getBackgroundColor(modalController.getOption().getBackgroundDark(),
                modalController.getOption().getBackgroundLight(),
                getBackground());
    }

    protected Shape getBackgroundShape() {
        int arc = getWindowRoundBorder();
        if (arc > 0 && parentWindow != null) {
            Rectangle2D windowsRec = FlatUIUtils.subtractInsets(parentWindow.getBounds(), parentWindow.getInsets());
            Shape roundRec = new RoundRectangle2D.Double(windowsRec.getX(), windowsRec.getY(), windowsRec.getWidth(), windowsRec.getHeight(), arc, arc);
            Rectangle2D componentShape = new Rectangle(getLocationOnScreen(), new Dimension(getWidth(), getHeight()));
            Area area = new Area(roundRec);
            area.intersects(componentShape);
            return area;
        }
        return null;
    }

    private int getWindowRoundBorder() {
        if (SystemInfo.isWindows_11_orLater && !isWindowMaximized()) {
            return 8;
        }
        return 0;
    }

    protected boolean isWindowMaximized() {
        return parentWindow instanceof Frame && (((Frame) parentWindow).getExtendedState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH;
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        removeMouseListener(mouseListener);
        mouseListener = null;
    }
}
