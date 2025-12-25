package raven.modal.demo.simple.color;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import raven.modal.Toast;
import raven.modal.toast.option.ToastBorderStyle;
import raven.modal.toast.option.ToastLocation;
import raven.modal.toast.option.ToastOption;
import raven.modal.toast.option.ToastStyle;
import raven.modal.utils.FlatLafStyleUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class WheelColorHarmoniesPreview extends JPanel {

    private JButton[] components;
    public Color[] colors;

    public WheelColorHarmoniesPreview() {
        setLayout(new MigLayout("novisualpadding,insets 2 n n n,wrap,fill", "[fill,200]", "[fill,sg]"));
    }

    public Color[] getColors() {
        return colors;
    }

    public void setColors(Color[] colors) {
        this.colors = colors;
        repaint();
        initComponent();
    }

    private void initComponent() {
        if (colors == null) {
            components = null;
            removeAll();
            repaint();
            revalidate();
            return;
        }
        if (components == null || colors.length != components.length) {
            components = new JButton[colors.length];
            removeAll();
            for (int i = 0; i < components.length; i++) {
                components[i] = createPreviewComponent();
                add(components[i]);
            }
            repaint();
            revalidate();
        }

        for (int i = 0; i < colors.length; i++) {
            String hex = colorToHex(colors[i]);
            JButton button = components[i];
            FlatLafStyleUtils.appendStyle(button, "foreground:contrast(" + hex + ",#444,#fff);");
            button.setBackground(colors[i]);
            button.setText(hex);
        }
    }

    private String colorToHex(Color color) {
        if (color.getAlpha() == 255) {
            return String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
        }
        return String.format("#%02X%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    private JButton createPreviewComponent() {
        return new PreviewComponent();
    }

    private static class PreviewComponent extends JButton {

        public PreviewComponent() {
            putClientProperty(FlatClientProperties.STYLE, "" +
                    "borderWidth:0;" +
                    "focusWidth:0;" +
                    "innerFocusWidth:0;");
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addActionListener(e -> copy());
        }

        private void copy() {
            String hex = getText();
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(new StringSelection(hex), null);
            ToastOption option = Toast.createOption();
            option.setDuration(300)
                    .setCloseOnClick(true)
                    .getLayoutOption().setLocation(ToastLocation.BOTTOM_TRAILING);
            option.getStyle().setShowIcon(false)
                    .setShowCloseButton(false)
                    .setBackgroundType(ToastStyle.BackgroundType.NONE)
                    .getBorderStyle().setBorderType(ToastBorderStyle.BorderType.BOTTOM_LINE);
            Toast.show(this, Toast.Type.SUCCESS, "Copied hex code " + hex, option);
        }
    }
}
