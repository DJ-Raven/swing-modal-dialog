package raven.modal.component;

import com.formdev.flatlaf.ui.FlatDropShadowBorder;
import com.formdev.flatlaf.ui.FlatEmptyBorder;
import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.UIScale;
import raven.modal.utils.ModalUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * @author Raven
 */
public class OutlineBorder extends FlatEmptyBorder {

    private static final float inner = 0.2f;
    private final float borderWidth;

    private final Insets shadowSize;
    private final float round;
    private final Color borderColor;
    private FlatDropShadowBorder shadowBorder;

    public OutlineBorder(int shadowSize, float round) {
        this(new Insets(shadowSize, shadowSize, shadowSize, shadowSize), shadowSize, round);
    }

    public OutlineBorder(Insets shadowSize, float round) {
        this(shadowSize, 0, null, round);
    }

    public OutlineBorder(Insets shadowSize, float borderWidth, float round) {
        this(shadowSize, borderWidth, null, round);
    }

    public OutlineBorder(Insets shadowSize, float borderWidth, Color borderColor, float round) {
        this(shadowSize, -1, null, borderWidth, borderColor, round);
    }

    public OutlineBorder(Insets shadowSize, float shadowOpacity, Color shadowColor, float borderWidth, Color borderColor, float round) {
        super(getShadowInsets(shadowSize, borderWidth, round));
        this.shadowSize = shadowSize;
        this.borderWidth = borderWidth;
        this.borderColor = borderColor;
        this.round = round;

        int size = ModalUtils.maximumInsets(shadowSize);
        if (size > 0) {
            int width = (int) getBorderWidth(borderWidth, round);
            this.shadowBorder = new FlatDropShadowBorder(
                    shadowColor == null ? UIManager.getColor("Popup.dropShadowColor") : shadowColor,
                    new Insets(size + width, size + width, size + width, size + width),
                    shadowOpacity <= -1 ? FlatUIUtils.getUIFloat("Popup.dropShadowOpacity", 0.5f) : shadowOpacity);
        }
    }

    private static Insets getShadowInsets(Insets shadowInsets, float borderWidth, float round) {
        float addSize = getBorderWidth(borderWidth, round);
        if (!FlatUIUtils.isInsetsEmpty(shadowInsets)) {
            int top = (int) (shadowInsets.top + addSize);
            int left = (int) (shadowInsets.left + addSize);
            int bottom = (int) (shadowInsets.bottom + addSize);
            int right = (int) (shadowInsets.right + addSize);
            return new Insets(top, left, bottom, right);
        }
        int b = (int) addSize;
        return new Insets(b, b, b, b);
    }

    private static float getBorderWidth(float borderWidth, float round) {
        float innerArc = round - (borderWidth * 2f);
        return borderWidth + (innerArc * inner);
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();

        try {
            FlatUIUtils.setRenderingHints(g2);
            if (c.isOpaque()) {
                g2.setColor(getBackgroundColor());
                g2.fill(new Rectangle(0, 0, c.getWidth(), c.getHeight()));
            }
            // paint shadow
            if (shadowBorder != null) {
                shadowBorder.paintBorder(c, g2, x, y, width, height);
            }

            int top = shadowSize.top;
            int left = shadowSize.left;
            int bottom = shadowSize.bottom;
            int right = shadowSize.right;

            if (!c.getComponentOrientation().isLeftToRight()) {
                left = shadowSize.right;
                right = shadowSize.left;
            }

            float lineWidth = UIScale.scale(borderWidth);
            float arc = UIScale.scale(round);
            float lx = UIScale.scale(left);
            float ly = UIScale.scale(top);
            int w = width - UIScale.scale(left + right);
            int h = height - UIScale.scale(top + bottom);
            g2.translate(lx, ly);

            // paint background
            g2.setColor(c.getBackground());
            g2.fill(new RoundRectangle2D.Float(0, 0, w, h, arc, arc));

            // paint outline
            if (borderWidth > 0) {
                Color color = getBorderColor();
                g2.setColor(color);
                FlatUIUtils.paintOutline(g2, x, y, w, h, lineWidth, arc);
            }
        } finally {
            g2.dispose();
        }
    }

    protected Color getBorderColor() {
        if (borderColor != null) {
            return borderColor;
        }
        Color color = UIManager.getColor("Component.borderColor");
        return color;
    }

    private Color getBackgroundColor() {
        return UIManager.getColor("Panel.background");
    }
}
