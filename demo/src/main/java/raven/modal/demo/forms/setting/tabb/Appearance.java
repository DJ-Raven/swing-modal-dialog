package raven.modal.demo.forms.setting.tabb;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatSystemProperties;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import com.formdev.flatlaf.intellijthemes.FlatSpacegrayIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatVuesionIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatXcodeDarkIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMTGitHubDarkIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMTGitHubIJTheme;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.formdev.flatlaf.util.LoggingFacade;
import com.formdev.flatlaf.util.UIScale;
import net.miginfocom.swing.MigLayout;
import raven.modal.demo.forms.setting.component.ThemesSelection;
import raven.modal.demo.system.Form;
import raven.modal.demo.utils.DemoPreferences;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;

public class Appearance extends Form {

    public Appearance() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("wrap,fillx", "[fill]", "[]20[][]"));
        JLabel lbTitle = new JLabel("Appearance or Themes");
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "" +
                "font:bold +1;");
        add(lbTitle);

        add(new JLabel("Select flatlaf core themes"));
        ThemesSelection coreThemes = new ThemesSelection(
                new FlatMacDarkLaf(),
                new FlatMacLightLaf()
        );
        ThemesSelection recommendThemes = new ThemesSelection(
                new FlatXcodeDarkIJTheme(),
                new FlatVuesionIJTheme(),
                new FlatSpacegrayIJTheme(),
                new FlatMTGitHubDarkIJTheme(),
                new FlatMTGitHubIJTheme()
        );

        JComboBox<Object> comboUIScale = new JComboBox<>(new Object[]{"100%", "110%", "125%", "150%", "175%", "200%"});

        coreThemes.setCallback(theme -> {
            recommendThemes.clearSelected();
            changeThemes(theme);
        });
        recommendThemes.setCallback(theme -> {
            coreThemes.clearSelected();
            changeThemes(theme);
        });
        String currentScale = ((int) (UIScale.getUserScaleFactor() * 100)) + "%";
        comboUIScale.setSelectedItem(currentScale);
        comboUIScale.addActionListener(e -> {
            changeUIScale(comboUIScale.getSelectedItem().toString());
        });

        add(createScroll(coreThemes));

        add(new JLabel("Select recommend themes"));
        add(createScroll(recommendThemes));
        add(new JLabel("UI Scale:"), "grow 0,split 3");
        add(comboUIScale, "grow 0,width 100");
        add(Box.createHorizontalBox());
    }

    private JScrollPane createScroll(Component component) {
        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        scrollPane.getHorizontalScrollBar().setUnitIncrement(20);
        scrollPane.getHorizontalScrollBar().putClientProperty(FlatClientProperties.STYLE, "" +
                "trackArc:$ScrollBar.thumbArc;" +
                "thumbInsets:0,3,0,3;" +
                "trackInsets:0,3,0,3;" +
                "width:12;");

        return scrollPane;
    }

    private void changeThemes(FlatLaf theme) {
        EventQueue.invokeLater(() -> {
            FlatAnimatedLafChange.showSnapshot();
            try {
                UIManager.setLookAndFeel(theme.getClass().getName());
            } catch (Exception err) {
                LoggingFacade.INSTANCE.logSevere(null, err);
            }
            FlatLaf.updateUI();
            FlatAnimatedLafChange.hideSnapshotWithAnimation();
        });
    }

    private void changeUIScale(String scaleValue) {
        System.setProperty(FlatSystemProperties.UI_SCALE, scaleValue);
        DemoPreferences.getState().put(DemoPreferences.KEY_SCALE_FACTOR, scaleValue);

        // change laf
        EventQueue.invokeLater(() -> {
            Font oldFont = UIManager.getFont("defaultFont");
            if (oldFont != null) {
                oldFont = UIScale.applyCustomScaleFactor(new FontUIResource(oldFont));
            }
            FlatAnimatedLafChange.showSnapshot();
            UIManager.put("defaultFont", oldFont);
            try {
                Class<? extends LookAndFeel> lafClass = UIManager.getLookAndFeel().getClass();
                FlatLaf.setup(lafClass.getDeclaredConstructor().newInstance());
            } catch (Exception ex) {
                LoggingFacade.INSTANCE.logSevere(null, ex);
            }
            FlatLaf.updateUI();
            FlatAnimatedLafChange.hideSnapshotWithAnimation();
        });
    }
}
