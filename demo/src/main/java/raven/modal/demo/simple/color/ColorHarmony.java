package raven.modal.demo.simple.color;

import java.awt.*;
import java.awt.geom.Point2D;

public interface ColorHarmony {

    Color[] getSelectedColor(ColorWheelHarmoniesModel model);

    Point2D.Float[] toLocation(ColorWheelHarmoniesModel model, Color color);
}
