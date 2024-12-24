package raven.modal.demo.forms;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import raven.datetime.DatePicker;
import raven.datetime.TimePicker;
import raven.modal.demo.system.Form;
import raven.modal.demo.utils.SystemForm;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.time.LocalTime;

@SystemForm(name = "DateTime", description = "date time picker user interface component")
public class FormDateTime extends Form {

    public FormDateTime() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("wrap,fillx", "[fill]"));
        add(createInfo());
        add(createOptions());
    }

    @Override
    public void formInit() {
        datePicker.now();
        timePicker.now();
    }

    private JPanel createInfo() {
        JPanel panel = new JPanel(new MigLayout("fillx,wrap", "[fill]"));
        JLabel title = new JLabel("DateTime Picker");
        JTextPane text = new JTextPane();
        text.setText("A DateTime picker is a user interface component that allows users to select both a date and a time from a graphical interface.\nIt simplifies date and time input, ensuring accuracy and consistency.");
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
        panel.add(createDateOption());
        panel.add(createTimeOption());
        panel.add(createDateTimePicker(), "span 2");
        return panel;
    }

    private Component createDateOption() {
        JPanel panel = new JPanel(new MigLayout("wrap", "[200]"));
        panel.setBorder(new TitledBorder("Date option"));
        JCheckBox chDateBetween = new JCheckBox("Date between");
        JCheckBox chUsePanelOption = new JCheckBox("Use panel option");
        JCheckBox chCloseAfterSelected = new JCheckBox("Close after selected");
        JCheckBox chEditorValidation = new JCheckBox("Editor validation", true);
        JCheckBox chValidationOnNull = new JCheckBox("Validation on null");
        JCheckBox chAnimationEnabled = new JCheckBox("Animation Enabled", true);

        chDateBetween.addActionListener(e -> {
            datePicker.setDateSelectionMode(chDateBetween.isSelected() ? DatePicker.DateSelectionMode.BETWEEN_DATE_SELECTED
                    : DatePicker.DateSelectionMode.SINGLE_DATE_SELECTED);
        });
        chUsePanelOption.addActionListener(e -> datePicker.setUsePanelOption(chUsePanelOption.isSelected()));
        chCloseAfterSelected.addActionListener(e -> datePicker.setCloseAfterSelected(chCloseAfterSelected.isSelected()));
        chEditorValidation.addActionListener(e -> {
            datePicker.setEditorValidation(chEditorValidation.isSelected());
            chValidationOnNull.setEnabled(chEditorValidation.isSelected());
        });
        chValidationOnNull.addActionListener(e -> datePicker.setValidationOnNull(chValidationOnNull.isSelected()));
        chAnimationEnabled.addActionListener(e -> datePicker.setAnimationEnabled(chAnimationEnabled.isSelected()));

        panel.add(chDateBetween);
        panel.add(chUsePanelOption);
        panel.add(chCloseAfterSelected);
        panel.add(chEditorValidation);
        panel.add(chValidationOnNull);
        panel.add(chAnimationEnabled);

        return panel;
    }

    private Component createTimeOption() {
        JPanel panel = new JPanel(new MigLayout("wrap"));
        panel.setBorder(new TitledBorder("Time option"));
        JCheckBox ch24HourView = new JCheckBox("24 hour view");
        JCheckBox chDisablePast = new JCheckBox("Disable past");
        JCheckBox chEditorValidation = new JCheckBox("Editor validation", true);
        JCheckBox chValidationOnNull = new JCheckBox("Validation on null");
        JCheckBox chHorizontal = new JCheckBox("Horizontal");

        ch24HourView.addActionListener(e -> {
            timePicker.set24HourView(ch24HourView.isSelected());
        });
        chDisablePast.addActionListener(e -> {
            boolean disable = chDisablePast.isSelected();
            if (disable) {
                disablePast();
            } else {
                timePicker.setTimeSelectionAble(null);
            }
        });
        chEditorValidation.addActionListener(e -> {
            timePicker.setEditorValidation(chEditorValidation.isSelected());
            chValidationOnNull.setEnabled(chEditorValidation.isSelected());
        });

        chValidationOnNull.addActionListener(e -> timePicker.setValidationOnNull(chValidationOnNull.isSelected()));
        chHorizontal.addActionListener(e -> timePicker.setOrientation(chHorizontal.isSelected() ? SwingConstants.HORIZONTAL
                : SwingConstants.VERTICAL));

        panel.add(ch24HourView);
        panel.add(chDisablePast);
        panel.add(chEditorValidation);
        panel.add(chValidationOnNull);
        panel.add(chHorizontal);

        return panel;
    }

    private Component createDateTimePicker() {
        JPanel panel = new JPanel(new MigLayout());
        panel.setBorder(new TitledBorder("Example"));

        datePicker = new DatePicker();
        timePicker = new TimePicker();

        JFormattedTextField dateEditor = new JFormattedTextField();
        JFormattedTextField timeEditor = new JFormattedTextField();

        datePicker.setEditor(dateEditor);
        timePicker.setEditor(timeEditor);

        panel.add(dateEditor, "width 220");
        panel.add(timeEditor, "width 220");
        return panel;
    }

    private void disablePast() {
        timePicker.setTimeSelectionAble((time, hourView) -> {
            LocalTime now = LocalTime.now().withSecond(0).withNano(0);
            if (hourView) {
                return time.getHour() >= now.getHour();
            }
            return !time.isBefore(now);
        });
    }

    private DatePicker datePicker;
    private TimePicker timePicker;
}
