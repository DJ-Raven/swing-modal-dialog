package raven.modal.toast;

import raven.modal.layout.ToastLayout;
import raven.modal.option.LayoutOption;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Raven
 */
public class ToastContainerLayer extends JLayeredPane {

    private List<ToastPanel> toastPanels;
    private Object propertyData;

    public ToastContainerLayer() {
        init();
    }

    private void init() {
        toastPanels = new ArrayList<>();
        setLayout(new ToastLayout());
    }

    public void addToastPanel(ToastPanel toastPanel) {
        toastPanels.add(toastPanel);
        setVisible(true);
    }

    public void removeToastPanel(ToastPanel toastPanel) {
        if (toastPanels != null) {
            toastPanels.remove(toastPanel);
            if (toastPanels.isEmpty()) {
                setVisible(false);
            }
        }
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

    public void closeAll(LayoutOption option) {
        synchronized (toastPanels) {
            for (int i = toastPanels.size() - 1; i >= 0; i--) {
                ToastPanel p = toastPanels.get(i);
                if (p.checkSameLayout(option)) {
                    if (!p.isCurrenPromise()) {
                        p.stop();
                    }
                }
            }
        }
    }

    private void closeAllAsRemove() {
        synchronized (toastPanels) {
            for (int i = toastPanels.size() - 1; i >= 0; i--) {
                ToastPanel p = toastPanels.get(i);
                p.close();
            }
        }
    }

    public Object getPropertyData() {
        return propertyData;
    }

    public void setPropertyData(Object propertyData) {
        this.propertyData = propertyData;
    }

    public void remove() {
        closeAllAsRemove();
        toastPanels = null;
        propertyData = null;
    }
}
