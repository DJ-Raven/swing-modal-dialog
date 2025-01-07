package raven.modal.demo.component.chart.renderer;

import org.jfree.chart.renderer.xy.ClusteredXYBarRenderer;
import raven.modal.demo.component.chart.themes.DefaultChartTheme;

public class ChartXYBarRenderer extends ClusteredXYBarRenderer {

    public ChartXYBarRenderer() {
        setBarPainter(DefaultChartTheme.getInstance().getXYBarPainter());
        setShadowVisible(DefaultChartTheme.getInstance().isShadowVisible());
        setMargin(0.3);
    }

    @Override
    public String toString() {
        return "Bar";
    }
}
