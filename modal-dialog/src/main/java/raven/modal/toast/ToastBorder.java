package raven.modal.toast;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.ui.FlatDropShadowBorder;
import com.formdev.flatlaf.ui.FlatEmptyBorder;
import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.ColorFunctions;
import com.formdev.flatlaf.util.UIScale;
import raven.modal.toast.option.ToastStyle;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

/**
 * @author Raven
 */
class ToastBorder extends FlatEmptyBorder {

    private static int SHADOW_SIZE = 10;
    private Border shadowBorder;
    private Component component;
    private ToastPanel.ToastData toastData;

    protected ToastBorder(Component component, ToastPanel.ToastData toastData) {
        super(SHADOW_SIZE, SHADOW_SIZE, SHADOW_SIZE, SHADOW_SIZE);
        this.component = component;
        this.toastData = toastData;
        shadowBorder = new FlatDropShadowBorder(
                UIManager.getColor("Popup.dropShadowColor"), SHADOW_SIZE,
                FlatUIUtils.getUIFloat("Popup.dropShadowOpacity", 0.5f));
    }

    private Insets getStyleBorderInsets(Component c) {
        ToastStyle style = toastData.getOption().getStyle();
        if (style.getBorderType() != ToastStyle.BorderType.NONE) {
            if (style.getBorderType() == ToastStyle.BorderType.OUTLINE) {
                int line = UIScale.scale(style.getOutlineSize());
                return new Insets(line, line, line, line);
            } else {
                int line = UIScale.scale(style.getLineSize());
                boolean ltr = c.getComponentOrientation().isLeftToRight();
                int top = 0;
                int left = 0;
                int bottom = 0;
                int right = 0;
                if ((style.getBorderType() == ToastStyle.BorderType.LEADING_LINE && ltr) || (style.getBorderType() == ToastStyle.BorderType.TRAILING_LINE && !ltr)) {
                    left = line;
                } else if ((style.getBorderType() == ToastStyle.BorderType.TRAILING_LINE && ltr) || (style.getBorderType() == ToastStyle.BorderType.LEADING_LINE && !ltr)) {
                    right = line;
                } else if (style.getBorderType() == ToastStyle.BorderType.TOP_LINE) {
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
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            FlatUIUtils.setRenderingHints(g2);
            if (shadowBorder != null) {
                shadowBorder.paintBorder(c, g2, x, y, width, height);
            }

            ToastStyle style = toastData.getOption().getStyle();
            boolean ltr = c.getComponentOrientation().isLeftToRight();

            int s = (int) UIScale.scale(SHADOW_SIZE * 0.8f);
            int bw = width - s * 2;
            int bh = height - s * 2;
            float arc = UIScale.scale(SHADOW_SIZE) / 2;

            // create background style
            ToastPanel.ThemesData themesData = toastData.getThemes();
            Color defaultColor = Color.decode(FlatLaf.isLafDark() ? themesData.getColors()[1] : themesData.getColors()[0]);
            if (style.getBackgroundType() == ToastStyle.BackgroundType.GRADIENT) {
                Color color = ColorFunctions.mix(defaultColor, component.getBackground(), 0.3f);
                g2.setPaint(new GradientPaint(x, y, color, (x + width) * 0.8f, y, component.getBackground()));
            } else {
                g2.setColor(component.getBackground());
            }
            Shape shapeBackground = FlatUIUtils.createRoundRectanglePath(s, s, bw, bh, arc, arc, arc, arc);
            g2.fill(shapeBackground);

            // create border style
            if (style.getBorderType() != ToastStyle.BorderType.NONE) {
                Color color = ColorFunctions.mix(defaultColor, component.getBackground(), 0.6f);
                g2.setColor(color);
                if (style.getBorderType() == ToastStyle.BorderType.OUTLINE) {
                    float lineWidth = UIScale.scale(style.getOutlineSize());
                    g2.fill(FlatUIUtils.createRoundRectangle(s, s, bw, bh, lineWidth, arc, arc, arc, arc));
                } else {
                    float lineWidth = UIScale.scale(style.getLineSize());
                    Area area = new Area(shapeBackground);
                    if ((style.getBorderType() == ToastStyle.BorderType.LEADING_LINE && ltr) || style.getBorderType() == ToastStyle.BorderType.TRAILING_LINE && !ltr) {
                        area.intersect(new Area(new Rectangle2D.Float(s, s, lineWidth, bh)));
                    } else if ((style.getBorderType() == ToastStyle.BorderType.TRAILING_LINE && ltr) || (style.getBorderType() == ToastStyle.BorderType.LEADING_LINE && !ltr)) {
                        area.intersect(new Area(new Rectangle2D.Float(s + bw - lineWidth, s, lineWidth, bh)));
                    } else if (style.getBorderType() == ToastStyle.BorderType.TOP_LINE) {
                        area.intersect(new Area(new Rectangle2D.Float(s, s, bw, lineWidth)));
                    } else {
                        area.intersect(new Area(new Rectangle2D.Float(s, s + bh - lineWidth, bw, lineWidth)));
                    }
                    g2.fill(area);
                }
            }
        } finally {
            g2.dispose();
        }
    }
}
