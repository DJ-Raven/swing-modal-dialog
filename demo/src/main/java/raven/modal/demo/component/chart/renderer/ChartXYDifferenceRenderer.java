package raven.modal.demo.component.chart.renderer;

import org.jfree.chart.renderer.xy.XYDifferenceRenderer;
import raven.modal.demo.component.chart.utils.ChartDefaultThemes;

import java.awt.*;

public class ChartXYDifferenceRenderer extends XYDifferenceRenderer {

    public ChartXYDifferenceRenderer() {
        Color[] colors = ChartDefaultThemes.getDefaultColor();
        setPositivePaint(colors[0]);
        setNegativePaint(colors[1]);
        int index = 0;
        for (Color color : colors) {
            initSeriesStyle(index++, color);
        }
    }

    private void initSeriesStyle(int series, Color color) {
        setSeriesPaint(series, color);
        setSeriesStroke(series, new BasicStroke(0f));
    }

    @Override
    public String toString() {
        return "Different";
    }
}
