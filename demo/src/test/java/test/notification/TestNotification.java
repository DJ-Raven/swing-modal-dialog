package test.notification;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.modal.component.SimpleModalBorder;
import raven.modal.option.*;
import test.base.BaseFrame;

import javax.swing.*;
import java.awt.*;

public class TestNotification extends BaseFrame {

    public TestNotification() {
        super("Test Notification");

        setLayout(new MigLayout("al trailing top,insets 30"));

        JButton cmd = new JButton(new FlatSVGIcon("raven/icon/notification.svg", 0.5f));
        cmd.putClientProperty(FlatClientProperties.STYLE, "" +
                "margin:8,8,8,8;" +
                "arc:999;" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0");

        cmd.addActionListener(e -> {
            Option option = ModalDialog.createOption();
            option.getLayoutOption()
                    .setMargin(35, 0, 0, -10)
                    .setLocation(Location.TRAILING, 0)
                    .setOverflowAlignmentAuto(false)
                    .setRelativeToOwner(true)
                    .setRelativeToOwnerType(LayoutOption.RelativeToOwnerType.RELATIVE_GLOBAL);
            option.setOpacity(0f)
                    .getBorderOption()
                    .setBorderWidth(0.5f)
                    .setShadow(BorderOption.Shadow.MEDIUM);
            ModalBorderOption borderOption = new ModalBorderOption().setPadding(ModalBorderOption.PaddingType.MEDIUM);
            ModalDialog.showModal(cmd, new SimpleModalBorder(new NotificationPanel(), "Notifications", borderOption), option);
        });
        add(cmd);
    }

    public static void main(String[] args) {
        installLaf();
        EventQueue.invokeLater(() -> new TestNotification().setVisible(true));
    }
}
