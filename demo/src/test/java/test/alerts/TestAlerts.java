package test.alerts;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.modal.component.SimpleModalBorder;
import raven.modal.option.BorderOption;
import test.base.BaseFrame;

import javax.swing.*;
import java.awt.*;

public class TestAlerts extends BaseFrame {

    public TestAlerts() {
        super("Test Alerts");
        setLayout(new MigLayout("al center center"));
        JButton button = new JButton("show");
        button.addActionListener(e -> {
            AlertsOption alertsOption = AlertsOption.getAlertsOption(AlertsOption.Type.ERROR);
            JLabel label = new JLabel();
            label.putClientProperty(FlatClientProperties.STYLE, "" +
                    "border:5,35,2,35");
            label.setHorizontalAlignment(JTextField.CENTER);
            label.setText("Hello! I hope you're having a wonderful day.");

            // apply alerts background
            label.setBackground(new Color(240, 245, 213));

            ModalDialog.getDefaultOption()
                    .setOpacity(0f)
                    .getBorderOption()
                    .setShadow(BorderOption.Shadow.LARGE);
            ModalDialog.showModal(this, new SimpleAlerts(label, "Alerts", alertsOption, SimpleModalBorder.OK_OPTION, (controller, action) -> {

            }));
        });
        add(button);
    }

    public static void main(String[] args) {
        installLaf();
        EventQueue.invokeLater(() -> new TestAlerts().setVisible(true));
    }
}
