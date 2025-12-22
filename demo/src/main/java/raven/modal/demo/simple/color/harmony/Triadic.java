package raven.modal.demo.simple.color.harmony;

import raven.modal.demo.simple.color.AbstractColorHarmony;

public class Triadic extends AbstractColorHarmony {

    public Triadic() {
        super("Triadic");
    }

    @Override
    public float[] getAngles(float selectedAngle) {
        int diff = 120;
        return new float[]{
                selectedAngle - diff,
                selectedAngle + diff
        };
    }

    @Override
    public int getSelectedColorIndex() {
        return 0;
    }
}
