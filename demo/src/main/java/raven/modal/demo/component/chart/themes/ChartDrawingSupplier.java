package raven.modal.demo.component.chart.themes;

import com.formdev.flatlaf.util.UIScale;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.ui.RectangleInsets;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class ChartDrawingSupplier extends DefaultDrawingSupplier {

    protected ChartDrawingSupplier(ColorThemes colorThemes) {
        this(colorThemes.getColors());
    }

    private ChartDrawingSupplier(Paint[] paintSequence) {
        super(paintSequence, DEFAULT_FILL_PAINT_SEQUENCE, paintSequence, DEFAULT_STROKE_SEQUENCE, DEFAULT_OUTLINE_STROKE_SEQUENCE, DEFAULT_SHAPE_SEQUENCE);
    }

    @Override
    public Shape getNextShape() {
        float size = UIScale.scale(3f);
        return new Ellipse2D.Double(-size, -size, size * 2, size * 2);
    }

    @Override
    public Paint getNextOutlinePaint() {
        return alpha(super.getNextOutlinePaint(), 0.2f);
    }

    @Override
    public Stroke getNextOutlineStroke() {
        return scale(new BasicStroke(6f));
    }

    @Override
    public Stroke getNextStroke() {
        return scale(super.getNextStroke());
    }

    public static Shape getDefaultShape() {
        float size = UIScale.scale(10f);
        return new Ellipse2D.Double(0, 0, size, size);
    }

    public static Stroke getDefaultGridlineStroke() {
        float dash[] = {UIScale.scale(4f)};
        return new BasicStroke(UIScale.scale(1f), BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, UIScale.scale(5f), dash, 0f);
    }

    public static Stroke scale(Stroke stroke) {
        if (stroke instanceof BasicStroke) {
            BasicStroke basicStroke = (BasicStroke) stroke;
            float lineWidth = UIScale.scale(basicStroke.getLineWidth());
            if (lineWidth != basicStroke.getLineWidth()) {
                return new BasicStroke(lineWidth, basicStroke.getEndCap(), basicStroke.getLineJoin(), basicStroke.getMiterLimit(), basicStroke.getDashArray(), basicStroke.getDashPhase());
            }
        }
        return stroke;
    }

    public static Paint alpha(Paint paint, float alpha) {
        if (paint instanceof Color) {
            Color color = (Color) paint;
            return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (255 * alpha));
        }
        return paint;
    }

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
}
