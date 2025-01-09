package raven.modal.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowStateListener;

/**
 * @author Raven
 */
public class ModalUtils {

    public static int maximumInsets(Insets insets) {
        int maxTopBottom = Math.max(insets.top, insets.bottom);
        int maxLeftRight = Math.max(insets.left, insets.right);
        return Math.max(maxTopBottom, maxLeftRight);
    }

    public static int minimumInsets(Insets insets) {
        int maxTopBottom = Math.min(insets.top, insets.bottom);
        int maxLeftRight = Math.min(insets.left, insets.right);
        return Math.min(maxTopBottom, maxLeftRight);
    }

    public static WindowStateListener installWindowStateListener(RootPaneContainer rootPane, WindowStateListener listener) {
        Window window = SwingUtilities.getWindowAncestor(rootPane.getRootPane());
        if (window != null) {
            window.addWindowStateListener(listener);
        }
        return listener;
    }

    public static void uninstallWindowStateListener(RootPaneContainer rootPane, WindowStateListener listener) {
        Window window = SwingUtilities.getWindowAncestor(rootPane.getRootPane());
        if (window != null) {
            window.removeWindowStateListener(listener);
        }
    }
}
