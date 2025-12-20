package raven.modal.demo.simple.color;

import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.UIScale;
import raven.color.DiskColorPickerModel;
import raven.color.component.ColorLocation;
import raven.modal.demo.simple.color.harmony.Analogous;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class ColorWheelHarmoniesModel extends DiskColorPickerModel {

    private BufferedImage wheelColorImage;
    private ColorHarmony colorHarmony;

    public ColorWheelHarmoniesModel() {
        colorHarmony = new Analogous();
    }

    public ColorHarmony getColorPalette() {
        return colorHarmony;
    }

    public void setColorPalette(ColorHarmony colorHarmony) {
        this.colorHarmony = colorHarmony;
    }

    public Color[] getSelectedColors() {
        if (wheelColorImage == null) {
            return null;
        }
        return colorHarmony.getSelectedColor(this, wheelColorImage.getWidth(), wheelColorImage.getHeight());
    }

    @Override
    public BufferedImage getColorImage(int width, int height, int arc) {
        super.getColorImage(width, height, arc);
        return wheelColorImage;
    }

    @Override
    protected void createColorImage(int width, int height, int arc) {
        super.createColorImage(width, height, arc);
        Color selectedColor = getSelectedColor();
        if (selectedColor != null && colorImage != null) {
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            // paint selected
            Graphics2D g2 = image.createGraphics();

            GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice().getDefaultConfiguration();
            double systemScale = UIScale.getSystemScaleFactor(gc);

            g2.drawImage(colorImage, 0, 0, null);
            FlatUIUtils.setRenderingHints(g2);

            Point2D.Float[] points = colorHarmony.toLocation(this, selectedColor, width, height);


            int lineWidth = scale(1, systemScale);
            float size = scale(12, systemScale);
            Color borderColor = UIManager.getColor("Component.borderColor");
            for (Point2D.Float point : points) {
                g2.setColor(Color.WHITE);
                FlatUIUtils.paintOutline(g2, point.x - size / 2 + lineWidth, point.y - size / 2 + lineWidth, size - lineWidth * 2, size - lineWidth * 2, lineWidth, width);
                g2.setColor(borderColor);
                FlatUIUtils.paintOutline(g2, point.x - size / 2, point.y - size / 2, size, size, lineWidth, width);
            }
            g2.dispose();
            wheelColorImage = image;
        }
    }

    public ColorLocation getLocation() {
        return new ColorLocation(location.getX(), location.getY());
    }

    public void setLocation(ColorLocation location) {
        this.location.set(location);
    }

    protected int scale(int value, double scaleFactor) {
        return (int) Math.ceil(UIScale.scale(value) * scaleFactor);
    }
}
