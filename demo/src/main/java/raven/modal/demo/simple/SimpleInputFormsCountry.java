package raven.modal.demo.simple;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import raven.modal.demo.model.ModelCountry;

import javax.swing.*;

public class SimpleInputFormsCountry extends JPanel {

    public SimpleInputFormsCountry() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("fillx,wrap,insets 5 30 5 30,width 400", "[fill]", ""));
        txtName = new JTextField();
        txtIso2Code = new JTextField();
        txtIso3Code = new JTextField();
        txtCapital = new JTextField();
        txtRegion = new JTextField();

        // style
        txtName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Name");
        txtIso2Code.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "IOS 2");
        txtIso3Code.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "IOS 3");

        add(new JLabel("Country Name"), "gapy 5 0");
        add(txtName);
        add(new JLabel("ISO Letter Code"), "gapy 5 0");
        add(txtIso2Code, "split 2");
        add(txtIso3Code);
        add(new JLabel("Capital City"), "gapy 5 0");
        add(txtCapital);
        add(new JLabel("Region"), "gapy 5 0");
        add(txtRegion);
    }

    public void formOpen() {
        txtName.grabFocus();
    }

    public ModelCountry getInputData() {
        String name = txtName.getText().trim();
        String iso2Code = txtIso2Code.getText().trim();
        String ios3Code = txtIso3Code.getText().trim();
        String capitalCity = txtCapital.getText().trim();
        String region = txtRegion.getText().trim();

        if (name.isEmpty()) {
            txtName.grabFocus();
            return null;
        }

        return new ModelCountry(name, iso2Code, ios3Code, capitalCity, region);
    }

    private JTextField txtName;
    private JTextField txtIso2Code;
    private JTextField txtIso3Code;
    private JTextField txtCapital;
    private JTextField txtRegion;
}