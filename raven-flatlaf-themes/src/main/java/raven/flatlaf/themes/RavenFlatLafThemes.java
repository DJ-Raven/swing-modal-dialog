package raven.flatlaf.themes;

import com.formdev.flatlaf.FlatLaf;
import raven.flatlaf.themes.styles.DefaultThemesStyles;
import raven.flatlaf.themes.styles.ThemesStyles;

/**
 * @author Raven
 */
public class RavenFlatLafThemes {

    private static final String THEMES_SOURCE = "raven.flatlaf.themes";

    public static void install(DefaultThemesStyles defaultThemesStyles) {
        ThemesStyles.setDefaultThemesStyles(defaultThemesStyles);
        install();
    }

    public static void install() {
        FlatLaf.registerCustomDefaultsSource(THEMES_SOURCE);
    }
}
