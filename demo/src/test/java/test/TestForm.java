package test;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import net.miginfocom.swing.MigLayout;
import raven.modal.Drawer;
import raven.modal.ModalDialog;
import raven.modal.Toast;
import raven.modal.component.SimpleModalBorder;
import raven.modal.demo.menu.MyDrawerBuilder;
import raven.modal.demo.utils.DemoPreferences;
import raven.modal.toast.ToastPromise;

import javax.swing.*;
import java.awt.*;

public class TestForm extends JFrame {


    public TestForm() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
        setLayout(new MigLayout("al center center"));
        // add(new FormToast());
        // add(new FormDateTime());
        JButton show = new JButton("Show");
        show.addActionListener(e -> {
            Box b = new Box(this);
            b.setVisible(true);
        });
        Toast.getDefaultOption().setAnimationEnabled(false);
        add(show);
    }

    private class Box extends JDialog {
        public Box(Frame frame) {
            super(frame, true);
            setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            setSize(new Dimension(500, 500));
            setLocationRelativeTo(null);
            JButton show = new JButton("show");
            ModalDialog.getDefaultOption().getLayoutOption().setOnTop(true);
            show.addActionListener(e -> {
                JButton hide = new JButton("Hide");
                hide.addActionListener(e1 -> setVisible(false));
                ModalDialog.showModal(this, new SimpleModalBorder(hide, "Test"));
            });
            Drawer.installDrawer(this, new MyDrawerBuilder());
            setLayout(new MigLayout("al center center"));
            add(show);
            JButton showToast = new JButton("Show toast");
            showToast.addActionListener(e -> {
                Toast.showPromise(this, "HI", new ToastPromise() {
                    @Override
                    public void execute(PromiseCallback callback) {
                        try {
                            Thread.sleep(2000);
                            callback.done(Toast.Type.SUCCESS, "HI");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public boolean rejectAble() {
                        return true;
                    }
                });

            });
            add(showToast);
            JButton showDrawer = new JButton("Show drawer");
            showDrawer.addActionListener(e -> Drawer.showDrawer());
            add(showDrawer);
        }
    }

    public static void main(String[] args) {
        DemoPreferences.init();
        FlatRobotoFont.install();
        FlatLaf.registerCustomDefaultsSource("raven.modal.demo.themes");
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        DemoPreferences.setupLaf();
        EventQueue.invokeLater(() -> new TestForm().setVisible(true));
    }
}
