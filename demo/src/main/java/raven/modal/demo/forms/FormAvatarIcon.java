package raven.modal.demo.forms;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import raven.extras.AvatarIcon;
import raven.modal.demo.system.Form;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class FormAvatarIcon extends Form {

    public FormAvatarIcon() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("wrap,fillx,insets 7 15 7 15", "[fill]"));
        add(createInfo());
        add(createOptions());
    }

    private JPanel createInfo() {
        JPanel panel = new JPanel(new MigLayout("fillx,wrap", "[fill]"));
        JLabel title = new JLabel("Avatar Icon");
        JTextArea text = new JTextArea();
        text.setText("An avatar icon is a graphical element that visually represents a user within an application.\nIt is often used in user profiles, comment sections, and contact lists to personalize the user interface.");
        text.setEditable(false);
        text.setBorder(BorderFactory.createEmptyBorder());
        title.putClientProperty(FlatClientProperties.STYLE, "" +
                "font:bold +3");

        panel.add(title);
        panel.add(text);
        return panel;
    }

    private Component createOptions() {
        JPanel panel = new JPanel(new MigLayout("wrap 2,fillx", "[grow 0,fill][fill]", "[fill]"));
        panel.add(createOption());
        panel.add(createFlatLafOption());
        panel.add(createExample(), "span 2");
        return panel;
    }

    private Component createOption() {
        JPanel panel = new JPanel(new MigLayout("wrap 2"));
        panel.setBorder(new TitledBorder("Option"));
        jrRound = new JRadioButton("Round");
        jrMaskSquircle = new JRadioButton("Mask squircle");

        JLabel labelValue = new JLabel("100");
        JLabel labelWidth = new JLabel("100");
        JLabel labelHeight = new JLabel("100");

        sliderValue = new JSlider(0, 300, 200);
        sliderWidth = new JSlider(0, 300, 200);
        sliderHeight = new JSlider(0, 300, 200);

        sliderValue.setPaintTrack(true);
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
        panel.add(labelWidth);
        panel.add(new JLabel("Height:"));
        panel.add(sliderHeight, "split 2");
        panel.add(labelHeight);
        return panel;
    }

    private Component createFlatLafOption() {
        JPanel panel = new JPanel(new MigLayout("wrap"));
        panel.setBorder(new TitledBorder("FlatLaf option"));

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
        AvatarIcon avatarIcon = new AvatarIcon(getClass().getResource("/raven/modal/demo/images/profile_6.jpg"), width, height, value);
        if (squircle) {
            avatarIcon.setType(AvatarIcon.Type.MASK_SQUIRCLE);
        }
        labelAvatar.setIcon(avatarIcon);
    }

    private JLabel labelAvatar;

    // option
    private JRadioButton jrRound;
    private JRadioButton jrMaskSquircle;
    private JSlider sliderValue;
    private JSlider sliderWidth;
    private JSlider sliderHeight;
}
