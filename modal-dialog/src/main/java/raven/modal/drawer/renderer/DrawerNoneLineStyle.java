package raven.modal.drawer.renderer;

import raven.modal.drawer.menu.AbstractMenuElement;

import javax.swing.*;
import java.awt.*;

/**
 * This class for draw none line style
 *
 * @author Raven
 */
public class DrawerNoneLineStyle extends AbstractDrawerLineStyleRenderer {

    public DrawerNoneLineStyle() {
    }

    public DrawerNoneLineStyle(Color lineColor) {
        super(lineColor);
    }

    @Override
    public void draw(Graphics2D g2, JComponent component, int startX, int startY, int endX, int endY, int[] subMenuLocation, int selectedIndex, boolean isLeftToRight, AbstractMenuElement menuElement) {
    }
}
