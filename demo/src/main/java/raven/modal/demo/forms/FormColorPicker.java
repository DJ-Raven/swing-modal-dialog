package raven.modal.demo.forms;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import raven.color.ColorPicker;
import raven.color.component.ColorPaletteType;
import raven.modal.demo.component.LabelButton;
import raven.modal.demo.system.Form;
import raven.modal.demo.utils.SystemForm;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

@SystemForm(name = "Color Picker", description = "color picker user interface component")
public class FormColorPicker extends Form {

    public FormColorPicker() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("wrap,fillx", "[fill]"));
        add(createInfo());
        add(createOptions());
    }

    private JPanel createInfo() {
        JPanel panel = new JPanel(new MigLayout("fillx,wrap", "[fill]"));
        JLabel title = new JLabel("Color Picker");
        JTextPane text = new JTextPane();
        text.setText("A color picker is a user interface component that allows users to choose a color easily and accurately.");
        text.setEditable(false);
        text.setBorder(BorderFactory.createEmptyBorder());
        title.putClientProperty(FlatClientProperties.STYLE, "" +
                "font:bold +3");

        panel.add(title);
        panel.add(text, "width 500");
        return panel;
    }

    private Component createOptions() {
        JPanel panel = new JPanel(new MigLayout("wrap 1,fillx", "[fill]", "[fill]"));
        panel.add(createColorPaletteOption());
        panel.add(createColorPicker());
        return panel;
    }

    private Component createColorPaletteOption() {
        JPanel panel = new JPanel(new MigLayout("wrap 5,flowy", "[200]"));
        panel.setBorder(new TitledBorder("Color palette option"));
        JRadioButton jrDisabled = new JRadioButton("Disabled");
        JRadioButton jrDefault = new JRadioButton("Default Color", true);
        JRadioButton jrTailwind = new JRadioButton("Tailwind Color");
        JRadioButton jrMaterial = new JRadioButton("Material Color");

        ButtonGroup group = new ButtonGroup();
        group.add(jrDisabled);
        group.add(jrDefault);
        group.add(jrTailwind);
        group.add(jrMaterial);

        jrDisabled.addActionListener(e -> {
            colorPicker.setColorPaletteEnabled(false);
        });
        jrDefault.addActionListener(e -> {
            if (jrDefault.isSelected()) {
                colorPicker.setColorPaletteEnabled(true);
                colorPicker.applyColorPaletteType(ColorPaletteType.DEFAULT);
            }
        });
        jrTailwind.addActionListener(e -> {
            if (jrTailwind.isSelected()) {
                colorPicker.setColorPaletteEnabled(true);
                colorPicker.applyColorPaletteType(ColorPaletteType.TAILWIND);
            }
        });
        jrMaterial.addActionListener(e -> {
            if (jrMaterial.isSelected()) {
                colorPicker.setColorPaletteEnabled(true);
                colorPicker.applyColorPaletteType(ColorPaletteType.MATERIAL);
            }
        });

        panel.add(jrDisabled);
        panel.add(jrDefault);
        panel.add(jrTailwind);
        panel.add(jrMaterial);

        return panel;
    }

    private Component createColorPicker() {
        JPanel panel = new JPanel(new MigLayout());
        panel.setBorder(new TitledBorder("Example"));

        colorPicker = new ColorPicker();

        LabelButton lbDefault = new LabelButton("Show dialog");
        lbDefault.addOnClick(o -> {
            ColorPicker.showDialog(this, "Pick Color", colorPicker);
        });

        panel.add(lbDefault);
        return panel;
    }

    private ColorPicker colorPicker;
}
