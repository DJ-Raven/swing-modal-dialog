package raven.modal.toast;

import raven.modal.layout.FrameModalLayout;
import raven.modal.layout.FrameToastLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowStateListener;
import java.beans.PropertyChangeListener;
import java.util.Map;

/**
 * @author Raven
 */
public class ToastContainerLayer extends AbstractToastContainerLayer {

    private final Map<RootPaneContainer, ToastContainerLayer> map;
    private final RootPaneContainer rootPaneContainer;
    private PropertyChangeListener propertyListener;
    private WindowStateListener stateListener;

    public ToastContainerLayer(Map<RootPaneContainer, ToastContainerLayer> map, RootPaneContainer rootPaneContainer) {
        this.map = map;
        this.rootPaneContainer = rootPaneContainer;
    }

    @Override
    public void showContainer(boolean show) {
        layeredPane.setVisible(show);
    }

    public void remove() {
        closeAllImmediately();
        propertyListener = null;
        stateListener = null;
    }

    public void installWindowListener() {
        JRootPane rootPane = rootPaneContainer.getRootPane();
        Window window = SwingUtilities.getWindowAncestor(rootPane);
        if (window != null) {
            stateListener = e -> {
                if (e.getNewState() == 6 || e.getNewState() == 0) {
                    SwingUtilities.invokeLater(() -> {
                        if (map.containsKey(rootPaneContainer)) {
                            map.get(rootPaneContainer).updateLayout();
                        }
                    });
                }
            };
            window.addWindowStateListener(stateListener);
        }
        propertyListener = evt -> {
            if (evt.getNewValue() == null && evt.getOldValue() instanceof RootPaneContainer) {
                uninstallWindowListener((RootPaneContainer) evt.getOldValue());
            }
        };
        rootPane.addPropertyChangeListener("ancestor", propertyListener);
    }

    public void uninstallWindowListener(RootPaneContainer rootPaneContainer) {
        if (map.containsKey(rootPaneContainer)) {
            JRootPane rootPane = rootPaneContainer.getRootPane();
            Window window = SwingUtilities.getWindowAncestor(rootPane);
            if (window != null) {
                window.removeWindowStateListener(stateListener);
            }
            rootPaneContainer.getRootPane().removePropertyChangeListener("ancestor", propertyListener);

            // uninstall layout
            JLayeredPane windowLayeredPane = rootPaneContainer.getLayeredPane();
            LayoutManager oldLayout = windowLayeredPane.getLayout();
            if (oldLayout != null) {
                if (oldLayout instanceof FrameToastLayout) {
                    windowLayeredPane.setLayout(null);
                } else if (oldLayout instanceof FrameModalLayout) {
                    ((FrameModalLayout) oldLayout).setOtherComponent(null, null);
                }
            }

            // remove
            map.remove(rootPaneContainer);
            windowLayeredPane.remove(layeredPane);
            remove();
        }
    }
}
