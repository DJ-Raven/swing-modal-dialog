package test.combobox;

import net.miginfocom.swing.MigLayout;
import raven.extras.LightDarkButton;
import raven.modal.demo.component.EmbeddedComboBox;
import test.base.BaseFrame;

import javax.swing.*;
import java.awt.*;

public class TestComboBox extends BaseFrame {

    public TestComboBox() {
        super("Test ComboBox");
        setLayout(new MigLayout("wrap,al center center"));
        LightDarkButton lightDarkButton = new LightDarkButton();
        lightDarkButton.installAutoLafChangeListener();
        add(lightDarkButton);

        EmbeddedComboBox<Object> comboBox = new EmbeddedComboBox<>(new Object[]{
                "The constraints used are all entered as Strings",
                "or through chained API method calls."
        });
        JToolBar toolBar = new JToolBar();
        toolBar.add(new JButton("Go-1"));
        toolBar.add(new JButton("Go-2"));
        toolBar.add(new JButton("Go-3"));
        toolBar.add(new JButton("Go-4"));
        comboBox.setEmbedded(toolBar);
        add(comboBox);
    }

    public static void main(String[] args) {
        installLaf();
        EventQueue.invokeLater(() -> new TestComboBox().setVisible(true));
    }
}
