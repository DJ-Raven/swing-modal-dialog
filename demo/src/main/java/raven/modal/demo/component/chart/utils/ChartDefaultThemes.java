package raven.modal.demo.component.chart.utils;

import java.awt.*;

public class ChartDefaultThemes {

    private final static Color[] defaultColors = new Color[]{
            Color.decode("#10b981"),
            Color.decode("#f43f5e"),
            Color.decode("#0ea5e9"),
            Color.decode("#737373")
    };

    public static Color[] getDefaultColor() {
        return defaultColors;
    }
}
