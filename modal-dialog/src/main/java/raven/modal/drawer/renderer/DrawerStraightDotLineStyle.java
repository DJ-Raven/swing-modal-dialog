package raven.modal.drawer.renderer;

import com.formdev.flatlaf.util.UIScale;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

/**
 * This class for draw straight dot line style
 *
 * @author Raven
 */
public class DrawerStraightDotLineStyle extends AbstractDrawerLineStyleRenderer {

    private final boolean useRound;

    public DrawerStraightDotLineStyle() {
        this(true);
    }

    public DrawerStraightDotLineStyle(boolean useRound) {
        this.useRound = useRound;
    }

    public DrawerStraightDotLineStyle(boolean useRound, Color lineColor) {
        super(lineColor);
        this.useRound = useRound;
    }

    @Override
    public void draw(Graphics2D g2, JComponent component, int startX, int startY, int endX, int endY, int[] subMenuLocation, boolean isLeftToRight) {
        float dotSize = UIScale.scale(6f);
        float stroke = UIScale.scale(1f);
        Path2D.Double p = new Path2D.Double();
        p.moveTo(startX, startY);
        p.lineTo(startX, endY);
        Path2D.Double dot = new Path2D.Double();
        for (int l : subMenuLocation) {
            dot.append(createDot(startX + stroke / 2f, l, dotSize), false);
        }
        g2.setColor(getLineColor(component));
        g2.setStroke(new BasicStroke(stroke));
        g2.draw(p);
        g2.fill(dot);
    }

    private Shape createDot(float x, float y, float dotSize) {
        if (useRound) {
            return new Ellipse2D.Float(x - dotSize / 2f, y - dotSize / 2f, dotSize, dotSize);
        } else {
            return new Rectangle2D.Float(x - dotSize / 2f, y - dotSize / 2f, dotSize, dotSize);
        }
    }
}
