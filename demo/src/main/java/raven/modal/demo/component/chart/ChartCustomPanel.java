package raven.modal.demo.component.chart;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.util.UIScale;
import net.miginfocom.swing.MigLayout;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ui.RectangleInsets;

import javax.swing.*;
import java.awt.*;

public abstract class ChartCustomPanel extends JPanel {

    private JFreeChart freeChart;
    private ChartPanel chartPanel;

    public ChartCustomPanel() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("fill", "[fill]", "[fill]"));
        putClientProperty(FlatClientProperties.STYLE_CLASS, "dashboardBackground");

        freeChart = createChart();
        chartPanel = new ChartPanel(freeChart);

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
        chart.getLegend().setBackgroundPaint(background);
        chart.getLegend().setItemPaint(foreground);
        chart.getLegend().setItemFont(font);

        styleChart(chart, panel);
    }

    protected abstract JFreeChart createChart();

    protected void styleChart(JFreeChart chart, ChartPanel panel) {
    }

    protected RectangleInsets scaleRectangleInsets(int top, int left, int bottom, int right) {
        return new RectangleInsets(UIScale.scale(top), UIScale.scale(left), UIScale.scale(bottom), UIScale.scale(right));
    }

    protected Color alphaColor(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    @Override
    public void updateUI() {
        super.updateUI();
        if (freeChart != null && chartPanel != null) {
            applyStyledChart(freeChart, chartPanel);
        }
    }
}
