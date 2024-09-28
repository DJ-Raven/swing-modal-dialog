package test;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import net.miginfocom.swing.MigLayout;
import raven.modal.Drawer;
import raven.modal.ModalDialog;
import raven.modal.demo.menu.MyDrawerBuilder;

import javax.swing.*;
import java.awt.*;

public class TestDrawer extends JFrame {

    public TestDrawer() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1366, 768));
        setLocationRelativeTo(null);
        getRootPane().putClientProperty(FlatClientProperties.FULL_WINDOW_CONTENT, true);
        setLayout(new MigLayout("al center center"));
        JButton button = new JButton("show");
        Drawer.installDrawer(this, new MyDrawerBuilder());

        button.addActionListener(e -> {
            Drawer.showDrawer();
        });
        add(button);
    }

    public static void main(String[] args) {
        FlatRobotoFont.install();
        FlatLaf.registerCustomDefaultsSource("raven.modal.demo.themes");
        FlatDarculaLaf.setup();
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        EventQueue.invokeLater(() -> new TestDrawer().setVisible(true));
    }
}
