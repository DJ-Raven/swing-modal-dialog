package raven.modal.toast.icon;

import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.Animator;
import com.formdev.flatlaf.util.UIScale;
import raven.modal.toast.PromiseIcon;
import raven.modal.toast.ToastPromise;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

/**
 * @author Raven
 */
public class RollingIcon extends PromiseIcon {

    public RollingIcon(ToastPromise promise, int size) {
        super(promise, size, size);
    }

    @Override
    protected void paintAnimatedIcon(Component c, Graphics g, int x, int y, float animate) {
        float margin = UIScale.scale(2f);
        float width = getIconWidth() - margin * 2;
        float height = getIconHeight() - margin * 2;
        float border = width * 0.1f;
        float lx = margin + border;
        float ly = margin + border;
        float w = width - border * 2;
        float h = height - border * 2;
        Graphics2D g2 = (Graphics2D) g.create();
        FlatUIUtils.setRenderingHints(g2);

        Area area = new Area(new Ellipse2D.Float(margin, margin, width, height));
        area.subtract(new Area(new Ellipse2D.Float(lx, ly, w, h)));
        g2.setColor(UIManager.getColor("Component.borderColor"));
        g2.fill(area);
        if (getAnimator() != null) {
            area.intersect(new Area(new Arc2D.Float(margin, margin, width, height, -360 * animate, 70, Arc2D.PIE)));
            g2.setColor(UIManager.getColor("Component.accentColor"));
            g2.fill(area);
        }
        g2.dispose();
    }

    @Override
    protected Animator.Interpolator getAnimationInterpolator() {
        return null;
    }

    @Override
    protected int getAnimationDuration() {
        return 600;
    }
}
