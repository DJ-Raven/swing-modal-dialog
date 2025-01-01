package raven.modal.demo.component.chart;

import com.formdev.flatlaf.util.UIScale;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBezierRenderer;
import raven.modal.demo.sample.SampleData;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class TimeSeriesChart extends ChartCustomPanel {

    public TimeSeriesChart() {
        super();
    }

    @Override
    protected JFreeChart createChart() {
        JFreeChart chart = ChartFactory.createTimeSeriesChart(null, null, null, SampleData.createTimeSeriesDataset());
        return chart;
    }

    @Override
    protected void styleChart(JFreeChart chart, ChartPanel panel) {
        XYPlot plot = (XYPlot) chart.getPlot();
        Color foreground = getForeground();
        Font font = getFont();
        Color border = UIManager.getColor("Component.borderColor");

        NumberAxis range = (NumberAxis) plot.getRangeAxis();
        DateAxis domain = (DateAxis) plot.getDomainAxis();

        range.setAxisLineVisible(false);
        range.setTickMarksVisible(false);
        range.setTickLabelPaint(foreground);
        range.setTickLabelFont(font);

        range.setUpperMargin(0.2);
        range.setLowerMargin(0.1);

        domain.setAxisLineVisible(false);
        domain.setTickMarksVisible(false);
        domain.setTickLabelPaint(foreground);
        domain.setTickLabelFont(font);

        plot.setDomainPannable(true);
        float dash[] = {UIScale.scale(4f)};
        plot.setDomainGridlineStroke(new BasicStroke(UIScale.scale(1f), BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, UIScale.scale(5f), dash, 0f));
        plot.setRangeGridlinesVisible(false);
        plot.setInsets(scaleRectangleInsets(4, 8, 15, 8));
        plot.setDomainGridlinePaint(border);

        XYBezierRenderer renderer = new XYBezierRenderer(UIScale.scale(10), UIScale.scale(25));

        initSeriesStyle(renderer, 0, Color.decode("#10b981"));
        initSeriesStyle(renderer, 1, Color.decode("#f43f5e"));

        renderer.setDefaultItemLabelPaint(foreground);
        plot.setRenderer(renderer);

        Color color = UIManager.getColor("List.selectionBackground");
        panel.setZoomFillPaint(alphaColor(color, 50));
    }

    private void initSeriesStyle(XYBezierRenderer renderer, int series, Color color) {
        renderer.setSeriesPaint(series, color);
        renderer.setSeriesStroke(series, new BasicStroke(UIScale.scale(1.5f)));
        int s = UIScale.scale(4);
        renderer.setSeriesShape(series, new Ellipse2D.Float(-s, -s, s * 2, s * 2));

        renderer.setSeriesOutlinePaint(series, alphaColor(color, 50));
        renderer.setSeriesOutlineStroke(series, new BasicStroke(UIScale.scale(6f)));
        renderer.setUseOutlinePaint(true);
    }
}
