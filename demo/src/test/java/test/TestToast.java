package test;

import net.miginfocom.swing.MigLayout;
import raven.modal.Toast;
import raven.modal.toast.option.ToastDirection;
import raven.modal.toast.option.ToastOption;
import test.base.BaseFrame;

import javax.swing.*;
import java.awt.*;

public class TestToast extends BaseFrame {

    public TestToast() {
        super("Test Toast");
        setLayout(new MigLayout("al center center"));
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
        installLaf();
        EventQueue.invokeLater(() -> new TestToast().setVisible(true));
    }
}
