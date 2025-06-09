package raven.extras;

import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.HiDPIUtils;
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

    private final int iconWidth;
    private final int iconHeight;
    private final float round;

    private ImageIcon imageIcon;
    private int borderWidth;
    private int innerBorderWidth;
    private BorderColor borderColor;
    private Type type = Type.ROUND;

    private Image lastImage;
    private double lastSystemScaleFactor;
    private double lastUserScaleFactor;

    public AvatarIcon(String filename, int iconWidth, int iconHeight, float round) {
        this(new ImageIcon(filename), iconWidth, iconHeight, round);
    }

    public AvatarIcon(URL location, int iconWidth, int iconHeight, float round) {
        this(new ImageIcon(location), iconWidth, iconHeight, round);
    }

    public AvatarIcon(ImageIcon imageIcon, int iconWidth, int iconHeight, float round) {
        this.imageIcon = imageIcon;
        this.iconWidth = iconWidth;
        this.iconHeight = iconHeight;
        this.round = round;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {

        if (imageIcon == null || iconWidth <= 0 || iconHeight <= 0) return;

        // scale factor
        double systemScaleFactor = UIScale.getSystemScaleFactor((Graphics2D) g);
        float userScaleFactor = UIScale.getUserScaleFactor();
        double scaleFactor = systemScaleFactor * userScaleFactor;

        // paint cached scaled icon
        if (systemScaleFactor == lastSystemScaleFactor && userScaleFactor == lastUserScaleFactor && lastImage != null) {
            paintLastImage(g, x, y);
            return;
        }

        // destination image size
        int destImageWidth = (int) (iconWidth * scaleFactor);
        int destImageHeight = (int) (iconHeight * scaleFactor);

        Image image = imageIcon.getImage();

        image = resizeImage(image, destImageWidth, destImageHeight, scaleFactor);

        // cache image
        lastSystemScaleFactor = systemScaleFactor;
        lastUserScaleFactor = userScaleFactor;
        lastImage = image;

        paintLastImage(g, x, y);
    }

    private void paintLastImage(Graphics g, int x, int y) {
        if (lastSystemScaleFactor > 1) {
            HiDPIUtils.paintAtScale1x((Graphics2D) g, x, y, 100, 100, // width and height are not used
                    (g2, x2, y2, width2, height2, scaleFactor2) -> {
                        g2.drawImage(lastImage, x2, y2, null);
                    });
        } else {
            g.drawImage(lastImage, x, y, null);
        }
    }

    private Image resizeImage(Image image, int targetWidth, int targetHeight, double scaleFactor) {
        double border = getAllBorder() * scaleFactor;
        int width = (int) (targetWidth - border * 2);
        int height = (int) (targetHeight - border * 2);
        int sw = width - image.getWidth(null);
        int sh = height - image.getHeight(null);
        Image img = null;
        if (width > 0 && height > 0) {
            if (sw > sh) {
                // resize width
                img = new ImageIcon(image.getScaledInstance(width, -1, Image.SCALE_SMOOTH)).getImage();
            } else {
                // resize height
                img = new ImageIcon(image.getScaledInstance(-1, height, Image.SCALE_SMOOTH)).getImage();
            }
        }
        return roundImage(img, width, height, targetWidth, targetHeight, border, round, scaleFactor);
    }

    private Image roundImage(Image image, int width, int height, int targetWidth, int targetHeight, double border, float round, double scaleFactor) {
        BufferedImage buff = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = buff.createGraphics();
        FlatUIUtils.setRenderingHints(g);
        Shape mask = null;
        if (image != null) {
            mask = createMask(width, height, border, round, scaleFactor);
            g.fill(mask);
            g.setComposite(AlphaComposite.SrcIn);
            int x = (int) ((width - image.getWidth(null)) / 2 + border);
            int y = (int) ((height - image.getHeight(null)) / 2 + border);
            g.drawImage(image, x, y, null);
        }

        // create border
        int borderSize = (int) Math.round(borderWidth * scaleFactor);
        int innerSize = (int) Math.round(innerBorderWidth * scaleFactor);
        if (borderSize > 0 && (borderColor == null || borderColor.getOpacity() > 0)) {
            paintBorder(g, mask, targetWidth, targetHeight, borderSize, innerSize, round, scaleFactor);
        }
        g.dispose();
        if (image != null) image.flush();
        return buff;
    }

    private void paintBorder(Graphics2D g, Shape mask, int width, int height, double borderSize, double innerSize, float round, double scaleFactor) {
        Area areaBorder = new Area(createMask(width, height, 0, round, scaleFactor));
        Shape shape = innerSize > 0 ? createMask(width - borderSize * 2, height - borderSize * 2, borderSize, round, scaleFactor) : mask;
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

    private Shape createMask(double width, double height, double border, float round, double scaleFactor) {
        if (round <= 0) return new Rectangle2D.Double(border, border, width, height);
        Shape mask;
        if (type == Type.ROUND) {
            if (round == 999) {
                mask = new Ellipse2D.Double(border, border, width, height);
            } else {
                double r = Math.max(round * scaleFactor - border, 0);
                mask = new RoundRectangle2D.Double(border, border, width, height, r, r);
            }
        } else {
            mask = new SuperEllipse2D(border, border, width, height, round).getShape();
        }
        return mask;
    }

    @Override
    public int getIconWidth() {
        return UIScale.scale(iconWidth);
    }

    @Override
    public int getIconHeight() {
        return UIScale.scale(iconHeight);
    }

    public float getRound() {
        return round;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        lastImage = null;
    }

    public int getInnerBorderWidth() {
        return innerBorderWidth;
    }

    public void setInnerBorderWidth(int innerBorderWidth) {
        this.innerBorderWidth = innerBorderWidth;
        lastImage = null;
    }

    public void setBorder(int borderWidth, int innerBorderWidth) {
        this.borderWidth = borderWidth;
        this.innerBorderWidth = innerBorderWidth;
        lastImage = null;
    }

    public BorderColor getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(BorderColor borderColor) {
        this.borderColor = borderColor;
        lastImage = null;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
        lastImage = null;
    }

    public void setIcon(String filename) {
        setIcon(new ImageIcon(filename));
    }

    public void setIcon(URL location) {
        setIcon(new ImageIcon(location));
    }

    public void setIcon(ImageIcon imageIcon) {
        this.imageIcon = imageIcon;
        lastImage = null;
    }

    public ImageIcon getImageIcon() {
        return imageIcon;
    }

    private int getAllBorder() {
        return borderWidth + innerBorderWidth;
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