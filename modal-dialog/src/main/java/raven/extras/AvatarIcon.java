package raven.extras;

import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.UIScale;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
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
    private int borderWidth;
    private int innerBorderWidth;
    private BorderColor borderColor;
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
        if (width <= 0 || height <= 0) return;
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
        int border = getAllBorder();
        width -= border * 2;
        height -= border * 2;
        int sw = width - icon.getWidth(null);
        int sh = height - icon.getHeight(null);
        Image img = null;
        if (width > 0 && height > 0) {
            if (sw > sh) {
                // resize width
                img = new ImageIcon(icon.getScaledInstance(width, -1, Image.SCALE_SMOOTH)).getImage();
            } else {
                // resize height
                img = new ImageIcon(icon.getScaledInstance(-1, height, Image.SCALE_SMOOTH)).getImage();
            }
        }
        return roundImage(img, width, height, border, round);
    }

    private Image roundImage(Image image, int width, int height, int border, float round) {
        BufferedImage buff = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = buff.createGraphics();
        FlatUIUtils.setRenderingHints(g);
        Shape mask = null;
        if (image != null) {
            mask = createMask(width, height, border, round);
            g.fill(mask);
            g.setComposite(AlphaComposite.SrcIn);
            int x = (width - image.getWidth(null)) / 2;
            int y = (height - image.getHeight(null)) / 2;
            g.drawImage(image, x + border, y + border, null);
        }
        // create border
        int borderSize = UIScale.scale(borderWidth);
        int innerSize = UIScale.scale(innerBorderWidth);
        if (borderSize > 0 && (borderColor == null || borderColor.getOpacity() > 0)) {
            paintBorder(g, mask, 0, 0, imageWidth, imageHeight, borderSize, innerSize, round);
        }
        g.dispose();
        if (image != null) image.flush();
        return buff;
    }

    private void paintBorder(Graphics2D g, Shape mask, int x, int y, int width, int height, int borderSize, int innerSize, float round) {
        Area areaBorder = new Area(createMask(width, height, 0, round));
        Shape shape = innerSize > 0 ? createMask(width - borderSize * 2, height - borderSize * 2, borderSize, round) : mask;
        if (shape != null) {
            areaBorder.subtract(new Area(shape));
        }
        g.setComposite(AlphaComposite.SrcOver);
        if (borderColor != null) {
            borderColor.paint(g, 0, 0, width, height);
        } else {
            g.setColor(UIManager.getColor("Component.borderColor"));
        }
        g.fill(areaBorder);
    }

    private Shape createMask(int width, int height, int border, float round) {
        if (round <= 0) return new Rectangle2D.Double(border, border, width, height);
        Shape mask;
        if (type == Type.ROUND) {
            if (round == 999) {
                mask = new Ellipse2D.Double(border, border, width, height);
            } else {
                float r = UIScale.scale(round);
                mask = new RoundRectangle2D.Double(border, border, width, height, r, r);
            }
        } else {
            mask = new SuperEllipse2D(border, border, width, height, round).getShape();
        }
        return mask;
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

    public int getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        image = null;
    }

    public int getInnerBorderWidth() {
        return innerBorderWidth;
    }

    public void setInnerBorderWidth(int innerBorderWidth) {
        this.innerBorderWidth = innerBorderWidth;
        image = null;
    }

    public void setBorder(int borderWidth, int innerBorderWidth) {
        this.borderWidth = borderWidth;
        this.innerBorderWidth = innerBorderWidth;
        image = null;
    }

    public BorderColor getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(BorderColor borderColor) {
        this.borderColor = borderColor;
        image = null;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
        image = null;
    }

    public Icon getDefaultIcon() {
        Icon icon;
        if (filename != null) {
            icon = new ImageIcon(filename);
        } else if (location != null) {
            icon = new ImageIcon(location);
        } else {
            icon = this.icon;
        }
        return icon;
    }

    private int getAllBorder() {
        return UIScale.scale(borderWidth + innerBorderWidth);
    }

    public enum Type {
        ROUND, MASK_SQUIRCLE,
    }

    public static class BorderColor {

        private Color startColor;
        private Color endColor;
        private float startPoint;
        private float endPoint;
        private float opacity = 1f;

        public BorderColor(int r, int g, int b) {
            this(new Color(r, g, b));
        }

        public BorderColor(int r, int g, int b, float opacity) {
            this(new Color(r, g, b), opacity);
        }

        public BorderColor(Color color) {
            this.startColor = color;
        }

        public BorderColor(Color color, float opacity) {
            this.startColor = color;
            this.opacity = opacity;
        }

        public BorderColor(Color startColor, Color endColor) {
            this(startColor, endColor, 0f, 0f);
        }

        public BorderColor(Color startColor, Color endColor, float opacity) {
            this(startColor, endColor, 0f, 0f, opacity);
        }

        public BorderColor(Color startColor, Color endColor, float startPoint, float endPoint) {
            this(startColor, endColor, startPoint, endPoint, 1f);
        }

        public BorderColor(Color startColor, Color endColor, float startPoint, float endPoint, float opacity) {
            this.startColor = startColor;
            this.endColor = endColor;
            this.startPoint = startPoint;
            this.endPoint = endPoint;
            this.opacity = opacity;
        }

        public void paint(Graphics2D g, int x, int y, int width, int height) {
            float op;
            float opt = getOpacity();
            if (opt > 1f) {
                op = 1f;
            } else if (opt < 0) {
                op = 0;
            } else {
                op = opt;
            }
            if (op == 0f) return;
            if (op < 1f) {
                g.setComposite(AlphaComposite.SrcOver.derive(op));
            }

            Color eColor = getEndColor();
            Color sColor = getStartColor();
            float sPoint = getStartPoint();
            float ePoint = getEndPoint();

            if (eColor == null) {
                g.setColor(sColor);
            } else {
                g.setPaint(new GradientPaint(x, y + height * sPoint, sColor, x + width, y + height * ePoint, eColor));
            }
        }

        public Color getStartColor() {
            return startColor;
        }

        public void setStartColor(Color startColor) {
            this.startColor = startColor;
        }

        public Color getEndColor() {
            return endColor;
        }

        public void setEndColor(Color endColor) {
            this.endColor = endColor;
        }

        public float getStartPoint() {
            return startPoint;
        }

        public void setStartPoint(float startPoint) {
            this.startPoint = startPoint;
        }

        public float getEndPoint() {
            return endPoint;
        }

        public void setEndPoint(float endPoint) {
            this.endPoint = endPoint;
        }

        public float getOpacity() {
            return opacity;
        }

        public void setOpacity(float opacity) {
            this.opacity = opacity;
        }
    }
}