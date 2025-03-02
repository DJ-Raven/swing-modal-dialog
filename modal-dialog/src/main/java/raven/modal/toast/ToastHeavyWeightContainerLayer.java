package raven.modal.toast;

import raven.modal.component.HeavyWeightRelativeLayout;
import raven.modal.toast.option.ToastLocation;
import raven.modal.utils.ModalWindow;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Raven
 */
public class ToastHeavyWeightContainerLayer implements BaseToastContainer {

    private final Map<Window, ToastHeavyWeightContainerLayer> map;
    private List<ToastHeavyWeightLayout> listToastHeavyWeight;
    private final Window parentWindow;
    private PropertyChangeListener propertyListener;

    public ToastHeavyWeightContainerLayer(Map<Window, ToastHeavyWeightContainerLayer> map, Window parentWindow) {
        this.map = map;
        this.parentWindow = parentWindow;
        init();
    }

    private void init() {
        listToastHeavyWeight = new ArrayList<>();
        installWindowListener();
    }

    @Override
    public void add(ToastPanel toastPanel) {
        Component owner = getOwner(toastPanel.getOwner());
        ToastHeavyWeightLayout toastLayout = getToastLayout(owner);
        if (toastLayout == null) {
            toastLayout = new ToastHeavyWeightLayout(owner);
            listToastHeavyWeight.add(toastLayout);
        }
        toastLayout.add(toastPanel);
    }

    @Override
    public void remove(ToastPanel toastPanel) {
        Component owner = getOwner(toastPanel.getOwner());
        ToastHeavyWeightLayout toastLayout = getToastLayout(owner);
        if (toastLayout != null) {
            toastLayout.remove(toastPanel);
            if (toastLayout.isEmpty()) {
                toastLayout.uninstallOwner();
                listToastHeavyWeight.remove(toastLayout);
            }
        }
    }

    @Override
    public void closeAll() {
        for (int i = listToastHeavyWeight.size() - 1; i >= 0; i--) {
            ToastHeavyWeightLayout toastLayout = listToastHeavyWeight.get(i);
            if (toastLayout != null) {
                closeAll(toastLayout);
            }
        }
    }

    @Override
    public void closeAllImmediately() {
        for (int i = listToastHeavyWeight.size() - 1; i >= 0; i--) {
            ToastHeavyWeightLayout toastLayout = listToastHeavyWeight.get(i);
            if (toastLayout != null) {
                closeAllImmediately(toastLayout);
            }
        }
    }

    @Override
    public void closeAll(ToastLocation location) {
        for (int i = listToastHeavyWeight.size() - 1; i >= 0; i--) {
            ToastHeavyWeightLayout toastLayout = listToastHeavyWeight.get(i);
            if (toastLayout != null) {
                closeAll(toastLayout, location);
            }
        }
    }

    @Override
    public boolean checkPromiseId(String id) {
        for (int i = 0; i < listToastHeavyWeight.size(); i++) {
            ToastHeavyWeightLayout toastLayout = listToastHeavyWeight.get(i);
            List<ModalWindow> list = toastLayout.getModalWindows();
            for (int j = 0; j < list.size(); j++) {
                ToastPanel toastPanel = (ToastPanel) list.get(j).getContents();
                if (toastPanel.checkPromiseId(id)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void updateLayout() {
        listToastHeavyWeight.forEach(toast -> toast.updateLayout());
    }

    @Override
    public void updateLayout(Component owner) {
        ToastHeavyWeightLayout toastLayout = getToastLayout(getOwner(owner));
        if (toastLayout != null) {
            toastLayout.updateLayout();
        }
    }

    protected void setEnableHierarchy(boolean enable) {
        listToastHeavyWeight.forEach(modal -> modal.setEnableHierarchy(enable));
    }

    private void closeAll(HeavyWeightRelativeLayout heavyWeight) {
        List<ModalWindow> modalWindows = heavyWeight.getModalWindows();
        for (int i = modalWindows.size() - 1; i >= 0; i--) {
            ((ToastPanel) modalWindows.get(i).getContents()).stop();
        }
    }

    private void closeAllImmediately(HeavyWeightRelativeLayout heavyWeight) {
        List<ModalWindow> modalWindows = heavyWeight.getModalWindows();
        for (int i = modalWindows.size() - 1; i >= 0; i--) {
            ((ToastPanel) modalWindows.get(i).getContents()).close();
        }
    }

    private void closeAll(HeavyWeightRelativeLayout heavyWeight, ToastLocation location) {
        List<ModalWindow> modalWindows = heavyWeight.getModalWindows();
        for (int i = modalWindows.size() - 1; i >= 0; i--) {
            ToastPanel toastPanel = (ToastPanel) modalWindows.get(i).getContents();
            if (toastPanel.checkSameLayout(location)) {
                toastPanel.stop();
            }
        }
    }

    private ToastHeavyWeightLayout getToastLayout(Component owner) {
        for (int i = 0; i < listToastHeavyWeight.size(); i++) {
            ToastHeavyWeightLayout toastHeavyWeight = listToastHeavyWeight.get(i);
            if (toastHeavyWeight.getOwner() == owner) {
                return toastHeavyWeight;
            }
        }
        return null;
    }

    private Component getOwner(Component owner) {
        if (owner != null) {
            return owner;
        }
        return parentWindow;
    }

    private void windowRemoved() {
        closeAllImmediately();
        map.remove(parentWindow);
    }

    private void installWindowListener() {
        propertyListener = evt -> {
            if (evt.getNewValue() == null && evt.getOldValue() instanceof RootPaneContainer) {
                uninstallWindowListener((RootPaneContainer) evt.getOldValue());
            }
        };

        if (parentWindow instanceof RootPaneContainer) {
            ((RootPaneContainer) parentWindow).getRootPane().addPropertyChangeListener("ancestor", propertyListener);
        }
    }

    protected void uninstallWindowListener(RootPaneContainer rootPaneContainer) {
        if (parentWindow instanceof RootPaneContainer) {
            rootPaneContainer.getRootPane().removePropertyChangeListener("ancestor", propertyListener);
        }
        windowRemoved();
    }
}
