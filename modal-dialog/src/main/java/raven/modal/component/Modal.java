package raven.modal.component;

import javax.swing.*;

/**
 * The super class for all modal dialog
 *
 * @author Raven
 */
public abstract class Modal extends JPanel {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isInstalled() {
        return installed;
    }

    public void setInstalled(boolean installed) {
        this.installed = installed;
    }

    public ControllerAction getController() {
        return controller;
    }

    public void setController(ControllerAction controller) {
        this.controller = controller;
        controllerInit();
    }

    private ControllerAction controller;
    private String id;
    private boolean installed;

    public void installComponent() {
    }

    protected void controllerInit() {
    }

    protected void modalOpened() {
    }
}
