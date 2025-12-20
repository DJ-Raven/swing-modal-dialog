package raven.modal.demo.simple.color;

import raven.color.component.ColorLocation;

import java.awt.*;
import java.awt.geom.Point2D;

public abstract class ColorHarmony {

    private final String name;

    public ColorHarmony(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }

    public abstract Point2D.Float[] toLocation(ColorWheelHarmoniesModel model, Color color, int width, int height);

    public abstract int getSelectedColorIndex();

    public float locationToAngle(float x, float y, int width, int height) {
        float cx = width / 2f;
        float cy = height / 2f;

        float dx = x - cx;
        float dy = cy - y;

        float angle = (float) Math.toDegrees(Math.atan2(dy, dx));
        return angle < 0 ? angle + 360 : angle;
    }

    public Point2D.Float angleToLocation(float angle, float radius, int width, int height) {
        float cx = width / 2f;
        float cy = height / 2f;

        float rad = (float) Math.toRadians(angle);

        float x = (float) (cx + Math.cos(rad) * radius);
        float y = (float) (cy - Math.sin(rad) * radius);
        return new Point2D.Float(x, y);
    }

    public float locationToRadius(float x, float y, int width, int height) {
        float cx = width / 2f;
        float cy = height / 2f;

        float dx = x - cx;
        float dy = y - cy;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    public Color[] getSelectedColor(ColorWheelHarmoniesModel model, int width, int height) {
        Color selectedColor = model.getSelectedColor();
        Point2D.Float[] locations = toLocation(model, selectedColor, width, height);
        Color[] colors = new Color[locations.length + 1];
        int alpha = selectedColor.getAlpha();
        int colorIndex = getSelectedColorIndex();
        int indexAdd = 0;
        ColorLocation oldLocation = model.getLocation();
        for (int i = 0; i < locations.length; i++) {
            if (colorIndex >= 0 && i == colorIndex) {
                colors[i] = selectedColor;
                indexAdd++;
            }
            Point2D.Float l = locations[i];
            Color c = model.locationToColor(new ColorLocation(l.x / width, l.y / height), model.getValue());
            colors[i + indexAdd] = applyAlpha(c, alpha);
        }
        if (indexAdd == 0 && colorIndex < colors.length) {
            colors[colorIndex] = selectedColor;
        }
        model.setLocation(oldLocation);
        return colors;
    }

    private Color applyAlpha(Color color, int alpha) {
        if (alpha == 255) {
            return color;
        }
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
}
