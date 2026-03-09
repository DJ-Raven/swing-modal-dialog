package test;

import com.formdev.flatlaf.util.UIScale;
import net.miginfocom.swing.MigLayout;
import raven.extras.LightDarkButton;
import raven.modal.Toast;
import raven.modal.demo.simple.SimpleCustomNotificationToast;
import raven.modal.toast.ToastPromise;
import raven.modal.toast.option.ToastBorderStyle;
import raven.modal.toast.option.ToastLocation;
import raven.modal.toast.option.ToastOption;
import raven.modal.toast.option.ToastStyle;
import raven.modal.utils.EasingInterpolator;
import test.base.BaseFrame;

import javax.swing.*;
import java.awt.*;

public class TestToast extends BaseFrame {

    public TestToast() {
        super("Test Toast");
        setLayout(new MigLayout("wrap,al center center"));
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
        customPromise("Promise failed", Toast.Type.ERROR, "<html>\n" +
                "<b><font color='#E74C3C'>✖ Payment Failed</font></b><br>\n" +
                "Invoice <font color='#8E44AD'>#INV-2048</font> could not be processed.<br>\n" +
                "<font color='#F39C12'><i>Network timeout detected.</i></font><br>\n" +
                "<font color='#7F8C8D'>Please check the connection and try again.</font>\n" +
                "</html>");
        int size = UIScale.scale(200);
        customPromise("Promise success", Toast.Type.SUCCESS, "<html>\n" +
                "<div style='width:" + size + "px'>\n" +
                "<b><font color='#3498DB'>\uD83E\uDDFE Sales Receipt</font></b><br>\n" +
                "--------------------------------<br>\n" +
                "\n" +
                "Coffee <font color='#7F8C8D'>x2</font>\n" +
                "<span></span><font color='#E67E22'>$6.00</font><br>\n" +
                "Cake <font color='#7F8C8D'>x1</font>\n" +
                "<span></span><font color='#E67E22'>$3.50</font><br>\n" +
                "\n" +
                "--------------------------------<br>\n" +
                "\n" +
                "Subtotal: <font color='#3498DB'>$9.50</font><br>\n" +
                "Tax: <font color='#3498DB'>$0.95</font><br>\n" +
                "\n" +
                "<b>Total: <font color='#2ECC71'>$10.45</font></b><br><br>\n" +
                "\n" +
                "<b><font color='#2ECC71'>✔ Payment Completed</font></b><br>\n" +
                "<i><font color='#7F8C8D'>Thank you for shopping with us.\n" +
                "Please keep this receipt for reference.</font></i>\n" +
                "</div>\n" +
                "</html>");

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
            Toast.showCustom(this, new SimpleCustomNotificationToast(), toastOption);
        });
        add(cmdShow);
    }

    private void customPromise(String button, Toast.Type type, String response) {
        JButton cmdShow = new JButton(button);
        cmdShow.addActionListener(e -> {
            ToastOption toastOption = Toast.createOption();
            toastOption.setHtmlEnabled(true);
            toastOption.getLayoutOption()
                    .setRelativeToOwner(true)
                    .setLocation(ToastLocation.TOP_CENTER);
            toastOption.getStyle().setBackgroundType(ToastStyle.BackgroundType.NONE);

            toastOption.getStyle()
                    .setIconSeparateLine(true)
                    .setShowCloseButton(false)
                    .setShowIcon(false)
                    .getBorderStyle().setBorderType(ToastBorderStyle.BorderType.LEADING_LINE);
            int size = UIScale.scale(150);
            Toast.showPromise(this, "" +
                            "<html>\n" +
                            "<div style='width:" + size + ";'>\n" +
                            "<b><font color='#3498DB'>\uD83D\uDD04 Processing Payment</font></b><br>\n" +
                            "<font color='#7F8C8D'>Please wait a moment...</font>\n" +
                            "</div>\n" +
                            "</html>"
                    , toastOption, new ToastPromise() {
                        @Override
                        public void execute(PromiseCallback callback) {
                            try {
                                Thread.sleep(2000);
                                callback.done(type, response);
                            } catch (Exception e) {
                                System.err.println(e.getMessage());
                            }
                        }
                    });
        });
        add(cmdShow);
    }

    public static void main(String[] args) {
        installLaf();
        EventQueue.invokeLater(() -> new TestToast().setVisible(true));
    }
}
