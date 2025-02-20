package raven.modal.component;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.ColorFunctions;
import com.formdev.flatlaf.util.UIScale;
import raven.modal.layout.ModalLayout;
import raven.modal.option.LayoutOption;
import raven.modal.option.Option;

import javax.swing.*;
import javax.swing.border.Border;
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
    private AbstractModalContainerLayer modalContainerLayer;
    private ModalController modalController;
    private Component owner;
    private MouseListener mouseListener;
    private ActionListener escapeAction;
    private ModalLayout modalLayout;

    private JWindow window;
    private ComponentListener componentListener;

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
            if (modalController.isUseEmbedWindow()) {
                window.getRootPane().registerKeyboardAction(escapeAction, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
            } else {
                registerKeyboardAction(escapeAction, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
            }
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

    protected void showWindow() {
        if (!modalController.isUseEmbedWindow()) {
            return;
        }
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        componentListener = new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                window.revalidate();
                revalidate();
            }
        };
        addComponentListener(componentListener);
        window = new JWindow(parentWindow);
        modalLayout.setWindow(window);
        window.setContentPane(modalController);
        window.setVisible(true);
    }

    protected void closeWindow() {
        if (window == null || !modalController.isUseEmbedWindow()) {
            return;
        }
        removeComponentListener(componentListener);
        modalLayout.setWindow(null);
        add(modalController);
        window.dispose();
    }

    public ModalController getController() {
        return modalController;
    }

    public Point getControllerLocation() {
        if (window != null && modalController.isUseEmbedWindow()) {
            return getWindowControllerLocation();
        }
        return modalController.getLocation();
    }

    public void updateLayout() {
        if (modalController.isUseEmbedWindow()) {
            repaint();
        }
        revalidate();
    }

    private Point getWindowControllerLocation() {
        Point point = SwingUtilities.convertPoint(getParent(), getLocation(), window.getParent());
        Point windowLocation = window.getLocation();
        Point windowParentLocation = window.getParent().getLocation();
        int x = windowLocation.x - (windowParentLocation.x + point.x);
        int y = windowLocation.y - (windowParentLocation.y + point.y);
        return new Point(x, y);
    }

    protected void uninstallOption() {
        if (mouseListener != null) {
            removeMouseListener(mouseListener);
        }
        if (escapeAction != null) {
            if (modalController.isUseEmbedWindow()) {
                window.getRootPane().unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
            } else {
                unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        float opacity = modalController.getOption().getOpacity();
        Option opt = modalController.getOption();
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
        paintModalBorder(g);
        super.paintComponent(g);
    }

    private void paintModalBorder(Graphics g) {
        Border border = modalController.getModalBorder();
        if (window != null && modalController.isUseEmbedWindow() && border != null) {
            Graphics2D g2 = (Graphics2D) g.create();
            Rectangle rec = window.getBounds();
            Point point = SwingUtilities.convertPoint(getParent(), getLocation(), window.getParent());
            Insets insets = border.getBorderInsets(modalController);
            Point windowParentLocation = window.getParent().getLocation();
            int x = rec.x - windowParentLocation.x - point.x - insets.left;
            int y = rec.y - windowParentLocation.y - point.y - insets.top;
            border.paintBorder(this, g, x, y, rec.width + (insets.left + insets.right), rec.height + (insets.top + insets.bottom));
            g2.dispose();
        }
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
        if (container == null) {
            return;
        }
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
