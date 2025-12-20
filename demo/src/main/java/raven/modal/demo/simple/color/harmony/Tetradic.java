package raven.modal.demo.simple.color.harmony;

import raven.color.component.ColorLocation;
import raven.modal.demo.simple.color.ColorHarmony;
import raven.modal.demo.simple.color.ColorWheelHarmoniesModel;

import java.awt.*;
import java.awt.geom.Point2D;

public class Tetradic extends ColorHarmony {

    public Tetradic() {
        super("Tetradic");
    }

    @Override
    public Point2D.Float[] toLocation(ColorWheelHarmoniesModel model, Color color, int width, int height) {
        ColorLocation location = model.colorToLocation(color);
        float x = (location.getX() * width);
        float y = location.getY() * height;
        float selectedAngle = locationToAngle(x, y, width, height);
        int diff = 90;
        float angle1 = selectedAngle - diff;
        float angle2 = angle1 - diff;
        float angle3 = angle2 - diff;

        float radius = locationToRadius(x, y, width, height);

        return new Point2D.Float[]{
                angleToLocation(angle1, radius, width, height),
                angleToLocation(angle2, radius, width, height),
                angleToLocation(angle3, radius, width, height)
        };
    }

    @Override
    public int getSelectedColorIndex() {
        return 0;
    }
}
