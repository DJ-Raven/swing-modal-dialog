package raven.modal.demo;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.util.UIScale;
import raven.modal.Drawer;
import raven.modal.demo.menu.MyDrawerBuilder;
import raven.modal.demo.system.FormManager;
import raven.modal.demo.utils.DemoPreferences;

import javax.swing.*;
import java.awt.*;

public class Demo extends JFrame {

    public static final String DEMO_VERSION = "2.5.1";

    public Demo() {
        init();
    }

    private void init() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getRootPane().putClientProperty(FlatClientProperties.FULL_WINDOW_CONTENT, true);
        Drawer.installDrawer(this, MyDrawerBuilder.getInstance());
        FormManager.install(this);
        setSize(UIScale.scale(new Dimension(1366, 768)));
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        DemoPreferences.init();
        FlatRobotoFont.install();
        FlatLaf.registerCustomDefaultsSource("raven.modal.demo.themes");
        DemoPreferences.setupLaf();
        EventQueue.invokeLater(() -> new Demo().setVisible(true));
    }
}
