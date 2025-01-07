package raven.modal.demo.component.chart;

import com.formdev.flatlaf.util.UIScale;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.general.PieDataset;
import raven.modal.demo.component.chart.themes.ChartDrawingSupplier;

import java.awt.*;

public class PieChart extends DefaultChartPanel {

    public PieChart() {
    }

    @Override
    protected JFreeChart createChart() {
        JFreeChart freeChart = ChartFactory.createPieChart(null, null, true, false, false);
        return freeChart;
    }

    @Override
    protected void defaultStyleChart(JFreeChart chart, ChartPanel panel) {
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setShadowPaint(null);
        plot.setLabelShadowPaint(null);
        plot.setLabelOutlinePaint(null);
        plot.setLabelBackgroundPaint(null);
        plot.setDefaultSectionOutlineStroke(new BasicStroke(0f));
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} {2}"));

        // test
        plot.setExplodePercent("Tablet", 0.15f);
    }

    @Override
    protected void styleChart(JFreeChart chart, ChartPanel panel) {
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLegendItemShape(ChartDrawingSupplier.getDefaultShape());
        plot.setLabelLinkStroke(new BasicStroke(UIScale.scale(1f)));
        plot.setLabelPadding(ChartDrawingSupplier.scaleRectangleInsets(new RectangleInsets(2, 2, 2, 2)));
    }

    public void setDataset(PieDataset dataset) {
        PiePlot plot = (PiePlot) freeChart.getPlot();
        plot.setDataset(dataset);
    }
}
