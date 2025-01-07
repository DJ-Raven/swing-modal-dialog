package raven.modal.demo.component.chart.renderer;

import org.jfree.chart.renderer.xy.XYDifferenceRenderer;
import raven.modal.demo.component.chart.themes.DefaultChartTheme;

import java.awt.*;

public class ChartXYDifferenceRenderer extends XYDifferenceRenderer {

    public ChartXYDifferenceRenderer() {
        setPositivePaint(DefaultChartTheme.getColor(0));
        setNegativePaint(DefaultChartTheme.getColor(1));
        setAutoPopulateSeriesStroke(false);
        setDefaultStroke(new BasicStroke(0f));
    }

    @Override
    public String toString() {
        return "Different";
    }
}
