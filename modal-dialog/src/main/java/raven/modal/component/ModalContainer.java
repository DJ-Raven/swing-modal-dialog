package raven.modal.component;

import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.UIScale;
import raven.modal.layout.ModalLayout;
import raven.modal.option.LayoutOption;
import raven.modal.option.Option;
import raven.modal.utils.ModalUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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

    public Option getOption() {
        return modalController.getOption();
    }

    private final String id;
    private final AbstractModalContainerLayer modalContainerLayer;
    private final Component owner;
    private ModalController modalController;
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
        installOption(modalController.getOption());
    }

    public ModalController getController() {
        return modalController;
    }

    public Point getControllerLocation() {
        return modalController.getLocation();
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
        Option opt = modalController.getOption();
        float opacity = opt.getOpacity();
        if (opt.isHeavyWeight()) {
            if (opt.getBackgroundClickType() == Option.BackgroundClickType.NONE) {
                if (opacity != 0) {
                    opacity = 0;
                }
            } else if (opt.getBackgroundClickType() != Option.BackgroundClickType.NONE && opacity == 0) {
                opacity = 0.01f;
            }
        }
        opacity = opacity * modalController.getAnimated();
        if (opacity > 1) {
            opacity = 1;
        } else if (opacity < 0) {
            opacity = 0;
        }
        if (opacity > 0) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(getBackgroundColor());
            g2.setComposite(AlphaComposite.SrcOver.derive(opacity));
            g2.fill(getBackgroundShape());
            g2.dispose();
        }
        super.paintComponent(g);
    }

    private Shape getBackgroundShape() {
        LayoutOption layoutOption = modalController.getOption().getLayoutOption();
        if (modalController.getOption().isHeavyWeight()
                && layoutOption.isRelativeToOwner()
                && layoutOption.getRelativeToOwnerType() == LayoutOption.RelativeToOwnerType.RELATIVE_BOUNDLESS
                && !(owner instanceof RootPaneContainer)
        ) {
            Point location = SwingUtilities.convertPoint(owner.getParent(), owner.getLocation(), this);
            Rectangle rec = new Rectangle(location, owner.getSize());
            Insets padding = layoutOption.getBackgroundPadding();
            if (!FlatUIUtils.isInsetsEmpty(padding)) {
                rec = FlatUIUtils.subtractInsets(rec, UIScale.scale(padding));
            }
            return rec;
        }
        return new Rectangle(0, 0, getWidth(), getHeight());
    }

    protected Color getBackgroundColor() {
        return ModalUtils.getBackgroundColor(modalController.getOption().getBackgroundDark(),
                modalController.getOption().getBackgroundLight(),
                getBackground());
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
