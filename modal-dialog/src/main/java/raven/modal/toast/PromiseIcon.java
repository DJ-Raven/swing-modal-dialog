package raven.modal.toast;

import com.formdev.flatlaf.util.Animator;
import com.formdev.flatlaf.util.CubicBezierEasing;
import com.formdev.flatlaf.util.UIScale;

import javax.swing.*;
import java.awt.*;

/**
 * @author Raven
 */
public abstract class PromiseIcon implements Icon {

    private final PromiseIconAction promise;
    private final int width;
    private final int height;
    private Animator animator;
    private float animate;
    private Component target;
    private boolean stopped;

    public PromiseIcon(PromiseIconAction promise, int width, int height) {
        this.promise = promise;
        this.width = width;
        this.height = height;
    }

    protected int getAnimationDuration() {
        return 500;
    }

    protected Animator.Interpolator getAnimationInterpolator() {
        return CubicBezierEasing.STANDARD_EASING;
    }

    protected int getAnimationResolution() {
        return 10;
    }

    protected final Animator getAnimator() {
        return animator;
    }

    public void start() {
        if (animator == null) {
            animator = new Animator(getAnimationDuration(), new Animator.TimingTarget() {
                @Override
                public void timingEvent(float v) {
                    animate = v;
                    if (target != null) {
                        target.repaint();
                    }
                }

                @Override
                public void end() {
                    if (!promise.isDone() && !stopped) {
                        SwingUtilities.invokeLater(() -> {
                            if (animator != null && !animator.isRunning()) {
                                animator.start();
                            }
                        });
                    }
                }
            });
            animator.setInterpolator(getAnimationInterpolator());
            animator.setResolution(getAnimationResolution());
        }
        if (animator.isRunning()) {
            animator.stop();
        }
        stopped = false;
        animator.start();
    }

    public void stop() {
        if (animator != null && animator.isRunning()) {
            stopped = true;
            animator.stop();
        }
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        if (c != target) {
            target = c;
        }
        paintAnimatedIcon(c, g, x, y, animate);
    }

    protected abstract void paintAnimatedIcon(Component c, Graphics g, int x, int y, float animate);

    @Override
    public int getIconWidth() {
        return UIScale.scale(width);
    }

    @Override
    public int getIconHeight() {
        return UIScale.scale(height);
    }

    public interface PromiseIconAction {

        boolean isDone();
    }
}
