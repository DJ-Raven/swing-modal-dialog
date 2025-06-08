package raven.modal.component;

import raven.modal.layout.ModalContainerLayout;
import raven.modal.option.LayoutOption;
import raven.modal.option.Option;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Raven
 */
public abstract class AbstractModalContainerLayer extends AbstractRelativeContainer implements BaseModalContainer, RelativeLayerPane.LayoutCallback {

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

    @Override
    public void addModal(Component owner, Modal modal, Option option, String id) {
        addModalWithoutShowing(owner, modal, option, id)
                .showModal();
    }

    @Override
    public ModalContainer addModalWithoutShowing(Component owner, Modal modal, Option option, String id) {
        ModalContainer modalContainer = new ModalContainer(this, owner, option, id);
        int layer = JLayeredPane.MODAL_LAYER + (option.getLayoutOption().isOnTop() ? 1 : 0);
        boolean visibility = isVisibility(option);
        boolean fixedLayout = isFixedLayout(option, owner);
        getLayerAndCreate(owner, visibility, fixedLayout).add(modalContainer, layer, 0);
        modalContainer.initModal(modal);
        modalContainer.setComponentOrientation(layeredPane.getComponentOrientation());
        modal.setId(id);
        containers.add(modalContainer);
        return modalContainer;
    }

    @Override
    public void remove(AbstractModalController controller) {
        ModalContainer container = controller.getModalContainer();
        Option option = container.getOption();
        boolean visibility = isVisibility(option);
        boolean fixedLayout = isFixedLayout(option, container.getOwner());
        removeLayer(container, container.getOwner(), visibility, fixedLayout);
        containers.remove(container);
        checkAndRemoveChildForOwner(container);
    }

    @Override
    public void pushModal(Modal modal, String id) {
        getModalControllerById(id).getController().pushModal(modal);
    }

    @Override
    public void popModal(String id) {
        getModalControllerById(id).getController().popModal();
    }

    @Override
    public void closeModal(String id) {
        getModalControllerById(id).getController().closeModal();
    }

    @Override
    public void closeAllModal() {
        for (ModalContainer con : containers.toArray(new ModalContainer[0])) {
            con.getController().closeModal();
        }
    }

    @Override
    public void closeModalImmediately(String id) {
        getModalControllerById(id).getController().closeImmediately();
    }

    @Override
    public void closeAllModalImmediately() {
        for (ModalContainer con : containers.toArray(new ModalContainer[0])) {
            con.getController().closeImmediately();
        }
    }

    @Override
    public boolean checkId(String id) {
        for (ModalContainer con : containers) {
            if (con.getId() != null && Objects.equals(con.getId(), id)) {
                return true;
            }
        }
        return false;
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

    private ModalContainer getModalControllerById(String id) {
        for (ModalContainer con : containers) {
            if (con.getId() != null && con.getId().equals(id)) {
                return con;
            }
        }
        throw new IllegalArgumentException("id '" + id + "' not found");
    }

    private void checkAndRemoveChildForOwner(Component owner) {
        for (ModalContainer c : containers.toArray(new ModalContainer[0])) {
            if (c.getOption().getLayoutOption().isRelativeToOwner()) {
                Component cOwner = c.getOwner();
                ModalContainer container = (ModalContainer) SwingUtilities.getAncestorOfClass(ModalContainer.class, cOwner);
                if (container == owner) {
                    remove(c.getController());
                }
            }
        }
    }

    public Set<ModalContainer> getContainers() {
        return containers;
    }

    private boolean isVisibility(Option option) {
        return option.getLayoutOption().isRelativeToOwner() && option.getLayoutOption().getRelativeToOwnerType() != LayoutOption.RelativeToOwnerType.RELATIVE_GLOBAL;
    }

    private boolean isFixedLayout(Option option, Component owner) {
        return !option.getLayoutOption().isRelativeToOwner() || option.getLayoutOption().getRelativeToOwnerType() != LayoutOption.RelativeToOwnerType.RELATIVE_CONTAINED || owner instanceof RootPaneContainer;
    }
}
