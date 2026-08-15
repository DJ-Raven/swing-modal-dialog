package raven.modal.demo.utils;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import raven.datetime.DatePicker;
import raven.datetime.TimePicker;
import raven.swingpack.JDateTimeField;
import raven.swingpack.datetime.event.InputChangeEvent;
import raven.swingpack.datetime.event.InputChangeListener;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.function.Consumer;

public class DateTimeFieldPicker {

    public static void installDatePicker(JDateTimeField field) {
        Notify notify = new Notify();
        DatePicker datePicker = getDatePicker(field, notify);
        field.getHandler().addInputListener(new InputChangeListener() {
            @Override
            public void selectionChanged(InputChangeEvent event) {
            }

            @Override
            public void valueChanged(InputChangeEvent event) {
                if (!notify.notified) return;
                try {
                    notify.disable();
                    if (event.getValue() != null) {
                        datePicker.setSelectedDate(event.getValue().toLocalDate());
                    } else {
                        datePicker.clearSelectedDate();
                    }
                } finally {
                    notify.reset();
                }
            }
        });
        applyTrailingButton(field, index -> {
            field.requestFocus();
            datePicker.showPopup(field);
        }, new FlatSVGIcon("raven/datetime/icon/calendar.svg", 0.38f));
    }

    public static void installTimePicker(JDateTimeField field) {
        final Notify notify = new Notify();
        TimePicker timePicker = getTimePicker(field, notify);
        field.getHandler().addInputListener(new InputChangeListener() {
            @Override
            public void selectionChanged(InputChangeEvent event) {
            }

            @Override
            public void valueChanged(InputChangeEvent event) {
                if (!notify.notified) return;
                try {
                    notify.disable();
                    if (event.getValue() != null) {
                        timePicker.setSelectedTime(event.getValue().toLocalTime());
                    } else {
                        timePicker.clearSelectedTime();
                    }
                } finally {
                    notify.reset();
                }
            }
        });
        applyTrailingButton(field, index -> {
            field.requestFocus();
            timePicker.showPopup(field);
        }, new FlatSVGIcon("raven/datetime/icon/clock.svg", 0.38f));
    }

    public static void installDateTimePicker(JDateTimeField field) {
        final Notify timeNotify = new Notify();
        final Notify dateNotify = new Notify();
        TimePicker timePicker = getTimePicker(field, timeNotify);
        DatePicker datePicker = getDatePicker(field, dateNotify);
        field.getHandler().addInputListener(new InputChangeListener() {
            @Override
            public void selectionChanged(InputChangeEvent event) {
            }

            @Override
            public void valueChanged(InputChangeEvent event) {
                LocalDateTime date = event.getValue();
                if (timeNotify.notified) {
                    try {
                        timeNotify.disable();
                        if (date != null) {
                            timePicker.setSelectedTime(date.toLocalTime());
                        } else {
                            timePicker.clearSelectedTime();
                        }
                    } finally {
                        timeNotify.reset();
                    }
                }
                if (dateNotify.notified) {
                    try {
                        dateNotify.disable();
                        if (date != null) {
                            datePicker.setSelectedDate(date.toLocalDate());
                        } else {
                            datePicker.clearSelectedDate();
                        }
                    } finally {
                        dateNotify.reset();
                    }
                }
            }
        });
        applyTrailingButton(field, index -> {
                    field.requestFocus();
                    if (index == 0) {
                        datePicker.showPopup(field);
                    } else {
                        timePicker.showPopup(field);
                    }
                },
                new FlatSVGIcon("raven/datetime/icon/calendar.svg", 0.38f),
                new FlatSVGIcon("raven/datetime/icon/clock.svg", 0.38f));
    }

    private static TimePicker getTimePicker(JDateTimeField field, Notify notify) {
        TimePicker timePicker = new TimePicker();
        timePicker.addTimeSelectionListener(e -> {
            if (!notify.notified) return;
            try {
                LocalTime time = timePicker.getSelectedTime();
                notify.disable();
                if (time != null) {
                    LocalDateTime dateTime = field.getInputDateTime();
                    if (dateTime != null) {
                        field.setSelectedDateTime(dateTime.with(time));
                    } else {
                        field.setSelectedTime(time);
                    }
                } else {
                    field.clearSelectedDateTime();
                }
            } finally {
                notify.reset();
            }
        });
        return timePicker;
    }

    private static DatePicker getDatePicker(JDateTimeField field, Notify notify) {
        DatePicker datePicker = new DatePicker();
        datePicker.addDateSelectionListener(e -> {
            if (!notify.notified) return;
            try {
                LocalDate date = datePicker.getSelectedDate();
                notify.disable();
                if (date != null) {
                    LocalDateTime dateTime = field.getInputDateTime();
                    if (dateTime != null) {
                        field.setSelectedDateTime(dateTime.with(date));
                    } else {
                        field.setSelectedDate(date);
                    }
                } else {
                    field.clearSelectedDateTime();
                }
            } finally {
                notify.reset();
            }
        });
        return datePicker;
    }

    private static void applyTrailingButton(JTextField field, Consumer<Integer> consumer, Icon... icons) {
        JToolBar toolBar = new JToolBar();
        for (int i = 0; i < icons.length; i++) {
            final int index = i;
            Icon icon = icons[i];
            JButton button = new JButton(icon);
            button.addActionListener(e -> {
                consumer.accept(index);
            });
            toolBar.add(button);
        }
        field.putClientProperty(FlatClientProperties.TEXT_FIELD_TRAILING_COMPONENT, toolBar);
    }

    private static class Notify {

        private boolean notified = true;

        private void disable() {
            notified = false;
        }

        public void reset() {
            notified = true;
        }
    }
}
