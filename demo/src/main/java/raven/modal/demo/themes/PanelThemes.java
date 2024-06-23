package raven.modal.demo.themes;

import com.formdev.flatlaf.*;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import com.formdev.flatlaf.util.LoggingFacade;
import net.miginfocom.swing.MigLayout;
import raven.modal.demo.utils.DemoPreferences;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.util.*;
import java.util.List;

public class PanelThemes extends JPanel {

    public static final String THEMES_PACKAGE = "/com/formdev/flatlaf/intellijthemes/themes/";
    private final ThemesManager themesManager = new ThemesManager();
    private final Map<Integer, String> categories = new HashMap<>();
    private final List<ThemesInfo> themes = new ArrayList<>();

    public PanelThemes() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("fillx,insets 3", "[fill]", "[fill,grow]"));
        themesList = new JList<>();
        themesList.setCellRenderer(new DefaultListCellRenderer() {

            private int index;
            private boolean isSelected;
            private int titleHeight;

            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                this.index = index;
                this.isSelected = isSelected;
                this.titleHeight = 0;

                String title = categories.get(index);
                String name = ((ThemesInfo) value).name;
                int sep = name.indexOf('/');
                if (sep >= 0) {
                    name = name.substring(sep + 1).trim();
                }

                JComponent com = (JComponent) super.getListCellRendererComponent(list, name, index, isSelected, cellHasFocus);
                com.setToolTipText(buildToolTip((ThemesInfo) value));
                if (title != null) {
                    Border titBorder = new ListCellTitledBorder(themesList, title);
                    com.setBorder(new CompoundBorder(titBorder, com.getBorder()));
                    this.titleHeight = titBorder.getBorderInsets(com).top;
                }
                return com;
            }

            private String buildToolTip(ThemesInfo th) {
                if (th.resourceName == null) {
                    return th.name;
                }
                return "Name :" + th.name
                        + "\nLicense: " + th.license
                        + "\nSource Code: " + th.sourceCodeUrl;
            }
        });
        themesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        themesList.addListSelectionListener(e -> themesListValueChanged(e));
        JScrollPane scrollPane = new JScrollPane(themesList);
        add(scrollPane);

        themesManager.loadThemes();
        updateThemesList(0);
    }

    public void updateThemesList(int option) {
        boolean showLight = option != 2;
        boolean showDark = option != 1;

        ThemesInfo oldSelected = themesList.getSelectedValue();

        categories.clear();
        themes.clear();

        // add core themes
        categories.put(themes.size(), "Core Themes");
        for (ThemesInfo th : themesManager.coreThemes) {
            boolean show = (showLight && !th.dark) || (showDark && th.dark);
            if (show && !th.name.contains("/")) {
                themes.add(th);
            }
        }

        // add uncategorized bundled themes
        categories.put(themes.size(), "IntelliJ Themes");
        for (ThemesInfo th : themesManager.bundledThemes) {
            boolean show = (showLight && !th.dark) || (showDark && th.dark);
            if (show && !th.name.contains("/")) {
                themes.add(th);
            }
        }

        // add categorized bundled themes
        String lastCategory = null;
        for (ThemesInfo th : themesManager.bundledThemes) {
            boolean show = (showLight && !th.dark) || (showDark && th.dark);
            int sep = th.name.indexOf('/');
            if (!show || sep < 0) {
                continue;
            }
            String category = th.name.substring(0, sep).trim();
            if (!Objects.equals(lastCategory, category)) {
                lastCategory = category;
                categories.put(themes.size(), category);
            }
            themes.add(th);
        }

        // add themes list model
        themesList.setModel(new AbstractListModel<ThemesInfo>() {
            @Override
            public int getSize() {
                return themes.size();
            }

            @Override
            public ThemesInfo getElementAt(int index) {
                return themes.get(index);
            }
        });

        // restore selection
        if (oldSelected != null) {
            themesList.setSelectedValue(oldSelected, true);
            if (themesList.isSelectionEmpty()) {
                themesList.setSelectedIndex(0);
            }
        } else {
            selectedCurrentLookAndFeel();
        }
    }

    private void selectedCurrentLookAndFeel() {
        LookAndFeel lookAndFeel = UIManager.getLookAndFeel();
        String theme = UIManager.getLookAndFeelDefaults().getString(DemoPreferences.THEME_UI_KEY);
        String lafClassName = lookAndFeel.getClass().getName();
        for (int i = 0; i < themes.size(); i++) {
            ThemesInfo ti = themes.get(i);
            if (theme == null && ti.lafClassName != null && lafClassName.equals(ti.lafClassName)) {
                themesList.setSelectedIndex(i);
                break;
            }
            if (theme != null && ti.resourceName != null && theme.substring(DemoPreferences.RESOURCE_PREFIX.length()).equals(ti.resourceName)) {
                themesList.setSelectedIndex(i);
                break;
            }
        }
    }

    private void themesListValueChanged(ListSelectionEvent e) {
        ThemesInfo themesInfo = themesList.getSelectedValue();
        boolean bundledTheme = (themesInfo != null && themesInfo.resourceName != null);
        if (e.getValueIsAdjusting()) {
            return;
        }
        EventQueue.invokeLater(() -> setThemes(themesInfo));
    }

    private void setThemes(ThemesInfo themesInfo) {
        if (themesInfo == null) {
            return;
        }

        // change look and feel
        if (themesInfo.lafClassName != null) {
            if (themesInfo.lafClassName.equals(UIManager.getLookAndFeel().getClass().getName())) {
                return;
            }
            FlatAnimatedLafChange.showSnapshot();
            try {
                UIManager.setLookAndFeel(themesInfo.lafClassName);
            } catch (Exception e) {
                LoggingFacade.INSTANCE.logSevere(null, e);
                showInformationDialog("Failed to create '" + themesInfo.lafClassName + "'.", e);
            }
        } else {
            String theme = UIManager.getLookAndFeelDefaults().getString(DemoPreferences.THEME_UI_KEY);
            if (theme != null && themesInfo.resourceName.equals(theme.substring(DemoPreferences.RESOURCE_PREFIX.length()))) {
                return;
            }
            FlatAnimatedLafChange.showSnapshot();
            IntelliJTheme.setup(getClass().getResourceAsStream(THEMES_PACKAGE + themesInfo.resourceName));
            DemoPreferences.getState().put(DemoPreferences.KEY_LAF_THEME, DemoPreferences.RESOURCE_PREFIX + themesInfo.resourceName);
        }
        FlatLaf.updateUI();
        FlatAnimatedLafChange.hideSnapshotWithAnimation();
    }

    private void showInformationDialog(String message, Exception e) {
        JOptionPane.showMessageDialog(SwingUtilities.windowForComponent(this),
                message + "\n\n" + e.getMessage(),
                "Message",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private JList<ThemesInfo> themesList;
}