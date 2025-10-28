package raven.modal.demo.forms.setting.tabb;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import com.formdev.flatlaf.intellijthemes.*;
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
                new FlatIntelliJLaf(),
                new FlatDarculaLaf(),
                new FlatMacDarkLaf(),
                new FlatMacLightLaf()
        );
        ThemesSelection recommendThemes = new ThemesSelection(
                new FlatXcodeDarkIJTheme(),
                new FlatVuesionIJTheme(),
                new FlatSpacegrayIJTheme(),
                new FlatMTGitHubDarkIJTheme(),
                new FlatGruvboxDarkHardIJTheme(),
                new FlatMTGitHubIJTheme(),
                new FlatGrayIJTheme()
        );

        JComboBox<Object> comboUIScale = new JComboBox<>();
        for (float zoomFacto : UIScale.getSupportedZoomFactors()) {
            comboUIScale.addItem((int) (zoomFacto * 100) + "%");
        }

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
            Object select = comboUIScale.getSelectedItem();
            if (select != null) {
                String zoom = select.toString();
                float zoomFactor = Integer.parseInt(zoom.substring(0, zoom.length() - 1)) / 100f;
                changeUIScale(zoomFactor);
            }

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

    private void changeUIScale(float zoom) {
        DemoPreferences.getState().put(DemoPreferences.KEY_ZOOM_FACTOR, zoom + "");
        if (UIScale.setZoomFactor(zoom)) {
            FlatLaf.updateUI();
        }
    }
}
