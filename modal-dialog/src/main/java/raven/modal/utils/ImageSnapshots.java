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

    public static Image createSnapshotsImage(Component component, Component comBorder, Border border) {
        Image image = createSnapshotsImage(component, 0);
        Insets insets = border.getBorderInsets(comBorder);
        int width = component.getWidth() + insets.left + insets.right;
        int height = component.getHeight() + insets.top + insets.bottom;
        BufferedImage buffImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = buffImage.createGraphics();
        border.paintBorder(comBorder, g2, 0, 0, comBorder.getWidth(), comBorder.getHeight());
        g2.drawImage(image, insets.left, insets.top, null);
        g2.dispose();
        return buffImage;
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
