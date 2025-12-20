package raven.modal.demo.simple.color;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

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
            JButton button = components[i];
            button.setBackground(colors[i]);
            button.setText(colorToHex(colors[i]));
        }
    }

    private String colorToHex(Color color) {
        return String.format("%02X%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    private JButton createPreviewComponent() {
        JButton button = new JButton();
        button.putClientProperty(FlatClientProperties.STYLE, "" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0;");
        return button;
    }
}
