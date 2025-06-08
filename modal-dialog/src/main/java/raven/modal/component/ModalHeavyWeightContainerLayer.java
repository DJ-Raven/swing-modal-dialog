package raven.modal.component;

import raven.modal.option.Option;
import raven.modal.utils.ModalWindow;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Raven
 */
public class ModalHeavyWeightContainerLayer implements BaseModalContainer {

    private final Map<Window, ModalHeavyWeightContainerLayer> map;
    private List<ModalHeavyWeightLayout> listModalHeavyWeight;
    private final Window parentWindow;
    private PropertyChangeListener propertyListener;

    public ModalHeavyWeightContainerLayer(Map<Window, ModalHeavyWeightContainerLayer> map, Window parentWindow) {
        this.map = map;
        this.parentWindow = parentWindow;
        init();
    }

    private void init() {
        listModalHeavyWeight = new ArrayList<>();
        installWindowListener();
    }

    @Override
    public void addModal(Component owner, Modal modal, Option option, String id) {
        owner = getOwner(owner);
        ModalHeavyWeightLayout modalLayout = getModalLayout(owner);
        if (modalLayout == null) {
            modalLayout = new ModalHeavyWeightLayout(owner);
            listModalHeavyWeight.add(modalLayout);
        }
        HeavyWeightModalController modalController = new HeavyWeightModalController(this, owner, option);
        modalController.initModal(modal);
        modal.setId(id);
        modalController.showModal();
        ModalWindow modalWindow = modalLayout.add(modalController);
        modalController.setModalWindow(modalWindow);
        modalController.applyComponentOrientation(owner.getComponentOrientation());
    }

    @Override
    public ModalContainer addModalWithoutShowing(Component owner, Modal modal, Option option, String id) {
        // this method use to showing drawer
        throw new UnsupportedOperationException("Modal HeavyWeight not support `addModalWithoutShowing`");
    }

    @Override
    public void remove(AbstractModalController controller) {
        Component owner = getOwner(((HeavyWeightModalController) controller).getOwner());
        ModalHeavyWeightLayout modalLayout = getModalLayout(owner);
        if (modalLayout != null) {
            modalLayout.remove(controller);
            if (modalLayout.isEmpty()) {
                modalLayout.uninstallOwner();
                listModalHeavyWeight.remove(modalLayout);
            }
        }
    }

    @Override
    public void pushModal(Modal modal, String id) {
        getModalControllerById(id).pushModal(modal);
        updateLayout();
    }

    @Override
    public void popModal(String id) {
        getModalControllerById(id).popModal();
        updateLayout();
    }

    @Override
    public void closeModal(String id) {
        getModalControllerById(id).closeModal();
    }

    @Override
    public void closeAllModal() {
        for (int i = listModalHeavyWeight.size() - 1; i >= 0; i--) {
            ModalHeavyWeightLayout modalLayout = listModalHeavyWeight.get(i);
            if (modalLayout != null) {
                closeAll(modalLayout);
            }
        }
    }

    @Override
    public void closeModalImmediately(String id) {
        getModalControllerById(id).closeModal();
    }

    @Override
    public void closeAllModalImmediately() {
        closeAllModal();
    }

    @Override
    public boolean checkId(String id) {
        for (int i = 0; i < listModalHeavyWeight.size(); i++) {
            List<ModalWindow> list = listModalHeavyWeight.get(i).getModalWindows();
            for (int j = 0; j < list.size(); j++) {
                HeavyWeightModalController modal = (HeavyWeightModalController) list.get(j).getContents();
                if (modal.getId() != null && Objects.equals(modal.getId(), id)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void updateLayout() {
        listModalHeavyWeight.forEach(modal -> modal.updateLayout());
    }

    protected void setEnableHierarchy(boolean enable) {
        listModalHeavyWeight.forEach(modal -> modal.setEnableHierarchy(enable));
    }

    private void closeAll(HeavyWeightRelativeLayout heavyWeight) {
        List<ModalWindow> modalWindows = heavyWeight.getModalWindows();
        for (int i = modalWindows.size() - 1; i >= 0; i--) {
            ((HeavyWeightModalController) modalWindows.get(i).getContents()).closeModal();
        }
    }

    private HeavyWeightModalController getModalControllerById(String id) {
        for (int i = 0; i < listModalHeavyWeight.size(); i++) {
            List<ModalWindow> list = listModalHeavyWeight.get(i).getModalWindows();
            for (int j = 0; j < list.size(); j++) {
                HeavyWeightModalController modal = (HeavyWeightModalController) list.get(j).getContents();
                if (modal.getId() != null && Objects.equals(modal.getId(), id)) {
                    return modal;
                }
            }
        }
        throw new IllegalArgumentException("id '" + id + "' not found");
    }

    private ModalHeavyWeightLayout getModalLayout(Component owner) {
        for (int i = 0; i < listModalHeavyWeight.size(); i++) {
            ModalHeavyWeightLayout toastHeavyWeight = listModalHeavyWeight.get(i);
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
        closeAllModalImmediately();
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
