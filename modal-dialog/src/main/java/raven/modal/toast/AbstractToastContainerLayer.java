package raven.modal.toast;

import raven.modal.component.AbstractRelativeContainer;
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
public abstract class AbstractToastContainerLayer extends AbstractRelativeContainer implements BaseToastContainer {

    protected final List<ToastPanel> toastPanels;

    public abstract void showContainer(boolean show);

    public AbstractToastContainerLayer() {
        super(new ToastLayout());
        toastPanels = new ArrayList<>();
    }

    public void addToastPanel(ToastPanel toastPanel) {
        toastPanels.add(toastPanel);
        toastPanel.revalidate();
        toastPanel.repaint();
        showContainer(true);
    }

    @Override
    public void add(ToastPanel toastPanel) {
        ToastOption option = toastPanel.getOption();
        boolean visibility = isVisibility(option);
        boolean fixedLayout = isFixedLayout(option, toastPanel.getOwner());
        getLayerAndCreate(toastPanel.getOwner(), visibility, fixedLayout).add(toastPanel, JLayeredPane.PALETTE_LAYER, 0);
        addToastPanel(toastPanel);
    }

    @Override
    public void remove(ToastPanel toastPanel) {
        ToastOption option = toastPanel.getOption();
        boolean visibility = isVisibility(option);
        boolean fixedLayout = isFixedLayout(option, toastPanel.getOwner());
        removeLayer(toastPanel, toastPanel.getOwner(), visibility, fixedLayout);
        toastPanels.remove(toastPanel);
        if (toastPanels.isEmpty()) {
            showContainer(false);
        }
    }

    @Override
    public void closeAll() {
        synchronized (toastPanels) {
            for (int i = toastPanels.size() - 1; i >= 0; i--) {
                toastPanels.get(i).stop();
            }
        }
    }

    @Override
    public void closeAllImmediately() {
        synchronized (toastPanels) {
            for (int i = toastPanels.size() - 1; i >= 0; i--) {
                ToastPanel p = toastPanels.get(i);
                p.close();
            }
        }
    }

    @Override
    public void closeAll(ToastLocation location) {
        synchronized (toastPanels) {
            for (int i = toastPanels.size() - 1; i >= 0; i--) {
                ToastPanel p = toastPanels.get(i);
                if (p.checkSameLayout(location)) {
                    p.stop();
                }
            }
        }
    }

    @Override
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

    public List<ToastPanel> getToastPanels() {
        return toastPanels;
    }

    private boolean isVisibility(ToastOption option) {
        return option.getLayoutOption().isRelativeToOwner() && option.getLayoutOption().getRelativeToOwnerType() != ToastLayoutOption.RelativeToOwnerType.RELATIVE_GLOBAL;
    }

    private boolean isFixedLayout(ToastOption option, Component owner) {
        return !option.getLayoutOption().isRelativeToOwner() || option.getLayoutOption().getRelativeToOwnerType() != ToastLayoutOption.RelativeToOwnerType.RELATIVE_CONTAINED || owner instanceof RootPaneContainer;
    }
}
