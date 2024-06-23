package raven.modal.demo.forms;

import net.miginfocom.swing.MigLayout;
import raven.modal.demo.system.Form;

import javax.swing.*;

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
