package raven.modal.demo.forms;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import raven.color.*;
import raven.color.component.palette.ColorPaletteType;
import raven.modal.demo.component.LabelButton;
import raven.modal.demo.simple.color.AbstractColorHarmony;
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
        JRadioButton jrDino = new JRadioButton("Dino", true);
        JRadioButton jrDisk = new JRadioButton("Disk");
        JRadioButton jrCorelTriangle = new JRadioButton("Corel Triangle");
        JRadioButton jrCorelSquare = new JRadioButton("Corel Square");
        JRadioButton jrCorelRhombus = new JRadioButton("Corel Rhombus");

        ButtonGroup group = new ButtonGroup();
        group.add(jrDino);
        group.add(jrDisk);
        group.add(jrCorelTriangle);
        group.add(jrCorelSquare);
        group.add(jrCorelRhombus);

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
        jrCorelTriangle.addActionListener(e -> {
            if (jrCorelTriangle.isSelected()) {
                colorPicker.setModel(new CorelTriangleColorPickerModel());
            }
        });
        jrCorelSquare.addActionListener(e -> {
            if (jrCorelSquare.isSelected()) {
                colorPicker.setModel(new CorelSquareColorPickerModel());
            }
        });
        jrCorelRhombus.addActionListener(e -> {
            if (jrCorelRhombus.isSelected()) {
                colorPicker.setModel(new CorelRhombusColorPickerModel());
            }
        });

        panel.add(jrDino);
        panel.add(jrDisk);
        panel.add(jrCorelTriangle);
        panel.add(jrCorelSquare);
        panel.add(jrCorelRhombus);

        return panel;
    }

    private Component createOtherOption() {
        JPanel panel = new JPanel(new MigLayout("wrap 5,flowy", "[200]"));
        panel.setBorder(new TitledBorder("Other option"));

        JCheckBox chPipettePicker = new JCheckBox("Pipette Picker Enabled (Windows)", true);
        JCheckBox chPreview = new JCheckBox("Preview Enabled", true);
        JCheckBox chAlpha = new JCheckBox("Alpha Enabled", true);

        chPipettePicker.addActionListener(e -> colorPicker.setColorPipettePickerEnabled(chPipettePicker.isSelected()));
        chPreview.addActionListener(e -> colorPicker.setColorPreviewEnabled(chPreview.isSelected()));
        chAlpha.addActionListener(e -> colorPicker.setColorAlphaEnabled(chAlpha.isSelected()));

        panel.add(chPipettePicker);
        panel.add(chPreview);
        panel.add(chAlpha);

        return panel;
    }

    private Component createColorWheelHarmonies() {
        JPanel panel = new JPanel(new MigLayout("filly", "", "[fill]"));
        panel.setBorder(new TitledBorder("Color Wheel Harmonies"));

        JPanel panelRight = new JPanel(new MigLayout("wrap,fill,gapy 5", "[fill]", "[grow 0][fill]"));
        WheelColorHarmoniesPreview colorPreview = new WheelColorHarmoniesPreview();
        JComboBox<AbstractColorHarmony> comboHarmony = new JComboBox<>(new AbstractColorHarmony[]{
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
        colorPk.setSelectedColor(Color.decode("#5856FF"));
        colorPk.setColorPaletteEnabled(false);

        colorModel.addChangeListener((color, evt) -> colorPreview.setColors(colorModel.getSelectedColors()));
        comboHarmony.addActionListener(e -> {
            colorModel.setColorHarmony((AbstractColorHarmony) comboHarmony.getSelectedItem());
            colorPreview.setColors(colorModel.getSelectedColors());
        });

        colorPreview.setColors(colorModel.getSelectedColors());

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

    @Override
    protected boolean formCheck() {
        boolean changed = super.formCheck();
        if (changed) {
            SwingUtilities.updateComponentTreeUI(colorPicker);
        }
        return changed;
    }
}
