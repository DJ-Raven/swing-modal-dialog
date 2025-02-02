package raven.flatlaf.themes.styles;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.ui.MigLayoutVisualPadding;

import javax.swing.*;
import java.awt.*;

/**
 * @author Raven
 */
public class ThemesStyles {

    public static void setDefaultThemesStyles(DefaultThemesStyles defaultStyles) {
        defaultThemesStyles = defaultStyles;
    }

    private static DefaultThemesStyles defaultThemesStyles;

    private static String getStyleValue(JComponent component, String key, StyleValue value) {
        if (value != null) {
            return value.getValue();
        }
        return defaultThemesStyles.getDefault(component, key);
    }

    protected interface StyleValue {

        String getValue();

        String getKey();
    }

    public enum Color implements StyleValue {
        DEFAULT("default"),
        ACCENT("accent"),
        PRIMARY("primary"),
        SECONDARY("secondary"),
        INFO("info"),
        SUCCESS("success"),
        WARNING("warning"),
        DANGER("danger"),
        GHOST("ghost");

        public static String KEY = "color";
        private final String value;

        Color(String value) {
            this.value = value;
        }

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public String getKey() {
            return KEY;
        }
    }

    public enum Border implements StyleValue {
        DEFAULT("borderDefault"),
        OUTLINE("borderOutline");

        public static String KEY = "border";
        private final String value;

        Border(String value) {
            this.value = value;
        }

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public String getKey() {
            return KEY;
        }
    }

    public static StyleBuilder build() {
        return new StyleBuilder();
    }

    public static class StyleBuilder {

        private Color color;
        private Border border;

        private StyleBuilder() {
        }

        public StyleBuilder color(Color color) {
            this.color = color;
            return this;
        }

        public StyleBuilder border(Border border) {
            this.border = border;
            return this;
        }

        public void apply(JComponent... components) {
            if (components.length == 0) {
                throw new IllegalArgumentException("Themes styles apply with empty components");
            }
            // init default if null
            if (defaultThemesStyles == null) {
                defaultThemesStyles = new DefaultThemesStyles();
            }

            for (JComponent component : components) {
                String styles[] = {
                        "style",
                        getStyleValue(component, Color.KEY, color),
                        getStyleValue(component, Border.KEY, border)
                };
                component.putClientProperty(FlatClientProperties.STYLE_CLASS, styles);
                adjustComponent(component);
            }
        }

        private void adjustComponent(JComponent component) {
            if (component instanceof JScrollPane) {
                MigLayoutVisualPadding.uninstall(component);
                MigLayoutVisualPadding.install(component, new Insets(2, 0, 2, 0));
                component.putClientProperty(FlatClientProperties.STYLE, "" +
                        "viewportBorder:3,-2,3,-2");
            }
        }
    }
}
