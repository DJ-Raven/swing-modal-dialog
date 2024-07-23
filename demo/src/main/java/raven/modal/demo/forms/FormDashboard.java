package raven.modal.demo.forms;

import net.miginfocom.swing.MigLayout;
import raven.modal.demo.system.Form;
import raven.modal.demo.utils.SystemForm;

import javax.swing.*;

@SystemForm(name = "Dashboard", description = "dashboard form display some details")
public class FormDashboard extends Form {

    public FormDashboard() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("al center center"));
        JLabel text = new JLabel("Dashboard");
        add(text);
    }
}
