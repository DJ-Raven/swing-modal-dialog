package test.component;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;
import raven.modal.toast.ToastCustomPanel;

import javax.swing.*;

public class NotificationBox extends ToastCustomPanel {

    public NotificationBox() {
        setLayout(new MigLayout("insets 3,wrap,gap 3"));
        setOpaque(false);

        JLabel title = new JLabel("Update Available");
        title.putClientProperty(FlatClientProperties.STYLE, "font:bold;");
        JTextArea text = new JTextArea("A new update is available.\nPlease refresh or restart to apply the changes.");
        text.setBorder(BorderFactory.createEmptyBorder());
        text.setWrapStyleWord(true);
        text.setEditable(false);
        text.setOpaque(false);

        JButton buttonClose = new JButton(new FlatSVGIcon("raven/modal/demo/icons/close.svg", 0.3f));
        buttonClose.setFocusable(false);

        buttonClose.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:10;" +
                "margin:3,3,3,3;" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0;" +
                "background:null;");

        buttonClose.addActionListener(e -> toastAction.close());

        add(title, "split 2");
        add(buttonClose, "gapx push");
        add(text);
    }
}
