package raven.modal.demo.component.chart;

import com.formdev.flatlaf.util.UIScale;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.LegendItemEntity;
import org.jfree.chart.plot.SpiderWebPlot;
import org.jfree.data.category.CategoryDataset;
import raven.modal.demo.component.chart.themes.ChartDrawingSupplier;
import raven.modal.demo.component.chart.themes.DefaultChartTheme;

import java.awt.*;
import java.util.List;

public class SpiderChart extends DefaultChartPanel {

    public SpiderChart() {
    }

    @Override
    protected JFreeChart createChart() {
        SpiderWebPlot plot = new SpiderWebPlot();
        JFreeChart freeChart = new JFreeChart(null, null, plot, true);
        return freeChart;
    }

    @Override
    protected void defaultStyleChart(JFreeChart chart, ChartPanel panel) {
        SpiderWebPlot plot = (SpiderWebPlot) chart.getPlot();

        panel.addChartMouseListener(new ChartMouseListener() {

            @Override
            public void chartMouseClicked(ChartMouseEvent evt) {
                if (evt.getEntity() instanceof LegendItemEntity) {
                    LegendItemEntity entity = (LegendItemEntity) evt.getEntity();
                    Comparable key = entity.getSeriesKey();
                    setSectionKey(key);
                }
            }

            @Override
            public void chartMouseMoved(ChartMouseEvent evt) {
            }

            private void setSectionKey(Comparable key) {
                List<Comparable> keys = plot.getDataset().getRowKeys();
                int index = 0;
                for (Comparable k : keys) {
                    if (k == key) {
                        Paint paint = plot.getSeriesPaint(index);
                        if (paint instanceof Color) {
                            if (((Color) paint).getAlpha() == 255) {
                                plot.setSeriesPaint(index, ChartDrawingSupplier.alpha(paint, 0.3f));
                            } else {
                                plot.setSeriesPaint(index, ChartDrawingSupplier.alpha(paint, 1f));
                            }
                        }
                        return;
                    }
                    index++;
                }
            }
        });
    }

    @Override
    protected void styleChart(JFreeChart chart, ChartPanel panel) {
        SpiderWebPlot plot = (SpiderWebPlot) chart.getPlot();

        plot.setLegendItemShape(ChartDrawingSupplier.getDefaultShape());
        plot.setSeriesOutlineStroke(new BasicStroke(UIScale.scale(1f)));
        plot.setAxisLineStroke(new BasicStroke(UIScale.scale(1f)));

        int index = 0;
        for (Color color : DefaultChartTheme.getColors()) {
            initSeriesStyle(plot, index++, color, index != 1);
        }
    }

    private void initSeriesStyle(SpiderWebPlot plot, int series, Color color, boolean alpha) {
        Paint c = alpha ? ChartDrawingSupplier.alpha(color, 0.3f) : color;
        plot.setSeriesPaint(series, c);
        plot.setSeriesOutlinePaint(series, c);
        plot.setSeriesOutlineStroke(series, new BasicStroke(UIScale.scale(1f)));
    }

    public void setDataset(CategoryDataset dataset) {
        SpiderWebPlot plot = (SpiderWebPlot) freeChart.getPlot();
        plot.setDataset(dataset);
    }
}
