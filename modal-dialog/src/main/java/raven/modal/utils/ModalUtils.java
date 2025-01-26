package raven.modal.utils;

import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import java.awt.*;

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

    public static boolean isFullWindowContent(JRootPane rootPane) {
        return FlatClientProperties.clientPropertyBoolean(rootPane, FlatClientProperties.FULL_WINDOW_CONTENT, false);
    }

    public static Insets copyInsets(Insets insets) {
        return new Insets(insets.top, insets.left, insets.bottom, insets.right);
    }
}
