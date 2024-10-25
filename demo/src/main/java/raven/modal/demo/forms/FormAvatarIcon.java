package raven.modal.demo.forms;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import raven.extras.AvatarIcon;
import raven.modal.demo.component.AvatarBorderColorIcon;
import raven.modal.demo.system.Form;
import raven.modal.demo.utils.SystemForm;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

@SystemForm(name = "Avatar Icon", description = "avatar icon graphical element", tags = {"profile", "photo", "image", "circle", "picture"})
public class FormAvatarIcon extends Form {

    public FormAvatarIcon() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("wrap,fillx", "[fill]"));
        add(createInfo());
        add(createOptions());
    }

    private JPanel createInfo() {
        JPanel panel = new JPanel(new MigLayout("fillx,wrap", "[fill]"));
        JLabel title = new JLabel("Avatar Icon");
        JTextPane text = new JTextPane();
        text.setText("An avatar icon is a graphical element that visually represents a user within an application.\nIt is often used in user profiles, comment sections, and contact lists to personalize the user interface.");
        text.setEditable(false);
        text.setBorder(BorderFactory.createEmptyBorder());
        title.putClientProperty(FlatClientProperties.STYLE, "" +
                "font:bold +3");

        panel.add(title);
        panel.add(text, "width 500");
        return panel;
    }

    private Component createOptions() {
        JPanel panel = new JPanel(new MigLayout("wrap 2,fillx", "[grow 0,fill][fill]", "[fill]"));
        panel.add(createOption());
        panel.add(createBorderOption());
        panel.add(createExample(), "span 2");
        return panel;
    }

    private Component createOption() {
        JPanel panel = new JPanel(new MigLayout("wrap 2"));
        panel.setBorder(new TitledBorder("Option"));
        jrRound = new JRadioButton("Round");
        jrMaskSquircle = new JRadioButton("Mask squircle");

        sliderValue = new JSlider(0, 300, 200);
        sliderWidth = new JSlider(0, 300, 200);
        sliderHeight = new JSlider(0, 300, 200);

        JLabel labelValue = new JLabel(sliderValue.getValue() + "");
        JLabel labelWidth = new JLabel(sliderWidth.getValue() + "");
        JLabel labelHeight = new JLabel(sliderHeight.getValue() + "");

        ButtonGroup group = new ButtonGroup();
        group.add(jrRound);
        group.add(jrMaskSquircle);

        jrRound.setSelected(true);

        jrRound.addActionListener(e -> {
            if (jrRound.isSelected()) {
                labelValue.setText(sliderValue.getValue() + "");
            } else {
                labelValue.setText(getSquircleValue() + "");
            }
            setAvatar();
        });
        jrMaskSquircle.addActionListener(e -> {
            if (jrRound.isSelected()) {
                labelValue.setText(sliderValue.getValue() + "");
            } else {
                labelValue.setText(getSquircleValue() + "");
            }
            setAvatar();
        });

        sliderValue.addChangeListener(e -> {
            if (jrRound.isSelected()) {
                labelValue.setText(sliderValue.getValue() + "");
            } else {
                labelValue.setText(getSquircleValue() + "");
            }
            setAvatar();
        });
        sliderWidth.addChangeListener(e -> {
            labelWidth.setText(Math.max(sliderWidth.getValue(), 1) + "");
            setAvatar();
        });
        sliderHeight.addChangeListener(e -> {
            labelHeight.setText(Math.max(sliderHeight.getValue(), 1) + "");
            setAvatar();
        });

        panel.add(new JLabel("Type:"));
        panel.add(jrRound, "split 2");
        panel.add(jrMaskSquircle);
        panel.add(new JLabel("Round:"));
        panel.add(sliderValue, "split 2");
        panel.add(labelValue, "width 40::");
        panel.add(new JLabel("Width:"));
        panel.add(sliderWidth, "split 2");
        panel.add(labelWidth, "width 40::");
        panel.add(new JLabel("Height:"));
        panel.add(sliderHeight, "split 2");
        panel.add(labelHeight, "width 40::");
        return panel;
    }

    private Component createBorderOption() {
        JPanel panel = new JPanel(new MigLayout("wrap 2,hidemode 3"));
        panel.setBorder(new TitledBorder("Border option"));
        jrColor = new JRadioButton("Color");
        jrGradient = new JRadioButton("Gradient");

        sliderBorderWidth = new JSlider(0, 50, 5);
        sliderInnerBorderWidth = new JSlider(0, 50, 5);

        JLabel labelBorderWidth = new JLabel(sliderBorderWidth.getValue() + "");
        JLabel labelInnerBorderWidth = new JLabel(sliderInnerBorderWidth.getValue() + "");

        AvatarIcon.BorderColor[] colors = {
                new AvatarIcon.BorderColor(Color.decode("#3B82F6")),
                new AvatarIcon.BorderColor(Color.decode("#10B981")),
                new AvatarIcon.BorderColor(Color.decode("#F97316")),
                new AvatarIcon.BorderColor(Color.decode("#E11D48")),
                new AvatarIcon.BorderColor(Color.decode("#7C3AED")),
                new AvatarIcon.BorderColor(Color.decode("#14B8A6")),
                new AvatarIcon.BorderColor(Color.decode("#FACC15")),
        };

        AvatarIcon.BorderColor[] gradientColors = {
                new AvatarIcon.BorderColor(Color.decode("#FF7E5F"), Color.decode("#FEB47B")),
                new AvatarIcon.BorderColor(Color.decode("#00C9FF"), Color.decode("#92FE9D")),
                new AvatarIcon.BorderColor(Color.decode("#FF4E50"), Color.decode("#F9D423")),
                new AvatarIcon.BorderColor(Color.decode("#DA22FF"), Color.decode("#9733EE")),
                new AvatarIcon.BorderColor(Color.decode("#11998E"), Color.decode("#38EF7D")),
                new AvatarIcon.BorderColor(Color.decode("#642B73"), Color.decode("#C6426E")),
                new AvatarIcon.BorderColor(Color.decode("#ED4264"), Color.decode("#FFEDBC")),
        };

        final Component componentColors = createColors(colors, false);
        final Component componentGradientColors = createColors(gradientColors, true);

        componentGradientColors.setVisible(false);

        ButtonGroup group = new ButtonGroup();
        group.add(jrColor);
        group.add(jrGradient);

        jrColor.setSelected(true);

        jrColor.addActionListener(e -> {
            componentColors.setVisible(jrColor.isSelected());
            componentGradientColors.setVisible(!jrColor.isSelected());
            setAvatar();
        });
        jrGradient.addActionListener(e -> {
            componentColors.setVisible(!jrGradient.isSelected());
            componentGradientColors.setVisible(jrGradient.isSelected());
            setAvatar();
        });
        sliderBorderWidth.addChangeListener(e -> {
            labelBorderWidth.setText(sliderBorderWidth.getValue() + "");
            setAvatar();
        });
        sliderInnerBorderWidth.addChangeListener(e -> {
            labelInnerBorderWidth.setText(sliderInnerBorderWidth.getValue() + "");
            setAvatar();
        });

        panel.add(new JLabel("Color type:"));
        panel.add(jrColor, "split 2");
        panel.add(jrGradient);
        panel.add(new JLabel("Border width:"));
        panel.add(sliderBorderWidth, "split 2");
        panel.add(labelBorderWidth, "width 40::");
        panel.add(new JLabel("Inner border width:"));
        panel.add(sliderInnerBorderWidth, "split 2");
        panel.add(labelInnerBorderWidth, "width 40::");
        panel.add(new JLabel("Border color:"));
        panel.add(componentColors);
        panel.add(componentGradientColors);
        return panel;
    }

    private Component createExample() {
        JPanel panel = new JPanel(new MigLayout());
        panel.setBorder(new TitledBorder("Example"));

        labelAvatar = new JLabel();

        setAvatar();

        panel.add(labelAvatar);
        return panel;
    }

    private float getSquircleValue() {
        float value = sliderValue.getValue();
        float max = sliderValue.getMaximum();
        float v = value / max;
        // to get two decimal places
        return Math.max(Math.round((v * 15f) * 100f) / 100f, 0.1f);
    }

    private void setAvatar() {
        boolean squircle = jrMaskSquircle.isSelected();
        float value = squircle ? getSquircleValue() : sliderValue.getValue();
        int width = Math.max(sliderWidth.getValue(), 1);
        int height = Math.max(sliderHeight.getValue(), 1);
        int borderWidth = sliderBorderWidth.getValue();
        int innerBorderWidth = sliderInnerBorderWidth.getValue();
        AvatarIcon avatarIcon = new AvatarIcon(getClass().getResource("/raven/modal/demo/images/profile_6.jpg"), width, height, value);
        if (jrColor.isSelected()) {
            avatarIcon.setBorderColor(borderColor);
        } else {
            avatarIcon.setBorderColor(borderGradientColor);
        }
        avatarIcon.setBorder(borderWidth, innerBorderWidth);
        if (squircle) {
            avatarIcon.setType(AvatarIcon.Type.MASK_SQUIRCLE);
        }
        labelAvatar.setIcon(avatarIcon);
    }

    private Component createColors(AvatarIcon.BorderColor[] borderColors, boolean gradient) {
        JToolBar toolBar = new JToolBar();
        toolBar.putClientProperty(FlatClientProperties.STYLE, "" +
                "hoverButtonGroupBackground:null;");
        ButtonGroup group = new ButtonGroup();
        boolean init = false;
        for (AvatarIcon.BorderColor color : borderColors) {
            JToggleButton button = new JToggleButton(new AvatarBorderColorIcon(color));
            button.addActionListener(e -> {
                if (button.isSelected()) {
                    if (gradient) {
                        borderGradientColor = color;
                    } else {
                        borderColor = color;
                    }
                    setAvatar();
                }
            });
            toolBar.add(button);
            group.add(button);
            if (!init) {
                button.setSelected(true);
                init = true;
                if (gradient) {
                    borderGradientColor = color;
                } else {
                    borderColor = color;
                }
            }
        }
        return toolBar;
    }

    private JLabel labelAvatar;

    // option
    private JRadioButton jrRound;
    private JRadioButton jrMaskSquircle;
    private JSlider sliderValue;
    private JSlider sliderWidth;
    private JSlider sliderHeight;

    // border option
    private JRadioButton jrColor;
    private JRadioButton jrGradient;
    private JSlider sliderBorderWidth;
    private JSlider sliderInnerBorderWidth;

    // value
    private AvatarIcon.BorderColor borderColor;
    private AvatarIcon.BorderColor borderGradientColor;
}
