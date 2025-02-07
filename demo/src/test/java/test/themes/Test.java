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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;

public class Test extends JFrame {

    public Test() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1000, 768));
        setLocationRelativeTo(null);
        setLayout(new MigLayout("wrap,al center top,fill", "fill", "[fill][grow 0]"));

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Button", createButton());
        tabbedPane.addTab("TextField", createTextField());
        tabbedPane.addTab("ComboBox", createComboBox());
        tabbedPane.addTab("Data Component", createDataComponent());

        tabbedPane.addTab("Themes", createThemePanel());
        add(tabbedPane);
        add(createControlBar(tabbedPane));
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
        JButton cmdNeutral = new JButton("Neutral");
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
        ThemesStyles.build().color(ThemesStyles.Color.NEUTRAL).apply(cmdNeutral);
        ThemesStyles.build().color(ThemesStyles.Color.ACCENT).apply(cmdAccent);
        ThemesStyles.build().color(ThemesStyles.Color.PRIMARY).apply(cmdPrimary);
        ThemesStyles.build().color(ThemesStyles.Color.SECONDARY).apply(cmdSecondary);
        ThemesStyles.build().color(ThemesStyles.Color.INFO).apply(cmdInfo);
        ThemesStyles.build().color(ThemesStyles.Color.SUCCESS).apply(cmdSuccess);
        ThemesStyles.build().color(ThemesStyles.Color.WARNING).apply(cmdWarning);
        ThemesStyles.build().color(ThemesStyles.Color.DANGER).apply(cmdDanger);
        ThemesStyles.build().color(ThemesStyles.Color.GHOST).apply(cmdGhost);

        panel.add(cmdDefault);
        panel.add(cmdNeutral);
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
        main.add(createRadioButton());
        main.add(createCheckBox());

        return main;
    }

    private JPanel createLoadingButtonComponent() {
        JPanel panel = new JPanel(new MigLayout("gap 10"));
        panel.setBorder(new TitledBorder("Loading and ToggleButton"));

        // component
        LoadingButton cmdLoading = new LoadingButton("Loading Button");
        cmdLoading.addActionListener(e -> {
            if (cmdLoading.isRunning()) {
                cmdLoading.stop();
            } else {
                cmdLoading.start();
            }
        });
        JToggleButton cmdDefault = new JToggleButton("Default");
        JToggleButton cmdNeutral = new JToggleButton("Neutral");
        JToggleButton cmdAccent = new JToggleButton("Accent");
        JToggleButton cmdPrimary = new JToggleButton("Primary");
        JToggleButton cmdInfo = new JToggleButton("Secondary");
        JToggleButton cmdGhost = new JToggleButton("Ghost");

        // style class
        ThemesStyles.build().color(ThemesStyles.Color.NEUTRAL).apply(cmdLoading);

        ThemesStyles.build().color(ThemesStyles.Color.DEFAULT).apply(cmdDefault);
        ThemesStyles.build().color(ThemesStyles.Color.NEUTRAL).apply(cmdNeutral);
        ThemesStyles.build().color(ThemesStyles.Color.ACCENT).apply(cmdAccent);
        ThemesStyles.build().color(ThemesStyles.Color.PRIMARY).apply(cmdPrimary);
        ThemesStyles.build().color(ThemesStyles.Color.INFO).apply(cmdInfo);
        ThemesStyles.build().color(ThemesStyles.Color.GHOST).apply(cmdGhost);

        panel.add(cmdLoading);
        panel.add(cmdDefault);
        panel.add(cmdNeutral);
        panel.add(cmdAccent);
        panel.add(cmdPrimary);
        panel.add(cmdInfo);
        panel.add(cmdGhost);

        return panel;
    }

    private JPanel createRadioButton() {
        JPanel panel = new JPanel(new MigLayout("gap 10"));
        panel.setBorder(new TitledBorder("Button"));

        // component
        JRadioButton cmdDefault = new JRadioButton("Default");
        JRadioButton cmdNeutral = new JRadioButton("Neutral");
        JRadioButton cmdAccent = new JRadioButton("Accent");
        JRadioButton cmdPrimary = new JRadioButton("Primary");
        JRadioButton cmdSecondary = new JRadioButton("Secondary");
        JRadioButton cmdInfo = new JRadioButton("Secondary");
        JRadioButton cmdSuccess = new JRadioButton("Success");
        JRadioButton cmdWarning = new JRadioButton("Warning");
        JRadioButton cmdDanger = new JRadioButton("Danger");
        JRadioButton cmdGhost = new JRadioButton("Ghost");

        // style class
        ThemesStyles.build().color(ThemesStyles.Color.DEFAULT).apply(cmdDefault);
        ThemesStyles.build().color(ThemesStyles.Color.NEUTRAL).apply(cmdNeutral);
        ThemesStyles.build().color(ThemesStyles.Color.ACCENT).apply(cmdAccent);
        ThemesStyles.build().color(ThemesStyles.Color.PRIMARY).apply(cmdPrimary);
        ThemesStyles.build().color(ThemesStyles.Color.SECONDARY).apply(cmdSecondary);
        ThemesStyles.build().color(ThemesStyles.Color.INFO).apply(cmdInfo);
        ThemesStyles.build().color(ThemesStyles.Color.SUCCESS).apply(cmdSuccess);
        ThemesStyles.build().color(ThemesStyles.Color.WARNING).apply(cmdWarning);
        ThemesStyles.build().color(ThemesStyles.Color.DANGER).apply(cmdDanger);
        ThemesStyles.build().color(ThemesStyles.Color.GHOST).apply(cmdGhost);

        panel.add(cmdDefault);
        panel.add(cmdNeutral);
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

    private JPanel createCheckBox() {
        JPanel panel = new JPanel(new MigLayout("gap 10"));
        panel.setBorder(new TitledBorder("Button"));

        // component
        JCheckBox cmdDefault = new JCheckBox("Default");
        JCheckBox cmdNeutral = new JCheckBox("Neutral");
        JCheckBox cmdAccent = new JCheckBox("Accent");
        JCheckBox cmdPrimary = new JCheckBox("Primary");
        JCheckBox cmdSecondary = new JCheckBox("Secondary");
        JCheckBox cmdInfo = new JCheckBox("Secondary");
        JCheckBox cmdSuccess = new JCheckBox("Success");
        JCheckBox cmdWarning = new JCheckBox("Warning");
        JCheckBox cmdDanger = new JCheckBox("Danger");
        JCheckBox cmdGhost = new JCheckBox("Ghost");

        // style class
        ThemesStyles.build().color(ThemesStyles.Color.DEFAULT).apply(cmdDefault);
        ThemesStyles.build().color(ThemesStyles.Color.NEUTRAL).apply(cmdNeutral);
        ThemesStyles.build().color(ThemesStyles.Color.ACCENT).apply(cmdAccent);
        ThemesStyles.build().color(ThemesStyles.Color.PRIMARY).apply(cmdPrimary);
        ThemesStyles.build().color(ThemesStyles.Color.SECONDARY).apply(cmdSecondary);
        ThemesStyles.build().color(ThemesStyles.Color.INFO).apply(cmdInfo);
        ThemesStyles.build().color(ThemesStyles.Color.SUCCESS).apply(cmdSuccess);
        ThemesStyles.build().color(ThemesStyles.Color.WARNING).apply(cmdWarning);
        ThemesStyles.build().color(ThemesStyles.Color.DANGER).apply(cmdDanger);
        ThemesStyles.build().color(ThemesStyles.Color.GHOST).apply(cmdGhost);

        panel.add(cmdDefault);
        panel.add(cmdNeutral);
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

    private JPanel createTextField() {
        JPanel main = new JPanel(new MigLayout("fill", "fill", "fill"));
        JPanel panel = new JPanel(new MigLayout("wrap,gap 10", "[trailing][200,fill]"));
        panel.setBorder(new TitledBorder("Input"));

        // component
        JTextField txtDefault = new JTextField();
        JTextField txtNeutral = new JTextField();
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
        ThemesStyles.build().color(ThemesStyles.Color.NEUTRAL).apply(txtNeutral);
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
        panel.add(new JLabel("Neutral style"));
        panel.add(txtNeutral);
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
        JFormattedTextField txtFormatted = new JFormattedTextField();
        JFormattedTextField txtFormattedGhost = new JFormattedTextField();
        JPasswordField password = new JPasswordField();
        JSpinner spinner = new JSpinner();
        JSpinner spinnerGhost = new JSpinner();
        JTextArea txtArea = new JTextArea();
        JTextArea txtAreaGhost = new JTextArea();
        txtArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(txtArea);
        JScrollPane scrollPaneGhost = new JScrollPane(txtAreaGhost);

        // style class
        ThemesStyles.build().color(ThemesStyles.Color.DEFAULT).apply(txtArea, txtDefault, scrollPane, txtFormatted);
        ThemesStyles.build().color(ThemesStyles.Color.SECONDARY).apply(password, spinner);
        ThemesStyles.build().color(ThemesStyles.Color.GHOST).apply(password, spinnerGhost, txtFormattedGhost, scrollPaneGhost, txtAreaGhost);

        panel.add(new JLabel("TextField"));
        panel.add(txtDefault);
        panel.add(new JLabel("Formatted"));
        panel.add(txtFormatted);
        panel.add(new JLabel("Formatted ghost"));
        panel.add(txtFormattedGhost);
        panel.add(new JLabel("Password ghost"));
        panel.add(password);
        panel.add(new JLabel("Spinner"));
        panel.add(spinner);

        panel.add(new JLabel("Spinner ghost"));
        panel.add(spinnerGhost);

        panel.add(new JLabel("TextArea"));
        panel.add(scrollPane, "height 200");
        panel.add(new JLabel("TextArea ghost"));
        panel.add(scrollPaneGhost, "height 200");
        return panel;
    }

    private JPanel createComboBox() {
        JPanel main = new JPanel(new MigLayout("fill", "fill", "fill"));
        JPanel panel = new JPanel(new MigLayout("wrap,gap 10", "[trailing][200,fill]"));
        panel.setBorder(new TitledBorder("ComboBox"));

        // component
        JComboBox comboDefault = new JComboBox(createComboBoxItems());
        JComboBox comboNeutral = new JComboBox(createComboBoxItems());
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
        comboGhost.setEditable(true);

        // style class
        ThemesStyles.build().color(ThemesStyles.Color.DEFAULT).apply(comboDefault);
        ThemesStyles.build().color(ThemesStyles.Color.NEUTRAL).apply(comboNeutral);
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
        panel.add(new JLabel("Neutral style"));
        panel.add(comboNeutral);
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

    private JPanel createDataComponent() {
        JPanel main = new JPanel(new MigLayout("fill", "fill", "fill"));
        JPanel panel = new JPanel(new MigLayout("wrap,gap 10", "[trailing][200,fill]"));
        panel.setBorder(new TitledBorder("List"));

        // component
        JList listDefault = new JList(createComboBoxItems());
        JList listSuccess = new JList(createComboBoxItems());
        JList listGhost = new JList(createComboBoxItems());

        JScrollPane scrollDefault = new JScrollPane(listDefault);
        JScrollPane scrollSuccess = new JScrollPane(listSuccess);
        JScrollPane scrollGhost = new JScrollPane(listGhost);

        // style class
        ThemesStyles.build().color(ThemesStyles.Color.DEFAULT).apply(scrollDefault, listDefault);
        ThemesStyles.build().color(ThemesStyles.Color.SUCCESS).apply(scrollSuccess, listSuccess);
        ThemesStyles.build().color(ThemesStyles.Color.GHOST).apply(scrollGhost, listGhost);

        panel.add(new JLabel("Default style"));
        panel.add(scrollDefault);
        panel.add(new JLabel("Success style"));
        panel.add(scrollSuccess);
        panel.add(new JLabel("Ghost style"));
        panel.add(scrollGhost);

        main.add(panel);
        main.add(createTable());
        return main;
    }

    private JPanel createTable() {
        JPanel panel = new JPanel(new MigLayout("wrap,gap 10,width 600", "[trailing][fill]"));
        panel.setBorder(new TitledBorder("Table"));

        // component
        JTable tblAccent = new JTable(createTableRow());
        JTable tblSecondary = new JTable(createTableRow());
        JTable tblWarning = new JTable(createTableRow());

        JScrollPane scrollAccent = new JScrollPane(tblAccent);
        JScrollPane scrollSecondary = new JScrollPane(tblSecondary);
        JScrollPane scrollWarning = new JScrollPane(tblWarning);

        // style class
        ThemesStyles.build().color(ThemesStyles.Color.ACCENT).apply(scrollAccent, tblAccent);
        ThemesStyles.build().color(ThemesStyles.Color.SECONDARY).apply(scrollSecondary, tblSecondary);
        ThemesStyles.build().color(ThemesStyles.Color.WARNING).apply(scrollWarning, tblWarning);

        panel.add(new JLabel("Accent style"));
        panel.add(scrollAccent);
        panel.add(new JLabel("Secondary style"));
        panel.add(scrollSecondary);
        panel.add(new JLabel("Warning style"));
        panel.add(scrollWarning);

        return panel;
    }

    private String[] createComboBoxItems() {
        return new String[]{"Default style", "Neutral style", "Accent style", "Primary style", "Secondary style", "Info style", "Success style", "Warning style", "Danger style", "Ghost style"};
    }

    private TableModel createTableRow() {
        Object[][] data = new Object[][]{
                {false, "ID-001", "John", 25, "Male", "Engineer", 50000},
                {false, "ID-002", "Alice", 30, "Female", "Doctor", 70000},
                {false, "ID-003", "Robert", 28, "Male", "Teacher", 45000},
                {false, "ID-004", "Sophia", 32, "Female", "Lawyer", 80000},
                {false, "ID-005", "Michael", 26, "Male", "Developer", 60000},
                {false, "ID-006", "Emma", 29, "Female", "Designer", 55000},
                {false, "ID-007", "David", 35, "Male", "Manager", 90000},
                {false, "ID-008", "Olivia", 27, "Female", "Nurse", 50000},
                {false, "ID-009", "James", 31, "Male", "Analyst", 62000},
                {false, "ID-010", "Emily", 33, "Female", "Architect", 75000},
                {false, "ID-011", "Daniel", 24, "Male", "Scientist", 58000},
                {false, "ID-012", "Charlotte", 28, "Female", "Accountant", 53000},
                {false, "ID-013", "Ethan", 29, "Male", "Consultant", 67000},
                {false, "ID-014", "Ava", 30, "Female", "Marketing", 72000},
                {false, "ID-015", "Liam", 26, "Male", "Technician", 49000},
                {false, "ID-016", "Mia", 34, "Female", "HR", 71000},
                {false, "ID-017", "Noah", 27, "Male", "Sales", 55000},
                {false, "ID-018", "Isabella", 31, "Female", "Pharmacist", 68000},
                {false, "ID-019", "Logan", 25, "Male", "Support", 48000},
                {false, "ID-020", "Amelia", 29, "Female", "Writer", 52000},
        };
        Object[] columns = new Object[]{"Action", "ID", "Name", "Age", "Gender", "Job", "Salary"};
        TableModel model = new DefaultTableModel(data, columns) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Boolean.class;
                }
                return super.getColumnClass(columnIndex);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };
        return model;
    }

    private JPanel createControlBar(JTabbedPane tabbed) {
        JPanel panel = new JPanel(new MigLayout());
        JCheckBox chEnabled = new JCheckBox("Enabled", true);
        chEnabled.addActionListener(e -> {
            boolean enabled = chEnabled.isSelected();
            enabledDisable(tabbed, enabled);
            tabbed.repaint();
        });
        tabbed.repaint();
        panel.add(chEnabled);
        return panel;
    }

    private void enabledDisable(Container container, boolean enabled) {
        for (Component c : container.getComponents()) {
            if (c instanceof JPanel) {
                enabledDisable((JPanel) c, enabled);
                continue;
            }

            c.setEnabled(enabled);

            if (c instanceof JScrollPane) {
                Component view = ((JScrollPane) c).getViewport().getView();
                if (view != null)
                    view.setEnabled(enabled);
            } else if (c instanceof JTabbedPane) {
                JTabbedPane tabPane = (JTabbedPane) c;
                int tabCount = tabPane.getTabCount();
                for (int i = 0; i < tabCount; i++) {
                    Component tab = tabPane.getComponentAt(i);
                    if (tab != null)
                        tab.setEnabled(enabled);
                }
            }
            if (c instanceof JToolBar)
                enabledDisable((JToolBar) c, enabled);
        }
    }

    public static void main(String[] args) {
        DemoPreferences.init();
        FlatRobotoFont.install();

        DefaultThemesStyles defaultThemesStyles = new DefaultThemesStyles();
        // defaultThemesStyles.setDefaultStyleClass(ThemesStyles.Border.NONE);
        RavenFlatLafThemes.install(defaultThemesStyles);

        FlatLaf.registerCustomDefaultsSource("raven.modal.demo.themes");
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        DemoPreferences.setupLaf();
        EventQueue.invokeLater(() -> new Test().setVisible(true));
    }
}
