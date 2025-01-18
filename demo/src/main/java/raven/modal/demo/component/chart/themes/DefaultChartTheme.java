package raven.modal.demo.component.chart.themes;

import com.formdev.flatlaf.util.UIScale;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.SpiderWebPlot;
import org.jfree.chart.renderer.AbstractRenderer;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYDifferenceRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.ui.RectangleEdge;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RectangularShape;

public class DefaultChartTheme extends StandardChartTheme {

    public static DefaultChartTheme getInstance() {
        return instance;
    }

    private static DefaultChartTheme instance = new DefaultChartTheme();
    public ColorThemes colorThemes = ColorThemes.DEFAULT;

    private DefaultChartTheme() {
        super("Default Themes", false);
        init();
    }

    private void init() {
        Color background = new Color(0, 0, 0, 0);
        Color foreground = UIManager.getColor("Label.foreground");
        Color border = UIManager.getColor("Component.borderColor");
        Font font = UIManager.getFont("Label.font");

        setDrawingSupplier(new ChartDrawingSupplier(colorThemes));
        // chart
        setChartBackgroundPaint(background);

        // plot
        setPlotBackgroundPaint(background);
        setPlotOutlinePaint(background);

        // renderer
        setDomainGridlinePaint(border);
        setRangeGridlinePaint(border);

        setBarPainter(new AlphaBarPainter());
        setXYBarPainter(new StandardXYBarPainter());

        // text
        setRegularFont(font);
        setTitlePaint(foreground);
        setSubtitlePaint(foreground);
        setTickLabelPaint(foreground);
        setItemLabelPaint(foreground);
        setLabelLinkPaint(border);

        // legend
        setLegendBackgroundPaint(background);
        setLegendItemPaint(foreground);

        // other
    }

    public static boolean setChartColors(ColorThemes colorThemes) {
        if (instance.colorThemes != colorThemes) {
            instance.colorThemes = colorThemes;
            instance.setDrawingSupplier(new ChartDrawingSupplier(colorThemes));
            return true;
        }
        return false;
    }

    public static Color getColor(int index) {
        Color[] colors = instance.colorThemes.getColors();
        if (index > colors.length - 1) {
            return colors[colors.length - 1];
        }
        return colors[index];
    }

    public static Color[] getColors() {
        return instance.colorThemes.getColors();
    }

    public static void applyTheme(JFreeChart chart) {
        instance.init();
        instance.apply(chart);
    }

    @Override
    protected void applyToSpiderWebPlot(SpiderWebPlot plot) {
        Color border = UIManager.getColor("Component.borderColor");
        plot.setLabelFont(getRegularFont());
        plot.setLabelPaint(getAxisLabelPaint());
        plot.setAxisLinePaint(border);
        int index = 0;
        for (Color color : instance.colorThemes.getColors()) {
            boolean alpha = false;
            Paint olePaint = plot.getSeriesPaint(index);
            if (olePaint instanceof Color) {
                alpha = ((Color) olePaint).getAlpha() < 255;
            }
            Paint c = alpha ? ChartDrawingSupplier.alpha(color, 0.3f) : color;
            plot.setSeriesPaint(index, c);
            plot.setSeriesOutlinePaint(index, c);
            plot.setSeriesOutlineStroke(index, new BasicStroke(UIScale.scale(1f)));
            index++;
        }
    }

    @Override
    protected void applyToXYItemRenderer(XYItemRenderer renderer) {
        super.applyToXYItemRenderer(renderer);
        if (renderer != null) {
            if (renderer instanceof XYDifferenceRenderer) {
                XYDifferenceRenderer r = (XYDifferenceRenderer) renderer;
                if (r.getAutoPopulateSeriesPaint()) {
                    r.setPositivePaint(getColor(0));
                    r.setNegativePaint(getColor(1));
                }
            } else if (renderer instanceof CandlestickRenderer) {
                CandlestickRenderer r = (CandlestickRenderer) renderer;
                if (r.getAutoPopulateSeriesPaint()) {
                    r.setDownPaint(getColor(0));
                    r.setUpPaint(getColor(1));
                }
            }
        }
    }

    @Override
    protected void applyToAbstractRenderer(AbstractRenderer renderer) {
        super.applyToAbstractRenderer(renderer);

        // apply null to series paint to get the new series paint
        if (renderer.getAutoPopulateSeriesOutlinePaint()) {
            int index = 0;
            while (renderer.getSeriesOutlinePaint(index) != null) {
                renderer.setSeriesOutlinePaint(index, null);
                index++;
            }
        }
    }

    @Override
    protected void applyToPiePlot(PiePlot plot) {
        plot.setLabelPaint(getItemLabelPaint());
        super.applyToPiePlot(plot);
    }

    public class AlphaBarPainter extends StandardBarPainter {
        @Override
        public void paintBar(Graphics2D g2, BarRenderer renderer, int row, int column, RectangularShape bar, RectangleEdge base) {
            g2.setComposite(AlphaComposite.SrcOver.derive(0.8f));
            super.paintBar(g2, renderer, row, column, bar, base);
        }
    }
}
