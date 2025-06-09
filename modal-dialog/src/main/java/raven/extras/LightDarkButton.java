package raven.extras;

import com.formdev.flatlaf.*;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.formdev.flatlaf.util.LoggingFacade;
import net.miginfocom.swing.MigLayout;
import raven.modal.utils.FlatLafStyleUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Raven
 */
public class LightDarkButton extends JPanel {

    private ButtonStyle buttonStyle;
    private String lightButtonText;
    private String darkButtonText;
    private Icon lightButtonIcon;
    private Icon darkButtonIcon;

    private JToggleButton buttonLight;
    private JToggleButton buttonDark;
    private JToggleButton buttonLightDark;
    private ButtonGroup group;
    private List<LookAndFeelLightDark> supportLookAndFeel;
    private List<ModeChangeListener> modeChangeListeners;
    private final int arc;
    private final int borderWidth = 4;
    private final int buttonMargin = 3;

    @Override
    public void updateUI() {
        super.updateUI();

        if (buttonLight != null) buttonLight.updateUI();
        if (buttonDark != null) buttonDark.updateUI();
        if (buttonLightDark != null) buttonLightDark.updateUI();
    }

    public LightDarkButton() {
        this(15);
    }

    public LightDarkButton(int arc) {
        this(ButtonStyle.DUAL_BUTTON, arc);
    }

    public LightDarkButton(ButtonStyle buttonStyle) {
        this(buttonStyle, 15);
    }

    public LightDarkButton(ButtonStyle buttonStyle, int arc) {
        this.buttonStyle = buttonStyle;
        this.arc = arc;
        init();
    }

    private void init() {
        supportLookAndFeel = new ArrayList<>();
        modeChangeListeners = new ArrayList<>();
        lightButtonText = "Light";
        darkButtonText = "Dark";
        lightButtonIcon = new FlatSVGIcon("raven/modal/icon/light.svg", 0.4f)
                .setColorFilter(getFilterColor(false));

        darkButtonIcon = new FlatSVGIcon("raven/modal/icon/dark.svg", 0.4f)
                .setColorFilter(getFilterColor(true));
        initSupportLookAndFeel();
        installButton();
        putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:" + arc + ";" +
                "[light]background:shade($Panel.background,10%);" +
                "[dark]background:tint($Panel.background,10%);");
    }

    private void initSupportLookAndFeel() {
        supportLookAndFeel.add(new LookAndFeelLightDark(FlatLightLaf.class, FlatDarkLaf.class));
        supportLookAndFeel.add(new LookAndFeelLightDark(FlatIntelliJLaf.class, FlatDarculaLaf.class));
        supportLookAndFeel.add(new LookAndFeelLightDark(FlatMacLightLaf.class, FlatMacDarkLaf.class));
    }

    public int getArc() {
        return arc;
    }

    private FlatSVGIcon.ColorFilter getFilterColor(boolean dark) {
        if (dark) {
            return new FlatSVGIcon.ColorFilter(color -> {
                if (buttonStyle == ButtonStyle.TOGGLE_BUTTON) {
                    return buttonLightDark == null ? color : buttonLightDark.getForeground();
                } else {
                    return buttonDark == null ? color : buttonDark.getForeground();
                }
            });
        } else {
            return new FlatSVGIcon.ColorFilter(color -> {
                if (buttonStyle == ButtonStyle.TOGGLE_BUTTON) {
                    return buttonLightDark == null ? color : buttonLightDark.getForeground();
                } else {
                    return buttonLight == null ? color : buttonLight.getForeground();
                }
            });
        }
    }

    private void installButton() {
        removeAll();
        if (buttonStyle == ButtonStyle.DUAL_BUTTON) {
            setLayout(new MigLayout("fill,novisualpadding,insets " + borderWidth, "[fill]", "[fill]"));
            if (buttonLight == null) {
                buttonLight = createButton(lightButtonText, lightButtonIcon, true);
                buttonLight.addActionListener(e -> notifyModeChanged(false));
                getGroup().add(buttonLight);
            }
            if (buttonDark == null) {
                buttonDark = createButton(darkButtonText, darkButtonIcon, true);
                buttonDark.addActionListener(e -> notifyModeChanged(true));
                getGroup().add(buttonDark);
            }
            add(buttonLight);
            add(buttonDark);
        } else {
            setLayout(new MigLayout("fill,novisualpadding,insets 0", "[fill]", "[fill]"));
            buttonLightDark = createButton(null, darkButtonIcon, false);
            buttonLightDark.addActionListener(e -> {
                boolean isDark = buttonLightDark.isSelected();
                buttonLightDark.setIcon(isDark ? darkButtonIcon : lightButtonIcon);
                notifyModeChanged(isDark);
            });
            add(buttonLightDark);
        }

        setSelectedMode(FlatLaf.isLafDark(), false);
        repaint();
        revalidate();
    }

    private JToggleButton createButton(String text, Icon icon, boolean isDualButton) {
        JToggleButton button = new JToggleButton(text, icon);
        int innerArc = arc - borderWidth;
        int margin = buttonMargin;
        String style = "" +
                "arc:" + innerArc + ";" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0;" +
                "background:null;" +
                "selectedBackground:$Button.background;" +
                "margin:" + margin + "," + 14 + "," + margin + "," + 14 + ";";
        if (!isDualButton) {
            margin += (borderWidth);
            style = FlatLafStyleUtils.appendStyle(style, "" +
                    "background:$Button.background;" +
                    "margin:" + margin + "," + margin + "," + margin + "," + margin + ";");
        }
        button.putClientProperty(FlatClientProperties.STYLE, style);
        return button;
    }

    private ButtonGroup getGroup() {
        if (group == null) {
            group = new ButtonGroup();
        }
        return group;
    }

    protected String getLookAndFeelChanged(LookAndFeel oldLaf, boolean isDarkMode) {
        if (oldLaf instanceof FlatLaf) {
            FlatLaf flatLaf = (FlatLaf) oldLaf;
            if (flatLaf.isDark() == isDarkMode) {
                return null;
            }
            String oldThemeName = oldLaf.getClass().getName();
            for (LookAndFeelLightDark lightDark : supportLookAndFeel) {
                String lfName = (isDarkMode ? lightDark.light : lightDark.dark).getName();
                if (oldThemeName.equals(lfName)) {
                    return (isDarkMode ? lightDark.dark : lightDark.light).getName();
                }
            }
        }
        return getLookAndFeelWhenUnknown(oldLaf, isDarkMode);
    }

    protected String getLookAndFeelWhenUnknown(LookAndFeel oldLaf, boolean isDarkMode) {
        if (isDarkMode) {
            return FlatDarkLaf.class.getName();
        } else {
            return FlatLightLaf.class.getName();
        }
    }

    protected void lookAndFeelChanged(String lafClassName) {
        EventQueue.invokeLater(() -> {
            FlatAnimatedLafChange.showSnapshot();
            try {
                UIManager.setLookAndFeel(lafClassName);
            } catch (Exception e) {
                LoggingFacade.INSTANCE.logSevere(null, e);
            }
            FlatLaf.updateUI();
            FlatAnimatedLafChange.hideSnapshotWithAnimation();
        });
    }

    public ButtonStyle getButtonStyle() {
        return buttonStyle;
    }

    public void setButtonStyle(ButtonStyle buttonStyle) {
        if (Objects.isNull(buttonStyle)) {
            throw new IllegalArgumentException("Button style cannot be null");
        }
        if (this.buttonStyle != buttonStyle) {
            this.buttonStyle = buttonStyle;
            installButton();
        }
    }

    public void setSelectedMode(boolean dark) {
        setSelectedMode(dark, true);
    }

    /**
     * If notify true it will call mode change listener
     */
    public void setSelectedMode(boolean dark, boolean notify) {
        if (buttonStyle == ButtonStyle.DUAL_BUTTON) {
            if (dark) {
                buttonDark.setSelected(true);
            } else {
                buttonLight.setSelected(true);
            }
        } else {
            buttonLightDark.setIcon(dark ? darkButtonIcon : lightButtonIcon);
            buttonLightDark.setSelected(dark);
        }
        if (notify) {
            notifyModeChanged(dark);
        }
    }

    public void installAutoLafChangeListener() {
        UIManager.addPropertyChangeListener(evt -> {
            if (evt.getPropertyName().equals("lookAndFeel")) {
                autoSelectedMode();
            }
        });
        addModeChangeListener(isDarkMode -> {
            String lafClassName = getLookAndFeelChanged(UIManager.getLookAndFeel(), isDarkMode);
            if (lafClassName != null)
                lookAndFeelChanged(lafClassName);
        });
    }

    public void autoSelectedMode() {
        setSelectedMode(FlatLaf.isLafDark(), false);
    }

    public String getLightButtonText() {
        return lightButtonText;
    }

    public void setLightButtonText(String lightButtonText) {
        this.lightButtonText = lightButtonText;
        if (buttonLight != null) {
            buttonLight.setText(lightButtonText);
        }
    }

    public String getDarkButtonText() {
        return darkButtonText;
    }

    public void setDarkButtonText(String darkButtonText) {
        this.darkButtonText = darkButtonText;
        if (buttonDark != null) {
            buttonDark.setText(darkButtonText);
        }
    }

    public Icon getLightButtonIcon() {
        return lightButtonIcon;
    }

    public void setLightButtonIcon(Icon lightButtonIcon) {
        this.lightButtonIcon = lightButtonIcon;
        if (buttonLight != null) {
            buttonLight.setIcon(lightButtonIcon);
        }
    }

    public Icon getDarkButtonIcon() {
        return darkButtonIcon;
    }

    public void setDarkButtonIcon(Icon darkButtonIcon) {
        this.darkButtonIcon = darkButtonIcon;
        if (buttonDark != null) {
            buttonDark.setIcon(darkButtonIcon);
        }
    }

    public void addModeChangeListener(ModeChangeListener listener) {
        modeChangeListeners.add(listener);
    }

    public void removeModeChangeListener(ModeChangeListener listener) {
        modeChangeListeners.remove(listener);
    }

    public List<LookAndFeelLightDark> getSupportLookAndFeel() {
        return supportLookAndFeel;
    }

    public void setSupportLookAndFeel(List<LookAndFeelLightDark> supportLookAndFeel) {
        this.supportLookAndFeel = supportLookAndFeel;
    }

    public void addSupportLookAndFeel(Class<? extends FlatLaf> light, Class<? extends FlatLaf> dark) {
        this.supportLookAndFeel.add(new LookAndFeelLightDark(light, dark));
    }

    private void notifyModeChanged(boolean isDarkMode) {
        for (ModeChangeListener listener : modeChangeListeners) {
            listener.onModeChanged(isDarkMode);
        }
    }

    /**
     * DUAL_BUTTON: Two buttons: one for dark and one for light
     * TOGGLE_BUTTON: Single button that switches icons when clicked
     */
    public enum ButtonStyle {
        DUAL_BUTTON, TOGGLE_BUTTON
    }

    public interface ModeChangeListener {
        void onModeChanged(boolean isDarkMode);
    }

    public static class LookAndFeelLightDark {

        public Class<? extends FlatLaf> getLight() {
            return light;
        }

        public Class<? extends FlatLaf> getDark() {
            return dark;
        }

        private final Class<? extends FlatLaf> light;
        private final Class<? extends FlatLaf> dark;

        public LookAndFeelLightDark(Class<? extends FlatLaf> light, Class<? extends FlatLaf> dark) {
            this.light = light;
            this.dark = dark;
        }
    }
}
