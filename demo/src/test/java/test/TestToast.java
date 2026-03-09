package test;

import net.miginfocom.swing.MigLayout;
import raven.extras.LightDarkButton;
import raven.modal.Toast;
import raven.modal.toast.option.ToastBorderStyle;
import raven.modal.toast.option.ToastLocation;
import raven.modal.toast.option.ToastOption;
import raven.modal.toast.option.ToastStyle;
import raven.modal.utils.EasingInterpolator;
import test.base.BaseFrame;
import test.component.NotificationBox;

import javax.swing.*;
import java.awt.*;

public class TestToast extends BaseFrame {

    public TestToast() {
        super("Test Toast");
        setLayout(new MigLayout("al center center"));
        JButton cmdShow = new JButton("Show Toast");
        cmdShow.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cmdShow.addActionListener(e -> {
            ToastOption toastOption = Toast.createOption();
            toastOption.setDuration(1000);
            toastOption.setInterpolator(EasingInterpolator::easeOutBack);
            toastOption.getLayoutOption()
                    .setLocation(ToastLocation.TOP_LEADING)
            ;
            toastOption.getStyle().setBackgroundType(ToastStyle.BackgroundType.NONE)
                    .setShowLabel(true)
                    .setIconSeparateLine(true)
                    .getBorderStyle().setBorderType(ToastBorderStyle.BorderType.LEADING_LINE);
            String id = Toast.show(this, Toast.Type.INFO, "The operation was completed successfully without any issues.", toastOption);
            System.out.println("id:" + id);
        });
        add(cmdShow);
        html();
        custom();
        LightDarkButton lightDarkButton = new LightDarkButton();
        lightDarkButton.installAutoLafChangeListener();
        add(lightDarkButton);
    }

    private void html() {
        JButton cmdShow = new JButton("Show as HTML");
        cmdShow.addActionListener(e -> {
            ToastOption toastOption = Toast.createOption();
            toastOption.setHtmlEnabled(true);
            toastOption.getStyle().setBackgroundType(ToastStyle.BackgroundType.NONE)
                    .setShowLabel(true)
                    .setIconSeparateLine(true)
                    .getBorderStyle().setBorderType(ToastBorderStyle.BorderType.LEADING_LINE);
            Toast.show(this, Toast.Type.INFO, "<html>The operation was <b><font color='#2ECC71'>completed successfully</font></b> without any issues.</html>", toastOption);
        });
        add(cmdShow);
    }

    private void custom() {
        JButton cmdShow = new JButton("Show Custom");
        cmdShow.addActionListener(e -> {
            ToastOption toastOption = Toast.createOption();
            toastOption.setAnimationEnabled(false)
                    .setAutoClose(false);
            toastOption.getLayoutOption()
                    .setLocation(ToastLocation.BOTTOM_TRAILING);
            toastOption.getStyle().setBackgroundType(ToastStyle.BackgroundType.NONE);

            toastOption.getStyle().getBorderStyle().setBorderType(ToastBorderStyle.BorderType.OUTLINE);
            Toast.showCustom(this, new NotificationBox(), toastOption);
        });
        add(cmdShow);
    }

    public static void main(String[] args) {
        installLaf();
        EventQueue.invokeLater(() -> new TestToast().setVisible(true));
    }
}
