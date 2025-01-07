package raven.modal.demo.component.chart;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ui.RectangleInsets;
import raven.modal.demo.component.chart.themes.ChartDrawingSupplier;
import raven.modal.demo.component.chart.themes.DefaultChartTheme;

import javax.swing.*;
import java.awt.*;

public abstract class DefaultChartPanel extends JPanel {

    public JFreeChart getFreeChart() {
        return freeChart;
    }

    public ChartPanel getChartPanel() {
        return chartPanel;
    }

    protected JFreeChart freeChart;
    protected ChartPanel chartPanel;

    public DefaultChartPanel() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("wrap,fillx,gap 0", "[fill]"));
        putClientProperty(FlatClientProperties.STYLE_CLASS, "dashboardBackground");

        freeChart = createChart();
        chartPanel = new ChartPanel(freeChart);
        chartPanel.putClientProperty(FlatClientProperties.STYLE, "" +
                "background:null;");
        createAnnotation(freeChart, chartPanel);
        defaultStyleChart(freeChart, chartPanel);
        applyStyledChart(freeChart, chartPanel);

        add(chartPanel);
    }

    private void applyStyledChart(JFreeChart chart, ChartPanel panel) {
        Color selectionColor = UIManager.getColor("List.selectionBackground");

        // themes
        DefaultChartTheme.applyTheme(chart);
        // panel
        chartPanel.setPopupMenu(null);
        chartPanel.setZoomFillPaint(ChartDrawingSupplier.alpha(selectionColor, 0.2f));

        // legend
        if (chart.getLegend() != null) {
            RectangleInsets insets = ChartDrawingSupplier.scaleRectangleInsets(new RectangleInsets(2, 2, 2, 2));
            chart.getLegend().setItemLabelPadding(insets);
        }
        styleChart(chart, panel);
    }

    protected abstract JFreeChart createChart();

    protected void defaultStyleChart(JFreeChart chart, ChartPanel panel) {
    }

    protected void styleChart(JFreeChart chart, ChartPanel panel) {
    }

    protected void createAnnotation(JFreeChart chart, ChartPanel panel) {
    }

    @Override
    public void updateUI() {
        super.updateUI();
        if (freeChart != null && chartPanel != null) {
            applyStyledChart(freeChart, chartPanel);
        }
    }
}
