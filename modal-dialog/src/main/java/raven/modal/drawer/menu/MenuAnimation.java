package raven.modal.drawer.menu;

import com.formdev.flatlaf.util.Animator;
import com.formdev.flatlaf.util.CubicBezierEasing;

/**
 * @author Raven
 */
public class MenuAnimation {

    private final DrawerMenu.SubMenuItem subMenuItem;

    public MenuAnimation(DrawerMenu.SubMenuItem subMenuItem) {
        this.subMenuItem = subMenuItem;
    }

    public void run(final boolean start) {
        Animator animator = new Animator(250, new Animator.TimingTarget() {
            @Override
            public void timingEvent(float v) {
                float f = start ? v : 1f - v;
                subMenuItem.setAnimate(f);
            }
        });
        animator.setInterpolator(CubicBezierEasing.EASE_IN_OUT);
        animator.start();
    }
}
