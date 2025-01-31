package raven.flatlaf.themes.styles;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Raven
 */
public class DefaultThemesStyles {

    protected Map<String, ThemesStyles.StyleValue> componentDefault;
    protected Map<Class<? extends JComponent>, Map<String, ThemesStyles.StyleValue>> defaultClass;

    public DefaultThemesStyles() {
        createDefault();
    }

    private Map<String, ThemesStyles.StyleValue> getComponentDefault() {
        if (componentDefault == null) {
            componentDefault = new HashMap<>();
        }
        return componentDefault;
    }

    private Map<String, ThemesStyles.StyleValue> get(Class<? extends JComponent> clazz) {
        if (defaultClass == null) {
            defaultClass = new HashMap<>();
        }
        Map<String, ThemesStyles.StyleValue> map = defaultClass.get(clazz);
        if (map == null) {
            map = new HashMap<>();
            defaultClass.put(clazz, map);
        }
        return map;
    }

    private void putDefault(ThemesStyles.StyleValue value) {
        getComponentDefault().put(value.getKey(), value);
    }

    private void put(Class<? extends JComponent> clazz, ThemesStyles.StyleValue value) {
        get(clazz).put(value.getKey(), value);
    }

    public DefaultThemesStyles setDefaultStyleClass(ThemesStyles.StyleValue value) {
        return setDefaultStyleClass(JComponent.class, value);
    }

    public DefaultThemesStyles setDefaultStyleClass(Class<? extends JComponent> clazz, ThemesStyles.StyleValue value) {
        if (clazz == JComponent.class) {
            putDefault(value);
        } else {
            put(clazz, value);
        }
        return this;
    }

    protected String getDefault(JComponent component, String key) {
        Class<? extends JComponent> clazz = component.getClass();
        ThemesStyles.StyleValue value = null;
        if (defaultClass != null) {
            Map<String, ThemesStyles.StyleValue> map = defaultClass.get(clazz);
            if (map != null) {
                value = map.get(key);
            }
        }
        if (value == null) {
            value = componentDefault.get(key);
        }
        return value.getValue();
    }

    private void createDefault() {
        putDefault(ThemesStyles.Color.DEFAULT);
        putDefault(ThemesStyles.Border.DEFAULT);
    }
}
