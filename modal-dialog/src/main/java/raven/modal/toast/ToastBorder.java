package raven.modal.toast;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.ui.FlatEmptyBorder;
import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.ColorFunctions;
import com.formdev.flatlaf.util.UIScale;
import raven.modal.toast.option.ToastBorderStyle;
import raven.modal.toast.option.ToastStyle;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * @author Raven
 */
public class ToastBorder extends FlatEmptyBorder {

    private ToastPanel.ToastData toastData;

    public ToastBorder(ToastPanel.ToastData toastData) {
        this.toastData = toastData;
    }

    private Insets getStyleBorderInsets(Component c) {
        ToastBorderStyle style = toastData.getOption().getStyle().getBorderStyle();
        if (style.getBorderType() != ToastBorderStyle.BorderType.NONE) {
            if (style.getBorderType() == ToastBorderStyle.BorderType.OUTLINE) {
                int line = UIScale.scale(style.getBorderWidth());
                return new Insets(line, line, line, line);
            } else {
                int line = UIScale.scale(style.getLineSize());
                boolean ltr = c.getComponentOrientation().isLeftToRight();
                int top = 0;
                int left = 0;
                int bottom = 0;
                int right = 0;
                if ((style.getBorderType() == ToastBorderStyle.BorderType.LEADING_LINE && ltr) || (style.getBorderType() == ToastBorderStyle.BorderType.TRAILING_LINE && !ltr)) {
                    left = line;
                } else if ((style.getBorderType() == ToastBorderStyle.BorderType.TRAILING_LINE && ltr) || (style.getBorderType() == ToastBorderStyle.BorderType.LEADING_LINE && !ltr)) {
                    right = line;
                } else if (style.getBorderType() == ToastBorderStyle.BorderType.TOP_LINE) {
                    top = line;
                } else {
                    bottom = line;
                }
                return new Insets(top, left, bottom, right);
            }
        }
        return null;
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
        Graphics2D g2 = null;
        try {
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            g2 = image.createGraphics();
            FlatUIUtils.setRenderingHints(g2);
            ToastStyle style = toastData.getOption().getStyle();
            ToastBorderStyle borderStyle = style.getBorderStyle();
            boolean ltr = c.getComponentOrientation().isLeftToRight();

            int lx = 0;
            int ly = 0;
            int bw = width;
            int bh = height;
            float innerRound = style.getBorderStyle().getRound() * 0.6f;
            float arc = Math.min(UIScale.scale(innerRound), height) / 2f;

            // create background style
            ToastPanel.ThemesData themesData = toastData.getThemes();
            Color defaultColor = Color.decode(FlatLaf.isLafDark() ? themesData.getColors()[1] : themesData.getColors()[0]);
            if (style.getBackgroundType() == ToastStyle.BackgroundType.GRADIENT) {
                Color color = ColorFunctions.mix(defaultColor, c.getBackground(), 0.3f);
                float start = ltr ? 0 : (width) * 0.8f;
                float end = ltr ? (width) * 0.8f : 0;
                g2.setPaint(new GradientPaint(start, 0, color, end, 0, c.getBackground()));
            } else {
                g2.setColor(c.getBackground());
            }
            Shape shapeBackground = FlatUIUtils.createRoundRectanglePath(lx, ly, bw, bh, arc, arc, arc, arc);
            g2.fill(shapeBackground);

            // create border style
            if (borderStyle.getBorderType() != ToastBorderStyle.BorderType.NONE) {
                Color color = ColorFunctions.mix(defaultColor, c.getBackground(), 0.6f);
                g2.setColor(color);
                if (borderStyle.getBorderType() == ToastBorderStyle.BorderType.OUTLINE) {
                    float lineWidth = UIScale.scale(borderStyle.getBorderWidth());
                    g2.fill(FlatUIUtils.createRoundRectangle(lx, ly, bw, bh, lineWidth, arc, arc, arc, arc));
                } else {
                    float lineWidth = UIScale.scale(borderStyle.getLineSize());
                    Area area = new Area(shapeBackground);
                    if ((borderStyle.getBorderType() == ToastBorderStyle.BorderType.LEADING_LINE && ltr) || borderStyle.getBorderType() == ToastBorderStyle.BorderType.TRAILING_LINE && !ltr) {
                        area.intersect(new Area(new Rectangle2D.Float(lx, ly, lineWidth, bh)));
                    } else if ((borderStyle.getBorderType() == ToastBorderStyle.BorderType.TRAILING_LINE && ltr) || (borderStyle.getBorderType() == ToastBorderStyle.BorderType.LEADING_LINE && !ltr)) {
                        area.intersect(new Area(new Rectangle2D.Float(lx + bw - lineWidth, ly, lineWidth, bh)));
                    } else if (borderStyle.getBorderType() == ToastBorderStyle.BorderType.TOP_LINE) {
                        area.intersect(new Area(new Rectangle2D.Float(lx, ly, bw, lineWidth)));
                    } else {
                        area.intersect(new Area(new Rectangle2D.Float(lx, ly + bh - lineWidth, bw, lineWidth)));
                    }
                    g2.fill(area);
                }
            }
            g.drawImage(image, x, y, null);
        } finally {
            if (g2 != null) {
                g2.dispose();
            }
        }
    }
}
