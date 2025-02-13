package test.notification;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import raven.extras.AvatarIcon;

import javax.swing.*;

public class NotificationItem extends JPanel {

    public NotificationItem(String icon, String name, String message, String description) {
        putClientProperty(FlatClientProperties.STYLE, "" +
                "background:null;");
        setLayout(new MigLayout("insets 3", "[]10[][]"));
        add(new JLabel(new AvatarIcon(getClass().getResource("/raven/modal/demo/images/" + icon + ".jpg"), 45, 45, 999)), "cell 0 0,span 1 2");
        JLabel lbName = new JLabel(name);
        JLabel lbMessage = new JLabel(message);
        JLabel lbDescription = new JLabel(description);
        lbName.putClientProperty(FlatClientProperties.STYLE, "" +
                "font:bold;");
        lbDescription.putClientProperty(FlatClientProperties.STYLE, "" +
                "foreground:$Label.disabledForeground;");

        add(lbName, "cell 1 0,gapy 5 0");
        add(lbMessage, "cell 2 0");
        add(lbDescription, "cell 1 1,span 2,gapy 0 5");
    }
}
