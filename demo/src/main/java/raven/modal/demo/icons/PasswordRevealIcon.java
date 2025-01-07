package raven.modal.demo.icons;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.util.AnimatedIcon;
import com.formdev.flatlaf.util.UIScale;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;

public class PasswordRevealIcon implements AnimatedIcon {

    private final Icon icon;
    private final int space;
    private final Color color;

    public PasswordRevealIcon() {
        this(new FlatSVGIcon("raven/modal/demo/icons/eye.svg", 0.75f),
                UIManager.getColor("PasswordField.revealIconColor"), 3);
    }

    public PasswordRevealIcon(FlatSVGIcon icon, Color color, int space) {
        this.icon = icon;
        this.color = color;
        this.space = space;
        icon.setColorFilter(new FlatSVGIcon.ColorFilter(color1 -> color));
    }

    @Override
    public void paintIconAnimated(Component c, Graphics g, int x, int y, float animatedValue) {
        Graphics2D g2 = (Graphics2D) g.create();
        int s = UIScale.scale(space);
        icon.paintIcon(c, g2, x, y);
        if (animatedValue > 0) {
            float startX = x + s;
            float startY = y + getIconHeight() - s;

            float endX = x + getIconWidth() - s;
            float endY = y + s;

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

            Shape shape = new Line2D.Float(startX, startY, startX + (endX - startX) * animatedValue, startY + (endY - startY) * animatedValue);

            drawLine(g2, shape, c.getParent().getBackground(), 4f);
            drawLine(g2, shape, color, 1.2f);
        }
        g2.dispose();
    }

    private void drawLine(Graphics2D g2, Shape shape, Color color, float size) {
        g2.setColor(color);
        g2.setStroke(new BasicStroke(UIScale.scale(size), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.draw(shape);
    }

    @Override
    public float getValue(Component c) {
        return ((AbstractButton) c).isSelected() ? 0 : 1;
    }

    @Override
    public int getIconWidth() {
        return icon.getIconWidth();
    }

    @Override
    public int getIconHeight() {
        return icon.getIconHeight();
    }
}