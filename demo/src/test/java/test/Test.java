package test;

import net.miginfocom.swing.MigLayout;
import raven.extras.LightDarkButton;
import raven.modal.ModalDialog;
import raven.modal.component.SimpleModalBorder;
import raven.modal.demo.simple.SimpleInputForms;
import raven.modal.demo.simple.SimpleInputForms2;
import raven.modal.option.BorderOption;
import test.base.BaseFrame;

import javax.swing.*;
import java.awt.*;

public class Test extends BaseFrame {

    public Test() {
        super("Test");
        setLayout(new MigLayout("wrap,al center center"));
        JButton button = new JButton("show");
        ModalDialog.getDefaultOption()
                .setOpacity(0f)
                .setAnimationOnClose(false)
                .getBorderOption()
                .setBorderWidth(0.5f)
                .setShadow(BorderOption.Shadow.MEDIUM);

        button.addActionListener(e -> {
            ModalDialog.showModal(this, new SimpleModalBorder(new SimpleInputForms(), "Input", SimpleModalBorder.YES_NO_OPTION, (controller, action) -> {
                System.out.println(action);
                if (action == SimpleModalBorder.YES_OPTION) {
                    controller.consume();
                    ModalDialog.pushModal(new SimpleModalBorder(new SimpleInputForms2(), "New Input", SimpleModalBorder.YES_NO_OPTION, (controller1, action1) -> {
                    }), "input");
                }
            }), "input");
        });
        add(button);
        LightDarkButton lightDarkButton = new LightDarkButton();
        lightDarkButton.installAutoLafChangeListener();
        add(lightDarkButton);
    }

    public static void main(String[] args) {
        installLaf();
        EventQueue.invokeLater(() -> new Test().setVisible(true));
    }
}
