package raven.modal.demo.forms;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import raven.modal.demo.system.Form;
import raven.modal.demo.utils.DateTimeFieldPicker;
import raven.modal.demo.utils.SystemForm;
import raven.swingpack.JDateTimeField;
import raven.swingpack.datetime.DateTimePart;
import raven.swingpack.datetime.DateTimePatternParser;
import raven.swingpack.datetime.validation.ValidationResult;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

@SystemForm(name = "DateTime Field", description = "DateTime Field user interface component  (swing pack)", tags = {"swing pack", "page"})
public class FormDateTimeField extends Form {

    public FormDateTimeField() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("wrap,fillx", "[fill]"));
        add(createInfo());
        add(createOptions());
    }

    private JPanel createInfo() {
        JPanel panel = new JPanel(new MigLayout("fillx,wrap", "[fill]"));
        JLabel title = new JLabel("DateTime Field");
        JTextPane text = new JTextPane();
        text.setText("The JDateTimeField component provides a simple and flexible way to select and manage date and time values in Swing applications.");
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

        panel.add(createDateTimeField());
        return panel;
    }

    private Component createDateTimeField() {
        JPanel panel = new JPanel(new MigLayout("wrap", "[250,fill]"));
        panel.setBorder(new TitledBorder("Example"));

        // datetime field
        JDateTimeField dateTimeField = new JDateTimeField();
        dateTimeField.setShowClearButton(true);
        dateTimeField.setValidator(dateTime -> {
            ValidationResult result = new ValidationResult();
            if (dateTime != null) {
                if (dateTime.isBefore(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))) {
                    result.addViolation("The selected datetime is in the past.");
                }
            } else {
                result.addViolation("A datetime selection is required.");
            }
            return result;
        });
        dateTimeField.setPlaceholderFormatter(context -> {
            DateTimePart part = context.getPart();
            if (part.getType() == DateTimePart.Type.MONTH) {
                return "Month";
            } else if (part.getType() == DateTimePart.Type.YEAR) {
                return "Year";
            } else if (part.getType() == DateTimePart.Type.DAY) {
                return "Day";
            } else if (part.getType() == DateTimePart.Type.HOUR_12 || part.getType() == DateTimePart.Type.HOUR_24 || part.getType() == DateTimePart.Type.MINUTE) {
                return "--";
            } else if (part.getType() == DateTimePart.Type.AM_PM) {
                return "--";
            }
            return null;
        });
        panel.add(dateTimeField);

        // date field
        JDateTimeField dateField = new JDateTimeField();
        dateField.setShowClearButton(true);
        dateField.setPlaceholderCaseStyle(JDateTimeField.CaseStyle.UPPER);
        dateField.setPattern("'Day' dd/MMM/yyyy");
        dateField.putClientProperty(FlatClientProperties.STYLE, "" +
                "selectedTextColor:$TextField.foreground;" +
                "selectionColor:$ToggleButton.selectedBackground;");
        dateField.setPlaceholderFormatter(context -> {
            if (context.getPart().isSeparator() && context.getIndex() == 0) {
                LocalDateTime dateTime = context.getDateTime();
                if (dateTime != null) {
                    return dateTime.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
                }
            }
            return null;
        });
        dateField.setValidator(dateTime -> {
            ValidationResult result = new ValidationResult();
            LocalDateTime now = LocalDateTime.now();
            if (dateTime != null) {
                if (dateTime.toLocalDate().isBefore(now.toLocalDate())) {
                    result.addViolation(ValidationResult.Severity.ERROR, "The selected date is in the past.");
                } else {
                    boolean isWeekend = dateTime.getDayOfWeek().getValue() >= 6;
                    if (isWeekend) {
                        result.addViolation(ValidationResult.Severity.WARNING, "Weekend dates may not be preferred.");
                    }
                    if (dateTime.isAfter(now.plusMonths(1))) {
                        result.addViolation(ValidationResult.Severity.ERROR, "Date cannot be more than one month in the future.");
                    }
                }
            } else {
                result.addViolation("A date selection is required.");
            }
            return result;
        });
        panel.add(dateField);

        // date field custom placeholder
        JDateTimeField dateCustomField = new JDateTimeField();
        dateCustomField.setShowClearButton(true);
        dateCustomField.setPattern("'Date: ' dd/MM/yyyy");
        dateCustomField.setPlaceholderCaseStyle(JDateTimeField.CaseStyle.UPPER);
        // custom value
        dateCustomField.setValueFormatter(context -> {
            DateTimePart part = context.getPart();
            if (!context.isSelected() && part.getType() == DateTimePart.Type.MONTH) {
                int value = part.getValue();
                if (value > 0 && value <= 12) {
                    return DateTimePatternParser.getMonthsLong(value);
                }
            }
            return null;
        });
        panel.add(dateCustomField);

        // time field
        JDateTimeField timeField = new JDateTimeField();
        timeField.setShowClearButton(true);
        timeField.setPattern("hh:mm a");
        timeField.setPlaceholderFormatter(context -> {
            DateTimePart part = context.getPart();
            if (part.getType() == DateTimePart.Type.HOUR_12 || part.getType() == DateTimePart.Type.HOUR_24 || part.getType() == DateTimePart.Type.MINUTE) {
                return "--";
            } else if (part.getType() == DateTimePart.Type.AM_PM) {
                return "--";
            }
            return null;
        });
        timeField.setValidator(dateTime -> {
            ValidationResult result = new ValidationResult();
            if (dateTime != null) {
                LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
                LocalTime time = dateTime.toLocalTime();
                LocalTime start = LocalTime.of(7, 0);
                LocalTime end = LocalTime.of(22, 0);
                if (time.isBefore(start) || time.isAfter(end)) {
                    result.addViolation(ValidationResult.Severity.ERROR, "Selected time is not within working hours (7AM to 10PM).");
                } else {
                    if (time.truncatedTo(ChronoUnit.MINUTES).isBefore(now.toLocalTime())) {
                        result.addViolation(ValidationResult.Severity.WARNING, "" +
                                "<html>\n" +
                                "Selected time is in the past.<br>\n" +
                                "This time may apply to the next day.\n" +
                                "</html>");
                    } else {
                        result.addViolation(ValidationResult.Severity.SUCCESS, "" +
                                "<html>\n" +
                                "Selected time is within working hours.<br>\n" +
                                "The schedule will be applied for the selected day.\n" +
                                "</html>");
                    }
                }
            } else {
                result.addViolation("A time selection is required.");
            }
            return result;
        });
        DateTimeFieldPicker.installDateTimePicker(dateTimeField);
        DateTimeFieldPicker.installDatePicker(dateField);
        DateTimeFieldPicker.installTimePicker(timeField);
        panel.add(timeField);

        dateTimeField.now();
        dateField.now();
        timeField.now();
        dateCustomField.now();

        return panel;
    }
}
