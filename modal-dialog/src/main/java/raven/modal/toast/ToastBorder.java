package raven.modal.toast;

import com.formdev.flatlaf.ui.FlatEmptyBorder;
import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.ColorFunctions;
import com.formdev.flatlaf.util.HiDPIUtils;
import com.formdev.flatlaf.util.UIScale;
import raven.modal.toast.option.ToastBorderStyle;
import raven.modal.toast.option.ToastStyle;
import raven.modal.utils.ModalUtils;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * @author Raven
 */
public class ToastBorder extends FlatEmptyBorder {

    private final ToastPanel.ToastData toastData;

    public ToastBorder(ToastPanel.ToastData toastData) {
        this.toastData = toastData;
    }

    private Insets getStyleBorderInsets(Component c) {
        ToastBorderStyle style = toastData.getOption().getStyle().getBorderStyle();
        ToastBorderStyle.BorderType borderType = style.getBorderType();
        if (borderType == ToastBorderStyle.BorderType.NONE || borderType == ToastBorderStyle.BorderType.OUTLINE) {
            return null;
        }
        int line = UIScale.scale(style.getLineSize());
        boolean ltr = c.getComponentOrientation().isLeftToRight();
        int top = 0;
        int left = 0;
        int bottom = 0;
        int right = 0;
        if ((borderType == ToastBorderStyle.BorderType.LEADING_LINE && ltr) || (borderType == ToastBorderStyle.BorderType.TRAILING_LINE && !ltr)) {
            left = line;
        } else if ((borderType == ToastBorderStyle.BorderType.TRAILING_LINE && ltr) || (borderType == ToastBorderStyle.BorderType.LEADING_LINE && !ltr)) {
            right = line;
        } else if (borderType == ToastBorderStyle.BorderType.TOP_LINE) {
            top = line;
        } else {
            bottom = line;
        }
        return new Insets(top, left, bottom, right);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        Insets styleInsets = getStyleBorderInsets(c);
        if (styleInsets == null) {
            return super.getBorderInsets(c);
        } else {
            return FlatUIUtils.addInsets(super.getBorderInsets(c), styleInsets);
        }
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

            ToastStyle style = toastData.getOption().getStyle();
            ToastBorderStyle borderStyle = style.getBorderStyle();
            boolean ltr = c.getComponentOrientation().isLeftToRight();

            int lx = 0;
            int ly = 0;
            int bw = width;
            int bh = height;
            float innerRound = style.getBorderStyle().getRound() * 0.6f;
            float arc;
            if (ModalUtils.isShadowAndRoundBorderSupport()) {
                arc = Math.min(scale(innerRound, scaleFactor), height) / 2f;
            } else {
                arc = 0;
            }

            // create background style
            ToastPanel.ThemesData themesData = toastData.getThemes();
            Color defaultColor = themesData.getColor();
            if (style.getBackgroundType() == ToastStyle.BackgroundType.GRADIENT) {
                Color color = ColorFunctions.mix(defaultColor, c.getBackground(), 0.3f);
                float start = ltr ? 0 : (width) * 0.8f;
                float end = ltr ? (width) * 0.8f : 0;
                g.setPaint(new GradientPaint(start, 0, color, end, 0, c.getBackground()));
            } else {
                g.setColor(c.getBackground());
            }
            Shape shapeBackground = FlatUIUtils.createRoundRectanglePath(lx, ly, bw, bh, arc, arc, arc, arc);
            g.fill(shapeBackground);

            // create border style
            ToastBorderStyle.BorderType borderType = borderStyle.getBorderType();
            if (borderType != ToastBorderStyle.BorderType.NONE && borderType != ToastBorderStyle.BorderType.OUTLINE) {
                float lineWidth = scale(borderStyle.getLineSize(), scaleFactor);
                Area area = new Area(shapeBackground);
                if ((borderType == ToastBorderStyle.BorderType.LEADING_LINE && ltr) || borderType == ToastBorderStyle.BorderType.TRAILING_LINE && !ltr) {
                    area.intersect(new Area(new Rectangle2D.Float(lx, ly, lineWidth, bh)));
                } else if ((borderType == ToastBorderStyle.BorderType.TRAILING_LINE && ltr) || (borderType == ToastBorderStyle.BorderType.LEADING_LINE && !ltr)) {
                    area.intersect(new Area(new Rectangle2D.Float(lx + bw - lineWidth, ly, lineWidth, bh)));
                } else if (borderType == ToastBorderStyle.BorderType.TOP_LINE) {
                    area.intersect(new Area(new Rectangle2D.Float(lx, ly, bw, lineWidth)));
                } else {
                    area.intersect(new Area(new Rectangle2D.Float(lx, ly + bh - lineWidth, bw, lineWidth)));
                }
                Color color = ColorFunctions.mix(defaultColor, c.getBackground(), 0.6f);
                g.setColor(color);
                g.fill(area);
            }
        } finally {
            g.dispose();
        }
        g2d.drawImage(image, x, y, null);
    }

    private int scale(int value, double scaleFactor) {
        return (int) Math.ceil(UIScale.scale(value) * scaleFactor);
    }

    private float scale(float value, double scaleFactor) {
        return (float) (UIScale.scale(value) * scaleFactor);
    }
}
