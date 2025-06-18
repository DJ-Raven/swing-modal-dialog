package test;

import com.formdev.flatlaf.FlatSystemProperties;
import net.miginfocom.swing.MigLayout;
import raven.modal.Toast;
import raven.modal.toast.option.ToastBorderStyle;
import raven.modal.toast.option.ToastDirection;
import raven.modal.toast.option.ToastOption;
import raven.modal.toast.option.ToastStyle;
import raven.modal.utils.EasingInterpolator;
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
            toastOption.setDuration(1500);
            toastOption.setInterpolator(EasingInterpolator::easeOutBack);
            toastOption.getLayoutOption()
                    .setLocation(0.5f, 0.3f)
                    .setDirection(ToastDirection.LEFT_TO_RIGHT_BOTTOM)
            ;
            toastOption.getStyle().setBackgroundType(ToastStyle.BackgroundType.NONE)
                    .getBorderStyle().setBorderType(ToastBorderStyle.BorderType.LEADING_LINE);

            Toast.show(this, Toast.Type.SUCCESS, "Success message", toastOption);
        });
        add(cmdShow);
    }

    public static void main(String[] args) {
        System.setProperty(FlatSystemProperties.UI_SCALE, "200%");
        installLaf();
        EventQueue.invokeLater(() -> new TestToast().setVisible(true));
    }
}
