package raven.modal.toast;

import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.ColorFunctions;
import com.formdev.flatlaf.util.UIScale;
import raven.modal.toast.option.ToastBorderStyle;
import raven.modal.toast.option.ToastStyle;
import raven.modal.utils.ModalUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * @author Raven
 */
class ToastContent extends JComponent {

    private final ToastPanel.ToastData toastData;

    protected ToastContent(LayoutManager layout, ToastPanel.ToastData toastData) {
        this.toastData = toastData;
        setLayout(layout);
        setOpaque(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Container parent = getParent();
        if (parent == null) {
            return;
        }
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            FlatUIUtils.setRenderingHints(g2);
            g2.setColor(parent.getBackground());
            g2.fill(new Rectangle2D.Double(0, 0, getWidth(), getHeight()));

            if (toastData.getOption().getStyle().getBackgroundType() == ToastStyle.BackgroundType.GRADIENT) {
                ToastPanel.ThemesData themesData = toastData.getThemes();
                ToastBorderStyle.BorderType borderType = toastData.getOption().getStyle().getBorderStyle().getBorderType();
                boolean ltr = getComponentOrientation().isLeftToRight();
                float arc;
                if (ModalUtils.isShadowAndRoundBorderSupport()) {
                    arc = UIScale.scale(toastData.getOption().getStyle().getBorderStyle().getRound() * 0.6f) / 2f;
                } else {
                    arc = 0;
                }

                float topLeft = 0;
                float topRight = 0;
                float bottomLeft = 0;
                float bottomRight = 0;
                if (arc > 0) {
                    if (borderType == ToastBorderStyle.BorderType.NONE || borderType == ToastBorderStyle.BorderType.OUTLINE) {
                        topLeft = topRight = bottomLeft = bottomRight = arc;
                    } else if ((borderType == ToastBorderStyle.BorderType.LEADING_LINE && ltr) || borderType == ToastBorderStyle.BorderType.TRAILING_LINE && !ltr) {
                        topRight = bottomRight = arc;
                    } else if ((borderType == ToastBorderStyle.BorderType.TRAILING_LINE && ltr) || (borderType == ToastBorderStyle.BorderType.LEADING_LINE && !ltr)) {
                        topLeft = bottomLeft = arc;
                    } else if (borderType == ToastBorderStyle.BorderType.TOP_LINE) {
                        bottomLeft = bottomRight = arc;
                    } else {
                        topLeft = topRight = arc;
                    }
                }
                // gradient paint base on the parent component
                Rectangle rec = FlatUIUtils.addInsets(new Rectangle(0, 0, getWidth(), getHeight()), parent.getInsets());
                Color color = ColorFunctions.mix(themesData.getColor(), parent.getBackground(), 0.3f);
                float start = ltr ? rec.x : (rec.x + rec.width) * 0.8f;
                float end = ltr ? (rec.x + rec.width) * 0.8f : rec.x;
                g2.setPaint(new GradientPaint(start, 0, color, end, 0, parent.getBackground()));
                Shape shape;
                if (topLeft == topRight && topLeft == bottomLeft && topLeft == bottomRight) {
                    shape = FlatUIUtils.createComponentRectangle(0, 0, getWidth(), getHeight(), topLeft * 2f);
                } else {
                    shape = FlatUIUtils.createRoundRectanglePath(0, 0, getWidth(), getHeight(), topLeft, topRight, bottomLeft, bottomRight);
                }
                g2.fill(shape);
            }
        } finally {
            g2.dispose();
        }
        super.paintComponent(g);
    }
}
