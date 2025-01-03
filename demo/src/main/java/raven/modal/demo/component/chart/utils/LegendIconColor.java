package raven.modal.demo.component.chart.utils;

import com.formdev.flatlaf.icons.FlatAbstractIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class LegendIconColor extends FlatAbstractIcon {

    public LegendIconColor(Color color) {
        super(10, 10, color);
    }

    @Override
    protected void paintIcon(Component component, Graphics2D g2) {
        if (component instanceof AbstractButton) {
            if (!((AbstractButton) component).isSelected()) {
                g2.setComposite(AlphaComposite.SrcOver.derive(0.5f));
            }
        }
        g2.setColor(color);
        g2.fill(new Ellipse2D.Double(0, 0, width, height));
        g2.dispose();
    }
}
