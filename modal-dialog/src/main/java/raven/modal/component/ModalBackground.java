package raven.modal.component;

import raven.modal.option.Option;
import raven.modal.utils.ModalUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;

/**
 * @author Raven
 */
public class ModalBackground extends JComponent {

    private final HeavyWeightModalController modalController;
    private MouseListener mouseListener;

    public ModalBackground(HeavyWeightModalController modalController) {
        this.modalController = modalController;
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
        g2.fill(new Rectangle2D.Double(0, 0, getWidth(), getHeight()));
        g2.dispose();
        super.paintComponent(g);
    }

    protected Color getBackgroundColor() {
        return ModalUtils.getBackgroundColor(modalController.getOption().getBackgroundDark(),
                modalController.getOption().getBackgroundLight(),
                getBackground());
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        removeMouseListener(mouseListener);
        mouseListener = null;
    }
}
