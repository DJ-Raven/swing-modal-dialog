package raven.modal.component;

import raven.modal.option.Option;

import java.awt.*;

/**
 * @author Raven
 */
public interface BaseModalContainer {

    void addModal(Component owner, Modal modal, Option option, String id);

    ModalContainer addModalWithoutShowing(Component owner, Modal modal, Option option, String id);

    void remove(AbstractModalController controller);

    void pushModal(Modal modal, String id);

    void popModal(String id);

    void closeModal(String id);

    void closeAllModal();

    void closeModalImmediately(String id);

    void closeAllModalImmediately();

    boolean checkId(String id);

    void updateLayout();
}
