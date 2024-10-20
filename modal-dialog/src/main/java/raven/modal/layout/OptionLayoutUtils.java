package raven.modal.layout;

import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.UIScale;
import raven.modal.option.LayoutOption;
import raven.modal.option.Location;

import java.awt.*;

/**
 * @author Raven
 */
public class OptionLayoutUtils {

    public static Rectangle getLayoutLocation(Container parent, Component component, float animate, LayoutOption layoutOption) {
        Insets insets = FlatUIUtils.addInsets(parent.getInsets(), UIScale.scale(layoutOption.getMargin()));
        int x = insets.left;
        int y = insets.top;
        int width = parent.getWidth() - (insets.left + insets.right);
        int height = parent.getHeight() - (insets.top + insets.bottom);
        Dimension comSize = getComponentSize(component, width, height, animate, layoutOption);
        boolean rightToLeft = !parent.getComponentOrientation().isLeftToRight();
        Location lh = layoutOption.getHorizontalLocation();
        if (rightToLeft) {
            if (lh == Location.LEADING) {
                lh = Location.RIGHT;
            } else if (lh == Location.TRAILING) {
                lh = Location.LEFT;
            } else {
                rightToLeft = false;
            }
        }
        Point point = location(width, height, comSize, lh, layoutOption.getVerticalLocation());
        Point animatePoint = getAnimatePoint(comSize, animate, rightToLeft, layoutOption);
        int cx = x + point.x + animatePoint.x;
        int cy = y + point.y + animatePoint.y;
        return new Rectangle(cx, cy, comSize.width, comSize.height);
    }

    protected static Point location(int width, int height, Dimension componentSize, Location lx, Location ly) {
        float x = (width * lx.getValue()) - (componentSize.width / 2);
        float y = (height * ly.getValue()) - (componentSize.height / 2);
        if (lx != Location.CENTER) {
            if (x < 0) {
                x = 0;
            } else if (x > width - componentSize.width) {
                x = width - componentSize.width;
            }
        }
        if (ly != Location.CENTER) {
            if (y < 0) {
                y = 0;
            } else if (y > height - componentSize.height) {
                y = height - componentSize.height;
            }
        }
        return new Point((int) x, (int) y);
    }

    protected static Dimension getComponentSize(Component component, int width, int height, float animate, LayoutOption layoutOption) {
        Dimension componentSize = component.getPreferredSize();
        Dimension minimumSize = component.getMinimumSize();
        Dimension targetSize = layoutOption.getSize().getSize(componentSize, new Dimension(width, height));
        int cw = Math.max(Math.min(targetSize.width, width), minimumSize.width);
        int ch = Math.max(Math.min(targetSize.height, height), minimumSize.height);
        return new Dimension(cw, ch);
    }

    protected static Point getAnimatePoint(Dimension componentSize, float animate, boolean rightToLeft, LayoutOption layoutOption) {
        Dimension dimension = layoutOption.getAnimateDistance().getSize(componentSize);
        int x = (int) (dimension.getWidth() * (1f - animate));
        int y = (int) (dimension.getHeight() * (1f - animate));
        if (rightToLeft) {
            x *= -1;
        }
        return new Point(x, y);
    }
}
