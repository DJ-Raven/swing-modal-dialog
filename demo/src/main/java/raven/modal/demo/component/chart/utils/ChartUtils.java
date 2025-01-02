package raven.modal.demo.component.chart.utils;

import com.formdev.flatlaf.util.UIScale;
import org.jfree.chart.ui.RectangleInsets;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class ChartUtils {

    public static RectangleInsets scaleRectangleInsets(double top, double left, double bottom, double right) {
        return new RectangleInsets(UIScale.scale((float) top), UIScale.scale((float) left), UIScale.scale((float) bottom), UIScale.scale((float) right));
    }

    public static RectangleInsets scaleRectangleInsets(RectangleInsets rec) {
        return scaleRectangleInsets(rec.getTop(), rec.getLeft(), rec.getBottom(), rec.getRight());
    }

    public static Rectangle2D scaleRectangle(Rectangle2D rec) {
        Rectangle r = rec.getBounds();
        return new Rectangle2D.Double(UIScale.scale((float) r.getX()), UIScale.scale((float) rec.getY()), UIScale.scale((float) rec.getWidth()), UIScale.scale((float) rec.getHeight()));
    }

    public static Rectangle2D unscaleRectangle(Rectangle2D rec) {
        Rectangle r = rec.getBounds();
        return new Rectangle2D.Double(UIScale.unscale((float) r.getX()), UIScale.unscale((float) rec.getY()), UIScale.unscale((float) rec.getWidth()), UIScale.unscale((float) rec.getHeight()));
    }

    public static Color alphaColor(Color color, float alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (alpha * 255));
    }
}
