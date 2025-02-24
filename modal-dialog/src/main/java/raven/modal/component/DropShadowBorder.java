package raven.modal.component;

import com.formdev.flatlaf.ui.FlatDropShadowBorder;
import com.formdev.flatlaf.ui.FlatEmptyBorder;
import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.UIScale;
import raven.modal.utils.ModalUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * @author Raven
 */
public class DropShadowBorder extends FlatEmptyBorder {

    private static final float inner = 0.2f;
    private final float borderWidth;

    private final Insets shadowSize;
    private final float round;
    private final Color borderColor;
    private FlatDropShadowBorder shadowBorder;

    public DropShadowBorder(int shadowSize, float round) {
        this(new Insets(shadowSize, shadowSize, shadowSize, shadowSize), 0, round);
    }

    public DropShadowBorder(Insets shadowSize, float round) {
        this(shadowSize, 0, null, round);
    }

    public DropShadowBorder(Insets shadowSize, float borderWidth, float round) {
        this(shadowSize, borderWidth, null, round);
    }

    public DropShadowBorder(Insets shadowSize, float borderWidth, Color borderColor, float round) {
        this(shadowSize, -1, null, borderWidth, borderColor, round);
    }

    public DropShadowBorder(Insets shadowSize, float shadowOpacity, Color shadowColor, float borderWidth, Color borderColor, float round) {
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
        return (float) Math.ceil((borderWidth + Math.max((innerArc * inner), 0)));
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = null;
        try {
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            g2 = image.createGraphics();
            FlatUIUtils.setRenderingHints(g2);

            // paint shadow
            if (shadowBorder != null) {
                g2.fill(getPaintShape(width, height));
                g2.setComposite(AlphaComposite.SrcIn);
                shadowBorder.paintBorder(c, g2, 0, 0, width, height);
                g2.setComposite(AlphaComposite.SrcOver);
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
            Shape shape = getBorderShape(0, 0, w, h, arc);
            if (shape != null) {
                g2.setColor(c.getBackground());
                g2.fill(shape);
            }

            // paint outline
            if (borderWidth > 0) {
                Color color = getBorderColor();
                g2.setColor(color);
                FlatUIUtils.paintOutline(g2, 0, 0, w, h, lineWidth, arc);
            }
            g.drawImage(image, x, y, null);
        } finally {
            if (g2 != null) {
                g2.dispose();
            }
        }
    }

    protected Color getBorderColor() {
        if (borderColor != null) {
            return borderColor;
        }
        Color color = UIManager.getColor("Component.borderColor");
        return color;
    }

    private Shape getPaintShape(float width, float height) {
        Area area = new Area(new Rectangle2D.Float(0, 0, width, height));
        Insets insets = getBorderInsets();
        float innerWidth = width - (insets.left + insets.right);
        float innerHeight = height - (insets.top + insets.bottom);
        if (innerWidth > 0 && innerHeight > 0) {
            area.subtract(new Area(new Rectangle2D.Float(insets.left, insets.top, innerWidth, innerHeight)));
        }
        return area;
    }

    private Shape getBorderShape(float x, float y, float width, float height, float arc) {
        if (round == 0) {
            return null;
        }
        Area area = new Area(FlatUIUtils.createComponentRectangle(x, y, width, height, arc));
        Insets insets = getBorderInsets();
        Insets shadowSize = UIScale.scale(getShadowSize());
        float innerWidth = width - (insets.left + insets.right) + (shadowSize.left + shadowSize.right);
        float innerHeight = height - (insets.top + insets.bottom) + (shadowSize.top + shadowSize.bottom);
        if (innerWidth > 0 && innerHeight > 0) {
            area.subtract(new Area(new Rectangle2D.Float(
                    insets.left - shadowSize.left,
                    insets.top - shadowSize.top,
                    innerWidth,
                    innerHeight)));
        }
        return area;
    }

    public Insets getShadowSize() {
        return new Insets(shadowSize.top, shadowSize.left, shadowSize.bottom, shadowSize.right);
    }

    public float getRound() {
        return round;
    }
}
