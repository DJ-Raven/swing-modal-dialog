package raven.modal.demo.forms;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import raven.modal.demo.component.swingpack.ColorMultiSelectItemRenderer;
import raven.modal.demo.component.swingpack.IconMultiSelectItemRenderer;
import raven.modal.demo.system.Form;
import raven.modal.demo.utils.SystemForm;
import raven.swingpack.JMultiSelectComboBox;
import raven.swingpack.multiselect.DefaultMultiSelectItemRenderer;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

@SystemForm(name = "MultiSelect", description = "multi-select user interface component (swing pack)", tags = {"swing pack", "combobox", "multi", "select"})
public class FormMultiSelect extends Form {

    public FormMultiSelect() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("wrap,fillx", "[fill]"));
        add(createInfo());
        add(createOptions());
    }

    private JPanel createInfo() {
        JPanel panel = new JPanel(new MigLayout("fillx,wrap", "[fill]"));
        JLabel title = new JLabel("MultiSelect");
        JTextPane text = new JTextPane();
        text.setText("The JMultiSelectComboBox is a modern Swing component that allows users to select multiple items in a single combo box.\n" +
                "It supports wrapping, scrolling, and right-to-left (RTL) layouts.");
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
        // no options
        panel.add(createOption());
        panel.add(createMultiSelect());
        return panel;
    }

    private Component createOption() {
        JPanel panel = new JPanel(new MigLayout());
        panel.setBorder(new TitledBorder("Option"));

        JCheckBox chScroll = new JCheckBox("Enable scroll");
        JCheckBox chShowRemovable = new JCheckBox("Show removable", true);
        JCheckBox chIconRenderer = new JCheckBox("Icon renderer", true);
        JCheckBox chFullRound = new JCheckBox("Full round");

        chScroll.addActionListener(e -> {
            if (chScroll.isSelected()) {
                multiSelect.setDisplayMode(JMultiSelectComboBox.DisplayMode.WRAP_SCROLL);
                multiSelectColor.setDisplayMode(JMultiSelectComboBox.DisplayMode.WRAP_SCROLL);
            } else {
                multiSelect.setDisplayMode(JMultiSelectComboBox.DisplayMode.AUTO_WRAP);
                multiSelectColor.setDisplayMode(JMultiSelectComboBox.DisplayMode.AUTO_WRAP);
            }
        });
        chShowRemovable.addActionListener(e -> {
            multiSelect.setShowItemRemovableIcon(chShowRemovable.isSelected());
            multiSelectColor.setShowItemRemovableIcon(chShowRemovable.isSelected());
        });
        chIconRenderer.addActionListener(e -> {
            if (chIconRenderer.isSelected()) {
                IconMultiSelectItemRenderer.apply(multiSelect);
            } else {
                multiSelect.setItemRenderer(new DefaultMultiSelectItemRenderer());
            }
        });

        chFullRound.addActionListener(e -> {
            if (chFullRound.isSelected()) {
                multiSelect.setItemArc(999);
                multiSelectColor.setItemArc(999);
            } else {
                multiSelect.setItemArc(-1);
                multiSelectColor.setItemArc(-1);
            }
        });

        panel.add(chScroll);
        panel.add(chShowRemovable);
        panel.add(chIconRenderer);
        panel.add(chFullRound);

        return panel;
    }

    private Component createMultiSelect() {
        JPanel panel = new JPanel(new MigLayout("fillx", "[fill][fill]"));
        panel.setBorder(new TitledBorder("Example"));

        // create data
        String[] items = new String[]{
                "Blueberry",
                "Kiwi",
                "Mango",
                "Pineapple",
                "Strawberry",
                "Watermelon",
                "Raspberry",
                "Peach",
                "Orange",
                "Apple",
                "Banana",
                "Pear",
                "Cherry",
                "Papaya",
                "Plum",
                "Apricot",
                "Lime",
                "Lychee",
                "Coconut",
                "Pomegranate",
                "Avocado",
                "Grape"
        };

        ColorMultiSelectItemRenderer.ColorItem[] colorItems =
                new ColorMultiSelectItemRenderer.ColorItem[]{
                        new ColorMultiSelectItemRenderer.ColorItem("Blue", "#1976D2", "#64B5F6"),
                        new ColorMultiSelectItemRenderer.ColorItem("Teal", "#00796B", "#4DB6AC"),
                        new ColorMultiSelectItemRenderer.ColorItem("Green", "#388E3C", "#81C784"),
                        new ColorMultiSelectItemRenderer.ColorItem("Amber", "#FFA000", "#FFB74D"),
                        new ColorMultiSelectItemRenderer.ColorItem("Deep Orange", "#E64A19", "#FF7043"),
                        new ColorMultiSelectItemRenderer.ColorItem("Pink", "#D81B60", "#F06292"),
                        new ColorMultiSelectItemRenderer.ColorItem("Purple", "#8E24AA", "#BA68C8"),
                        new ColorMultiSelectItemRenderer.ColorItem("Gray", "#616161", "#B0BEC5")
                };

        // create multi select
        multiSelect = new JMultiSelectComboBox<>();
        multiSelectColor = new JMultiSelectComboBox<>();

        multiSelect.setRow(3);
        multiSelectColor.setRow(3);

        // add item
        for (String item : items) {
            multiSelect.addItem(item, true);
        }
        for (ColorMultiSelectItemRenderer.ColorItem item : colorItems) {
            multiSelectColor.addItem(item, true);
        }

        // style
        multiSelect.putClientProperty(FlatClientProperties.STYLE, "" +
                "background:null;" +
                "editableBackground:null;");
        multiSelectColor.putClientProperty(FlatClientProperties.STYLE, "" +
                "background:null;" +
                "editableBackground:null;");

        IconMultiSelectItemRenderer.apply(multiSelect);
        ColorMultiSelectItemRenderer.apply(multiSelectColor);

        panel.add(multiSelect);
        panel.add(multiSelectColor);

        return panel;
    }

    private JMultiSelectComboBox<String> multiSelect;
    private JMultiSelectComboBox<ColorMultiSelectItemRenderer.ColorItem> multiSelectColor;
}
