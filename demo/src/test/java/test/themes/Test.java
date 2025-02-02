package test.themes;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import net.miginfocom.swing.MigLayout;
import raven.flatlaf.themes.RavenFlatLafThemes;
import raven.flatlaf.themes.styles.DefaultThemesStyles;
import raven.flatlaf.themes.styles.ThemesStyles;
import raven.modal.demo.component.flalaf.LoadingButton;
import raven.modal.demo.themes.PanelThemes;
import raven.modal.demo.utils.DemoPreferences;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class Test extends JFrame {

    public Test() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1000, 768));
        setLocationRelativeTo(null);
        setLayout(new MigLayout("wrap,al center top,fill", "fill", "fill"));

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Button", createButton());
        tabbedPane.addTab("TextField", createTextField());
        tabbedPane.addTab("ComboBox", createComboBox());
        tabbedPane.addTab("Themes", createThemePanel());
        add(tabbedPane);
    }

    private JPanel createThemePanel() {
        JPanel panel = new JPanel(new MigLayout("fill", "fill", "fill"));
        panel.add(new PanelThemes());

        return panel;
    }

    private JPanel createButton() {
        JPanel main = new JPanel(new MigLayout("fillx,wrap", "fill"));
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
        main.add(panel);
        main.add(createLoadingButtonComponent());
        return main;
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
        JPanel main = new JPanel(new MigLayout("fill", "fill", "fill"));
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
        JTextField txtGhost = new JTextField();

        // style class
        ThemesStyles.build().color(ThemesStyles.Color.DEFAULT).apply(txtDefault);
        ThemesStyles.build().color(ThemesStyles.Color.ACCENT).apply(txtAccent);
        ThemesStyles.build().color(ThemesStyles.Color.PRIMARY).apply(txtPrimary);
        ThemesStyles.build().color(ThemesStyles.Color.SECONDARY).apply(txtSecondary);
        ThemesStyles.build().color(ThemesStyles.Color.INFO).apply(txtInfo);
        ThemesStyles.build().color(ThemesStyles.Color.SUCCESS).apply(txtSuccess);
        ThemesStyles.build().color(ThemesStyles.Color.WARNING).apply(txtWarning);
        ThemesStyles.build().color(ThemesStyles.Color.DANGER).apply(txtDanger);
        ThemesStyles.build().color(ThemesStyles.Color.GHOST).apply(txtGhost);

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
        panel.add(new JLabel("Ghost style"));
        panel.add(txtGhost);

        main.add(panel);
        main.add(createOtherInput());
        return main;
    }

    private JPanel createOtherInput() {
        JPanel panel = new JPanel(new MigLayout("wrap,gap 10", "[trailing][200,fill]"));
        panel.setBorder(new TitledBorder("Other Input"));

        JTextField txtDefault = new JTextField();
        JPasswordField password = new JPasswordField();
        JTextArea txtArea = new JTextArea();
        txtArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(txtArea);

        // style class
        ThemesStyles.build().color(ThemesStyles.Color.DEFAULT).apply(txtDefault);
        ThemesStyles.build().color(ThemesStyles.Color.SECONDARY).apply(password);
        ThemesStyles.build().color(ThemesStyles.Color.DANGER).apply(scrollPane);

        panel.add(new JLabel("TextField"));
        panel.add(txtDefault);
        panel.add(new JLabel("Password"));
        panel.add(password);
        panel.add(new JLabel("TextArea"));
        panel.add(scrollPane, "height 200");

        return panel;
    }

    private JPanel createComboBox() {
        JPanel main = new JPanel(new MigLayout("fill", "fill", "fill"));
        JPanel panel = new JPanel(new MigLayout("wrap,gap 10", "[trailing][200,fill]"));
        panel.setBorder(new TitledBorder("ComboBox"));

        // component
        JComboBox comboDefault = new JComboBox(createComboBoxItems());
        JComboBox comboAccent = new JComboBox(createComboBoxItems());
        JComboBox comboPrimary = new JComboBox(createComboBoxItems());
        JComboBox comboSecondary = new JComboBox(createComboBoxItems());
        JComboBox comboInfo = new JComboBox(createComboBoxItems());
        JComboBox comboSuccess = new JComboBox(createComboBoxItems());
        JComboBox comboWarning = new JComboBox(createComboBoxItems());
        JComboBox comboDanger = new JComboBox(createComboBoxItems());
        JComboBox comboGhost = new JComboBox(createComboBoxItems());
        comboWarning.setEditable(true);
        comboDanger.setEditable(true);

        // style class
        ThemesStyles.build().color(ThemesStyles.Color.DEFAULT).apply(comboDefault);
        ThemesStyles.build().color(ThemesStyles.Color.ACCENT).apply(comboAccent);
        ThemesStyles.build().color(ThemesStyles.Color.PRIMARY).apply(comboPrimary);
        ThemesStyles.build().color(ThemesStyles.Color.SECONDARY).apply(comboSecondary);
        ThemesStyles.build().color(ThemesStyles.Color.INFO).apply(comboInfo);
        ThemesStyles.build().color(ThemesStyles.Color.SUCCESS).apply(comboSuccess);
        ThemesStyles.build().color(ThemesStyles.Color.WARNING).apply(comboWarning);
        ThemesStyles.build().color(ThemesStyles.Color.DANGER).apply(comboDanger);
        ThemesStyles.build().color(ThemesStyles.Color.GHOST).apply(comboGhost);

        panel.add(new JLabel("Default style"));
        panel.add(comboDefault);
        panel.add(new JLabel("Accent style"));
        panel.add(comboAccent);
        panel.add(new JLabel("Primary style"));
        panel.add(comboPrimary);
        panel.add(new JLabel("Secondary style"));
        panel.add(comboSecondary);
        panel.add(new JLabel("Info style"));
        panel.add(comboInfo);
        panel.add(new JLabel("Success style"));
        panel.add(comboSuccess);
        panel.add(new JLabel("Warning style"));
        panel.add(comboWarning);
        panel.add(new JLabel("Danger style"));
        panel.add(comboDanger);
        panel.add(new JLabel("Ghost style"));
        panel.add(comboGhost);

        main.add(panel);

        return main;
    }

    private String[] createComboBoxItems() {
        return new String[]{"Default style", "Accent style", "Primary style", "Secondary style", "Info style", "Success style", "Warning style", "Danger style", "Ghost style"};
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
