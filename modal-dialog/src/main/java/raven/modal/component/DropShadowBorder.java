package raven.modal.component;

import com.formdev.flatlaf.ui.FlatDropShadowBorder;
import com.formdev.flatlaf.ui.FlatEmptyBorder;
import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.HiDPIUtils;
import com.formdev.flatlaf.util.UIScale;
import raven.modal.utils.ImageSnapshots;
import raven.modal.utils.ModalUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author Raven
 */
public class DropShadowBorder extends FlatEmptyBorder {

    private static final float inner = 0.2f;
    private final int borderWidth;

    private final Insets shadowSize;
    private final int round;
    private final Color borderColor;
    private FlatDropShadowBorder shadowBorder;

    public DropShadowBorder(int shadowSize, int round) {
        this(new Insets(shadowSize, shadowSize, shadowSize, shadowSize), 0, round);
    }

    public DropShadowBorder(Insets shadowSize, int round) {
        this(shadowSize, 0, null, round);
    }

    public DropShadowBorder(Insets shadowSize, int borderWidth, int round) {
        this(shadowSize, borderWidth, null, round);
    }

    public DropShadowBorder(Insets shadowSize, int borderWidth, Color borderColor, int round) {
        this(shadowSize, -1, null, borderWidth, borderColor, round);
    }

    public DropShadowBorder(Insets shadowSize, float shadowOpacity, Color shadowColor, int borderWidth, Color borderColor, int round) {
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

    private static Insets getShadowInsets(Insets shadowInsets, int borderWidth, int round) {
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

    private static float getBorderWidth(int borderWidth, int round) {
        float innerArc = round - (borderWidth * 2f);
        return (float) Math.ceil((borderWidth + Math.max((innerArc * inner), 0)));
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        HiDPIUtils.paintAtScale1x((Graphics2D) g, x, y, width, height, (g2d, x1, y1, w1, h1, scaleFactor) ->
                paintImpl(c, g2d, x1, y1, w1, h1, scaleFactor));
    }

    private void paintImpl(Component c, Graphics2D g2d, int x, int y, int width, int height, double scaleFactor) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        try {
            FlatUIUtils.setRenderingHints(g);

            // paint shadow
            if (shadowBorder != null) {
                ImageSnapshots.paintBorder(c, shadowBorder, g, 0, 0, c.getWidth(), c.getHeight());
            }

            int top = shadowSize.top;
            int left = shadowSize.left;
            int bottom = shadowSize.bottom;
            int right = shadowSize.right;

            if (!c.getComponentOrientation().isLeftToRight()) {
                left = shadowSize.right;
                right = shadowSize.left;
            }

            int lineWidth = scale(borderWidth, scaleFactor);
            int arc = scale(round, scaleFactor);
            int lx = scale(left, scaleFactor);
            int ly = scale(top, scaleFactor);
            int w = width - scale((left + right), scaleFactor);
            int h = height - scale((top + bottom), scaleFactor);
            g.translate(lx, ly);

            // paint background
            g.setColor(c.getBackground());
            g.fill(FlatUIUtils.createComponentRectangle(0, 0, w, h, arc));

            // paint outline
            if (lineWidth > 0) {
                Color color = getBorderColor();
                g.setColor(color);
                FlatUIUtils.paintOutline(g, x, y, w, h, lineWidth, arc);
            }
        } finally {
            g.dispose();
        }
        g2d.drawImage(image, x, y, null);
    }

    private int scale(int value, double scaleFactor) {
        return (int) Math.ceil(UIScale.scale(value) * scaleFactor);
    }

    protected Color getBorderColor() {
        if (borderColor != null) {
            return borderColor;
        }
        return UIManager.getColor("Component.borderColor");
    }

    public Insets getShadowSize() {
        return new Insets(shadowSize.top, shadowSize.left, shadowSize.bottom, shadowSize.right);
    }

    public float getRound() {
        return round;
    }
}
