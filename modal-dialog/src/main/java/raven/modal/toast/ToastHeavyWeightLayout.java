package raven.modal.toast;

import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.UIScale;
import raven.modal.Toast;
import raven.modal.component.HeavyWeightRelativeLayout;
import raven.modal.layout.OptionLayoutUtils;
import raven.modal.option.LayoutOption;
import raven.modal.toast.option.ToastBorderStyle;
import raven.modal.toast.option.ToastOption;
import raven.modal.toast.option.ToastStyle;
import raven.modal.utils.ModalWindow;
import raven.modal.utils.ModalWindowBorder;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Raven
 */
public class ToastHeavyWeightLayout extends HeavyWeightRelativeLayout {

    public ToastHeavyWeightLayout(Component owner) {
        super(owner);
    }

    @Override
    protected ModalWindowBorder getModalWindowBorder(Component contents) {
        if (contents instanceof ToastPanel) {
            ToastPanel.ToastData toastData = ((ToastPanel) contents).getToastData();
            ToastOption toastOption = toastData.getOption();
            ToastStyle style = toastOption.getStyle();
            int borderWidth = style.getBorderStyle().getBorderType() == ToastBorderStyle.BorderType.OUTLINE ?
                    style.getBorderStyle().getBorderWidth() : 0;
            return new ModalWindowBorder(style.getBorderStyle().getShadowSize(),
                    style.getBorderStyle().getShadowOpacity(),
                    style.getBorderStyle().getShadowColor(),
                    borderWidth,
                    toastData.getThemes().getColor(),
                    style.getBorderStyle().getRound());
        }
        return null;
    }

    @Override
    public void updateLayout() {
        if (modalWindows.isEmpty()) return;

        boolean reverseOrder = Toast.isReverseOrder();
        ModalWindow[] windowsToastsArray = modalWindows.toArray(new ModalWindow[modalWindows.size()]);
        if (reverseOrder) {
            Collections.reverse(Arrays.asList(windowsToastsArray));
        }
        List<List<ModalWindow>> lists = groupByLocation(windowsToastsArray);
        for (int i = 0; i < lists.size(); i++) {
            updateLayout(lists.get(i));
        }
    }

    private Dimension getShadowSize(ToastBorderStyle borderStyle) {
        int borderWidth = borderStyle.getBorderType() == ToastBorderStyle.BorderType.OUTLINE ? borderStyle.getBorderWidth() : 0;
        if (FlatUIUtils.isInsetsEmpty(borderStyle.getShadowSize()) && borderWidth == 0) {
            return null;
        }
        Insets shadowSize = borderStyle.getShadowSize();
        int width = shadowSize.left + shadowSize.right + borderWidth * 2;
        int height = shadowSize.top + shadowSize.bottom + borderWidth * 2;
        return UIScale.scale(new Dimension(width, height));
    }

    private void updateLayout(List<ModalWindow> list) {
        if (list.isEmpty()) return;

        int lx = 0;
        int ly = 0;
        for (int i = 0; i < list.size(); i++) {
            ModalWindow modal = list.get(i);
            ToastPanel toastPanel = (ToastPanel) modal.getContents();
            if (toastPanel.isClose()) continue;
            ToastOption option = toastPanel.getOption();
            LayoutOption layoutOption = option.getLayoutOption().createLayoutOption(owner, toastPanel.getOwner());
            boolean isToBottomDirection = option.getLayoutOption().getDirection().isToBottomDirection();
            Dimension extraSize = getShadowSize(option.getStyle().getBorderStyle());

            Rectangle rec = OptionLayoutUtils.getLayoutLocation((Container) owner, null, toastPanel, toastPanel.getAnimate(), layoutOption, extraSize);
            if (i == 0) {
                ly += rec.y;
            }
            int x = lx + rec.x;
            int y = 0;

            modal.setLocation(x, ly + y);
            y += rec.height + UIScale.scale(option.getLayoutOption().getGap());
            if (isToBottomDirection) {
                ly += y;
            } else {
                ly -= y;
            }
        }
    }

    private List<List<ModalWindow>> groupByLocation(ModalWindow[] modalWindows) {
        ArrayList<List<ModalWindow>> lists = new ArrayList<>();
        label:
        for (int i = 0; i < modalWindows.length; i++) {
            ModalWindow window = modalWindows[i];
            ToastPanel toast = (ToastPanel) window.getContents();
            for (int j = 0; j < lists.size(); j++) {
                List<ModalWindow> list = lists.get(j);
                if (list != null && !list.isEmpty()) {
                    ToastPanel toastPanel = (ToastPanel) list.get(0).getContents();
                    if (toast.checkSameLayout(toastPanel.getToastData().getOption().getLayoutOption())) {
                        list.add(window);
                        continue label;
                    }
                }
            }
            List<ModalWindow> list = new ArrayList<>();
            list.add(window);
            lists.add(list);
        }
        return lists;
    }
}
