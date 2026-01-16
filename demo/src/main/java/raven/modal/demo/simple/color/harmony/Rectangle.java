package raven.modal.demo.simple.color.harmony;

import raven.color.utils.ColorLocation;
import raven.modal.demo.simple.color.AbstractColorHarmony;
import raven.modal.demo.simple.color.ColorWheelHarmoniesModel;

import java.awt.*;
import java.awt.geom.Point2D;

public class Rectangle extends AbstractColorHarmony {

    public Rectangle() {
        super("Rectangle");
    }

    @Override
    public Point2D.Float[] toLocation(ColorWheelHarmoniesModel model, Color color) {
        ColorLocation location = model.colorToLocation(color);
        float x = location.getX();
        float y = location.getY();
        float selectedAngle = locationToAngle(x, y);
        int diff = 60;
        float angle1 = selectedAngle - diff;
        float angle2 = angle1 - 180 + diff;
        float angle3 = angle2 - diff;

        float radius = locationToRadius(x, y);

        return new Point2D.Float[]{
                angleToLocation(angle1, radius),
                angleToLocation(angle2, radius),
                angleToLocation(angle3, radius)
        };
    }

    @Override
    public float[] getAngles(float selectedAngle) {
        int diff = 60;
        return new float[]{
                selectedAngle - diff,
                selectedAngle - 180,
                selectedAngle + 180 - diff
        };
    }

    @Override
    public int getSelectedColorIndex() {
        return 0;
    }
}
