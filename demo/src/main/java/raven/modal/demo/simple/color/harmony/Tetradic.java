package raven.modal.demo.simple.color.harmony;

import raven.modal.demo.simple.color.AbstractColorHarmony;

public class Tetradic extends AbstractColorHarmony {

    public Tetradic() {
        super("Tetradic");
    }

    @Override
    public float[] getAngles(float selectedAngle) {
        int diff = 90;
        return new float[]{
                selectedAngle - diff,
                selectedAngle - diff * 2,
                selectedAngle - diff * 3
        };
    }

    @Override
    public int getSelectedColorIndex() {
        return 0;
    }
}
