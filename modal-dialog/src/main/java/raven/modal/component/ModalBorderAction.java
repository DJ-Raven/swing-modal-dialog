package raven.modal.component;

import java.awt.*;

/**
 * @author Raven
 */
public interface ModalBorderAction {

    void doAction(int action);


    public static ModalBorderAction getModalBorderAction(Component com) {
        if (com == null) {
            return null;
        }
        if (com instanceof ModalBorderAction) {
            return (ModalBorderAction) com;
        }
        return getModalBorderAction(com.getParent());
    }
}
