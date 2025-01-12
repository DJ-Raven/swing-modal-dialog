package raven.modal.toast;

import raven.modal.layout.ToastLayout;
import raven.modal.toast.option.ToastLocation;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Raven
 */
public abstract class AbstractToastContainerLayer {

    protected final List<ToastPanel> toastPanels;
    protected final JLayeredPane layeredPane;

    public abstract void showContainer(boolean show);

    public AbstractToastContainerLayer() {
        toastPanels = new ArrayList<>();
        layeredPane = new JLayeredPane();
        layeredPane.setLayout(new ToastLayout());
    }

    public void add(Component component) {
        layeredPane.add(component, 0);
    }

    public void remove(Component component) {
        layeredPane.remove(component);
        layeredPane.repaint();
        layeredPane.revalidate();
    }

    public void addToastPanel(ToastPanel toastPanel) {
        toastPanels.add(toastPanel);
        toastPanel.revalidate();
        toastPanel.repaint();
        showContainer(true);
    }

    public void removeToastPanel(ToastPanel toastPanel) {
        if (toastPanels != null) {
            toastPanels.remove(toastPanel);
            if (toastPanels.isEmpty()) {
                showContainer(false);
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

    public List<ToastPanel> getToastPanels() {
        return toastPanels;
    }

    public JLayeredPane getLayeredPane() {
        return layeredPane;
    }
}
