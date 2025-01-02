package raven.modal.demo.component.chart.renderer;

import org.jfree.chart.renderer.xy.ClusteredXYBarRenderer;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import raven.modal.demo.component.chart.utils.ChartDefaultThemes;
import raven.modal.demo.component.chart.utils.ChartUtils;

import java.awt.*;

public class ChartXYBarRenderer extends ClusteredXYBarRenderer {

    public ChartXYBarRenderer() {
        int index = 0;
        for (Color color : ChartDefaultThemes.getDefaultColor()) {
            initSeriesStyle(index++, ChartUtils.alphaColor(color, 0.8f));
        }
        setBarPainter(new StandardXYBarPainter());
        setMargin(0.3);
        setShadowVisible(false);
    }

    private void initSeriesStyle(int series, Color color) {
        setSeriesPaint(series, color);
    }

    @Override
    public String toString() {
        return "Bar";
    }
}
