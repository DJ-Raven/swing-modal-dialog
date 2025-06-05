package raven.modal.utils;

import com.formdev.flatlaf.util.UIScale;

import javax.swing.border.Border;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;

/**
 * @author Raven
 */
public class ImageSnapshots {

    public static Image createSnapshotsImage(Component component, Component comBorder, Border border, double systemScaleFactor) {
        Image image = createSnapshotsImage(component, 0);
        Insets insets = border.getBorderInsets(comBorder);
        int width = component.getWidth() + insets.left + insets.right;
        int height = component.getHeight() + insets.top + insets.bottom;
        int x = insets.left;
        int y = insets.top;
        BufferedImage buffImage;
        if (systemScaleFactor > 1) {
            int imageWidth = (int) Math.round(component.getWidth() * systemScaleFactor);
            int imageHeight = (int) Math.round(component.getHeight() * systemScaleFactor);
            width = (int) Math.round(width * systemScaleFactor);
            height = (int) Math.round(height * systemScaleFactor);
            x = (int) (x * systemScaleFactor);
            y = (int) (y * systemScaleFactor);
            buffImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = buffImage.createGraphics();

            paintBorder(comBorder, border, g2, 0, 0, comBorder.getWidth(), comBorder.getHeight());
            g2.drawImage(image, x, y, imageWidth, imageHeight, null);
            g2.dispose();
        } else {
            buffImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = buffImage.createGraphics();
            border.paintBorder(comBorder, g2, 0, 0, comBorder.getWidth(), comBorder.getHeight());
            g2.drawImage(image, x, y, null);
            g2.dispose();
        }
        return buffImage;
    }

    public static void paintBorder(Component com, Border border, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setTransform(com.getGraphicsConfiguration().getDefaultTransform());
            border.paintBorder(com, g2, x, y, width, height);
        } finally {
            g2.dispose();
        }
    }

    public static Image createSnapshotsImage(Component component, float round) {
        int width = component.getWidth();
        int height = component.getHeight();
        if (width == 0 || height == 0) {
            return null;
        }
        VolatileImage image = component.createVolatileImage(width, height);
        if (image != null) {
            component.paint(image.createGraphics());
            return round > 0 ? roundImage(image, round) : image;
        }
        return null;
    }

    private static Image roundImage(VolatileImage image, float round) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage roundImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = roundImage.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        float arc = UIScale.scale(round);
        g2.fill(new RoundRectangle2D.Float(0, 0, width, height, arc, arc));
        g2.setComposite(AlphaComposite.SrcIn);
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return roundImage;
    }
}
