package raven.modal.utils;

import com.formdev.flatlaf.util.UIScale;

import java.awt.*;

/**
 * @author Raven
 */
public class DynamicSize {

    public Number getX() {
        return x;
    }

    public Number getY() {
        return y;
    }

    public DynamicSize(Number x, Number y) {
        this.x = x;
        this.y = y;
    }

    public DynamicSize(DynamicSize size) {
        this.x = size.x;
        this.y = size.y;
    }

    private final Number x;
    private final Number y;

    public Dimension getSize(Dimension source, Dimension target) {
        float nx = x.floatValue() <= -1 ? source.width : x instanceof Float ? target.width * x.floatValue() : UIScale.scale(x.floatValue());
        float ny = y.floatValue() <= -1 ? source.height : y instanceof Float ? target.height * y.floatValue() : UIScale.scale(y.floatValue());
        return new Dimension((int) nx, (int) ny);
    }

    public Dimension getSize(Dimension target) {
        float nx = x instanceof Float ? target.width * x.floatValue() : UIScale.scale(x.floatValue());
        float ny = y instanceof Float ? target.height * y.floatValue() : UIScale.scale(y.floatValue());
        return new Dimension((int) nx, (int) ny);
    }
}