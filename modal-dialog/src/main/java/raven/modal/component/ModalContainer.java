package raven.modal.component;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.util.ColorFunctions;
import raven.modal.layout.ModalLayout;
import raven.modal.option.Option;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;

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

    public Component getOwner() {
        return owner;
    }

    private final String id;
    private AbstractModalContainerLayer modalContainerLayer;
    private ModalController modalController;
    private Component owner;
    private MouseListener mouseListener;
    private ActionListener escapeAction;
    private ModalLayout modalLayout;

    public ModalContainer(AbstractModalContainerLayer modalContainerLayer, Component owner, Option option, String id) {
        this.modalContainerLayer = modalContainerLayer;
        this.owner = owner;
        this.id = id;
        init(modalContainerLayer, option);
    }

    private void init(AbstractModalContainerLayer modalContainerLayer, Option option) {
        modalController = new ModalController(modalContainerLayer, this, option);
        modalLayout = new ModalLayout(modalController, option.getLayoutOption());
        setLayout(modalLayout);
        installOption(option);
        add(modalController);
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
                        modalController.closeModal();
                    }
                }
            };
            addMouseListener(mouseListener);
        }
        if (option.isCloseOnPressedEscape()) {
            escapeAction = e -> modalController.closeModal();
            registerKeyboardAction(escapeAction, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        }
    }

    public void initModal(Modal modal) {
        modalController.initModal(modal);
    }

    public void showModal() {
        modalController.showModal();
        modalContainerLayer.showContainer(true);
    }

    public ModalController getController() {
        return modalController;
    }

    protected void uninstallOption() {
        if (mouseListener != null) {
            removeMouseListener(mouseListener);
        }
        if (escapeAction != null) {
            unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
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
            modalContainerLayer.showContainer(true);
        } else {
            for (ModalContainer c : modalContainerLayer.getContainers()) {
                if (c != this) {
                    return;
                }
            }
            modalContainerLayer.showContainer(false);
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
}
