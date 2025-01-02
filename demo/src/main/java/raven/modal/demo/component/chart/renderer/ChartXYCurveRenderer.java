package raven.modal.demo.component.chart.renderer;

import com.formdev.flatlaf.util.UIScale;
import org.jfree.chart.renderer.xy.XYBezierRenderer;
import raven.modal.demo.component.chart.utils.ChartDefaultThemes;
import raven.modal.demo.component.chart.utils.ChartUtils;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class ChartXYCurveRenderer extends XYBezierRenderer {

    private static final int precision = 10;
    private static final double tension = 25;

    public ChartXYCurveRenderer() {
        this(UIScale.scale(precision), UIScale.scale((float) tension));
    }

    public ChartXYCurveRenderer(int precision, double tension) {
        super(precision, tension);
        int index = 0;
        for (Color color : ChartDefaultThemes.getDefaultColor()) {
            initSeriesStyle(index++, color);
        }
    }

    private void initSeriesStyle(int series, Color color) {
        setSeriesPaint(series, color);
        setSeriesStroke(series, new BasicStroke(UIScale.scale(1.5f)));
        int s = UIScale.scale(3);
        setSeriesShape(series, new Ellipse2D.Float(-s, -s, s * 2, s * 2));

        setSeriesOutlinePaint(series, ChartUtils.alphaColor(color, 0.2f));
        setSeriesOutlineStroke(series, new BasicStroke(UIScale.scale(6f)));
        setUseOutlinePaint(true);
    }

    @Override
    public String toString() {
        return "Curve";
    }
}
