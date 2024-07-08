package test.alerts.animator;

import com.formdev.flatlaf.util.Animator;

public abstract class EasingInterpolator implements Animator.Interpolator {

    public static final EasingInterpolator EASE_OUT_BOUNCE = new EaseOutBounce();


    private static class EaseOutBounce extends EasingInterpolator {

        @Override
        public float interpolate(float f) {
            float n1 = 7.5625f;
            float d1 = 2.75f;
            double v;
            if (f < 1 / d1) {
                v = n1 * f * f;
            } else if (f < 2 / d1) {
                v = n1 * (f -= 1.5 / d1) * f + 0.75;
            } else if (f < 2.5 / d1) {
                v = n1 * (f -= 2.25 / d1) * f + 0.9375;
            } else {
                v = n1 * (f -= 2.625 / d1) * f + 0.984375;
            }
            return (float) v;
        }
    }

}
