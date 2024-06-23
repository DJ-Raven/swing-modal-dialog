package raven.modal.demo.component;

import com.formdev.flatlaf.util.Animator;
import com.formdev.flatlaf.util.CubicBezierEasing;
import com.formdev.flatlaf.util.UIScale;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RefreshLine extends JPanel {

    private Animator animator;
    private float animate;

    public RefreshLine() {
        init();
    }

    private void init() {
        animator = new Animator(500, new Animator.TimingTarget() {
            @Override
            public void timingEvent(float v) {
                animate = v;
                RefreshLine.this.repaint();
            }

            @Override
            public void end() {
                animate = 0f;
                repaint();
            }
        });
        animator.setInterpolator(CubicBezierEasing.EASE_OUT);
    }

    public void refresh() {
        if (animator.isRunning()) {
            animator.stop();
        }
        animate = 0f;
        animator.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        float pad = UIScale.scale(5f);
        float width = getWidth() - (pad * 2);
        float height = getHeight();
        g2.setColor(UIManager.getColor("Component.accentColor"));
        g2.setComposite(AlphaComposite.SrcOver.derive(0.5f));
        g2.fill(new RoundRectangle2D.Float(pad, 0, width * animate, height, height, height));
        g2.dispose();
    }
}
