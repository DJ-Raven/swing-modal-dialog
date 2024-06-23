package raven.modal.component;

import javax.swing.*;

/**
 * The super class for all modal dialog
 *
 * @author Raven
 */
public class Modal extends JPanel {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ModalController getController() {
        return controller;
    }

    public void setController(ModalController controller) {
        this.controller = controller;
        controllerInit();
    }

    private ModalController controller;
    private String id;

    public Modal() {
        init();
    }

    private void init() {
    }

    protected void controllerInit() {
    }
}
