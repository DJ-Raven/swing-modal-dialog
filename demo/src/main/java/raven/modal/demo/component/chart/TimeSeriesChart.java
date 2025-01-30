package raven.modal.demo.component.chart;

import org.jfree.chart.*;
import org.jfree.chart.axis.Axis;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.TableXYDataset;
import org.jfree.data.xy.XYDataset;
import raven.modal.demo.component.chart.renderer.ChartXYCurveRenderer;
import raven.modal.demo.component.chart.themes.ChartDrawingSupplier;
import raven.modal.demo.component.chart.themes.DefaultChartTheme;
import raven.modal.demo.component.chart.utils.MultiXYTextAnnotation;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;

public class TimeSeriesChart extends DefaultChartPanel {

    private XYItemRenderer renderer;

    public TimeSeriesChart() {
        super();
    }

    @Override
    protected JFreeChart createChart() {
        JFreeChart chart = ChartFactory.createTimeSeriesChart(null, null, null, null);
        return chart;
    }

    public void setRenderer(XYItemRenderer renderer) {
        if (this.renderer != renderer) {
            this.renderer = renderer;
            XYPlot plot = (XYPlot) freeChart.getPlot();
            plot.setRenderer(renderer);
            DefaultChartTheme.applyTheme(freeChart);
        }
    }

    @Override
    protected void defaultStyleChart(JFreeChart chart, ChartPanel panel) {
        XYPlot plot = (XYPlot) chart.getPlot();
        NumberAxis range = (NumberAxis) plot.getRangeAxis();
        DateAxis domain = (DateAxis) plot.getDomainAxis();

        range.setNumberFormatOverride(NumberFormat.getCurrencyInstance());
        range.setAxisLineVisible(false);
        range.setTickMarksVisible(false);
        range.setUpperMargin(0.2);
        range.setLowerMargin(0.1);

        domain.setAxisLineVisible(false);
        domain.setTickMarksVisible(false);

        plot.setDomainPannable(true);
        plot.setRangeGridlinesVisible(false);

        plot.setRenderer(getDefaultRender());
    }

    @Override
    protected void styleChart(JFreeChart chart, ChartPanel panel) {
        XYPlot plot = (XYPlot) chart.getPlot();
        Color background = getBackground();
        Color foreground = getForeground();
        Font font = getFont();
        Color selectionColor = UIManager.getColor("List.selectionBackground");
        Color border = UIManager.getColor("Component.borderColor");

        NumberAxis range = (NumberAxis) plot.getRangeAxis();
        DateAxis domain = (DateAxis) plot.getDomainAxis();

        range.setTickLabelInsets(ChartDrawingSupplier.scaleRectangleInsets(Axis.DEFAULT_TICK_LABEL_INSETS));
        domain.setTickLabelInsets(ChartDrawingSupplier.scaleRectangleInsets(Axis.DEFAULT_TICK_LABEL_INSETS));

        plot.setDomainGridlineStroke(ChartDrawingSupplier.getDefaultGridlineStroke());
        plot.setInsets(ChartDrawingSupplier.scaleRectangleInsets(4, 8, 15, 8));

        // annotation
        MultiXYTextAnnotation annotation = (MultiXYTextAnnotation) plot.getAnnotations().get(0);
        annotation.setBackgroundPaint(ChartDrawingSupplier.alpha(background, 0.7f));
        annotation.setDefaultPaint(foreground);
        annotation.setFont(font);
        annotation.setOutlinePaint(border);
        annotation.setTitleLinePain(border);
        annotation.setGridLinePaint(selectionColor);
    }


    @Override
    protected void createAnnotation(JFreeChart chart, ChartPanel chartPanel) {
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
                TableXYDataset dataset = (TableXYDataset) plot.getDataset();
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

    public void setDataset(XYDataset dataset) {
        freeChart.getXYPlot().setDataset(dataset);
    }
}
