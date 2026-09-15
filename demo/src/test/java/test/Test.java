package test;

import net.miginfocom.swing.MigLayout;
import raven.extras.LightDarkButton;
import raven.modal.ModalDialog;
import raven.modal.component.Modal;
import raven.modal.component.SimpleModalBorder;
import raven.modal.demo.simple.SimpleInputForms;
import raven.modal.demo.simple.SimpleInputForms2;
import raven.modal.option.BorderOption;
import raven.modal.option.Option;
import test.base.BaseFrame;
import test.docs.MyModal;

import javax.swing.*;
import java.awt.*;

public class Test extends BaseFrame {

    public Test() {
        super("Test");
        setLayout(new MigLayout("wrap,al center center"));
        JButton button = new JButton("show");
        Option option = ModalDialog.createOption();
        option.setHeavyWeight(true)
                .setOpacity(0.4f)
                .setBackgroundClickType(Option.BackgroundClickType.BLOCK);
        option.getBorderOption().setShadow(BorderOption.Shadow.MEDIUM).setBorderWidth(1);
        option.getLayoutOption().setMovable(true).setRelativeToOwner(true);


        button.addActionListener(e -> {
            ModalDialog.showModal(this,
                    new SimpleModalBorder(new JButton("Hello3333333333333333333333333333333333333333333333333"), "Sample Input Forms", SimpleModalBorder.YES_NO_CANCEL_OPTION,
                            (controller, action) -> {
                                if (action == SimpleModalBorder.YES_OPTION) {
                                    // handle confirm
                                }
                            }));

        });


        add(button);
    }

    public static void main(String[] args) {
        installLaf();
        EventQueue.invokeLater(() -> new Test().setVisible(true));
    }
}
