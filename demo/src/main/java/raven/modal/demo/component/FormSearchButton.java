package raven.modal.demo.component;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class FormSearchButton extends JButton {

    public FormSearchButton() {
        super("Quick Search...", new FlatSVGIcon("raven/modal/demo/icons/search.svg", 0.4f));
        init();
    }

    private void init() {
        setLayout(new MigLayout("insets 0,al trailing,filly", "", "[center]"));
        setHorizontalAlignment(JButton.LEADING);
        putClientProperty(FlatClientProperties.STYLE, "" +
                "margin:3,7,3,10;" +
                "background:$TextField.background;" +
                "arc:999;" +
                "borderWidth:1;" +
                "focusWidth:0;" +
                "innerFocusWidth:0;" +
                "foreground:$TextField.placeholderForeground;");
        JLabel label = new JLabel("Ctrl F");
        label.putClientProperty(FlatClientProperties.STYLE, "" +
                "foreground:$Label.disabledForeground;");
        add(label);
    }
}
