package raven.modal.toast;

import raven.modal.toast.option.ToastLocation;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Raven
 */
public class ToastHeavyWeight {

    private static ToastHeavyWeight instance;
    private final Map<Window, ToastHeavyWeightContainerLayer> map;

    private ToastHeavyWeight() {
        map = new HashMap<>();
    }

    public static ToastHeavyWeight getInstance() {
        if (instance == null) {
            instance = new ToastHeavyWeight();
        }
        return instance;
    }

    public void updateLayout() {
        for (ToastHeavyWeightContainerLayer com : map.values()) {
            com.updateLayout();
        }
    }

    public boolean checkPromiseId(String id) {
        for (ToastHeavyWeightContainerLayer com : map.values()) {
            if (com.checkPromiseId(id)) {
                return true;
            }
        }
        return false;
    }

    public void closeAll() {
        map.values().forEach(con -> {
            con.closeAll();
        });
    }

    public void closeAll(ToastLocation location) {
        map.values().forEach(con -> {
            con.closeAll(location);
        });
    }

    public void closeAllImmediately() {
        map.values().forEach(con -> {
            con.closeAllImmediately();
        });
    }

    public ToastHeavyWeightContainerLayer getToastHeavyWeightContainer(Component owner) {
        Window window = owner instanceof Window ? ((Window) owner) : SwingUtilities.getWindowAncestor(owner);
        if (map.containsKey(window)) {
            return map.get(window);
        } else {
            ToastHeavyWeightContainerLayer toastHeavyWeightContainerLayer = new ToastHeavyWeightContainerLayer(map, window);
            map.put(window, toastHeavyWeightContainerLayer);
            return toastHeavyWeightContainerLayer;
        }
    }

    public void setEnableHierarchy(boolean enable) {
        map.values().forEach(container -> container.setEnableHierarchy(enable));
    }
}
