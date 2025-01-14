package raven.modal.component;

import raven.modal.layout.ModalContainerLayout;
import raven.modal.option.Option;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Raven
 */
public abstract class AbstractModalContainerLayer {

    protected final Set<ModalContainer> containers;
    protected final JLayeredPane layeredPane;

    public AbstractModalContainerLayer() {
        containers = new HashSet<>();
        layeredPane = new JLayeredPane();
        layeredPane.setLayout(new ModalContainerLayout());
    }

    protected void animatedBegin() {
    }

    protected void animatedEnd() {
    }

    public abstract void showContainer(boolean show);

    public void removeContainer(ModalContainer container) {
        containers.remove(container);
        layeredPane.remove(container);
        layeredPane.repaint();
        layeredPane.revalidate();
    }

    public void addModal(Component owner, Modal modal, Option option, String id) {
        addModalWithoutShowing(owner, modal, option, id)
                .showModal();
    }

    public ModalContainer addModalWithoutShowing(Component owner, Modal modal, Option option, String id) {
        ModalContainer modalContainer = new ModalContainer(this, owner, option, id);
        layeredPane.setLayer(modalContainer, JLayeredPane.MODAL_LAYER + (option.getLayoutOption().isOnTop() ? 1 : 0));
        layeredPane.add(modalContainer, 0);
        modalContainer.initModal(modal);
        modal.setId(id);
        containers.add(modalContainer);
        return modalContainer;
    }

    public void pushModal(Modal modal, String id) {
        getModalControllerById(id).getController().pushModal(modal);
    }

    public void popModal(String id) {
        getModalControllerById(id).getController().popModal();
    }

    public void closeModal(String id) {
        getModalControllerById(id).getController().closeModal();
    }

    public void closeAllModal() {
        for (ModalContainer con : containers.toArray(new ModalContainer[containers.size()])) {
            con.getController().closeModal();
        }
    }

    public void closeModalImmediately(String id) {
        getModalControllerById(id).getController().closeImmediately();
    }

    public void closeAllModalImmediately() {
        for (ModalContainer con : containers.toArray(new ModalContainer[containers.size()])) {
            con.getController().closeImmediately();
        }
    }

    public void updateModalLayout() {
        layeredPane.revalidate();
    }

    public boolean checkId(String id) {
        for (ModalContainer con : containers) {
            if (con.getId() != null && con.getId() == id) {
                return true;
            }
        }
        return false;
    }

    private ModalContainer getModalControllerById(String id) {
        for (ModalContainer con : containers) {
            if (con.getId() != null && con.getId().equals(id)) {
                return con;
            }
        }
        throw new IllegalArgumentException("id '" + id + "' not found");
    }

    public Set<ModalContainer> getContainers() {
        return containers;
    }

    public JLayeredPane getLayeredPane() {
        return layeredPane;
    }
}
