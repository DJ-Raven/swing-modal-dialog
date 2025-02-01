package test.themes;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import net.miginfocom.swing.MigLayout;
import raven.extras.LightDarkButton;
import raven.flatlaf.themes.RavenFlatLafThemes;
import raven.flatlaf.themes.styles.DefaultThemesStyles;
import raven.flatlaf.themes.styles.ThemesStyles;
import raven.modal.demo.component.flalaf.LoadingButton;
import raven.modal.demo.utils.DemoPreferences;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class Test extends JFrame {

    public Test() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1000, 768));
        setLocationRelativeTo(null);
        setLayout(new MigLayout("wrap,al center top,fillx", "fill"));
        LightDarkButton lightDarkButton = new LightDarkButton();
        lightDarkButton.installAutoLafChangeListener();
        add(lightDarkButton, "grow 0");

        add(createButton());
        add(createLoadingButtonComponent());
        add(createTextField());
    }

    private JPanel createButton() {
        JPanel panel = new JPanel(new MigLayout("gap 10"));
        panel.setBorder(new TitledBorder("Button"));

        // component
        JButton cmdDefault = new JButton("Default");
        JButton cmdAccent = new JButton("Accent");
        JButton cmdPrimary = new JButton("Primary");
        JButton cmdSecondary = new JButton("Secondary");
        JButton cmdInfo = new JButton("Secondary");
        JButton cmdSuccess = new JButton("Success");
        JButton cmdWarning = new JButton("Warning");
        JButton cmdDanger = new JButton("Danger");
        JButton cmdGhost = new JButton("Ghost");

        // style class
        ThemesStyles.build().color(ThemesStyles.Color.DEFAULT).apply(cmdDefault);
        ThemesStyles.build().color(ThemesStyles.Color.ACCENT).apply(cmdAccent);
        ThemesStyles.build().color(ThemesStyles.Color.PRIMARY).apply(cmdPrimary);
        ThemesStyles.build().color(ThemesStyles.Color.SECONDARY).apply(cmdSecondary);
        ThemesStyles.build().color(ThemesStyles.Color.INFO).apply(cmdInfo);
        ThemesStyles.build().color(ThemesStyles.Color.SUCCESS).apply(cmdSuccess);
        ThemesStyles.build().color(ThemesStyles.Color.WARNING).apply(cmdWarning);
        ThemesStyles.build().color(ThemesStyles.Color.DANGER).apply(cmdDanger);
        ThemesStyles.build().color(ThemesStyles.Color.GHOST).apply(cmdGhost);

        panel.add(cmdDefault);
        panel.add(cmdAccent);
        panel.add(cmdPrimary);
        panel.add(cmdSecondary);
        panel.add(cmdInfo);
        panel.add(cmdSuccess);
        panel.add(cmdWarning);
        panel.add(cmdDanger);
        panel.add(cmdGhost);

        return panel;
    }

    private JPanel createLoadingButtonComponent() {
        JPanel panel = new JPanel(new MigLayout("gap 10"));
        panel.setBorder(new TitledBorder("Loading"));

        // component
        LoadingButton cmdLoading = new LoadingButton("Loading Button");
        cmdLoading.addActionListener(e -> {
            if (cmdLoading.isRunning()) {
                cmdLoading.stop();
            } else {
                cmdLoading.start();
            }
        });
        // style class
        ThemesStyles.build().color(ThemesStyles.Color.DEFAULT).apply(cmdLoading);

        panel.add(cmdLoading);
        return panel;
    }

    private JPanel createTextField() {
        JPanel panel = new JPanel(new MigLayout("wrap,gap 10", "[trailing][200,fill]"));
        panel.setBorder(new TitledBorder("Input"));

        // component
        JTextField txtDefault = new JTextField();
        JTextField txtAccent = new JTextField();
        JTextField txtPrimary = new JTextField();
        JTextField txtSecondary = new JTextField();
        JTextField txtInfo = new JTextField();
        JTextField txtSuccess = new JTextField();
        JTextField txtWarning = new JTextField();
        JTextField txtDanger = new JTextField();
        JTextArea txtArea = new JTextArea();
        txtArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(txtArea);

        // style class
        ThemesStyles.build().color(ThemesStyles.Color.DEFAULT).apply(txtDefault);
        ThemesStyles.build().color(ThemesStyles.Color.ACCENT).apply(txtAccent);
        ThemesStyles.build().color(ThemesStyles.Color.PRIMARY).apply(txtPrimary);
        ThemesStyles.build().color(ThemesStyles.Color.SECONDARY).apply(txtSecondary);
        ThemesStyles.build().color(ThemesStyles.Color.INFO).apply(txtInfo);
        ThemesStyles.build().color(ThemesStyles.Color.SUCCESS).apply(txtSuccess);
        ThemesStyles.build().color(ThemesStyles.Color.WARNING).apply(txtWarning);
        ThemesStyles.build().color(ThemesStyles.Color.DANGER).apply(txtDanger);
        ThemesStyles.build().color(ThemesStyles.Color.PRIMARY).apply(scrollPane);

        panel.add(new JLabel("Default style"));
        panel.add(txtDefault);
        panel.add(new JLabel("Accent style"));
        panel.add(txtAccent);
        panel.add(new JLabel("Primary style"));
        panel.add(txtPrimary);
        panel.add(new JLabel("Secondary style"));
        panel.add(txtSecondary);
        panel.add(new JLabel("Info style"));
        panel.add(txtInfo);
        panel.add(new JLabel("Success style"));
        panel.add(txtSuccess);
        panel.add(new JLabel("Warning style"));
        panel.add(txtWarning);
        panel.add(new JLabel("Danger style"));
        panel.add(txtDanger);

        panel.add(new JLabel("Text Area"));
        panel.add(scrollPane, "height 200");
        return panel;
    }

    public static void main(String[] args) {
        DemoPreferences.init();
        FlatRobotoFont.install();

        DefaultThemesStyles defaultThemesStyles = new DefaultThemesStyles();
        defaultThemesStyles.setDefaultStyleClass(ThemesStyles.Border.OUTLINE);
        RavenFlatLafThemes.install(defaultThemesStyles);

        FlatLaf.registerCustomDefaultsSource("raven.modal.demo.themes");
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        DemoPreferences.setupLaf();
        EventQueue.invokeLater(() -> new Test().setVisible(true));
    }
}
