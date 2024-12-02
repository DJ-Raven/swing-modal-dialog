package test.validation;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import net.miginfocom.swing.MigLayout;
import raven.modal.Drawer;
import raven.modal.demo.menu.MyDrawerBuilder;

import javax.swing.*;
import java.awt.*;

public class TestMenuValidation extends JFrame {

    public TestMenuValidation() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1366, 768));
        setLocationRelativeTo(null);
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
        FlatRobotoFont.install();
        FlatLaf.registerCustomDefaultsSource("raven.modal.demo.themes");
        FlatDarculaLaf.setup();
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        EventQueue.invokeLater(() -> new TestMenuValidation().setVisible(true));
    }
}
