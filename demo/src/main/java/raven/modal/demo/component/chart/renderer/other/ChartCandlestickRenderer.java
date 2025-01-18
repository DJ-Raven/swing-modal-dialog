package raven.modal.demo.component.chart.renderer.other;

import com.formdev.flatlaf.util.UIScale;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.data.xy.OHLCDataset;

import java.awt.*;

public class ChartCandlestickRenderer extends CandlestickRenderer {

    public ChartCandlestickRenderer() {
        initRedGreenColor(this);
        setCandleWidth(UIScale.scale(8));
        setDefaultToolTipGenerator(null);
    }

    @Override
    public Paint getItemPaint(int row, int column) {
        OHLCDataset highLowData = (OHLCDataset) getPlot().getDataset();
        double yOpen = highLowData.getOpenValue(row, column);
        double yClose = highLowData.getCloseValue(row, column);
        boolean isUpCandle = yClose > yOpen;
        if (isUpCandle) {
            return getUpPaint();
        } else {
            return getDownPaint();
        }
    }

    public static void initRedGreenColor(CandlestickRenderer renderer) {
        renderer.setDownPaint(new Color(241, 89, 89));
        renderer.setUpPaint(new Color(37, 176, 127));
    }
}
