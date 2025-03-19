package raven.modal.utils;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.util.ColorFunctions;
import com.formdev.flatlaf.util.SystemInfo;
import com.formdev.flatlaf.util.UIScale;
import raven.modal.toast.option.ToastOption;

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

    public static Color getBackgroundColor(Color dark, Color light, Color background) {
        if (FlatLaf.isLafDark()) {
            if (dark != null) {
                return dark;
            } else {
                return ColorFunctions.tint(background, 0.2f);
            }
        } else {
            if (light != null) {
                return light;
            } else {
                return ColorFunctions.shade(background, 0.2f);
            }
        }
    }

    public static boolean isShadowAndRoundBorderSupport() {
        return SystemInfo.isWindows;
    }

    public static int getToastExtraBorderPadding(ToastOption option) {
        // extra padding apply when use native border
        if (!option.isHeavyWeight() || !SystemInfo.isWindows_11_orLater) return 0;
        int round = UIScale.scale(option.getStyle().getBorderStyle().getRound());
        if (round <= 0) return 0;
        return 2;
    }

    public static int getToastExtraGap(ToastOption option) {
        // extra gap apply when use native border
        if (!option.isHeavyWeight() || !SystemInfo.isWindows_11_orLater) return 0;
        int round = UIScale.scale(option.getStyle().getBorderStyle().getRound());
        if (round <= 0) return 0;
        return 10;
    }
}
