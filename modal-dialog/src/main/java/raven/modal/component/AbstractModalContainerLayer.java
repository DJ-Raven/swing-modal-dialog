package raven.modal.component;

import raven.modal.layout.ModalContainerLayout;
import raven.modal.option.LayoutOption;
import raven.modal.option.Option;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Raven
 */
public abstract class AbstractModalContainerLayer extends AbstractRelativeContainer implements RelativeLayerPane.LayoutCallback {

    protected final Set<ModalContainer> containers;

    public AbstractModalContainerLayer() {
        super(new ModalContainerLayout());
        containers = new HashSet<>();
        setLayoutCallback(this);
    }

    protected void animatedBegin() {
    }

    protected void animatedEnd() {
    }

    public abstract void showContainer(boolean show);

    public void removeContainer(ModalContainer container) {
        Option option = container.getOption();
        boolean visibility = isVisibility(option);
        boolean fixedLayout = isFixedLayout(option);
        removeLayer(container, container.getOwner(), visibility, fixedLayout);
        containers.remove(container);
    }

    public void addModal(Component owner, Modal modal, Option option, String id) {
        addModalWithoutShowing(owner, modal, option, id)
                .showModal();
    }

    public ModalContainer addModalWithoutShowing(Component owner, Modal modal, Option option, String id) {
        ModalContainer modalContainer = new ModalContainer(this, owner, option, id);
        int layer = JLayeredPane.MODAL_LAYER + (option.getLayoutOption().isOnTop() ? 1 : 0);
        boolean visibility = isVisibility(option);
        boolean fixedLayout = isFixedLayout(option);
        getLayerAndCreate(owner, visibility, fixedLayout).add(modalContainer, layer, 0);
        modalContainer.initModal(modal);
        modalContainer.setComponentOrientation(layeredPane.getComponentOrientation());
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

    @Override
    public void updateLayout() {
        layeredPane.revalidate();
        for (ModalContainer con : containers) {
            con.revalidate();
        }
    }

    @Override
    public void doLayout() {
        for (ModalContainer con : containers) {
            con.revalidate();
        }
        layeredPane.repaint();
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

    private boolean isVisibility(Option option) {
        return option.getLayoutOption().isRelativeToOwner() && option.getLayoutOption().getRelativeToOwnerType() != LayoutOption.RelativeToOwnerType.RELATIVE_GLOBAL;
    }

    private boolean isFixedLayout(Option option) {
        return !option.getLayoutOption().isRelativeToOwner() || option.getLayoutOption().getRelativeToOwnerType() != LayoutOption.RelativeToOwnerType.RELATIVE_CONTAINED;
    }
}
