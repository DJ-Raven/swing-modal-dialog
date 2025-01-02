package raven.modal.demo.component.chart.utils;

import com.formdev.flatlaf.FlatClientProperties;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import raven.modal.demo.component.chart.TimeSeriesChart;
import raven.modal.demo.component.chart.renderer.*;

import javax.swing.*;

public class ToolBarTimeSeriesChartRenderer extends JToolBar {

    public ToolBarTimeSeriesChartRenderer(TimeSeriesChart chart) {
        putClientProperty(FlatClientProperties.STYLE, "" +
                "background:null;");
        XYItemRenderer[] renderers = new XYItemRenderer[]{
                new ChartXYCurveRenderer(),
                new ChartXYLineRenderer(),
                new ChartXYBarRenderer(),
                new ChartDeviationStepRenderer(),
                new ChartXYDifferenceRenderer()
        };
        ButtonGroup group = new ButtonGroup();
        boolean selected = false;
        for (XYItemRenderer renderer : renderers) {
            JToggleButton button = new JToggleButton(renderer.toString());
            button.addActionListener(e -> chart.setRenderer(renderer));
            group.add(button);
            add(button);
            if (!selected) {
                button.setSelected(true);
                selected = true;
            }
            button.putClientProperty(FlatClientProperties.STYLE, "" +
                    "toolbar.margin:2,5,2,5;");
        }
    }
}
