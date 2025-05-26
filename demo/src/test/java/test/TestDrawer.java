package test;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import raven.modal.Drawer;
import raven.modal.demo.menu.MyDrawerBuilder;
import raven.modal.demo.menu.MyMenuValidation;
import raven.modal.demo.model.ModelUser;
import test.base.BaseFrame;

import javax.swing.*;
import java.awt.*;

public class TestDrawer extends BaseFrame {

    public TestDrawer() {
        super(null);
        getRootPane().putClientProperty(FlatClientProperties.FULL_WINDOW_CONTENT, true);
        setLayout(new MigLayout("al center center"));
        JButton button = new JButton("show");
        MyMenuValidation.setUser(new ModelUser("Ra Ven", "raven@gmail.com", ModelUser.Role.ADMIN));
        Drawer.installDrawer(this, MyDrawerBuilder.getInstance());

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
