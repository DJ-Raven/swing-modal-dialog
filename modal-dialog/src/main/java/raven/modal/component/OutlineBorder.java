package raven.modal.component;

import com.formdev.flatlaf.ui.FlatDropShadowBorder;
import com.formdev.flatlaf.ui.FlatEmptyBorder;
import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.UIScale;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

/**
 * @author Raven
 */
public class OutlineBorder extends FlatEmptyBorder {

    private static final float inner = 0.2f;
    private final float borderWidth;

    private final int shadowSize;
    private final int shadowTopSize;
    private final float round;
    private final Color borderColor;
    private FlatDropShadowBorder shadowBorder;

    public OutlineBorder(int shadowSize, float round) {
        this(shadowSize, shadowSize, round);
    }

    public OutlineBorder(float borderWidth, int shadowSize, float round) {
        this(borderWidth, shadowSize, shadowSize, round, null);
    }

    public OutlineBorder(int shadowSize, int shadowTopSize, float round) {
        this(0, shadowSize, shadowTopSize, round, null);
    }

    public OutlineBorder(float borderWidth, int shadowSize, int shadowTopSize, float round, Color borderColor) {
        this(borderWidth, shadowSize, shadowTopSize, -1, null, round, borderColor);
    }

    public OutlineBorder(float borderWidth, int shadowSize, int shadowTopSize, float shadowOpacity, Color shadowColor, float round, Color borderColor) {
        super(getShadowInsets(borderWidth, shadowSize, shadowTopSize, round));
        this.borderWidth = borderWidth;
        this.shadowSize = shadowSize;
        this.shadowTopSize = Math.min(shadowTopSize, shadowSize);
        this.round = round;
        this.borderColor = borderColor;

        if (shadowSize > 0) {
            int width = (int) getBorderWidth(borderWidth, round);
            this.shadowBorder = new FlatDropShadowBorder(
                    shadowColor == null ? UIManager.getColor("Popup.dropShadowColor") : shadowColor,
                    new Insets(shadowSize + width, shadowSize + width, shadowSize + width, shadowSize + width),
                    shadowOpacity <= -1 ? FlatUIUtils.getUIFloat("Popup.dropShadowOpacity", 0.5f) : shadowOpacity);
        }
    }

    private static Insets getShadowInsets(float borderWidth, int shadowSize, int shadowTopSize, float round) {
        float addSize = getBorderWidth(borderWidth, round);
        if (shadowSize > 0) {
            int top = (int) (shadowTopSize + addSize);
            int left = (int) (shadowSize + addSize);
            int bottom = (int) (shadowSize + addSize);
            int right = (int) (shadowSize + addSize);
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

            // paint shadow
            if (shadowBorder != null) {
                shadowBorder.paintBorder(c, g2, x, y, width, height);
            }

            float lineWidth = UIScale.scale(borderWidth);
            float arc = UIScale.scale(round);
            float lx = UIScale.scale(shadowSize);
            float ly = UIScale.scale(shadowTopSize);
            int w = width - UIScale.scale(shadowSize + shadowSize);
            int h = height - UIScale.scale(shadowTopSize + shadowSize);
            g2.translate(lx, ly);

            // paint background
            g2.setColor(c.getBackground());
            g2.fill(createShapeBorder(w, h));

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

    private Shape createShapeBorder(float width, float height) {
        float lineWidth = UIScale.scale(getBorderWidth(borderWidth, round));

        float arc = UIScale.scale(round);
        lineWidth += UIScale.scale(0.5f);
        float x = lineWidth;
        float y = lineWidth + UIScale.scale(shadowSize - shadowTopSize);
        Area area = new Area(new RoundRectangle2D.Float(0, 0, width, height, arc, arc));
        area.subtract(new Area(new Rectangle2D.Float(x, y, width - lineWidth * 2f, height - (lineWidth + y))));
        return area;
    }

    protected Color getBorderColor() {
        if (borderColor != null) {
            return borderColor;
        }
        Color color = UIManager.getColor("Component.borderColor");
        return color;
    }
}
