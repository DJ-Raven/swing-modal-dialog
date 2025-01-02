package raven.modal.demo.component.chart.renderer;

import com.formdev.flatlaf.util.UIScale;
import org.jfree.chart.renderer.xy.DeviationStepRenderer;
import raven.modal.demo.component.chart.utils.ChartDefaultThemes;
import raven.modal.demo.component.chart.utils.ChartUtils;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class ChartDeviationStepRenderer extends DeviationStepRenderer {

    public ChartDeviationStepRenderer() {
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
        return "Deviation Step";
    }
}
