package raven.modal.component;

import raven.modal.option.Option;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Raven
 */
public class ModalHeavyWeight {

    private static ModalHeavyWeight instance;
    private final Map<Window, ModalHeavyWeightContainerLayer> map;

    private ModalHeavyWeight() {
        map = new HashMap<>();
    }

    public static ModalHeavyWeight getInstance() {
        if (instance == null) {
            instance = new ModalHeavyWeight();
        }
        return instance;
    }

    public void showModal(Component owner, Modal modal, Option option, String id) {
        if (isIdExist(id)) {
            throw new IllegalArgumentException("id '" + id + "' already exist");
        }
        SwingUtilities.invokeLater(() -> {
            getModalHeavyWeightContainer(owner).addModal(owner, modal, option, id);
        });
    }

    public void closeAllModal() {
        map.values().forEach(con -> {
            con.closeAllModal();
        });
    }

    public void closeAllModalImmediately() {
        map.values().forEach(con -> {
            con.closeAllModalImmediately();
        });
    }

    public boolean isIdExist(String id) {
        for (Map.Entry<Window, ModalHeavyWeightContainerLayer> entry : getInstance().map.entrySet()) {
            if (entry.getValue().checkId(id)) {
                return true;
            }
        }
        return false;
    }

    public ModalHeavyWeightContainerLayer getModalHeavyWeightContainerById(String id) {
        for (Map.Entry<Window, ModalHeavyWeightContainerLayer> entry : map.entrySet()) {
            if (entry.getValue().checkId(id)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public ModalHeavyWeightContainerLayer getModalHeavyWeightContainer(Component owner) {
        Window window = owner instanceof Window ? ((Window) owner) : SwingUtilities.getWindowAncestor(owner);
        if (map.containsKey(window)) {
            return map.get(window);
        } else {
            ModalHeavyWeightContainerLayer modalHeavyWeightContainerLayer = new ModalHeavyWeightContainerLayer(map, window);
            map.put(window, modalHeavyWeightContainerLayer);
            return modalHeavyWeightContainerLayer;
        }
    }

    public void setEnableHierarchy(boolean enable) {
        map.values().forEach(container -> container.setEnableHierarchy(enable));
    }
}
