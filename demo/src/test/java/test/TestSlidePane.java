package test;

import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import net.miginfocom.swing.MigLayout;
import raven.extras.SlidePane;
import raven.extras.SlidePaneTransition;
import raven.modal.demo.simple.SimpleInputForms;
import raven.modal.demo.simple.SimpleInputForms2;

import javax.swing.*;
import java.awt.*;

public class TestSlidePane extends JFrame {

    private int state = 1;

    public TestSlidePane() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(800, 800));
        setLocationRelativeTo(null);
        setLayout(new MigLayout("wrap,al center", "[center]"));
        JButton button = new JButton("slide");

        SlidePane slidePane = new SlidePane((container, component) -> component.getPreferredSize());
        slidePane.addSlide(new SimpleInputForms());
        slidePane.setOpaque(true);

        button.addActionListener(e -> {
            Component com = state % 2 == 0 ? new SimpleInputForms() : new SimpleInputForms2();
            state++;
            slidePane.addSlide(com, SlidePaneTransition.Type.FORWARD);
        });

        add(slidePane);
        add(button);
    }

    public static void main(String[] args) {
        FlatRobotoFont.install();
        FlatMacLightLaf.setup();
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        EventQueue.invokeLater(() -> new TestSlidePane().setVisible(true));
    }
}
