package test;

import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import net.miginfocom.swing.MigLayout;
import raven.modal.Toast;
import raven.modal.toast.option.ToastDirection;
import raven.modal.toast.option.ToastOption;

import javax.swing.*;
import java.awt.*;

public class TestToast extends JFrame {

    public TestToast() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
        setLayout(new MigLayout("al center center"));
        //add(new FormToast());

        JButton cmdShow = new JButton("Show Toast");
        cmdShow.addActionListener(e -> {
            ToastOption toastOption = Toast.createOption();
            toastOption.getLayoutOption()
                    .setLocation(0.5f, 0.3f)
                    .setDirection(ToastDirection.LEFT_TO_RIGHT_BOTTOM);

            Toast.show(this, Toast.Type.SUCCESS, "Success message", toastOption);
        });
        add(cmdShow);
    }

    public static void main(String[] args) {
        FlatRobotoFont.install();
        FlatMacLightLaf.setup();
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        EventQueue.invokeLater(() -> new TestToast().setVisible(true));
    }
}
