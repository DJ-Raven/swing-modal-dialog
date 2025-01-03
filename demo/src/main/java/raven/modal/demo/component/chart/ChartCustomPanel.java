package raven.modal.demo.component.chart;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import javax.swing.*;
import java.awt.*;

public abstract class ChartCustomPanel extends JPanel {

    protected JFreeChart freeChart;
    protected ChartPanel chartPanel;

    public ChartCustomPanel() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("wrap,fillx,gap 0", "[fill]"));
        putClientProperty(FlatClientProperties.STYLE_CLASS, "dashboardBackground");

        freeChart = createChart();
        chartPanel = new ChartPanel(freeChart);
        createAnnotation(chartPanel);
        applyStyledChart(freeChart, chartPanel);

        add(chartPanel);
    }

    private void applyStyledChart(JFreeChart chart, ChartPanel panel) {
        Color background = getBackground();
        Color foreground = getForeground();
        Font font = getFont();

        // chart
        chart.setBackgroundPaint(background);

        // plot
        chart.getPlot().setBackgroundPaint(background);
        chart.getPlot().setOutlinePaint(background);

        // legend
        if (chart.getLegend() != null) {
            chart.getLegend().setBackgroundPaint(background);
            chart.getLegend().setItemPaint(foreground);
            chart.getLegend().setItemFont(font);
        }

        // panel
        chartPanel.setPopupMenu(null);
        styleChart(chart, panel);
    }

    protected abstract JFreeChart createChart();

    protected abstract void createAnnotation(ChartPanel panel);

    protected void styleChart(JFreeChart chart, ChartPanel panel) {
    }

    @Override
    public void updateUI() {
        super.updateUI();
        if (freeChart != null && chartPanel != null) {
            applyStyledChart(freeChart, chartPanel);
        }
    }
}
