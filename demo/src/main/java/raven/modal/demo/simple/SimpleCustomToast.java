package raven.modal.demo.simple;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;
import raven.extras.AvatarIcon;
import raven.modal.toast.ToastCustomPanel;

import javax.swing.*;

public class SimpleCustomToast extends ToastCustomPanel {

    public SimpleCustomToast() {
        init();
    }

    private void init() {
        setOpaque(false);
        setLayout(new MigLayout("ay center,insets 0", "", "[sg h,bottom][sg h,top]"));
        JLabel labelProfile = new JLabel(new AvatarIcon(getClass().getResource("/raven/modal/demo/images/profile_2.jpg"), 50, 50, 999));
        JLabel labelName = new JLabel("Samantha Smith");
        JLabel labelDescription = new JLabel("Experienced marketing professional with a focus on digital advertising.");
        JButton buttonClose = new JButton(new FlatSVGIcon("raven/modal/demo/icons/close.svg", 0.3f));
        buttonClose.setFocusable(false);

        labelName.putClientProperty(FlatClientProperties.STYLE, "" +
                "font:bold +1;" +
                "foreground:$Component.accentColor;");

        buttonClose.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:999;" +
                "margin:5,5,5,5;" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0;" +
                "background:null;");

        buttonClose.addActionListener(e -> toastAction.close());

        add(labelProfile, "span 1 2,w 55::,h 55::,grow 0");
        add(labelName, "cell 1 0");
        add(labelDescription, "cell 1 1");
        add(buttonClose, "cell 2 0,span 1 2,ay top,gap 3 3 3 3");
    }
}
