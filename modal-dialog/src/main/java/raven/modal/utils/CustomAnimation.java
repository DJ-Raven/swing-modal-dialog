package raven.modal.utils;

import com.formdev.flatlaf.util.Animator;
import com.formdev.flatlaf.util.CubicBezierEasing;

/**
 * @author Raven
 */
public class CustomAnimation {

    private final AnimationChanged animationChanged;
    private final int DURATION;
    private Animator animator;
    private boolean start;
    private float startFraction;
    private float fraction;

    public CustomAnimation(AnimationChanged animationChanged) {
        this(250, animationChanged);
    }

    public CustomAnimation(int duration, AnimationChanged animationChanged) {
        this.DURATION = duration;
        this.animationChanged = animationChanged;
    }

    public void run(final boolean start) {
        if (this.start == start) return;

        this.start = start;
        if (animator == null) {
            animator = new Animator(DURATION, v -> {
                float value = startFraction + ((1f - startFraction) * v);
                fraction = value;
                float f = this.start ? value : 1f - value;
                animationChanged.changed(f);
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
        if (duration > 0) {
            animator.setDuration(duration);
            animator.start();
        }
    }

    public Animator getAnimator() {
        return animator;
    }

    private float getFraction() {
        return fraction;
    }

    public interface AnimationChanged {

        void changed(float animate);
    }
}
