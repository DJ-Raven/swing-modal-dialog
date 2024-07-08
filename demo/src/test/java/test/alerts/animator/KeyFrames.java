package test.alerts.animator;

import com.formdev.flatlaf.util.Animator;

public class KeyFrames implements Animator.Interpolator {

    private final EvaluatorFloat evaluator = new EvaluatorFloat();
    private final float values[];

    public KeyFrames(float... values) {
        if (values.length == 0) {
            throw new IllegalArgumentException("number of array can't be empty");
        } else if (values.length == 1) {
            throw new IllegalArgumentException("number of array must be > 1");
        }
        this.values = values;
    }

    @Override
    public float interpolate(float fraction) {
        float frame = fraction * (values.length - 1);
        int index = (int) frame;
        if (index >= values.length - 1) {
            return values[values.length - 1];
        }
        float time = frame - (float) index;
        float from = values[index];
        float target = values[index + 1];
        return evaluator.evaluate(from, target, time);
    }
}
