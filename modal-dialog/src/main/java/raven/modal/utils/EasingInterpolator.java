package raven.modal.utils;

/**
 * Easing functions. https://easings.net/
 *
 * @author Raven
 */
public class EasingInterpolator {

    public static float easeOutBack(float fraction) {
        double c1 = 1.70158f;
        double c3 = c1 + 1;

        double value = 1 + c3 * Math.pow(fraction - 1, 3) + c1 * Math.pow(fraction - 1, 2);
        return (float) value;
    }

    public static float easeInOutBack(float fraction) {
        double c1 = 1.70158;
        double c2 = c1 * 1.525;

        double value = fraction < 0.5
                ? (Math.pow(2 * fraction, 2) * ((c2 + 1) * 2 * fraction - c2)) / 2
                : (Math.pow(2 * fraction - 2, 2) * ((c2 + 1) * (fraction * 2 - 2) + c2) + 2) / 2;
        return (float) value;
    }

    public static float easeOutExpo(float fraction) {
        double value = fraction == 1 ? 1 : 1 - Math.pow(2, -10 * fraction);
        return (float) value;
    }

    public static float easeOutElastic(float fraction) {
        double c4 = (2 * Math.PI) / 3;

        double value = fraction == 0
                ? 0
                : fraction == 1
                ? 1
                : Math.pow(2, -10 * fraction) * Math.sin((fraction * 10 - 0.75) * c4) + 1;
        return (float) value;
    }

    public static float easeOutBounce(float fraction) {
        float n1 = 7.5625f;
        float d1 = 2.75f;

        double value;
        if (fraction < 1 / d1) {
            value = n1 * fraction * fraction;
        } else if (fraction < 2 / d1) {
            value = n1 * (fraction -= 1.5f / d1) * fraction + 0.75;
        } else if (fraction < 2.5 / d1) {
            value = n1 * (fraction -= 2.25f / d1) * fraction + 0.9375;
        } else {
            value = n1 * (fraction -= 2.625f / d1) * fraction + 0.984375;
        }
        return (float) value;
    }
}
