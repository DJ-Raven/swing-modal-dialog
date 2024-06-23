package raven.modal.layout;

import com.formdev.flatlaf.util.UIScale;
import raven.modal.Toast;
import raven.modal.option.LayoutOption;
import raven.modal.option.Location;
import raven.modal.toast.ToastPanel;

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
                    LayoutOption option = toastPanel.getToastData().getOption().getLayoutOption();
                    boolean isVerticalSlide = option.getAnimateDistance().getY().floatValue() != 0;
                    Rectangle rec = OptionLayoutUtils.getLayoutLocation(parent, toastPanel, toastPanel.getAnimate(), option);
                    int index = i;
                    int y = rec.y;
                    if (index > 0) {
                        y = getY(components, toastPanel, index, option.getVerticalLocation(), rec, baseMargin, isVerticalSlide, toastPanel.getAnimate());
                    } else {
                        baseMargin = UIScale.scale(option.getMargin());
                    }
                    component.setBounds(rec.x, y, rec.width, rec.height);
                }
            }
        }
    }

    private int getY(Component[] components, ToastPanel parentPanel, int index, Location vertical, Rectangle rec, Insets baseMargin, boolean isVerticalSlide, float animate) {
        ToastPanel previousToast = getToastPanel(components, parentPanel, index - 1);
        if (previousToast == null) {
            return rec.y;
        }
        double y = 0;
        if (vertical == Location.TOP) {
            int h = previousToast.getHeight();
            if (!isVerticalSlide) {
                h *= previousToast.getAnimate();
            }
            y = rec.y + previousToast.getY() + h - baseMargin.top;
        } else if (vertical == Location.BOTTOM) {
            float h;
            if (!isVerticalSlide) {
                h = rec.height - (previousToast.getHeight() * (1f - previousToast.getAnimate()));
            } else {
                h = rec.height * (animate);
            }
            y = previousToast.getY() - h;
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
