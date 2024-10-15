package raven.modal.drawer.renderer;

import com.formdev.flatlaf.util.ColorFunctions;
import com.formdev.flatlaf.util.UIScale;
import raven.modal.drawer.menu.AbstractMenuElement;
import raven.modal.drawer.menu.MenuOption;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;

/**
 * @author Raven
 */
public abstract class AbstractDrawerLineStyleRenderer {

    private Color lineColor;

    public AbstractDrawerLineStyleRenderer() {
    }

    public AbstractDrawerLineStyleRenderer(Color lineColor) {
        this.lineColor = lineColor;
    }

    public abstract void draw(Graphics2D g2, JComponent component, int startX, int startY, int endX, int endY, int[] subMenuLocation, int selectedIndex, boolean isLeftToRight, AbstractMenuElement menuElement);

    public void drawArrow(Graphics2D g2, JComponent component, int width, int height, float animate, boolean isSelected, boolean isLeftToRight, AbstractMenuElement menuElement) {
        if (menuElement.getMenuOpenMode() == MenuOption.MenuOpenMode.FULL) {
            int arrowWidth = UIScale.scale(10);
            int arrowHeight = UIScale.scale(4);
            int gap = UIScale.scale(13);
            int x = isLeftToRight ? (width - arrowWidth - gap) : gap;
            int y = (height - arrowHeight) / 2;
            Path2D p = new Path2D.Double();
            p.moveTo(0, animate * arrowHeight);
            p.lineTo(arrowWidth / 2, (1f - animate) * arrowHeight);
            p.lineTo(arrowWidth, animate * arrowHeight);
            g2.translate(x, y);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            g2.setColor(getArrwoColor(component, isSelected));
            g2.draw(p);
        }
    }

    protected Color getLineColor(JComponent component, boolean isSelected) {
        if (lineColor != null) {
            return lineColor;
        }
        return getDefaultColor(component, isSelected);
    }

    protected Color getArrwoColor(JComponent component, boolean isSelected) {
        if (lineColor != null) {
            return lineColor;
        }
        return getDefaultColor(component, isSelected);
    }

    protected Color getDefaultColor(JComponent component, boolean isSelected) {
        if (isSelected) {
            return UIManager.getColor("Component.accentColor");
        }
        Component com = component.getComponentCount() > 0 ? component.getComponent(0) : component;
        Color color = ColorFunctions.mix(component.getBackground(), com.getForeground(), 0.7f);
        return color;
    }

    public Color getLineColor() {
        return lineColor;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }
}
