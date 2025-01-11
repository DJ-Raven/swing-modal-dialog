package raven.modal.component;

import com.formdev.flatlaf.ui.FlatUIUtils;
import raven.modal.utils.ModalUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowStateListener;
import java.beans.PropertyChangeListener;

/**
 * @author Raven
 */
public class ModalHeavyWeightContainerLayer extends AbstractModalContainerLayer {

    private final Window parentWindow;
    private final JRootPane rootPane;
    private JWindow window;
    private PropertyChangeListener propertyListener;
    private ComponentListener componentListener;
    private WindowStateListener stateListener;

    public ModalHeavyWeightContainerLayer(Window parentWindow) {
        this.parentWindow = parentWindow;
        this.rootPane = SwingUtilities.getRootPane(parentWindow);
        init();
    }

    private void init() {
        window = new JWindow(parentWindow);
        window.setBackground(new Color(0, 0, 0, 0));
        window.setContentPane(layeredPane);
    }

    @Override
    public void showContainer(boolean show) {
        updateLayout();
        window.setVisible(true);
    }

    @Override
    public void removeContainer(ModalContainer container) {
        super.removeContainer(container);
        if (containers.isEmpty()) {
            window.dispose();
        }
    }

    private void updateLayout() {
        Component contentPane = rootPane.getContentPane();
        boolean isFullWindowContent = ModalUtils.isFullWindowContent(rootPane);
        Rectangle parentRec = parentWindow.getBounds();
        Insets parentInsets = parentWindow.getInsets();
        parentInsets.top += contentPane.getY();
        if (isFullWindowContent) {
            parentInsets.top += 1;
        }
        Rectangle rectangle = FlatUIUtils.subtractInsets(parentRec, parentInsets);
        window.setBounds(rectangle);
        window.revalidate();
    }

    private void remove() {
        closeAllModalImmediately();
        componentListener = null;
        propertyListener = null;
        stateListener = null;
        window.dispose();
        window = null;
    }

    protected void installWindowListener() {
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
        stateListener = e -> {
            if (e.getNewState() == 6 || e.getNewState() == 0) {
                SwingUtilities.invokeLater(() -> {
                    updateModalLayout();
                });
            }
        };

        if (parentWindow instanceof RootPaneContainer) {
            ((RootPaneContainer) parentWindow).getRootPane().addPropertyChangeListener("ancestor", propertyListener);
        }
        parentWindow.addComponentListener(componentListener);
        parentWindow.addWindowStateListener(stateListener);
    }

    protected void uninstallWindowListener(RootPaneContainer rootPaneContainer) {
        parentWindow.removeComponentListener(componentListener);
        parentWindow.removeWindowStateListener(stateListener);
        if (parentWindow instanceof RootPaneContainer) {
            rootPaneContainer.getRootPane().removePropertyChangeListener("ancestor", propertyListener);
        }
        remove();
    }
}
