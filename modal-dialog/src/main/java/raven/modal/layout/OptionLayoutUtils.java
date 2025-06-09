package raven.modal.layout;

import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.UIScale;
import raven.modal.option.LayoutOption;
import raven.modal.option.Location;
import raven.modal.utils.DynamicSize;

import javax.swing.*;
import java.awt.*;

/**
 * @author Raven
 */
public class OptionLayoutUtils {

    public static Rectangle getLayoutLocation(Container parent, Component owner, Component component, float animate, LayoutOption layoutOption) {
        return getLayoutLocation(parent, owner, component, animate, layoutOption, null, false);
    }

    public static Rectangle getLayoutLocation(Container parent, Component owner, Component component, float animate, LayoutOption layoutOption, Dimension extraSize, boolean heavyWeight) {
        Insets insets = layoutOption.getMargin();
        Dimension defaultComSize = getComponentSize(parent, insets);
        Dimension comSize = getComponentSize(component, defaultComSize.width, defaultComSize.height, animate, layoutOption, extraSize, heavyWeight);
        ReferenceBoolean rightToLeft = ReferenceBoolean.of(false);
        Point point = convertToLocation(parent, owner, layoutOption, comSize, rightToLeft);
        Point animatePoint = getAnimatePoint(comSize, animate, rightToLeft.rightToLeft, layoutOption);
        int cx = point.x + animatePoint.x;
        int cy = point.y + animatePoint.y;
        return new Rectangle(cx, cy, comSize.width, comSize.height);
    }

    public static Insets getOwnerInsert(Component parent, Component owner, Insets insets) {
        if (owner == null || owner instanceof RootPaneContainer) {
            return insets;
        }
        Rectangle ownerRec = SwingUtilities.convertRectangle(owner.getParent(), owner.getBounds(), parent);
        int x = UIScale.unscale(ownerRec.x);
        int y = UIScale.unscale(ownerRec.y);
        int width = UIScale.unscale(parent.getWidth() - (ownerRec.x + ownerRec.width));
        int height = UIScale.unscale(parent.getHeight() - (ownerRec.y + ownerRec.height));

        int top = insets.top + y;
        int left = insets.left + x;
        int bottom = insets.bottom + height;
        int right = insets.right + width;
        return new Insets(top, left, bottom, right);
    }

    public static Point convertToLocation(Container parent, Component owner, LayoutOption layoutOption, Dimension comSize, ReferenceBoolean reference) {
        Insets parentInsert = parent.getInsets();
        Insets insets = layoutOption.getMargin();
        if (layoutOption.isRelativeToOwner() && layoutOption.getRelativeToOwnerType() != LayoutOption.RelativeToOwnerType.RELATIVE_CONTAINED) {
            insets = getOwnerInsert(parent, owner, insets);
        }
        insets = FlatUIUtils.addInsets(parentInsert, UIScale.scale(insets));
        int width = parent.getWidth() - (insets.left + insets.right);
        int height = parent.getHeight() - (insets.top + insets.bottom);
        ReferenceBoolean rightToLeft = ReferenceBoolean.of(!parent.getComponentOrientation().isLeftToRight());
        DynamicSize size = convertLocation(layoutOption, rightToLeft);
        Point point = location(width, height, comSize, size, layoutOption.isOverflowAlignmentAuto());
        if (reference != null) {
            reference.rightToLeft = rightToLeft.rightToLeft;
            point.x += insets.left;
            point.y += insets.top;
        }
        return point;
    }

    private static DynamicSize convertLocation(LayoutOption layoutOption, ReferenceBoolean reference) {
        Location lh = layoutOption.getHorizontalLocation();
        Number lx = layoutOption.getLocation().getX();
        Number ly = layoutOption.getLocation().getY();
        if (lh != null) {
            if (reference.rightToLeft) {
                if (lh == Location.LEADING) {
                    lh = Location.RIGHT;
                } else if (lh == Location.TRAILING) {
                    lh = Location.LEFT;
                } else {
                    reference.rightToLeft = false;
                }
            }
            lx = lh.getValue();
        }
        return new DynamicSize(lx, ly);
    }

    protected static Point location(int width, int height, Dimension componentSize, DynamicSize size, boolean overflowAlignmentAuto) {
        Dimension location = size.getSize(new Dimension(width, height));
        double x = location.width;
        double y = location.height;
        if (size.getX() instanceof Float) {
            x -= componentSize.width / 2f;
        }
        if (size.getY() instanceof Float) {
            y -= componentSize.height / 2f;
        }
        if (!size.isHorizontalCenter()) {
            x = adjustValue(size.getX(), x, componentSize.width, width, overflowAlignmentAuto);
        }
        if (!size.isVerticalCenter()) {
            y = adjustValue(size.getY(), y, componentSize.height, height, overflowAlignmentAuto);
        }
        return new Point((int) x, (int) y);
    }

    protected static double adjustValue(Number location, double value, int componentSize, int containerSize, boolean overflowAlignmentAuto) {
        if (componentSize > containerSize) {
            if (overflowAlignmentAuto) {
                value = (containerSize - componentSize) / 2f;
            } else {
                boolean alightRight = location instanceof Float && location.floatValue() > 0.5f;
                if (alightRight) {
                    value = containerSize - componentSize;
                } else {
                    value = 0;
                }
            }
        } else {
            if (value < 0) {
                value = 0;
            } else if (value > containerSize - componentSize) {
                value = containerSize - componentSize;
            }
        }
        return value;
    }

    protected static Dimension getComponentSize(Component component, int width, int height, float animate, LayoutOption layoutOption, Dimension extraSize, boolean heavyWeight) {
        Dimension componentSize = component.getPreferredSize();
        Dimension minimumSize = component.getMinimumSize();
        if (extraSize != null) {
            componentSize.width += extraSize.width;
            componentSize.height += extraSize.height;
            minimumSize.width += extraSize.width;
            minimumSize.height += extraSize.height;
        }
        Dimension targetSize = layoutOption.getSize().getSize(componentSize, new Dimension(width, height));
        int cw = targetSize.width;
        int ch = targetSize.height;

        if (!heavyWeight ||
                (layoutOption.isRelativeToOwner() && layoutOption.getRelativeToOwnerType() == LayoutOption.RelativeToOwnerType.RELATIVE_CONTAINED)) {
            cw = Math.min(cw, width);
            ch = Math.min(ch, height);
        }
        cw = Math.max(cw, minimumSize.width);
        ch = Math.max(ch, minimumSize.height);
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

    protected static Dimension getComponentSize(Container parent, Insets layoutInsets) {
        Insets insets = FlatUIUtils.addInsets(parent.getInsets(), UIScale.scale(layoutInsets));
        int width = parent.getWidth() - (insets.left + insets.right);
        int height = parent.getHeight() - (insets.top + insets.bottom);
        return new Dimension(width, height);
    }

    private static class ReferenceBoolean {

        public static ReferenceBoolean of(boolean rightToLeft) {
            ReferenceBoolean obj = new ReferenceBoolean();
            obj.rightToLeft = rightToLeft;
            return obj;
        }

        private boolean rightToLeft;
    }
}
