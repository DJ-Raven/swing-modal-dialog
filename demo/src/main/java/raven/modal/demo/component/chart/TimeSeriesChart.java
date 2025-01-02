package raven.modal.demo.component.chart;

import com.formdev.flatlaf.util.UIScale;
import org.jfree.chart.*;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.TimeSeriesCollection;
import raven.modal.demo.component.chart.renderer.ChartXYCurveRenderer;
import raven.modal.demo.component.chart.utils.ChartUtils;
import raven.modal.demo.component.chart.utils.MultiXYTextAnnotation;
import raven.modal.demo.sample.SampleData;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;

public class TimeSeriesChart extends ChartCustomPanel {

    private XYItemRenderer renderer;

    public TimeSeriesChart() {
        super();
    }

    @Override
    protected JFreeChart createChart() {
        JFreeChart chart = ChartFactory.createTimeSeriesChart(null, null, null, SampleData.createTimeSeriesDataset());
        return chart;
    }

    public void setRenderer(XYItemRenderer renderer) {
        if (this.renderer != renderer) {
            this.renderer = renderer;
            XYPlot plot = (XYPlot) freeChart.getPlot();
            plot.setRenderer(renderer);
        }
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

        plot.setRenderer(getDefaultRender());

        panel.setZoomFillPaint(ChartUtils.alphaColor(selectionColor, 0.2f));

        MultiXYTextAnnotation annotation = (MultiXYTextAnnotation) plot.getAnnotations().get(0);
        annotation.setBackgroundPaint(background);
        annotation.setDefaultPaint(foreground);
        annotation.setFont(font);
        annotation.setOutlinePaint(border);
        annotation.setTitleLinePain(border);
        annotation.setGridLinePaint(selectionColor);
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

    public XYItemRenderer getDefaultRender() {
        if (renderer == null) {
            renderer = new ChartXYCurveRenderer();
        }
        return renderer;
    }
}
