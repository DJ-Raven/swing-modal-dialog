package test;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.modal.component.SimpleModalBorder;
import test.alerts.AlertsOption;
import test.alerts.SimpleAlerts;

import javax.swing.*;
import java.awt.*;

public class TestAlerts extends JFrame {

    public TestAlerts() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
        setLayout(new MigLayout("al center center"));
        JButton button = new JButton("show");
        button.addActionListener(e -> {
            AlertsOption alertsOption = AlertsOption.getAlertsOption(AlertsOption.Type.ERROR);
            JLabel label = new JLabel();
            label.putClientProperty(FlatClientProperties.STYLE, "" +
                    "border:5,35,2,35");
            label.setHorizontalAlignment(JTextField.CENTER);
            label.setText("Hello! I hope you're having a wonderful day.");
            ModalDialog.showModal(this, new SimpleAlerts(label, "Alerts", alertsOption, SimpleModalBorder.OK_OPTION, (controller, action) -> {

            }));
        });
        add(button);
    }

    public static void main(String[] args) {
        FlatRobotoFont.install();
        FlatMacLightLaf.setup();
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        EventQueue.invokeLater(() -> new TestAlerts().setVisible(true));
    }
}
