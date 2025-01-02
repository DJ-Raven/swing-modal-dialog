package raven.modal.demo.component.chart;

import com.formdev.flatlaf.util.UIScale;
import org.jfree.chart.*;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBezierRenderer;
import org.jfree.data.time.TimeSeriesCollection;
import raven.modal.demo.component.chart.utils.MultiXYTextAnnotation;
import raven.modal.demo.component.chart.utils.ChartUtils;
import raven.modal.demo.sample.SampleData;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;

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
        Color background = getBackground();
        Color foreground = getForeground();
        Color selectionColor = UIManager.getColor("List.selectionBackground");
        Font font = getFont();
        Color border = UIManager.getColor("Component.borderColor");

        NumberAxis range = (NumberAxis) plot.getRangeAxis();
        DateAxis domain = (DateAxis) plot.getDomainAxis();

        range.setAxisLineVisible(false);
        range.setTickMarksVisible(false);
        range.setTickLabelPaint(foreground);
        range.setTickLabelFont(font);
        range.setNumberFormatOverride(NumberFormat.getCurrencyInstance());

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
        plot.setInsets(ChartUtils.scaleRectangleInsets(4, 8, 15, 8));
        plot.setDomainGridlinePaint(border);

        XYBezierRenderer renderer = new XYBezierRenderer(UIScale.scale(10), UIScale.scale(25));

        initSeriesStyle(renderer, 0, Color.decode("#10b981"));
        initSeriesStyle(renderer, 1, Color.decode("#f43f5e"));

        renderer.setDefaultItemLabelPaint(foreground);
        plot.setRenderer(renderer);

        panel.setZoomFillPaint(alphaColor(selectionColor, 0.2f));

        MultiXYTextAnnotation annotation = (MultiXYTextAnnotation) plot.getAnnotations().get(0);
        annotation.setBackgroundPaint(background);
        annotation.setDefaultPaint(foreground);
        annotation.setFont(font);
        annotation.setOutlinePaint(border);
        annotation.setTitleLinePain(border);
        annotation.setGridLinePaint(selectionColor);
    }

    private void initSeriesStyle(XYBezierRenderer renderer, int series, Color color) {
        renderer.setSeriesPaint(series, color);
        renderer.setSeriesStroke(series, new BasicStroke(UIScale.scale(1.5f)));
        int s = UIScale.scale(3);
        renderer.setSeriesShape(series, new Ellipse2D.Float(-s, -s, s * 2, s * 2));

        renderer.setSeriesOutlinePaint(series, alphaColor(color, 0.2f));
        renderer.setSeriesOutlineStroke(series, new BasicStroke(UIScale.scale(6f)));
        renderer.setUseOutlinePaint(true);
    }

    @Override
    protected void createAnnotation(ChartPanel chartPanel) {
        XYPlot plot = (XYPlot) chartPanel.getChart().getPlot();
        MultiXYTextAnnotation annotation = new MultiXYTextAnnotation();

        DateFormat titleFormat = DateFormat.getDateInstance();
        NumberFormat valueFormat = NumberFormat.getCurrencyInstance();
        annotation.setTitleGenerator(xValue -> titleFormat.format(new Date((long) xValue)));
        annotation.setNumberFormat(valueFormat);

        plot.addAnnotation(annotation);
        chartPanel.addChartMouseListener(new ChartMouseListener() {
            @Override
            public void chartMouseClicked(ChartMouseEvent event) {
            }

            @Override
            public void chartMouseMoved(ChartMouseEvent event) {
                Rectangle2D dataArea = chartPanel.getScreenDataArea();
                if (!dataArea.contains(event.getTrigger().getPoint())) {
                    annotation.setLabels(null);
                    return;
                }
                double x = plot.getDomainAxis().java2DToValue(event.getTrigger().getX(), dataArea, plot.getDomainAxisEdge());
                TimeSeriesCollection dataset = (TimeSeriesCollection) plot.getDataset();
                int seriesCount = plot.getSeriesCount();

                double minDistance = Double.MAX_VALUE;
                double closestX = 0;
                boolean found = false;

                for (int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++) {
                    for (int itemIndex = 0; itemIndex < plot.getDataset().getItemCount(seriesIndex); itemIndex++) {
                        double dataX = dataset.getXValue(seriesIndex, itemIndex);
                        double distance = Math.abs(x - dataX);
                        if (distance < minDistance) {
                            minDistance = distance;
                            closestX = dataX;
                            found = true;
                        }
                    }
                }
                if (found) {
                    annotation.autoCalculateX(closestX, dataset);
                }
            }
        });
    }
}
