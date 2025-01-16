package raven.modal.component;

import com.formdev.flatlaf.ui.FlatUIUtils;
import raven.modal.layout.FullContentLayout;
import raven.modal.toast.ToastHeavyWeightContainerLayer;
import raven.modal.utils.ModalUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Raven
 */
public class ModalWindow extends JWindow {

    private final Window parentWindow;
    private final JRootPane parentRootPane;
    private JLayeredPane layeredPane;
    private PropertyChangeListener propertyListener;
    private ComponentListener componentListener;
    private List<HeavyWindowAction> list;

    public ModalWindow(Window window) {
        super(window);
        this.parentWindow = window;
        this.parentRootPane = SwingUtilities.getRootPane(window);
        init();
    }

    private void init() {
        layeredPane = new JLayeredPane();
        list = new ArrayList<>();
        layeredPane.setLayout(new FullContentLayout());
        layeredPane.setComponentOrientation(parentWindow.getComponentOrientation());
        setContentPane(layeredPane);
        setBackground(new Color(0, 0, 0, 0));
        installParentWindowListener();
    }

    public void addToast(ToastHeavyWeightContainerLayer toast) {
        list.add(toast);
        toast.initComponentOrientation(parentWindow.getComponentOrientation());
        layeredPane.add(toast.getLayeredPane(), JLayeredPane.POPUP_LAYER);
    }

    public void addModal(ModalHeavyWeightContainerLayer modal) {
        list.add(modal);
        modal.initComponentOrientation(parentWindow.getComponentOrientation());
        layeredPane.add(modal.getLayeredPane(), JLayeredPane.MODAL_LAYER + 1);
    }

    public void updateLayout() {
        Component contentPane = parentRootPane.getContentPane();
        boolean isFullWindowContent = ModalUtils.isFullWindowContent(parentRootPane);
        Rectangle parentRec = parentWindow.getBounds();
        Insets parentInsets = parentWindow.getInsets();
        parentInsets.top += contentPane.getY();
        if (isFullWindowContent) {
            parentInsets.top += 1;
        }
        Rectangle rectangle = FlatUIUtils.subtractInsets(parentRec, parentInsets);
        setBounds(rectangle);
        revalidate();
    }

    public void setShowWindowAndCheck(boolean show) {
        if (show) {
            if (!isVisible()) {
                setVisible(true);
            }
        } else {
            for (HeavyWindowAction com : list) {
                if (!com.isEmptyItem()) {
                    return;
                }
            }
            setVisible(false);
        }
    }

    protected void installParentWindowListener() {
        propertyListener = evt -> {
            if (evt.getNewValue() == null && evt.getOldValue() instanceof RootPaneContainer) {
                uninstallWindowListener((RootPaneContainer) evt.getOldValue());
            }
        };
        componentListener = new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateLayout();
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                updateLayout();
            }
        };

        if (parentWindow instanceof RootPaneContainer) {
            ((RootPaneContainer) parentWindow).getRootPane().addPropertyChangeListener("ancestor", propertyListener);
        }
        parentWindow.addComponentListener(componentListener);
    }

    protected void uninstallWindowListener(RootPaneContainer rootPaneContainer) {
        parentWindow.removeComponentListener(componentListener);
        if (parentWindow instanceof RootPaneContainer) {
            rootPaneContainer.getRootPane().removePropertyChangeListener("ancestor", propertyListener);
        }
        remove();
    }

    private void remove() {
        list.forEach(com -> com.windowRemoved());
        componentListener = null;
        propertyListener = null;
        dispose();
        HeavyWeightWindowShared.getInstance().remove(parentWindow);
    }
}
