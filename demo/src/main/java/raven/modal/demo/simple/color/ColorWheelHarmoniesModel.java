package raven.modal.demo.simple.color;

import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.UIScale;
import raven.color.DiskColorPickerModel;
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

    public ColorHarmony getColorHarmony() {
        return colorHarmony;
    }

    public void setColorHarmony(ColorHarmony colorHarmony) {
        this.colorHarmony = colorHarmony;
    }

    public Color[] getSelectedColors() {
        return colorHarmony.getSelectedColor(this);
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

            Point2D.Float[] points = colorHarmony.toLocation(this, selectedColor);

            int lineWidth = scale(2, systemScale);
            int border = scale(1, systemScale);
            float size = scale(12, systemScale);
            Color borderColor = UIManager.getColor("Component.borderColor");
            for (Point2D.Float point : points) {
                g2.setColor(Color.WHITE);
                float x = point.x * width;
                float y = point.y * height;
                FlatUIUtils.paintOutline(g2, x - size / 2, y - size / 2, size, size, lineWidth, width);
                g2.setColor(borderColor);
                FlatUIUtils.paintOutline(g2, x - size / 2, y - size / 2, size, size, border, width);
            }
            g2.dispose();
            wheelColorImage = image;
        }
    }

    protected int scale(int value, double scaleFactor) {
        return (int) Math.ceil(UIScale.scale(value) * scaleFactor);
    }
}
