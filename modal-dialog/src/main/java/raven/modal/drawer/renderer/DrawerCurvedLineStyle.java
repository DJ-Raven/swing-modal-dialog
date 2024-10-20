package raven.modal.drawer.renderer;

import com.formdev.flatlaf.util.UIScale;
import raven.modal.drawer.menu.AbstractMenuElement;

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
    private final boolean lineSelectedPaint;

    public DrawerCurvedLineStyle() {
        this(true);
    }

    public DrawerCurvedLineStyle(boolean useRound) {
        this(useRound, false);
    }

    public DrawerCurvedLineStyle(boolean useRound, boolean lineSelectedPaint) {
        this.useRound = useRound;
        this.lineSelectedPaint = lineSelectedPaint;
    }

    public DrawerCurvedLineStyle(boolean useRound, boolean lineSelectedPaint, Color lineColor) {
        super(lineColor);
        this.useRound = useRound;
        this.lineSelectedPaint = lineSelectedPaint;
    }

    @Override
    public void draw(Graphics2D g2, JComponent component, int startX, int startY, int endX, int endY, int[] subMenuLocation, int selectedIndex, boolean isLeftToRight, AbstractMenuElement menuElement) {
        int round = UIScale.scale(8);
        Path2D.Double defaultLine = new Path2D.Double();
        Path2D.Double selectedLine = selectedIndex >= 0 ? new Path2D.Double() : null;
        defaultLine.moveTo(startX, startY);
        defaultLine.lineTo(startX, endY - (useRound ? round : 0));
        if (selectedLine != null) {
            selectedLine.moveTo(startX, startY);
            selectedLine.lineTo(startX, subMenuLocation[selectedIndex] - (useRound ? round : 0));
        }
        for (int i = 0; i < subMenuLocation.length; i++) {
            int l = subMenuLocation[i];
            Shape curve = createCurve(round, startX, l, isLeftToRight);
            defaultLine.append(curve, false);
            if (selectedLine != null && i <= selectedIndex) {
                selectedLine.append(curve, false);
            }
        }

        // draw default
        g2.setColor(getLineColor(component, false));
        g2.setStroke(new BasicStroke(UIScale.scale(1f)));
        g2.draw(defaultLine);

        // draw selected
        if (lineSelectedPaint && selectedLine != null) {
            g2.setColor(getLineColor(component, true));
            g2.draw(selectedLine);
        }
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
