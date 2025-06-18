package raven.modal.layout;

import com.formdev.flatlaf.util.UIScale;
import raven.modal.Toast;
import raven.modal.option.LayoutOption;
import raven.modal.toast.ToastPanel;
import raven.modal.toast.option.ToastLayoutOption;
import raven.modal.toast.option.ToastOption;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author Raven
 */
public class ToastLayout implements LayoutManager {

    @Override
    public void addLayoutComponent(String name, Component comp) {
    }

    @Override
    public void removeLayoutComponent(Component comp) {
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
            return new Dimension(0, 0);
        }
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
            return new Dimension(0, 0);
        }
    }

    @Override
    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
            int count = parent.getComponentCount();
            boolean reverseOrder = Toast.isReverseOrder();
            Component[] components = Arrays.copyOf(parent.getComponents(), count);
            if (reverseOrder) {
                Collections.reverse(Arrays.asList(components));
            }
            Insets baseMargin = new Insets(0, 0, 0, 0);
            for (int i = 0; i < count; i++) {
                Component component = components[i];
                if (component instanceof ToastPanel) {
                    ToastPanel toastPanel = (ToastPanel) component;
                    ToastOption option = toastPanel.getToastData().getOption();
                    LayoutOption layoutOption = option.getLayoutOption().createLayoutOption(parent, toastPanel.getOwner());
                    float animate = toastPanel.getEasingAnimate();
                    Rectangle rec = OptionLayoutUtils.getLayoutLocation(parent, null, toastPanel, animate, layoutOption);
                    int y = rec.y;
                    if (i > 0) {
                        float ly = getLayoutY(parent, rec.getSize(), layoutOption);
                        y = getY(components, toastPanel, option.getLayoutOption(), i, rec, baseMargin, ly, animate);
                    } else {
                        baseMargin = UIScale.scale(layoutOption.getMargin());
                    }
                    component.setBounds(rec.x, y, rec.width, rec.height);
                }
            }
        }
    }

    private float getLayoutY(Container parent, Dimension comSize, LayoutOption layoutOption) {
        int insets = parent.getInsets().top + parent.getInsets().bottom + UIScale.scale(layoutOption.getMargin().top + layoutOption.getMargin().bottom);
        int height = parent.getHeight() - insets;
        Point point = OptionLayoutUtils.location(0, height, comSize, layoutOption.getLocation(), layoutOption.isOverflowAlignmentAuto());
        return point.y;
    }

    private int getY(Component[] components, ToastPanel parentPanel, ToastLayoutOption layoutOption, int index, Rectangle rec, Insets baseMargin, float ly, float animate) {
        ToastPanel previousToast = getToastPanel(components, parentPanel, index - 1);
        if (previousToast == null) {
            return rec.y;
        }
        float previousAnimate = previousToast.getEasingAnimate();
        boolean isVerticalDirection = layoutOption.getDirection().isVerticalDirection();
        double y;
        float gap = UIScale.scale((float) layoutOption.getGap()) * previousAnimate;
        if (layoutOption.getDirection().isToBottomDirection()) {
            float h = previousToast.getHeight();
            if (!isVerticalDirection) {
                h *= previousAnimate;
            }
            y = rec.y + previousToast.getY() + h - (baseMargin.top + ly) + gap;
        } else {
            float h;
            if (!isVerticalDirection) {
                h = rec.height - (previousToast.getHeight() * (1f - previousAnimate));
            } else {
                h = rec.height * (animate);
            }
            y = previousToast.getY() - h - gap;
        }
        return (int) y;
    }

    private ToastPanel getToastPanel(Component[] components, ToastPanel parentPanel, int index) {
        if (index <= -1) {
            return null;
        }
        Component component = components[index];
        if (component instanceof ToastPanel) {
            ToastPanel toastPanel = (ToastPanel) component;
            if (toastPanel.checkSameLayout(parentPanel.getToastData().getOption().getLayoutOption())) {
                return toastPanel;
            } else {
                return getToastPanel(components, parentPanel, index - 1);
            }
        }
        return null;
    }
}
