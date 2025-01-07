package raven.modal.demo.component.chart.renderer;

import com.formdev.flatlaf.util.UIScale;
import org.jfree.chart.renderer.xy.DeviationStepRenderer;

import java.awt.*;

public class ChartDeviationStepRenderer extends DeviationStepRenderer {

    public ChartDeviationStepRenderer() {
        initStyle();
    }

    private void initStyle() {
        setAutoPopulateSeriesOutlinePaint(true);
        setDefaultOutlineStroke(new BasicStroke(UIScale.scale(6f)));
        setUseOutlinePaint(true);
    }

    @Override
    public String toString() {
        return "Deviation Step";
    }
}
