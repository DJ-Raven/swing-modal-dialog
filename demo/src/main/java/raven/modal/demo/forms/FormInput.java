package raven.modal.demo.forms;

import net.miginfocom.swing.MigLayout;
import raven.modal.demo.system.Form;
import raven.modal.demo.utils.SystemForm;

import javax.swing.*;

@SystemForm(name = "Form Input", description = "input form not yet update")
public class FormInput extends Form {

    public FormInput() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("al center center"));
        JLabel text = new JLabel("Input");
        add(text);
    }
}
