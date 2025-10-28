package raven.modal.demo.component.swingpack;

import raven.swingpack.multiselect.icons.DefaultRemovableIcon;

import java.awt.*;
import java.awt.geom.Path2D;

public class CrossRemovableIcon extends DefaultRemovableIcon {

    @Override
    protected void paintIcon(Component com, Graphics2D g, boolean pressed, boolean focus) {
        Path2D path = new Path2D.Float(Path2D.WIND_EVEN_ODD, 4);
        path.moveTo(5, 5);
        path.lineTo(11, 11);
        path.moveTo(5, 11);
        path.lineTo(11, 5);
        g.draw(path);
    }
}