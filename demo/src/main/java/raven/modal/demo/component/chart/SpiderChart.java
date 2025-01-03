package raven.modal.demo.component.chart;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.util.UIScale;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.SpiderWebPlot;
import org.jfree.data.category.CategoryDataset;
import raven.modal.demo.component.chart.utils.ChartDefaultThemes;
import raven.modal.demo.component.chart.utils.ChartUtils;
import raven.modal.demo.component.chart.utils.LegendIconColor;
import raven.modal.demo.sample.SampleData;

import javax.swing.*;
import java.awt.*;

public class SpiderChart extends ChartCustomPanel {

    public SpiderChart() {
        createCustomLegend();
    }

    private void createCustomLegend() {
        JToolBar toolBar = new JToolBar();
        toolBar.setOpaque(false);
        SpiderWebPlot plot = (SpiderWebPlot) freeChart.getPlot();
        CategoryDataset dataset = plot.getDataset();
        int count = dataset.getRowCount();
        for (int i = 0; i < count; i++) {
            final int index = i;
            String row = dataset.getRowKey(index).toString();
            Color color = ChartDefaultThemes.getDefaultColor()[index];
            JToggleButton button = new JToggleButton(row, new LegendIconColor(color), i == 0);
            button.putClientProperty(FlatClientProperties.STYLE, "" +
                    "toolbar.margin:2,5,2,5;" +
                    "foreground:fade($Label.foreground,50%);" +
                    "toolbar.selectedForeground:$Label.foreground;" +
                    "toolbar.selectedBackground:null;");
            button.addActionListener(e -> {
                initSeriesStyle(plot, index, color, !button.isSelected());
            });
            toolBar.add(button);
        }
        add(toolBar, "al center,grow 0");
    }

    @Override
    protected JFreeChart createChart() {
        SpiderWebPlot plot = new SpiderWebPlot(SampleData.getCategoryDataset());
        JFreeChart freeChart = new JFreeChart(null, null, plot, false);
        return freeChart;
    }

    @Override
    protected void styleChart(JFreeChart chart, ChartPanel panel) {
        Font font = getFont();
        Color foreground = getForeground();
        Color border = UIManager.getColor("Component.borderColor");
        SpiderWebPlot plot = (SpiderWebPlot) chart.getPlot();
        plot.setAxisLinePaint(border);
        plot.setLabelFont(font);
        plot.setLabelPaint(foreground);
        plot.setSeriesOutlineStroke(new BasicStroke(UIScale.scale(1f)));
        plot.setAxisLineStroke(new BasicStroke(UIScale.scale(1f)));

        int index = 0;
        for (Color color : ChartDefaultThemes.getDefaultColor()) {
            initSeriesStyle(plot, index++, color, index != 1);
        }
    }

    private void initSeriesStyle(SpiderWebPlot plot, int series, Color color, boolean alpha) {
        Color c = alpha ? ChartUtils.alphaColor(color, 0.3f) : color;
        plot.setSeriesPaint(series, c);
        plot.setSeriesOutlinePaint(series, c);
    }

    @Override
    protected void createAnnotation(ChartPanel panel) {
    }
}
