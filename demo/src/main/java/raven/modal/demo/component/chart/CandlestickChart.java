package raven.modal.demo.component.chart;

import org.jfree.chart.*;
import org.jfree.chart.axis.Axis;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCrosshairLabelGenerator;
import org.jfree.chart.panel.CrosshairOverlay;
import org.jfree.chart.plot.Crosshair;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ui.RectangleAnchor;
import org.jfree.data.xy.OHLCDataset;
import raven.modal.demo.component.chart.renderer.other.ChartCandlestickRenderer;
import raven.modal.demo.component.chart.themes.ChartDrawingSupplier;
import raven.modal.demo.component.chart.utils.CustomCrosshairToolTip;
import raven.modal.demo.component.chart.utils.DateCrosshairLabelGenerator;

import java.awt.geom.Rectangle2D;
import java.text.DateFormat;
import java.text.NumberFormat;

public class CandlestickChart extends DefaultChartPanel {

    private CustomCrosshairToolTip xCrosshair;
    private CustomCrosshairToolTip yCrosshair;

    @Override
    protected JFreeChart createChart() {
        JFreeChart freeChart = ChartFactory.createCandlestickChart(null, null, null, null, false);
        return freeChart;
    }

    @Override
    protected void defaultStyleChart(JFreeChart chart, ChartPanel panel) {
        XYPlot plot = chart.getXYPlot();
        NumberAxis range = (NumberAxis) plot.getRangeAxis();
        DateAxis domain = (DateAxis) plot.getDomainAxis();

        range.setNumberFormatOverride(NumberFormat.getCurrencyInstance());
        range.setAxisLineVisible(false);
        range.setTickMarksVisible(false);
        range.setAutoRangeIncludesZero(false);
        range.setLowerMargin(0.15);

        domain.setUpperMargin(0.1);
        domain.setAxisLineVisible(false);
        domain.setTickMarksVisible(false);

        plot.setDomainPannable(true);
        plot.setRangeGridlinesVisible(false);
        plot.setDomainGridlinesVisible(false);
        plot.setRenderer(new ChartCandlestickRenderer());

        panel.setMouseWheelEnabled(false);
        panel.setRangeZoomable(false);
    }

    @Override
    protected void styleChart(JFreeChart chart, ChartPanel panel) {
        XYPlot plot = chart.getXYPlot();
        NumberAxis range = (NumberAxis) plot.getRangeAxis();
        DateAxis domain = (DateAxis) plot.getDomainAxis();

        range.setTickLabelInsets(ChartDrawingSupplier.scaleRectangleInsets(Axis.DEFAULT_TICK_LABEL_INSETS));
        domain.setTickLabelInsets(ChartDrawingSupplier.scaleRectangleInsets(Axis.DEFAULT_TICK_LABEL_INSETS));

        plot.setDomainGridlineStroke(ChartDrawingSupplier.getDefaultGridlineStroke());
        plot.setInsets(ChartDrawingSupplier.scaleRectangleInsets(4, 8, 15, 8));
        if (xCrosshair != null) {
            xCrosshair.installStyle();
        }
        if (yCrosshair != null) {
            yCrosshair.installStyle();
        }
    }

    @Override
    protected void createAnnotation(JFreeChart chart, ChartPanel panel) {
        xCrosshair = new CustomCrosshairToolTip();
        yCrosshair = new CustomCrosshairToolTip();
        chartPanel.addOverlay(createCrosshair(chartPanel));
    }

    private CrosshairOverlay createCrosshair(ChartPanel panel) {
        CrosshairOverlay crosshairOverlay = new CrosshairOverlay();
        xCrosshair.setLabelAnchor(RectangleAnchor.BOTTOM);
        yCrosshair.setLabelAnchor(RectangleAnchor.RIGHT);
        DateFormat dateFormat = DateFormat.getDateInstance();
        xCrosshair.setLabelGenerator(new DateCrosshairLabelGenerator(" {0} ", dateFormat));
        yCrosshair.setLabelGenerator(new StandardCrosshairLabelGenerator(" {0} ", NumberFormat.getCurrencyInstance()));
        xCrosshair.setLabelVisible(true);
        yCrosshair.setLabelVisible(true);
        crosshairOverlay.addDomainCrosshair(xCrosshair);
        crosshairOverlay.addRangeCrosshair(yCrosshair);

        panel.addChartMouseListener(new ChartMouseListener() {

            private void showCrosshair(Crosshair crosshair, boolean visible) {
                if (crosshair.isVisible() != visible) {
                    crosshair.setVisible(visible);
                }
            }

            @Override
            public void chartMouseClicked(ChartMouseEvent chartMouseEvent) {
            }

            @Override
            public void chartMouseMoved(ChartMouseEvent event) {
                Rectangle2D dataArea = panel.getScreenDataArea();
                if (!dataArea.contains(event.getTrigger().getPoint())) {
                    showCrosshair(xCrosshair, false);
                    showCrosshair(yCrosshair, false);
                    return;
                }
                showCrosshair(xCrosshair, true);
                showCrosshair(yCrosshair, true);

                JFreeChart chart = panel.getChart();
                XYPlot plot = (XYPlot) chart.getPlot();
                int seriesCount = plot.getSeriesCount();
                OHLCDataset dataset = (OHLCDataset) plot.getDataset();

                double x = plot.getDomainAxis().java2DToValue(event.getTrigger().getX(), dataArea, plot.getDomainAxisEdge());
                double y = plot.getRangeAxis().java2DToValue(event.getTrigger().getY(), dataArea, plot.getRangeAxisEdge());

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
                    xCrosshair.setValue(closestX);
                }
                yCrosshair.setValue(y);
            }
        });
        return crosshairOverlay;
    }

    public void setDataset(OHLCDataset dataset) {
        freeChart.getXYPlot().setDataset(dataset);
    }
}
