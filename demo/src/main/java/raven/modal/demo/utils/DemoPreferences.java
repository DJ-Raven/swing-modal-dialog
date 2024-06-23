package raven.modal.demo.utils;

import com.formdev.flatlaf.*;
import com.formdev.flatlaf.util.LoggingFacade;
import raven.modal.demo.themes.PanelThemes;

import javax.swing.*;
import java.util.prefs.Preferences;

public class DemoPreferences {

    public static final String PREFERENCES_ROOT_PATH = "/raven-flatlaf-demo";
    public static final String KEY_LAF = "laf";
    public static final String KEY_LAF_THEME = "lafTheme";

    public static final String RESOURCE_PREFIX = "res:";

    public static final String THEME_UI_KEY = "__RaVen.flatlaf.demo.theme";

    private static Preferences state;

    public static Preferences getState() {
        return state;
    }

    public static void init() {
        state = Preferences.userRoot().node(PREFERENCES_ROOT_PATH);
    }

    public static void setupLaf() {
        // set look and feel
        try {
            String lafClassName = state.get(KEY_LAF, FlatLightLaf.class.getName());
            if (IntelliJTheme.ThemeLaf.class.getName().equals(lafClassName)) {
                String theme = state.get(KEY_LAF_THEME, "");
                if (theme.startsWith(RESOURCE_PREFIX)) {
                    IntelliJTheme.setup(PanelThemes.class.getResourceAsStream(PanelThemes.THEMES_PACKAGE + theme.substring(RESOURCE_PREFIX.length())));
                } else {
                    FlatLightLaf.setup();
                }
                if (!theme.isEmpty()) {
                    UIManager.getLookAndFeelDefaults().put(THEME_UI_KEY, theme);
                }
            } else {
                UIManager.setLookAndFeel(lafClassName);
            }
        } catch (Exception e) {
            LoggingFacade.INSTANCE.logSevere(null, e);
            FlatLightLaf.setup();
        }
        UIManager.addPropertyChangeListener(e -> {
            if (e.getPropertyName().equals("lookAndFeel")) {
                state.put(KEY_LAF, UIManager.getLookAndFeel().getClass().getName());
            }
        });
    }
}
