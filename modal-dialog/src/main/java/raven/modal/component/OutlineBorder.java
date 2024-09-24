package raven.modal.component;

import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.UIScale;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;

import static com.formdev.flatlaf.util.UIScale.scale;

/**
 * @author Raven
 */
public class OutlineBorder extends AbstractBorder {

    private final float round;

    public OutlineBorder(float round) {
        this.round = round;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        FlatUIUtils.setRenderingHints(g2);
        Color color = getBorderColor();
        g2.setColor(color);
        float lineWidth = UIScale.scale(getBorderWidth());
        float arc = UIScale.scale(round);
        FlatUIUtils.paintOutline(g2, x, y, width, height, lineWidth, arc);
        g2.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        int lineWidth = Math.round(UIScale.scale(getBorderWidth()));
        insets = super.getBorderInsets(c, insets);
        insets.top = scale(insets.top) + lineWidth;
        insets.left = scale(insets.left) + lineWidth;
        insets.bottom = scale(insets.bottom) + lineWidth;
        insets.right = scale(insets.right) + lineWidth;
        return insets;
    }

    protected Color getBorderColor() {
        Color color = UIManager.getColor("Component.borderColor");
        return color;
    }

    protected float getBorderWidth() {
        return FlatUIUtils.getUIFloat("Component.borderWidth", 1);
    }
}
