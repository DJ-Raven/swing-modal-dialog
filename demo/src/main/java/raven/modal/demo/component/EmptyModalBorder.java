package raven.modal.demo.component;

import net.miginfocom.swing.MigLayout;
import raven.modal.component.Modal;

import java.awt.*;

public class EmptyModalBorder extends Modal {

    public EmptyModalBorder(Component component) {
        setLayout(new MigLayout("fill,insets 8 0 8 0", "[fill]", "[fill]"));
        add(component);
    }
}
