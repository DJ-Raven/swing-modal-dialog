package raven.modal.demo.forms.setting.component;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.IntelliJTheme;
import com.formdev.flatlaf.icons.FlatAbstractIcon;
import com.formdev.flatlaf.util.UIScale;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class FlatLafThemeIcon extends FlatAbstractIcon {

    private FlatLaf theme;
    private Color background;
    private Color accentColor;
    private Color borderColor;

    public FlatLafThemeIcon(int width, int height, FlatLaf theme) {
        super(width, height, null);
        this.theme = theme;
        loadThemesColor();
    }

    private void loadThemesColor() {
        if (theme != null) {
            UIDefaults uiDefaults = theme.getDefaults();
            background = uiDefaults.getColor("Panel.background");
            borderColor = uiDefaults.getColor("Component.borderColor");
            if (theme instanceof IntelliJTheme.ThemeLaf) {
                if (isMaterialThemes(theme)) {
                    accentColor = uiDefaults.getColor("Button.default.focusColor");
                } else {
                    accentColor = uiDefaults.getColor("Button.default.endBackground");
                }
            } else {
                // core themes
                accentColor = uiDefaults.getColor("Component.accentColor");
            }
        }
    }

    protected boolean isMaterialThemes(FlatLaf theme) {
        return theme.getName().toLowerCase().contains("material");
    }

    @Override
    protected void paintIcon(Component c, Graphics2D g) {
        try {
            float width = UIScale.unscale(getIconWidth());
            float height = UIScale.unscale(getIconHeight());
            float arc = 10;
            g.setColor(background);
            g.fill(new RoundRectangle2D.Float(0, 0, width, height, arc, arc));
            g.setColor(accentColor);
            g.fill(new RoundRectangle2D.Float(5, 5, width - 10, 10, arc / 2f, arc / 2f));
            g.setColor(borderColor);
            g.fill(new RoundRectangle2D.Float(5, 20, (width - 10) * 0.6f, 10, arc / 2f, arc / 2f));
            g.fill(new RoundRectangle2D.Float(5, 35, (width - 10) * 0.4f, 10, arc / 2f, arc / 2f));
        } finally {
            g.dispose();
        }
    }
}
