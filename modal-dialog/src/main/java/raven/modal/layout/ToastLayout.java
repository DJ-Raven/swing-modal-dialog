package raven.modal.layout;

import com.formdev.flatlaf.util.UIScale;
import raven.modal.Toast;
import raven.modal.option.LayoutOption;
import raven.modal.toast.ToastPanel;
import raven.modal.toast.option.ToastLayoutOption;
import raven.modal.toast.option.ToastOption;

import java.awt.*;
import java.util.List;
import java.util.*;

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
            Map<Component, List<ToastPanel>> map = mapComponent(parent);
            boolean reverseOrder = Toast.isReverseOrder();
            for (Map.Entry<Component, List<ToastPanel>> entry : map.entrySet()) {
                List<ToastPanel> list = entry.getValue();
                if (reverseOrder) {
                    Collections.reverse(list);
                }
                Insets baseMargin = new Insets(0, 0, 0, 0);
                for (int i = 0; i < list.size(); i++) {
                    ToastPanel toastPanel = list.get(i);
                    ToastOption option = toastPanel.getToastData().getOption();
                    LayoutOption layoutOption = option.getLayoutOption().createLayoutOption(parent, toastPanel.getOwner());
                    Rectangle rec = OptionLayoutUtils.getLayoutLocation(parent, null, toastPanel, toastPanel.getAnimate(), layoutOption);
                    int index = i;
                    int y = rec.y;
                    if (index > 0) {
                        float ly = getLayoutY(parent, rec.getSize(), layoutOption);
                        y = getY(list, toastPanel, option.getLayoutOption(), index, rec, baseMargin, ly, toastPanel.getAnimate());
                    } else {
                        baseMargin = UIScale.scale(layoutOption.getMargin());
                    }
                    toastPanel.setBounds(rec.x, y, rec.width, rec.height);
                }
            }
        }
    }

    public Map<Component, List<ToastPanel>> mapComponent(Container parent) {
        Map<Component, List<ToastPanel>> map = new LinkedHashMap<>();
        Component[] components = parent.getComponents();
        for (Component com : components) {
            if (com instanceof ToastPanel) {
                ToastPanel toastPanel = (ToastPanel) com;
                Component key = toastPanel.getToastData().getOption().getLayoutOption().isRelativeToOwner() ? toastPanel.getOwner() : parent;
                map.computeIfAbsent(key, k -> new ArrayList<>()).add(toastPanel);
            }
        }
        return map;
    }

    private float getLayoutY(Container parent, Dimension comSize, LayoutOption layoutOption) {
        int insets = parent.getInsets().top + parent.getInsets().bottom + UIScale.scale(layoutOption.getMargin().top + layoutOption.getMargin().bottom);
        int height = parent.getHeight() - insets;
        Point point = OptionLayoutUtils.location(0, height, comSize, layoutOption.getLocation());
        return point.y;
    }

    private int getY(List<ToastPanel> components, ToastPanel parentPanel, ToastLayoutOption layoutOption, int index, Rectangle rec, Insets baseMargin, float ly, float animate) {
        ToastPanel previousToast = getToastPanel(components, parentPanel, index - 1);
        if (previousToast == null) {
            return rec.y;
        }
        boolean isVerticalDirection = layoutOption.getDirection().isVerticalDirection();
        double y;
        if (layoutOption.getDirection().isToBottomDirection()) {
            int h = previousToast.getHeight();
            if (!isVerticalDirection) {
                h *= previousToast.getAnimate();
            }
            y = rec.y + previousToast.getY() + h - (baseMargin.top + ly);
        } else {
            float h;
            if (!isVerticalDirection) {
                h = rec.height - (previousToast.getHeight() * (1f - previousToast.getAnimate()));
            } else {
                h = rec.height * (animate);
            }
            y = previousToast.getY() - h;
        }
        return (int) y;
    }

    private ToastPanel getToastPanel(List<ToastPanel> components, ToastPanel parentPanel, int index) {
        if (index <= -1) {
            return null;
        }
        ToastPanel component = components.get(index);
        if (component.checkSameLayout(parentPanel.getToastData().getOption().getLayoutOption())) {
            return component;
        } else {
            return getToastPanel(components, parentPanel, index - 1);
        }
    }
}
