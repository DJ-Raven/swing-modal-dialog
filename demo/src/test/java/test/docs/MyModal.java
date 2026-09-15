package test.docs;

import net.miginfocom.swing.MigLayout;
import raven.modal.component.Modal;

import java.awt.*;

public class MyModal extends Modal {

    public MyModal(Component content) {
        setLayout(new MigLayout("fill,insets 8", "[fill]", "[fill]"));
        add(content);
    }

    @Override
    public void installComponent() {
        // called once, the first time the modal is shown; build/attach the UI here
    }

    @Override
    protected void modalOpened() {
        // called after the modal has finished its open animation and gained focus
    }
}