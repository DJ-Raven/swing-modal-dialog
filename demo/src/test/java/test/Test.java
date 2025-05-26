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
                .setBorderWidth(1)
                .setShadow(BorderOption.Shadow.MEDIUM);


        button.addActionListener(e -> {
            SimpleInputForms simpleInputForms = new SimpleInputForms();
            ModalDialog.showModal(this, new SimpleModalBorder(simpleInputForms, "Input", SimpleModalBorder.YES_NO_OPTION, (controller, action) -> {
                System.out.println("Action: " + action);
                if (action == SimpleModalBorder.OPENED) {
                    simpleInputForms.formOpen();
                    System.out.println("Form-1 OPENED");
                } else if (action == SimpleModalBorder.YES_OPTION) {
                    controller.consume();
                    SimpleInputForms2 simpleInputForms2 = new SimpleInputForms2();
                    ModalDialog.pushModal(new SimpleModalBorder(simpleInputForms2, "New Input", SimpleModalBorder.YES_NO_OPTION, (controller1, action1) -> {
                        if (action1 == SimpleModalBorder.OPENED) {
                            simpleInputForms2.formOpen();
                            System.out.println("Form-2 OPENED");
                        }
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
