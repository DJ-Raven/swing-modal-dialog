package raven.modal.drawer.renderer;

import com.formdev.flatlaf.util.UIScale;
import raven.modal.drawer.menu.AbstractMenuElement;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

/**
 * This class for draw straight dot line style
 *
 * @author Raven
 */
public class DrawerStraightDotLineStyle extends AbstractDrawerLineStyleRenderer {

    private final boolean useRound;
    private final boolean lineSelectedPaint;

    public DrawerStraightDotLineStyle() {
        this(true);
    }

    public DrawerStraightDotLineStyle(boolean useRound) {
        this(useRound, false);
    }

    public DrawerStraightDotLineStyle(boolean useRound, boolean lineSelectedPaint) {
        this.useRound = useRound;
        this.lineSelectedPaint = lineSelectedPaint;
    }

    public DrawerStraightDotLineStyle(boolean useRound, boolean lineSelectedPaint, Color lineColor) {
        super(lineColor);
        this.useRound = useRound;
        this.lineSelectedPaint = lineSelectedPaint;
    }

    @Override
    public void draw(Graphics2D g2, JComponent component, int startX, int startY, int endX, int endY, int[] subMenuLocation, int selectedIndex, boolean isLeftToRight, AbstractMenuElement menuElement) {
        float dotSize = UIScale.scale(6f);
        float stroke = UIScale.scale(1f);
        float cut = UIScale.scale(2.5f);
        Path2D.Double defaultLine = new Path2D.Double();
        Path2D.Double selectedLine = selectedIndex >= 0 ? new Path2D.Double() : null;
        float moveY = startY;
        Path2D.Double dot = new Path2D.Double();
        for (int i = 0; i < subMenuLocation.length; i++) {
            float l = subMenuLocation[i];
            float x = startX + stroke / 2f;
            dot.append(createDot(x, l, dotSize), false);
            Shape line = new Line2D.Double(startX, moveY, startX, l - dotSize / 2f - cut);
            defaultLine.append(line, false);
            if (selectedLine != null && i <= selectedIndex) {
                selectedLine.append(line, false);
            }
            moveY = l + dotSize / 2f + cut;
        }

        // draw default
        Color color = getLineColor(component, false);
        g2.setColor(color);
        g2.setStroke(new BasicStroke(stroke));
        g2.draw(defaultLine);
        g2.fill(dot);

        // draw selected
        if (selectedIndex >= 0) {
            Color selectedColor = getLineColor(component, true);
            g2.setColor(selectedColor);
            if (lineSelectedPaint && selectedLine != null) {
                g2.draw(selectedLine);
            }
            g2.fill(createDot(startX + stroke / 2f, subMenuLocation[selectedIndex], dotSize));
        }
    }

    private Shape createDot(float x, float y, float dotSize) {
        if (useRound) {
            return new Ellipse2D.Float(x - dotSize / 2f, y - dotSize / 2f, dotSize, dotSize);
        } else {
            return new Rectangle2D.Float(x - dotSize / 2f, y - dotSize / 2f, dotSize, dotSize);
        }
    }
}
