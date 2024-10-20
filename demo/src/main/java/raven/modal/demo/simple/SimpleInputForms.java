package raven.modal.demo.simple;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import raven.modal.component.ModalBorderAction;
import raven.modal.component.SimpleModalBorder;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SimpleInputForms extends JPanel {

    public SimpleInputForms() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("fillx,wrap,insets 5 30 5 30,width 400", "[fill]", ""));
        JTextField txtFirstName = new JTextField();
        JTextField txtLastName = new JTextField();
        JTextField txtCompany = new JTextField();
        JTextField txtEmail = new JTextField();
        JComboBox comboCountry = new JComboBox();

        JTextArea txtAddress = new JTextArea();
        txtAddress.setWrapStyleWord(true);
        txtAddress.setLineWrap(true);
        JScrollPane scroll = new JScrollPane(txtAddress);

        // style
        txtFirstName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "First");
        txtLastName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Last");
        txtCompany.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "e.g. Tesla Motors");

        // add to panel
        createTitle("Contact info");

        add(new JLabel("Full name"), "gapy 5 0");
        add(txtFirstName, "split 2");
        add(txtLastName);
        add(new JLabel("Company name"), "gapy 5 0");
        add(txtCompany);
        add(new JLabel("Email address"), "gapy 5 0");
        add(txtEmail);
        add(new JLabel("Country"), "gapy 5 0");
        add(comboCountry);

        createTitle("Delivery address");

        add(new JLabel("Address"), "gapy 5 0");
        add(scroll, "height 150,grow,pushy");

        txtAddress.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.isControlDown() && e.getKeyChar() == 10) {
                    ModalBorderAction modalBorderAction = ModalBorderAction.getModalBorderAction(SimpleInputForms.this);
                    if (modalBorderAction != null) {
                        modalBorderAction.doAction(SimpleModalBorder.YES_OPTION);
                    }
                }
            }
        });
        initComboItem(comboCountry);
    }

    private void createTitle(String title) {
        JLabel lb = new JLabel(title);
        lb.putClientProperty(FlatClientProperties.STYLE, "" +
                "font:+2");
        add(lb, "gapy 5 0");
        add(new JSeparator(), "height 2!,gapy 0 0");
    }

    private void initComboItem(JComboBox combo) {
        combo.addItem("United States");
        combo.addItem("Canada");
        combo.addItem("Brazil");
        combo.addItem("United Kingdom");
        combo.addItem("France");
        combo.addItem("Germany");
        combo.addItem("Australia");
        combo.addItem("Japan");
        combo.addItem("China");
        combo.addItem("India");
    }
}