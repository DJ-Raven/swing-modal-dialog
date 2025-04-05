package raven.modal.demo.forms.setting.component;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.function.Consumer;

public class ThemesSelection extends JPanel {

    private Consumer<FlatLaf> callback;
    private ButtonGroup group;

    public ThemesSelection(FlatLaf... themes) {
        init(themes);
    }

    private void init(FlatLaf[] themes) {
        setLayout(new MigLayout("insets n 0 15 0, novisualpadding", "[fill]", "[fill]"));
        group = new ButtonGroup();
        boolean selected = false;
        for (FlatLaf theme : themes) {
            ThemeButton cmd = new ThemeButton(theme);
            if (!selected) {
                selected = isThemeSelected(theme);
                if (selected) {
                    cmd.setSelected(selected);
                }
            }
            group.add(cmd);
            cmd.addActionListener(e -> {
                if (callback != null) {
                    callback.accept(theme);
                }
            });
            add(cmd);
        }
    }

    private boolean isThemeSelected(FlatLaf theme) {
        return UIManager.getLookAndFeel().getName().equals(theme.getName());
    }

    public void setCallback(Consumer<FlatLaf> callback) {
        this.callback = callback;
    }

    public void clearSelected() {
        if (group != null) group.clearSelection();
    }

    private static class ThemeButton extends JToggleButton {

        public ThemeButton(FlatLaf theme) {
            setLayout(new MigLayout("wrap 2,fill", "[fill]push[]", "[grow 0][fill]"));
            putClientProperty(FlatClientProperties.STYLE, "" +
                    "margin:2,2,2,2;" +
                    "[light]background:shade($Panel.background,3%);" +
                    "[dark]background:tint($Panel.background,3%);" +
                    "selectedBackground:mix($Component.accentColor,$Panel.background,10%);");

            String icon = theme.isDark() ? "raven/modal/icon/dark.svg"
                    : "raven/modal/icon/light.svg";
            JLabel lbName = new JLabel(new FlatSVGIcon(icon, 0.35f));
            lbName.setText(theme.getName());

            add(lbName);
            JRadioButton check = new JRadioButton() {
                @Override
                public boolean isSelected() {
                    return ThemeButton.this.isSelected();
                }
            };
            check.setFocusable(false);
            setModel(check.getModel());
            add(check);
            add(new JLabel(new FlatLafThemeIcon(200, 50, theme)), "span 2");
        }

        @Override
        public boolean isFocusPainted() {
            return isSelected();
        }

        @Override
        public boolean hasFocus() {
            return isSelected();
        }
    }
}
