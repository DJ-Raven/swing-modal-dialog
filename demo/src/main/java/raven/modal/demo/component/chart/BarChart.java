package raven.modal.demo.component.chart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.Axis;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import raven.modal.demo.component.chart.renderer.bar.ChartBarRenderer;
import raven.modal.demo.component.chart.themes.ChartDrawingSupplier;

public class BarChart extends DefaultChartPanel {

    private BarRenderer renderer;

    @Override
    protected JFreeChart createChart() {
        JFreeChart freeChart = ChartFactory.createBarChart(null, null, null, null);
        return freeChart;
    }

    @Override
    protected void defaultStyleChart(JFreeChart chart, ChartPanel panel) {
        CategoryPlot plot = chart.getCategoryPlot();
        NumberAxis range = (NumberAxis) plot.getRangeAxis();
        CategoryAxis domain = plot.getDomainAxis();

        range.setAxisLineVisible(false);
        domain.setAxisLineVisible(false);

        plot.setRenderer(getDefaultRender());
    }

    @Override
    protected void styleChart(JFreeChart chart, ChartPanel panel) {
        CategoryPlot plot = chart.getCategoryPlot();
        NumberAxis range = (NumberAxis) plot.getRangeAxis();
        CategoryAxis domain = plot.getDomainAxis();

        range.setTickLabelInsets(ChartDrawingSupplier.scaleRectangleInsets(Axis.DEFAULT_TICK_LABEL_INSETS));

        domain.setTickLabelInsets(ChartDrawingSupplier.scaleRectangleInsets(Axis.DEFAULT_TICK_LABEL_INSETS));

        plot.setRangeGridlineStroke(ChartDrawingSupplier.getDefaultGridlineStroke());
        plot.setInsets(ChartDrawingSupplier.scaleRectangleInsets(Plot.DEFAULT_INSETS));
    }

    public BarRenderer getDefaultRender() {
        if (renderer == null) {
            renderer = new ChartBarRenderer();
        }
        return renderer;
    }

    public void setDataset(CategoryDataset dataset) {
        freeChart.getCategoryPlot().setDataset(dataset);
    }
}
