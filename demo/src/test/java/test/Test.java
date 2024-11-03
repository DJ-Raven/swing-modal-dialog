package test;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import net.miginfocom.swing.MigLayout;
import raven.extras.LightDarkButton;
import raven.modal.ModalDialog;
import raven.modal.component.SimpleModalBorder;
import raven.modal.demo.forms.FormResponsiveLayout;
import raven.modal.demo.simple.SimpleInputForms;
import raven.modal.demo.simple.SimpleInputForms2;
import raven.modal.demo.system.Form;
import raven.modal.option.BorderOption;
import raven.modal.option.Option;

import javax.swing.*;
import java.awt.*;

public class Test extends JFrame {

    public Test() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getRootPane().putClientProperty(FlatClientProperties.FULL_WINDOW_CONTENT, true);
        setSize(new Dimension(800, 800));
        setLocationRelativeTo(null);
        setLayout(new MigLayout("wrap,al center center,fillx"));
        JButton button = new JButton("show");
        ModalDialog.getDefaultOption()
                .setBackgroundBlur(Option.BackgroundBlur.SMALL)
                .setOpacity(0.1f)
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
        add(button, "split 2");
        LightDarkButton lightDarkButton = new LightDarkButton();
        lightDarkButton.installAutoLafChangeListener();
        add(lightDarkButton);

        Form testForm = new FormResponsiveLayout();
        testForm.formInit();
        add(testForm, "grow");
    }

    public static void main(String[] args) {
        FlatRobotoFont.install();
        FlatMacLightLaf.setup();
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        EventQueue.invokeLater(() -> new Test().setVisible(true));
    }
}
