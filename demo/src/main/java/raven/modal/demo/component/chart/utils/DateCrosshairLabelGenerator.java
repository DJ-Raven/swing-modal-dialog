package raven.modal.demo.component.chart.utils;

import org.jfree.chart.labels.CrosshairLabelGenerator;
import org.jfree.chart.plot.Crosshair;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.MessageFormat;

public class DateCrosshairLabelGenerator implements CrosshairLabelGenerator, Serializable {

    private final String labelTemplate;
    private final DateFormat format;

    public DateCrosshairLabelGenerator(String labelTemplate, DateFormat format) {
        this.labelTemplate = labelTemplate;
        this.format = format;
    }

    @Override
    public String generateLabel(Crosshair crosshair) {
        Object[] v = new Object[]{this.format.format(crosshair.getValue())};
        String result = MessageFormat.format(this.labelTemplate, v);
        return result;
    }
}
