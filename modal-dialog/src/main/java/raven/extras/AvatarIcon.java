package raven.extras;

import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.UIScale;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.net.URL;

/**
 * @author Raven
 */
public class AvatarIcon implements Icon {

    private String filename;
    private URL location;
    private Icon icon;
    private Image image;
    private float round;
    private int width;
    private int height;
    private Type type = Type.ROUND;

    private int imageWidth;
    private int imageHeight;

    public AvatarIcon(String filename, int width, int height, float round) {
        this.filename = filename;
        this.width = width;
        this.height = height;
        this.round = round;
    }

    public AvatarIcon(URL location, int width, int height, float round) {
        this.location = location;
        this.width = width;
        this.height = height;
        this.round = round;
    }

    public AvatarIcon(Icon icon, int width, int height, float round) {
        this.icon = icon;
        this.width = width;
        this.height = height;
        this.round = round;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        updateImage();
        Graphics2D g2 = (Graphics2D) g.create();
        g2.drawImage(image, x, y, null);
        g2.dispose();
    }

    private void updateImage() {
        if ((filename != null || location != null || icon != null) && (image == null)) {
            imageWidth = UIScale.scale(width);
            imageHeight = UIScale.scale(height);
            ImageIcon icon;
            if (filename != null) {
                icon = new ImageIcon(filename);
            } else if (location != null) {
                icon = new ImageIcon(location);
            } else {
                icon = (ImageIcon) this.icon;
            }
            image = resizeImage(icon.getImage(), imageWidth, imageHeight);
        }
    }

    private Image resizeImage(Image icon, int width, int height) {
        int sw = width - icon.getWidth(null);
        int sh = height - icon.getHeight(null);
        Image img;
        if (sw > sh) {
            // resize width
            img = new ImageIcon(icon.getScaledInstance(width, -1, Image.SCALE_SMOOTH)).getImage();
        } else {
            // resize height
            img = new ImageIcon(icon.getScaledInstance(-1, height, Image.SCALE_SMOOTH)).getImage();
        }
        return round > 0 ? roundImage(img, width, height, round) : img;
    }

    private Image roundImage(Image image, float width, float height, float round) {
        BufferedImage buff = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = buff.createGraphics();
        FlatUIUtils.setRenderingHints(g);
        Shape mask = null;
        if (round > 0) {
            if (type == Type.ROUND) {
                if (round == 999) {
                    mask = new Ellipse2D.Double(0, 0, width, height);
                } else {
                    float r = UIScale.scale(round);
                    mask = new RoundRectangle2D.Double(0, 0, width, height, r, r);
                }
            } else {
                mask = new SuperEllipse2D(0, 0, width, height, round).getShape();
            }
        }
        if (mask != null) {
            g.fill(mask);
            g.setComposite(AlphaComposite.SrcIn);
        }
        g.drawImage(image, 0, 0, null);
        g.dispose();
        image.flush();
        return buff;
    }

    @Override
    public int getIconWidth() {
        updateImage();
        if (image == null) {
            return 0;
        }
        return image.getWidth(null);
    }

    @Override
    public int getIconHeight() {
        updateImage();
        if (image == null) {
            return 0;
        }
        return image.getHeight(null);
    }

    public float getRound() {
        return round;
    }

    public void setRound(float round) {
        this.round = round;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type {
        ROUND, MASK_SQUIRCLE,
    }
}