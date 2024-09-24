package test;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.modal.component.SimpleModalBorder;
import raven.modal.demo.simple.SimpleInputForms;
import raven.modal.option.Option;

import javax.swing.*;
import java.awt.*;

public class Test extends JFrame {

    public Test() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
        setLayout(new MigLayout("al center center"));
        JButton button = new JButton("show");
        button.addActionListener(e -> {
            ModalDialog.showModal(this, new SimpleModalBorder(new SimpleInputForms(), "Input", SimpleModalBorder.YES_NO_OPTION, (controller, action) -> {
                //  controller.consume();
                System.out.println(action);
            }), new Option().setBorderWidth(1), "input");
        });
        add(button);
    }

    public static void main(String[] args) {
        FlatRobotoFont.install();
        FlatDarculaLaf.setup();
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        EventQueue.invokeLater(() -> new Test().setVisible(true));
    }
}
