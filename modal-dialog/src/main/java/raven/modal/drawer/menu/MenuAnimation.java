package raven.modal.drawer.menu;

import com.formdev.flatlaf.util.Animator;
import com.formdev.flatlaf.util.CubicBezierEasing;

/**
 * @author Raven
 */
public class MenuAnimation {

    private final DrawerMenu.SubMenuItem subMenuItem;
    private final int DURATION = 250;
    private Animator animator;
    private boolean start;
    private float startFraction;
    private float fraction;

    public MenuAnimation(DrawerMenu.SubMenuItem subMenuItem) {
        this.subMenuItem = subMenuItem;
    }

    public void run(final boolean start) {
        this.start = start;
        if (animator == null) {
            animator = new Animator(DURATION, v -> {
                float value = startFraction + ((1f - startFraction) * v);
                fraction = value;
                float f = this.start ? value : 1f - value;
                subMenuItem.setAnimate(f);
            });
            animator.setInterpolator(CubicBezierEasing.EASE_IN_OUT);
        }
        int duration = DURATION;
        startFraction = 0;
        if (animator.isRunning()) {
            animator.stop();
            duration = (int) (fraction * DURATION);
            startFraction = 1f - fraction;
        }
        animator.setDuration(duration);
        animator.start();
    }
}
