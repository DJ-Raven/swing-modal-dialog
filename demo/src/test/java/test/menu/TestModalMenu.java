package test.menu;

import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.modal.demo.component.EmptyModalBorder;
import raven.modal.demo.forms.setting.SettingsModal;
import raven.modal.option.BorderOption;
import raven.modal.option.Option;
import test.base.BaseFrame;

import javax.swing.*;
import java.awt.*;

public class TestModalMenu extends BaseFrame {

    public TestModalMenu() {
        super("Test Alerts");
        setLayout(new MigLayout("al center center"));
        JButton button = new JButton("show");

        button.addActionListener(e -> {
            Option option = ModalDialog.createOption()
                    .setOpacity(0.1f);
            option.getLayoutOption()
                    .setSize(750, 600);
            option.getBorderOption()
                    .setBorderWidth(1)
                    .setShadow(BorderOption.Shadow.EXTRA_LARGE);
            SettingsModal settingsModal = new SettingsModal();
            ModalDialog.showModal(this, new EmptyModalBorder(settingsModal), option);
        });
        add(button);
    }

    public static void main(String[] args) {
        installLaf();
        EventQueue.invokeLater(() -> new TestModalMenu().setVisible(true));
    }
}
