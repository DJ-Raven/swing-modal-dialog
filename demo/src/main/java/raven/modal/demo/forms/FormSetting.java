package raven.modal.demo.forms;

import com.formdev.flatlaf.*;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.formdev.flatlaf.util.LoggingFacade;
import net.miginfocom.swing.MigLayout;
import raven.modal.Drawer;
import raven.modal.ModalDialog;
import raven.modal.demo.component.AccentColorIcon;
import raven.modal.demo.system.Form;
import raven.modal.demo.system.FormManager;
import raven.modal.demo.themes.PanelThemes;
import raven.modal.demo.utils.SystemForm;
import raven.modal.drawer.DrawerBuilder;
import raven.modal.drawer.renderer.AbstractDrawerLineStyleRenderer;
import raven.modal.drawer.renderer.DrawerCurvedLineStyle;
import raven.modal.drawer.renderer.DrawerStraightDotLineStyle;
import raven.modal.drawer.simple.SimpleDrawerBuilder;
import raven.modal.option.LayoutOption;
import raven.modal.option.Location;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

@SystemForm(name = "Setting", description = "application setting and configuration", tags = {"themes", "options"})
public class FormSetting extends Form {

    public FormSetting() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("fill", "[fill][fill,grow 0,250:250]", "[fill]"));
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Layout", createLayoutOption());
        tabbedPane.addTab("Style", createStyleOption());
        add(tabbedPane, "gapy 1 0");
        add(createThemes());
    }

    private JPanel createLayoutOption() {
        JPanel panel = new JPanel(new MigLayout("wrap,fillx", "[fill]"));
        panel.add(createWindowsLayout());
        panel.add(createDrawerLayout());
        panel.add(createModalDefaultOption());
        return panel;
    }

    private Component createWindowsLayout() {
        JPanel panel = new JPanel(new MigLayout());
        panel.setBorder(new TitledBorder("Windows layout"));
        JCheckBox chRightToLeft = new JCheckBox("Right to Left", !getComponentOrientation().isLeftToRight());
        JCheckBox chFullWindow = new JCheckBox("Full Window Content", FlatClientProperties.clientPropertyBoolean(FormManager.getFrame().getRootPane(), FlatClientProperties.FULL_WINDOW_CONTENT, false));
        chRightToLeft.addActionListener(e -> {
            if (chRightToLeft.isSelected()) {
                FormManager.getFrame().applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            } else {
                FormManager.getFrame().applyComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
            }
            FormManager.getFrame().revalidate();
        });
        chFullWindow.addActionListener(e -> {
            FormManager.getFrame().getRootPane().putClientProperty(FlatClientProperties.FULL_WINDOW_CONTENT, chFullWindow.isSelected());
        });
        panel.add(chRightToLeft);
        panel.add(chFullWindow);
        return panel;
    }

    private Component createDrawerLayout() {
        JPanel panel = new JPanel(new MigLayout());
        panel.setBorder(new TitledBorder("Drawer layout"));

        JRadioButton jrLeft = new JRadioButton("Left");
        JRadioButton jrLeading = new JRadioButton("Leading");
        JRadioButton jrTrailing = new JRadioButton("Trailing");
        JRadioButton jrRight = new JRadioButton("Right");
        JRadioButton jrTop = new JRadioButton("Top");
        JRadioButton jrBottom = new JRadioButton("Bottom");

        ButtonGroup group = new ButtonGroup();
        group.add(jrLeft);
        group.add(jrLeading);
        group.add(jrTrailing);
        group.add(jrRight);
        group.add(jrTop);
        group.add(jrBottom);

        jrLeading.setSelected(true);

        jrLeft.addActionListener(e -> {
            DrawerBuilder drawerBuilder = Drawer.getDrawerBuilder();
            LayoutOption layoutOption = Drawer.getDrawerOption().getLayoutOption();
            layoutOption.setSize(drawerBuilder.getDrawerWidth(), 1f)
                    .setLocation(Location.LEFT, Location.TOP)
                    .setAnimateDistance(-0.7f, 0f);
            getRootPane().revalidate();
        });
        jrLeading.addActionListener(e -> {
            DrawerBuilder drawerBuilder = Drawer.getDrawerBuilder();
            LayoutOption layoutOption = Drawer.getDrawerOption().getLayoutOption();
            layoutOption.setSize(drawerBuilder.getDrawerWidth(), 1f)
                    .setLocation(Location.LEADING, Location.TOP)
                    .setAnimateDistance(-0.7f, 0f);
            getRootPane().revalidate();
        });
        jrTrailing.addActionListener(e -> {
            DrawerBuilder drawerBuilder = Drawer.getDrawerBuilder();
            LayoutOption layoutOption = Drawer.getDrawerOption().getLayoutOption();
            layoutOption.setSize(drawerBuilder.getDrawerWidth(), 1f)
                    .setLocation(Location.TRAILING, Location.TOP)
                    .setAnimateDistance(0.7f, 0f);
            getRootPane().revalidate();
        });
        jrRight.addActionListener(e -> {
            DrawerBuilder drawerBuilder = Drawer.getDrawerBuilder();
            LayoutOption layoutOption = Drawer.getDrawerOption().getLayoutOption();
            layoutOption.setSize(drawerBuilder.getDrawerWidth(), 1f)
                    .setLocation(Location.RIGHT, Location.TOP)
                    .setAnimateDistance(0.7f, 0f);
            getRootPane().revalidate();
        });
        jrTop.addActionListener(e -> {
            DrawerBuilder drawerBuilder = Drawer.getDrawerBuilder();
            LayoutOption layoutOption = Drawer.getDrawerOption().getLayoutOption();
            layoutOption.setSize(1f, drawerBuilder.getDrawerWidth())
                    .setLocation(Location.LEADING, Location.TOP)
                    .setAnimateDistance(0f, -0.7f);
            getRootPane().revalidate();
        });
        jrBottom.addActionListener(e -> {
            DrawerBuilder drawerBuilder = Drawer.getDrawerBuilder();
            LayoutOption layoutOption = Drawer.getDrawerOption().getLayoutOption();
            layoutOption.setSize(1f, drawerBuilder.getDrawerWidth())
                    .setLocation(Location.LEADING, Location.BOTTOM)
                    .setAnimateDistance(0f, 0.7f);
            getRootPane().revalidate();
        });

        panel.add(jrLeft);
        panel.add(jrLeading);
        panel.add(jrTrailing);
        panel.add(jrRight);
        panel.add(jrTop);
        panel.add(jrBottom);
        return panel;
    }

    private Component createModalDefaultOption() {
        JPanel panel = new JPanel(new MigLayout());
        panel.setBorder(new TitledBorder("Default modal option"));
        JCheckBox chAnimation = new JCheckBox("Animation enable");
        JCheckBox chCloseOnPressedEscape = new JCheckBox("Close on pressed escape");
        chAnimation.setSelected(ModalDialog.getDefaultOption().isAnimationEnabled());
        chCloseOnPressedEscape.setSelected(ModalDialog.getDefaultOption().isCloseOnPressedEscape());

        chAnimation.addActionListener(e -> ModalDialog.getDefaultOption().setAnimationEnabled(chAnimation.isSelected()));
        chCloseOnPressedEscape.addActionListener(e -> ModalDialog.getDefaultOption().setCloseOnPressedEscape(chCloseOnPressedEscape.isSelected()));

        panel.add(chAnimation);
        panel.add(chCloseOnPressedEscape);

        return panel;
    }

    private JPanel createStyleOption() {
        JPanel panel = new JPanel(new MigLayout("wrap,fillx", "[fill]"));
        panel.add(createAccentColor());
        panel.add(createDrawerStyle());
        return panel;
    }

    private static String[] accentColorKeys = {
            "Demo.accent.default", "Demo.accent.blue", "Demo.accent.purple", "Demo.accent.red",
            "Demo.accent.orange", "Demo.accent.yellow", "Demo.accent.green",
    };
    private static String[] accentColorNames = {
            "Default", "Blue", "Purple", "Red", "Orange", "Yellow", "Green",
    };
    private final JToggleButton[] accentColorButtons = new JToggleButton[accentColorKeys.length];
    private Color accentColor;

    private Component createAccentColor() {
        JPanel panel = new JPanel(new MigLayout());
        panel.setBorder(new TitledBorder("Accent color"));
        ButtonGroup group = new ButtonGroup();
        JToolBar toolBar = new JToolBar();
        toolBar.putClientProperty(FlatClientProperties.STYLE, "" +
                "hoverButtonGroupBackground:null;");
        for (int i = 0; i < accentColorButtons.length; i++) {
            accentColorButtons[i] = new JToggleButton(new AccentColorIcon(accentColorKeys[i]));
            accentColorButtons[i].setToolTipText(accentColorNames[i]);
            accentColorButtons[i].addActionListener(this::accentColorChanged);
            toolBar.add(accentColorButtons[i]);
            group.add(accentColorButtons[i]);
        }
        accentColorButtons[0].setSelected(true);

        FlatLaf.setSystemColorGetter(name -> name.equals("accent") ? accentColor : null);
        UIManager.addPropertyChangeListener(e -> {
            if ("lookAndFeel".equals(e.getPropertyName())) {
                updateAccentColorButtons();
            }
        });
        updateAccentColorButtons();
        panel.add(toolBar);
        return panel;
    }

    private Component createDrawerStyle() {
        JPanel panel = new JPanel(new MigLayout("insets 0,filly", "[][][grow,fill]", "[fill]"));
        JPanel lineStyle = new JPanel(new MigLayout("wrap", "[200]"));
        JPanel lineStyleOption = new JPanel(new MigLayout("wrap", "[200]"));
        JPanel lineColorOption = new JPanel(new MigLayout("wrap", "[200]"));

        lineStyle.setBorder(new TitledBorder("Drawer line style"));
        lineStyleOption.setBorder(new TitledBorder("Line style option"));
        lineColorOption.setBorder(new TitledBorder("Color option"));

        ButtonGroup groupStyle = new ButtonGroup();
        JRadioButton jrCurvedStyle = new JRadioButton("Curved line style");
        JRadioButton jrStraightDotStyle = new JRadioButton("Straight dot line style", true);
        groupStyle.add(jrCurvedStyle);
        groupStyle.add(jrStraightDotStyle);

        ButtonGroup groupStyleOption = new ButtonGroup();
        JRadioButton jrStyleOption1 = new JRadioButton("Rectangle");
        JRadioButton jrStyleOption2 = new JRadioButton("Ellipse", true);
        groupStyleOption.add(jrStyleOption1);
        groupStyleOption.add(jrStyleOption2);

        JCheckBox chPaintLineColor = new JCheckBox("Paint selected line color");

        jrCurvedStyle.addActionListener(e -> {
            if (jrCurvedStyle.isSelected()) {
                jrStyleOption1.setText("Line");
                jrStyleOption2.setText("Curved");
                boolean round = jrStyleOption2.isSelected();
                boolean paintSelectedLine = chPaintLineColor.isSelected();
                setDrawerLineStyle(true, round, paintSelectedLine);
            }
        });
        jrStraightDotStyle.addActionListener(e -> {
            if (jrStraightDotStyle.isSelected()) {
                jrStyleOption1.setText("Rectangle");
                jrStyleOption2.setText("Ellipse");
                boolean round = jrStyleOption2.isSelected();
                boolean paintSelectedLine = chPaintLineColor.isSelected();
                setDrawerLineStyle(false, round, paintSelectedLine);
            }
        });

        jrStyleOption1.addActionListener(e -> {
            if (jrStyleOption1.isSelected()) {
                boolean curved = jrCurvedStyle.isSelected();
                boolean paintSelectedLine = chPaintLineColor.isSelected();
                setDrawerLineStyle(curved, false, paintSelectedLine);
            }
        });

        jrStyleOption2.addActionListener(e -> {
            if (jrStyleOption2.isSelected()) {
                boolean curved = jrCurvedStyle.isSelected();
                boolean paintSelectedLine = chPaintLineColor.isSelected();
                setDrawerLineStyle(curved, true, paintSelectedLine);
            }
        });

        chPaintLineColor.addActionListener(e -> {
            boolean curved = jrCurvedStyle.isSelected();
            boolean round = jrStyleOption2.isSelected();
            boolean paintSelectedLine = chPaintLineColor.isSelected();
            setDrawerLineStyle(curved, round, paintSelectedLine);
        });

        lineStyle.add(jrCurvedStyle);
        lineStyle.add(jrStraightDotStyle);

        lineStyleOption.add(jrStyleOption1);
        lineStyleOption.add(jrStyleOption2);

        lineColorOption.add(chPaintLineColor);

        panel.add(lineStyle);
        panel.add(lineStyleOption);
        panel.add(lineColorOption);
        return panel;
    }

    private void setDrawerLineStyle(boolean curved, boolean round, boolean color) {
        AbstractDrawerLineStyleRenderer style;
        if (curved) {
            style = new DrawerCurvedLineStyle(round, color);
        } else {
            style = new DrawerStraightDotLineStyle(round, color);
        }
        ((SimpleDrawerBuilder) Drawer.getDrawerBuilder()).getSimpleMenuOption().getMenuStyle().setDrawerLineStyleRenderer(style);
        ((SimpleDrawerBuilder) Drawer.getDrawerBuilder()).getDrawerMenu().repaint();
    }

    private void accentColorChanged(ActionEvent e) {
        String accentColorKey = null;
        for (int i = 0; i < accentColorButtons.length; i++) {
            if (accentColorButtons[i].isSelected()) {
                accentColorKey = accentColorKeys[i];
                break;
            }
        }
        accentColor = (accentColorKey != null && accentColorKey != accentColorKeys[0])
                ? UIManager.getColor(accentColorKey)
                : null;
        Class<? extends LookAndFeel> lafClass = UIManager.getLookAndFeel().getClass();
        try {
            FlatLaf.setup(lafClass.getDeclaredConstructor().newInstance());
            FlatLaf.updateUI();
        } catch (Exception ex) {
            LoggingFacade.INSTANCE.logSevere(null, ex);
        }
    }

    private void updateAccentColorButtons() {
        Class<? extends LookAndFeel> lafClass = UIManager.getLookAndFeel().getClass();
        boolean isAccentColorSupported =
                lafClass == FlatLightLaf.class ||
                        lafClass == FlatDarkLaf.class ||
                        lafClass == FlatIntelliJLaf.class ||
                        lafClass == FlatDarculaLaf.class ||
                        lafClass == FlatMacLightLaf.class ||
                        lafClass == FlatMacDarkLaf.class;
        for (int i = 0; i < accentColorButtons.length; i++) {
            accentColorButtons[i].setEnabled(isAccentColorSupported);
        }
    }

    private JPanel createThemes() {
        JPanel panel = new JPanel(new MigLayout("wrap,fill,insets 0", "[fill]", "[grow 0,fill]0[fill]"));
        final PanelThemes panelThemes = new PanelThemes();
        JPanel panelHeader = new JPanel(new MigLayout("fillx,insets 3", "[grow 0]push[]"));
        panelHeader.add(new JLabel("Themes"));
        JComboBox combo = new JComboBox(new Object[]{"All", "Light", "Dark"});
        combo.addActionListener(e -> {
            panelThemes.updateThemesList(combo.getSelectedIndex());
        });
        panelHeader.add(combo);
        panel.add(panelHeader);
        panel.add(panelThemes);
        return panel;
    }

    private JTabbedPane tabbedPane;
}
