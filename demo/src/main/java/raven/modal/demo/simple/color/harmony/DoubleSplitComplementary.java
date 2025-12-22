package raven.modal.demo.simple.color.harmony;

import raven.modal.demo.simple.color.AbstractColorHarmony;

public class DoubleSplitComplementary extends AbstractColorHarmony {

    public DoubleSplitComplementary() {
        super("Double Split Complementary");
    }

    @Override
    public float[] getAngles(float selectedAngle) {
        int diff = 30;
        return new float[]{
                selectedAngle + diff,
                selectedAngle - diff,
                selectedAngle - 180 + diff,
                selectedAngle + 180 - diff
        };
    }

    @Override
    public int getSelectedColorIndex() {
        return 1;
    }
}
