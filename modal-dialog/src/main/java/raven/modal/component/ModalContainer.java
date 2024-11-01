package raven.modal.component;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.util.ColorFunctions;
import com.formdev.flatlaf.util.ScaledEmptyBorder;
import raven.modal.layout.ModalLayout;
import raven.modal.option.Option;
import raven.modal.utils.BlurImageUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;

/**
 * @author Raven
 */
public class ModalContainer extends JComponent {

    public String getId() {
        return id;
    }

    public ModalLayout getModalLayout() {
        return modalLayout;
    }

    private final String id;
    private ModalContainerLayer modalContainerLayer;
    private ModalController modalController;
    private MouseListener mouseListener;
    private ActionListener escapeAction;
    private ModalLayout modalLayout;
    private Image snapshotBlurImage;

    public ModalContainer(ModalContainerLayer modalContainerLayer, Option option, String id) {
        this.modalContainerLayer = modalContainerLayer;
        this.id = id;
        init(modalContainerLayer, option);
    }

    private void init(ModalContainerLayer modalContainerLayer, Option option) {
        setBorder(new ScaledEmptyBorder(option.getLayoutOption().getMargin()));
        modalController = new ModalController(modalContainerLayer, this, option);
        modalLayout = new ModalLayout(modalController, option.getLayoutOption());
        setLayout(modalLayout);
        installOption(option);
        add(modalController);
        modalController.setComponentOrientation(modalContainerLayer.getComponentOrientation());
    }

    private void installOption(Option option) {
        Option.BackgroundClickType clickType = option.getBackgroundClickType();
        if (clickType == Option.BackgroundClickType.BLOCK || clickType == Option.BackgroundClickType.CLOSE_MODAL) {
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
                        closeModal();
                    }
                }
            };
            addMouseListener(mouseListener);
        }
        if (option.isCloseOnPressedEscape()) {
            escapeAction = e -> closeModal();
            registerKeyboardAction(escapeAction, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        }
    }

    private void uninstallOption() {
        if (mouseListener != null) {
            removeMouseListener(mouseListener);
        }
        if (escapeAction != null) {
            unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
        }
    }

    public void addModal(Modal modal) {
        modalController.addModal(modal);
    }

    public void pushModal(Modal modal) {
        modalController.pushModal(modal);
    }

    public void popModal() {
        modalController.popModal();
    }

    public void showModal() {
        modalController.showModal();
        modalContainerLayer.setVisible(true);
    }

    public void closeModal() {
        if (modalController != null) {
            modalController.closeModal();
        }
        uninstallOption();
    }

    public void close() {
        if (modalController != null) {
            modalController.close();
        }
        uninstallOption();
    }

    protected void showSnapshot() {
        modalContainerLayer.showSnapshot();
        Option option = modalController.getOption();
        if (option.getBackgroundBlur().isBlur()) {
            VolatileImage image = modalContainerLayer.createSnapshot(this);
            if (image != null) {
                snapshotBlurImage = BlurImageUtils.applyGaussianBlur(image, option.getBackgroundBlur().getRadius());
            }
        } else {
            modalContainerLayer.updateAnotherSnapshot(true);
        }
    }

    protected void hideSnapshot() {
        modalContainerLayer.hideSnapshot();
        if (snapshotBlurImage != null) {
            snapshotBlurImage.flush();
            snapshotBlurImage = null;
        }
    }

    protected void updatePaintSnapshot(boolean paintSnapshot) {
        Option option = modalController.getOption();
        if (option.getBackgroundBlur().isBlur()) {
            if (paintSnapshot) {
                VolatileImage image = modalContainerLayer.createSnapshot(this);
                if (image != null) {
                    snapshotBlurImage = BlurImageUtils.applyGaussianBlur(image, option.getBackgroundBlur().getRadius());
                }
            } else {
                if (snapshotBlurImage != null) {
                    snapshotBlurImage.flush();
                    snapshotBlurImage = null;
                }
            }
        }
    }

    public ModalController getController() {
        return modalController;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        Option option = modalController.getOption();
        if (option.getBackgroundBlur().isBlur()) {
            if (snapshotBlurImage != null) {
                // paint snapshot blur image
                g2.setComposite(AlphaComposite.SrcOver.derive(modalController.getAnimated()));
                g2.drawImage(snapshotBlurImage, 0, 0, null);
            } else {
                // paint blur image
                VolatileImage image = modalContainerLayer.createSnapshot(this);
                if (image != null) {
                    g2.setComposite(AlphaComposite.SrcOver.derive(modalController.getAnimated()));
                    BufferedImage blurImage = BlurImageUtils.applyGaussianBlur(image, option.getBackgroundBlur().getRadius());
                    g2.drawImage(blurImage, 0, 0, null);
                    blurImage.flush();
                }
            }
        }
        g2.setColor(getBackgroundColor());
        float opacity = modalController.getOption().getOpacity() * modalController.getAnimated();
        if (opacity > 1) {
            opacity = 1;
        } else if (opacity < 0) {
            opacity = 0;
        }
        g2.setComposite(AlphaComposite.SrcOver.derive(opacity));
        g2.fill(new Rectangle2D.Double(0, 0, getWidth(), getHeight()));
        g2.dispose();
        super.paintComponent(g);
    }

    protected Color getBackgroundColor() {
        if (FlatLaf.isLafDark()) {
            if (modalController.getOption().getBackgroundDark() != null) {
                return modalController.getOption().getBackgroundDark();
            } else {
                return ColorFunctions.tint(getBackground(), 0.2f);
            }
        } else {
            if (modalController.getOption().getBackgroundLight() != null) {
                return modalController.getOption().getBackgroundLight();
            } else {
                return ColorFunctions.shade(getBackground(), 0.2f);
            }
        }
    }

    public void checkLayerVisible() {
        if (isVisible()) {
            modalContainerLayer.setVisible(true);
        } else {
            for (ModalContainer c : modalContainerLayer.getSetModalContainer()) {
                if (c != this) {
                    return;
                }
            }
            modalContainerLayer.setVisible(false);
        }
    }

    public Dimension getModalComponentSize(Component com, Container container) {
        Insets insets = new Insets(0, 0, 0, 0);
        addParentInsets(container, insets);
        return modalLayout.getComponentSize(this, com, insets);
    }

    private void addParentInsets(Container container, Insets insets) {
        Insets in = container.getInsets();
        insets.top += in.top;
        insets.left += in.left;
        insets.bottom += in.bottom;
        insets.right += in.right;
        if (container.getParent() != this) {
            addParentInsets(container.getParent(), insets);
        }
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        if (snapshotBlurImage != null) {
            snapshotBlurImage.flush();
        }
    }
}
