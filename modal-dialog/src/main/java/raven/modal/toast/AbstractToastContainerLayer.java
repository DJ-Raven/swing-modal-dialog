package raven.modal.toast;

import raven.modal.component.RelativeLayerPane;
import raven.modal.layout.RelativeLayout;
import raven.modal.layout.ToastLayout;
import raven.modal.toast.option.ToastLayoutOption;
import raven.modal.toast.option.ToastLocation;
import raven.modal.toast.option.ToastOption;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Raven
 */
public abstract class AbstractToastContainerLayer {

    protected final List<ToastPanel> toastPanels;
    protected final List<RelativeLayerPane> listToastLayer;
    protected final JLayeredPane layeredPane;

    public abstract void showContainer(boolean show);

    public AbstractToastContainerLayer() {
        toastPanels = new ArrayList<>();
        listToastLayer = new ArrayList<>();
        layeredPane = new JLayeredPane();
        layeredPane.setLayout(new RelativeLayout());
    }

    private RelativeLayerPane getLayer(Component owner, boolean controlVisibility, boolean fixedLayout) {
        for (int i = 0; i < listToastLayer.size(); i++) {
            RelativeLayerPane l = listToastLayer.get(i);
            if (l.getOwner() == owner
                    && l.isControlVisibility() == controlVisibility
                    && l.isFixedLayout() == fixedLayout
            ) {
                return l;
            }
        }
        return null;
    }

    private RelativeLayerPane getToastLayer(ToastPanel toastPanel) {
        boolean controlVisibility = isVisibility(toastPanel.getOption());
        boolean fixedLayout = isFixedLayout(toastPanel.getOption());
        RelativeLayerPane layer = getLayer(toastPanel.getOwner(), controlVisibility, fixedLayout);
        if (layer == null) {
            layer = new RelativeLayerPane(toastPanel.getOwner(), controlVisibility, fixedLayout);
            layer.setLayout(new ToastLayout());
            listToastLayer.add(layer);
            layeredPane.add(layer, JLayeredPane.MODAL_LAYER, 0);
        }
        return layer;
    }

    public void add(ToastPanel toastPanel) {
        getToastLayer(toastPanel).add(toastPanel, 0);
    }

    public void remove(ToastPanel toastPanel) {
        toastPanels.remove(toastPanel);
        ToastOption option = toastPanel.getOption();
        RelativeLayerPane layer = getLayer(toastPanel.getOwner(), isVisibility(option), isFixedLayout(option));
        if (layer != null) {
            layer.remove(toastPanel);
        }
        if (layer.getComponentCount() == 0) {
            layeredPane.remove(layer);
            listToastLayer.remove(layer);
        }
        layer.repaint();
        layer.revalidate();

        if (toastPanels.isEmpty()) {
            showContainer(false);
        }
    }

    public void addToastPanel(ToastPanel toastPanel) {
        toastPanels.add(toastPanel);
        toastPanel.revalidate();
        toastPanel.repaint();
        showContainer(true);
    }

    public void closeAll() {
        synchronized (toastPanels) {
            for (int i = toastPanels.size() - 1; i >= 0; i--) {
                ToastPanel p = toastPanels.get(i);
                if (!p.isCurrenPromise()) {
                    toastPanels.get(i).stop();
                }
            }
        }
    }

    public boolean checkPromiseId(String id) {
        synchronized (toastPanels) {
            for (int i = toastPanels.size() - 1; i >= 0; i--) {
                ToastPanel p = toastPanels.get(i);
                if (p.checkPromiseId(id)) {
                    return true;
                }
            }
            return false;
        }
    }

    public void closeAll(ToastLocation location) {
        synchronized (toastPanels) {
            for (int i = toastPanels.size() - 1; i >= 0; i--) {
                ToastPanel p = toastPanels.get(i);
                if (p.checkSameLayout(location)) {
                    if (!p.isCurrenPromise()) {
                        p.stop();
                    }
                }
            }
        }
    }

    public void closeAllImmediately() {
        synchronized (toastPanels) {
            for (int i = toastPanels.size() - 1; i >= 0; i--) {
                ToastPanel p = toastPanels.get(i);
                p.close();
            }
        }
    }

    public void revalidate(Component owner) {
        listToastLayer.forEach(layer -> {
            if (layer.getOwner() == owner) {
                layer.revalidate();
            }
        });
    }

    public void revalidateAll() {
        layeredPane.revalidate();
        listToastLayer.forEach(layer -> layer.revalidate());
    }

    public List<ToastPanel> getToastPanels() {
        return toastPanels;
    }

    public JLayeredPane getLayeredPane() {
        return layeredPane;
    }

    private boolean isVisibility(ToastOption option) {
        return option.getLayoutOption().isRelativeToOwner() && option.getLayoutOption().getRelativeToOwnerType() != ToastLayoutOption.RelativeToOwnerType.RELATIVE_GLOBAL;
    }

    private boolean isFixedLayout(ToastOption option) {
        return !option.getLayoutOption().isRelativeToOwner() || option.getLayoutOption().getRelativeToOwnerType() != ToastLayoutOption.RelativeToOwnerType.RELATIVE_CONTAINED;
    }
}
