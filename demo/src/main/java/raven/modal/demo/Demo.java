package raven.modal.demo;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import raven.modal.Drawer;
import raven.modal.demo.menu.MyDrawerBuilder;
import raven.modal.demo.system.FormManager;
import raven.modal.demo.utils.DemoPreferences;

import javax.swing.*;
import java.awt.*;

public class Demo extends JFrame {

    public static final String DEMO_VERSION = "2.4.0-SNAPSHOT";

    public Demo() {
        init();
    }

    private void init() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getRootPane().putClientProperty(FlatClientProperties.FULL_WINDOW_CONTENT, true);
        Drawer.installDrawer(this, new MyDrawerBuilder());
        FormManager.install(this);
        setSize(new Dimension(1366, 768));
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        DemoPreferences.init();
        FlatRobotoFont.install();
        FlatLaf.registerCustomDefaultsSource("raven.modal.demo.themes");
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        DemoPreferences.setupLaf();
        EventQueue.invokeLater(() -> new Demo().setVisible(true));
    }
}
