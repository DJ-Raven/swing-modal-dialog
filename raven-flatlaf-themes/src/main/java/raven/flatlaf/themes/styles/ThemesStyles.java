package raven.flatlaf.themes.styles;

import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import javax.swing.table.JTableHeader;

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
        NEUTRAL("neutral"),
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
        NONE("borderNone");

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
                String borderStyle = getStyleValue(component, Border.KEY, border);
                String prefix = getPrefix(component);
                String styles[] = {
                        prefix + "style",
                        prefix + getStyleValue(component, Color.KEY, color),
                        prefix + borderStyle
                };
                component.putClientProperty(FlatClientProperties.STYLE_CLASS, styles);
                adjustComponent(component, styles);
            }
        }

        private String getPrefix(JComponent component) {
            if (isIcon(component)) {
                return "icon.";
            } else if (isUnSupportBorder(component)) {
                return "noBorder.";
            }
            return "";
        }

        private boolean isIcon(JComponent component) {
            return component instanceof JRadioButton || component instanceof JCheckBox;
        }

        private boolean isUnSupportBorder(JComponent component) {
            return component instanceof JList || component instanceof JTextArea || component instanceof JTable;
        }

        private void adjustComponent(JComponent component, String[] styles) {
            if (component instanceof JScrollPane) {
                component.putClientProperty(FlatClientProperties.STYLE, "" +
                        "viewportBorder:3,0,3,0;");
            } else if (component instanceof JTable) {
                JTableHeader tableHeader = ((JTable) component).getTableHeader();
                if (tableHeader != null) {
                    tableHeader.putClientProperty(FlatClientProperties.STYLE_CLASS, styles);
                }
            }
        }
    }
}
