package test.validation;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import raven.modal.Drawer;
import test.base.BaseFrame;

import javax.swing.*;
import java.awt.*;

public class TestMenuValidation extends BaseFrame {

    public TestMenuValidation() {
        super("Test Menu Validation");
        getRootPane().putClientProperty(FlatClientProperties.FULL_WINDOW_CONTENT, true);
        setLayout(new MigLayout("al center center"));
        Drawer.installDrawer(this, ValidationDrawerBuilder.getInstance());

        JButton cmdAdmin = new JButton("Change to Admin");
        JButton cmdStaff = new JButton("Change to Staff");

        cmdAdmin.addActionListener(e -> {
            ValidationDrawerBuilder.getInstance().setUser(new User(new ImageIcon(getClass().getResource("/raven/modal/demo/images/profile_1.jpg")), "Raven", "raven@gmail.com", "admin"));
        });
        cmdStaff.addActionListener(e -> {
            ValidationDrawerBuilder.getInstance().setUser(new User(new ImageIcon(getClass().getResource("/raven/modal/demo/images/profile_2.jpg")), "Dara", "dara@gmail.com", "staff"));
        });

        add(cmdAdmin);
        add(cmdStaff);
    }

    public static void main(String[] args) {
        installLaf();
        EventQueue.invokeLater(() -> new TestMenuValidation().setVisible(true));
    }
}
