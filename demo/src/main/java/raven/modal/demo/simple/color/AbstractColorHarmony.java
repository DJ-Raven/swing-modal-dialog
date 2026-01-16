package raven.modal.demo.simple.color;

import raven.color.utils.ColorLocation;

import java.awt.*;
import java.awt.geom.Point2D;

public abstract class AbstractColorHarmony implements ColorHarmony {

    private final String name;

    public AbstractColorHarmony(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }

    public abstract float[] getAngles(float selectedAngle);

    public abstract int getSelectedColorIndex();

    public float locationToAngle(float x, float y) {
        float dx = x - 0.5f;
        float dy = 0.5f - y;
        float angle = (float) Math.toDegrees(Math.atan2(dy, dx));
        return angle < 0 ? angle + 360 : angle;
    }

    public Point2D.Float angleToLocation(float angle, float radius) {
        float rad = (float) Math.toRadians(angle);
        float x = (float) (0.5f + Math.cos(rad) * radius);
        float y = (float) (0.5f - Math.sin(rad) * radius);
        return new Point2D.Float(x, y);
    }

    public float locationToRadius(float x, float y) {
        float dx = x - 0.5f;
        float dy = y - 0.5f;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public Color[] getSelectedColor(ColorWheelHarmoniesModel model) {
        Color selectedColor = model.getSelectedColor();
        Point2D.Float[] locations = toLocation(model, selectedColor);
        Color[] colors = new Color[locations.length + 1];
        int alpha = selectedColor.getAlpha();
        int colorIndex = getSelectedColorIndex();
        int indexAdd = 0;
        for (int i = 0; i < locations.length; i++) {
            if (colorIndex >= 0 && i == colorIndex) {
                colors[i] = selectedColor;
                indexAdd++;
            }
            Point2D.Float l = locations[i];
            Color c = model.locationToColor(new ColorLocation(l.x, l.y), model.getValue());
            colors[i + indexAdd] = applyAlpha(c, alpha);
        }
        if (indexAdd == 0 && colorIndex < colors.length) {
            colors[colorIndex] = selectedColor;
        }
        return colors;
    }

    @Override
    public Point2D.Float[] toLocation(ColorWheelHarmoniesModel model, Color color) {
        ColorLocation location = model.colorToLocation(color);
        float x = location.getX();
        float y = location.getY();
        float selectedAngle = locationToAngle(x, y);
        float radius = locationToRadius(x, y);
        float[] angles = getAngles(selectedAngle);
        Point2D.Float[] anglePoints = new Point2D.Float[angles.length];
        for (int i = 0; i < angles.length; i++) {
            anglePoints[i] = angleToLocation(angles[i], radius);
        }
        return anglePoints;
    }

    private Color applyAlpha(Color color, int alpha) {
        if (alpha == 255) {
            return color;
        }
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
}
