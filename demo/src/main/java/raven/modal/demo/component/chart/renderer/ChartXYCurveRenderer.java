package raven.modal.demo.component.chart.renderer;

import com.formdev.flatlaf.util.UIScale;
import org.jfree.chart.renderer.xy.XYBezierRenderer;

public class ChartXYCurveRenderer extends XYBezierRenderer {

    private static final int precision = 10;
    private static final double tension = 25;

    public ChartXYCurveRenderer() {
        this(UIScale.scale(precision), UIScale.scale((float) tension));
    }

    public ChartXYCurveRenderer(int precision, double tension) {
        super(precision, tension);
        initStyle();
    }

    private void initStyle() {
        setAutoPopulateSeriesOutlinePaint(true);
        setAutoPopulateSeriesOutlineStroke(true);
        setUseOutlinePaint(true);
    }

    @Override
    public String toString() {
        return "Curve";
    }
}
