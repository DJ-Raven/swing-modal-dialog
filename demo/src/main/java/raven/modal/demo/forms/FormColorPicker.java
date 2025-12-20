package raven.modal.demo.forms;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import raven.color.ColorPicker;
import raven.color.DinoColorPickerModel;
import raven.color.DiskColorPickerModel;
import raven.color.component.ColorPaletteType;
import raven.modal.demo.component.LabelButton;
import raven.modal.demo.simple.color.ColorHarmony;
import raven.modal.demo.simple.color.ColorWheelHarmoniesModel;
import raven.modal.demo.simple.color.WheelColorHarmoniesPreview;
import raven.modal.demo.simple.color.harmony.Rectangle;
import raven.modal.demo.simple.color.harmony.*;
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
        JPanel panel = new JPanel(new MigLayout("wrap 3,fillx", "[grow 0][grow 0][fill]", "[fill]"));
        panel.add(createColorPaletteOption());
        panel.add(createColorModelOption());
        panel.add(createOtherOption());
        panel.add(createColorPicker(), "span 3,grow 1");
        panel.add(createColorWheelHarmonies(), "span 3,grow 1");
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

    private Component createColorModelOption() {
        JPanel panel = new JPanel(new MigLayout("wrap 5,flowy", "[200]"));
        panel.setBorder(new TitledBorder("Color model option"));
        JRadioButton jrDino = new JRadioButton("Dino Model", true);
        JRadioButton jrDisk = new JRadioButton("Disk Model");

        ButtonGroup group = new ButtonGroup();
        group.add(jrDino);
        group.add(jrDisk);

        jrDino.addActionListener(e -> {
            if (jrDino.isSelected()) {
                colorPicker.setModel(new DinoColorPickerModel());
            }
        });
        jrDisk.addActionListener(e -> {
            if (jrDisk.isSelected()) {
                colorPicker.setModel(new DiskColorPickerModel());
            }
        });

        panel.add(jrDino);
        panel.add(jrDisk);

        return panel;
    }

    private Component createOtherOption() {
        JPanel panel = new JPanel(new MigLayout("wrap 5,flowy", "[200]"));
        panel.setBorder(new TitledBorder("Other option"));

        JCheckBox chPipettePicker = new JCheckBox("Pipette Picker Enabled (Windows)", true);
        chPipettePicker.addActionListener(e -> colorPicker.setColorPipettePickerEnabled(chPipettePicker.isSelected()));

        panel.add(chPipettePicker);

        return panel;
    }

    private Component createColorWheelHarmonies() {
        JPanel panel = new JPanel(new MigLayout("filly", "", "[fill]"));
        panel.setBorder(new TitledBorder("Color Wheel Harmonies"));

        JPanel panelRight = new JPanel(new MigLayout("wrap,fill,gapy 5", "[fill]", "[grow 0][fill]"));
        WheelColorHarmoniesPreview colorPreview = new WheelColorHarmoniesPreview();
        JComboBox<ColorHarmony> comboHarmony = new JComboBox<>(new ColorHarmony[]{
                new Analogous(),
                new Complementary(),
                new DoubleSplitComplementary(),
                new Rectangle(),
                new SplitComplementary(),
                new Tetradic(),
                new Triadic()
        });

        panelRight.add(comboHarmony, "gapx 7 7");
        panelRight.add(colorPreview);

        ColorWheelHarmoniesModel colorModel = new ColorWheelHarmoniesModel();

        ColorPicker colorPk = new ColorPicker(colorModel);
        colorPk.setColorPaletteEnabled(false);

        colorModel.addChangeListener((color, evt) -> colorPreview.setColors(colorModel.getSelectedColors()));
        comboHarmony.addActionListener(e -> {
            colorModel.setColorPalette((ColorHarmony) comboHarmony.getSelectedItem());
            colorPreview.setColors(colorModel.getSelectedColors());
        });

        colorPreview.setColors(new Color[]{Color.WHITE, Color.WHITE, Color.WHITE});

        panel.add(colorPk, "height 250::");
        panel.add(panelRight, "height 250::");
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
