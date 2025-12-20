package raven.modal.demo.simple.color.harmony;

import raven.color.component.ColorLocation;
import raven.modal.demo.simple.color.ColorHarmony;
import raven.modal.demo.simple.color.ColorWheelHarmoniesModel;

import java.awt.*;
import java.awt.geom.Point2D;

public class Analogous extends ColorHarmony {

    public Analogous() {
        super("Analogous");
    }

    @Override
    public Point2D.Float[] toLocation(ColorWheelHarmoniesModel model, Color color, int width, int height) {
        ColorLocation location = model.colorToLocation(color);
        float x = (location.getX() * width);
        float y = location.getY() * height;
        float selectedAngle = locationToAngle(x, y, width, height);
        int diff = 30;
        float leftAngle = selectedAngle + diff;
        float rightAngle = selectedAngle - diff;
        float radius = locationToRadius(x, y, width, height);

        return new Point2D.Float[]{
                angleToLocation(leftAngle, radius, width, height),
                angleToLocation(rightAngle, radius, width, height)
        };
    }

    @Override
    public int getSelectedColorIndex() {
        return 1;
    }
}
