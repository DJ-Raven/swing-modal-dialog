package raven.modal.demo.simple;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;
import raven.modal.component.ModalBorderAction;
import raven.modal.component.SimpleModalBorder;
import raven.modal.demo.component.EmbeddedComboBox;
import raven.modal.demo.model.ModelCountry;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SimpleInputForms extends JPanel {

    public static int NEW_COUNTRY = 30;

    public SimpleInputForms() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("fillx,wrap,insets 5 30 5 30,width 400", "[fill]", ""));
        txtFirstName = new JTextField();

        JTextField txtLastName = new JTextField();
        JTextField txtCompany = new JTextField();
        JTextField txtEmail = new JTextField();
        comboCountry = createEmbeddedComboBox(createCountryInitData(), e -> {
            ModalBorderAction.getModalBorderAction(this).doAction(NEW_COUNTRY);
        });

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
    }

    private void createTitle(String title) {
        JLabel lb = new JLabel(title);
        lb.putClientProperty(FlatClientProperties.STYLE, "" +
                "font:+2");
        add(lb, "gapy 5 0");
        add(new JSeparator(), "height 2!,gapy 0 0");
    }

    private EmbeddedComboBox<ModelCountry> createEmbeddedComboBox(ModelCountry[] initData, ActionListener event) {
        EmbeddedComboBox<ModelCountry> embeddedComboBox = new EmbeddedComboBox<>(initData);
        JToolBar toolbar = new JToolBar();
        JButton button = new JButton(new FlatSVGIcon("raven/modal/demo/icons/add.svg", 0.4f));
        button.addActionListener(event);
        toolbar.add(button);
        toolbar.addSeparator();
        embeddedComboBox.setEmbedded(toolbar);
        return embeddedComboBox;
    }

    private ModelCountry[] createCountryInitData() {
        return new ModelCountry[]{
                new ModelCountry("Cambodia", "KH", "KHM", "Phnom Penh", "Asia"),
                new ModelCountry("United States", "US", "USA", "Washington, D.C.", "Americas"),
                new ModelCountry("France", "FR", "FRA", "Paris", "Europe"),
                new ModelCountry("Japan", "JP", "JPN", "Tokyo", "Asia"),
                new ModelCountry("Brazil", "BR", "BRA", "Brasília", "Americas")
        };
    }

    public void formOpen() {
        txtFirstName.grabFocus();
    }

    public void newCountryCreated(ModelCountry country) {
        comboCountry.addItem(country);
        comboCountry.setSelectedItem(country);
    }

    private JTextField txtFirstName;
    private EmbeddedComboBox<ModelCountry> comboCountry;
}