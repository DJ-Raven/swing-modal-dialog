package raven.modal.toast.icon;

import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.Animator;
import raven.modal.toast.PromiseIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

/**
 * @author Raven
 */
public class RollingIcon extends PromiseIcon {

    private Color borderColor;
    private Color color;

    public RollingIcon(int size) {
        super(() -> false, size, size);
    }

    public RollingIcon(PromiseIcon.PromiseIconAction promise, int size) {
        super(promise, size, size);
    }

    @Override
    protected void paintAnimatedIcon(Component c, Graphics g, int x, int y, float animate) {
        float width = getIconWidth();
        float height = getIconHeight();
        float border = width * 0.1f;
        float lx = border;
        float ly = border;
        float w = width - border * 2;
        float h = height - border * 2;
        Graphics2D g2 = (Graphics2D) g.create();
        FlatUIUtils.setRenderingHints(g2);

        g2.translate(x, y);

        Area area = new Area(new Ellipse2D.Float(0, 0, width, height));
        area.subtract(new Area(new Ellipse2D.Float(lx, ly, w, h)));
        g2.setColor(borderColor());
        g2.fill(area);
        if (getAnimator() != null) {
            area.intersect(new Area(new Arc2D.Float(0, 0, width, height, -360 * animate, 70, Arc2D.PIE)));
            g2.setColor(accentColor());
            g2.fill(area);
        }
        g2.dispose();
    }

    private Color borderColor() {
        if (borderColor == null) {
            return UIManager.getColor("Component.borderColor");
        }
        return borderColor;
    }

    private Color accentColor() {
        if (color == null) {
            return UIManager.getColor("Component.accentColor");
        }
        return color;
    }

    @Override
    protected Animator.Interpolator getAnimationInterpolator() {
        return null;
    }

    @Override
    protected int getAnimationDuration() {
        return 600;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
