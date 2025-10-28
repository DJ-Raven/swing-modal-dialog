package raven.modal.demo.utils;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.IntelliJTheme;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.FontUtils;
import com.formdev.flatlaf.util.LoggingFacade;
import com.formdev.flatlaf.util.UIScale;
import raven.modal.demo.system.FormManager;
import raven.modal.demo.themes.PanelThemes;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;

public class DemoPreferences {

    public static final String PREFERENCES_ROOT_PATH = "/raven-flatlaf-demo";
    public static final String KEY_LAF = "laf";
    public static final String KEY_LAF_THEME = "lafTheme";
    public static final String KEY_ZOOM_FACTOR = "zoomFactor";
    public static final String KEY_RECENT_SEARCH = "recentSearch";
    public static final String KEY_RECENT_SEARCH_FAVORITE = "recentSearchFavorite";

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
            UIManager.put("defaultFont", FontUtils.getCompositeFont(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
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
        initZoom();
        UIManager.addPropertyChangeListener(e -> {
            if (e.getPropertyName().equals("lookAndFeel")) {
                state.put(KEY_LAF, UIManager.getLookAndFeel().getClass().getName());
            }
        });
    }

    private static void initZoom() {
        String scaleFactor = state.get(KEY_ZOOM_FACTOR, null);
        if (scaleFactor != null) {
            float f = Float.parseFloat(scaleFactor);
            if (f != 1) {
                UIScale.setZoomFactor(f);
            }
        }
        UIScale.addPropertyChangeListener(e -> {
            if (UIScale.PROP_USER_SCALE_FACTOR.equals(e.getPropertyName())) {
                Window window = FormManager.getFrame();
                if (window != null) {
                    zoomWindowBounds(window, (float) e.getOldValue(), (float) e.getNewValue());
                }
            }
        });
    }

    private static void zoomWindowBounds(Window window, float oldZoomFactor, float newZoomFactor) {
        if (window instanceof Frame && ((Frame) window).getExtendedState() != Frame.NORMAL)
            return;

        Rectangle oldBounds = window.getBounds();

        // zoom window bounds
        float factor = (1f / oldZoomFactor) * newZoomFactor;
        int newWidth = (int) (oldBounds.width * factor);
        int newHeight = (int) (oldBounds.height * factor);
        int newX = oldBounds.x - ((newWidth - oldBounds.width) / 2);
        int newY = oldBounds.y - ((newHeight - oldBounds.height) / 2);

        // get maximum window bounds (screen bounds minus screen insets)
        GraphicsConfiguration gc = window.getGraphicsConfiguration();
        Rectangle screenBounds = gc.getBounds();
        Insets screenInsets = FlatUIUtils.getScreenInsets(gc);
        Rectangle maxBounds = FlatUIUtils.subtractInsets(screenBounds, screenInsets);

        // limit new window width/height
        newWidth = Math.min(newWidth, maxBounds.width);
        newHeight = Math.min(newHeight, maxBounds.height);

        // move window into screen bounds
        newX = Math.max(Math.min(newX, maxBounds.width - newWidth), maxBounds.x);
        newY = Math.max(Math.min(newY, maxBounds.height - newHeight), maxBounds.y);

        // set new window bounds
        window.setBounds(newX, newY, newWidth, newHeight);
    }

    public static String[] getRecentSearch(boolean favorite) {
        String stringArr = state.get(favorite ? KEY_RECENT_SEARCH_FAVORITE : KEY_RECENT_SEARCH, null);
        if (stringArr == null || stringArr.trim().isEmpty()) return null;
        return stringArr.trim().split(",");
    }

    public static void addRecentSearch(String value, boolean favorite) {
        String[] oldRecent = getRecentSearch(false);
        String[] oldFavorite = getRecentSearch(true);
        if (favorite) {
            if (oldRecent != null) {
                // remove from recent search
                List<String> list = new ArrayList<>(Arrays.asList(oldRecent));
                list.remove(value);
                state.put(KEY_RECENT_SEARCH, String.join(",", list));
            }
            if (oldFavorite != null) {
                List<String> list = new ArrayList<>(Arrays.asList(oldFavorite));
                list.remove(value);
                list.add(0, value);
                state.put(KEY_RECENT_SEARCH_FAVORITE, String.join(",", list));
            } else {
                state.put(KEY_RECENT_SEARCH_FAVORITE, value);
            }
        } else {
            if (oldFavorite != null) {
                List<String> list = new ArrayList<>(Arrays.asList(oldFavorite));
                if (list.contains(value)) {
                    return;
                }
            }
            if (oldRecent == null) {
                state.put(KEY_RECENT_SEARCH, value);
            } else {
                List<String> list = new ArrayList<>(Arrays.asList(oldRecent));
                list.remove(value);
                list.add(0, value);
                state.put(KEY_RECENT_SEARCH, String.join(",", list));
            }
        }
    }

    public static void removeRecentSearch(String value, boolean favorite) {
        String[] oldRecent = getRecentSearch(favorite);
        if (oldRecent != null) {
            List<String> list = new ArrayList<>(Arrays.asList(oldRecent));
            list.remove(value);
            state.put(favorite ? KEY_RECENT_SEARCH_FAVORITE : KEY_RECENT_SEARCH, String.join(",", list));
        }
    }
}
