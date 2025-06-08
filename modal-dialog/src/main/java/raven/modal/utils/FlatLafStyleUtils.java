package raven.modal.utils;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.ui.FlatStylingSupport;
import com.formdev.flatlaf.ui.FlatUIUtils;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * @author Raven
 */
public class FlatLafStyleUtils {

    public static String getStyleValue(JComponent component, String key, String defaultValue) {
        Map<String, String> styleMap = styleToMap(FlatClientProperties.clientProperty(component, FlatClientProperties.STYLE, null, String.class));
        return styleMap.getOrDefault(key, defaultValue);
    }

    public static void appendStyleIfAbsent(JComponent component, String style) {
        String oldStyle = FlatClientProperties.clientProperty(component, FlatClientProperties.STYLE, null, String.class);
        String styles = FlatLafStyleUtils.appendStyleIfAbsent(oldStyle, style);
        component.putClientProperty(FlatClientProperties.STYLE, styles);
    }

    public static void appendStyle(JComponent component, String style) {
        String oldStyle = FlatClientProperties.clientProperty(component, FlatClientProperties.STYLE, null, String.class);
        String styles = FlatLafStyleUtils.appendStyle(oldStyle, style);
        component.putClientProperty(FlatClientProperties.STYLE, styles);
    }

    public static String appendStyle(String oldStyle, String newStyle) {
        Map<String, String> oldStyleMap = styleToMap(oldStyle);
        Map<String, String> newStyleMap = styleToMap(newStyle);
        oldStyleMap.putAll(newStyleMap);
        return mapToString(oldStyleMap);
    }

    public static String appendStyleIfAbsent(String oldStyle, String newStyle) {
        Map<String, String> oldStyleMap = styleToMap(oldStyle);
        Map<String, String> newStyleMap = styleToMap(newStyle);
        newStyleMap.putAll(oldStyleMap);
        return mapToString(newStyleMap);
    }

    public static <T> T getStyleValue(JComponent com, String key, Class<T> type) {
        Object style = FlatStylingSupport.getStyle(com);
        if (style != null) {
            Object value = FlatStylingSupport.parse(style.toString()).get(key);
            if (type.isInstance(value)) {
                return (T) value;
            }
        }
        return null;
    }

    public static Map<String, String> styleToMap(String style) {
        Map<String, String> mapStyle = new HashMap<>();
        if (style != null) {
            String[] styles = style.split(";");
            for (String s : styles) {
                String[] parts = s.split(":");
                if (parts.length == 2) {
                    mapStyle.put(parts[0], parts[1]);
                }
            }
        }
        return mapStyle;
    }

    public static String mapToString(Map<String, String> mapStyle) {
        StringJoiner joiner = new StringJoiner(";");
        for (Map.Entry<String, String> entry : mapStyle.entrySet()) {
            joiner.add(entry.getKey() + ":" + entry.getValue());
        }
        return joiner.toString();
    }

    public static String appendMargin(JComponent button, Insets margin) {
        Insets defaultInset = FlatLafStyleUtils.getStyleValue(button, "margin", Insets.class);
        if (defaultInset != null) {
            margin = FlatUIUtils.addInsets(margin, defaultInset);
        }
        return margin.top + "," + margin.left + "," + margin.bottom + "," + margin.right;
    }
}
