package test;

import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import net.miginfocom.swing.MigLayout;
import raven.extras.LightDarkButton;
import raven.modal.ModalDialog;
import raven.modal.component.SimpleModalBorder;
import raven.modal.demo.simple.SimpleInputForms;
import raven.modal.demo.simple.SimpleInputForms2;

import javax.swing.*;
import java.awt.*;

public class Test extends JFrame {

    public Test() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(800, 800));
        setLocationRelativeTo(null);
        setLayout(new MigLayout("wrap,al center center"));
        JButton button = new JButton("show");
        ModalDialog.getDefaultOption()
                .setBorderWidth(1f);

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
        FlatRobotoFont.install();
        FlatMacLightLaf.setup();
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        EventQueue.invokeLater(() -> new Test().setVisible(true));
    }
}
