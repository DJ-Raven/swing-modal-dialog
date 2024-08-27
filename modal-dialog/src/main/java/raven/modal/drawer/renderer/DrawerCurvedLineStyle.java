package raven.modal.drawer.renderer;

import com.formdev.flatlaf.util.UIScale;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;

/**
 * This class for draw curved line style
 *
 * @author Raven
 */
public class DrawerCurvedLineStyle extends AbstractDrawerLineStyleRenderer {

    private final boolean useRound;

    public DrawerCurvedLineStyle() {
        this(true);
    }

    public DrawerCurvedLineStyle(boolean useRound) {
        this.useRound = useRound;
    }

    public DrawerCurvedLineStyle(boolean useRound, Color lineColor) {
        super(lineColor);
        this.useRound = useRound;
    }

    @Override
    public void draw(Graphics2D g2, JComponent component, int startX, int startY, int endX, int endY, int[] subMenuLocation, boolean isLeftToRight) {
        int round = UIScale.scale(8);
        Path2D.Double p = new Path2D.Double();
        p.moveTo(startX, startY);
        p.lineTo(startX, endY - (useRound ? round : 0));
        for (int l : subMenuLocation) {
            p.append(createCurve(round, startX, l, isLeftToRight), false);
        }
        g2.setColor(getLineColor(component));
        g2.setStroke(new BasicStroke(UIScale.scale(1f)));
        g2.draw(p);
    }

    private Shape createCurve(int round, int x, int y, boolean ltr) {
        float size = ltr ? round : -round;
        if (this.useRound) {
            Path2D p2 = new Path2D.Float();
            p2.moveTo(x, y - round);
            p2.curveTo(x, y - round, x, y, x + size, y);
            return p2;
        } else {
            return new Line2D.Float(x, y, x + size, y);
        }
    }
}
