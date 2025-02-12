package test;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import raven.modal.Drawer;
import raven.modal.demo.menu.MyDrawerBuilder;
import test.base.BaseFrame;

import javax.swing.*;
import java.awt.*;

public class TestDrawer extends BaseFrame {

    public TestDrawer() {
        super(null);
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
        installLaf();
        EventQueue.invokeLater(() -> new TestDrawer().setVisible(true));
    }
}
