package raven.modal.demo.forms;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import raven.extras.SlidePane;
import raven.extras.SlidePaneTransition;
import raven.modal.demo.component.LabelButton;
import raven.modal.demo.simple.SimpleInputForms;
import raven.modal.demo.simple.SimpleInputForms2;
import raven.modal.demo.system.Form;
import raven.modal.demo.utils.SystemForm;
import raven.modal.slider.PanelSlider;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

@SystemForm(name = "Slide Pane", description = "slide pane component transitions with animated sliding", tags = {"slider"})
public class FormSlidePane extends Form {

    public FormSlidePane() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("wrap,fillx", "[fill]"));
        add(createInfo());
        add(createOptions());
    }

    private JPanel createInfo() {
        JPanel panel = new JPanel(new MigLayout("fillx,wrap", "[fill]"));
        JLabel title = new JLabel("Slide Pane");
        JTextPane text = new JTextPane();
        text.setText("SlidePane is a custom Java Swing component that enables smooth transitions between panels with animated sliding effects.");
        text.setEditable(false);
        text.setBorder(BorderFactory.createEmptyBorder());
        title.putClientProperty(FlatClientProperties.STYLE, "" +
                "font:bold +3");

        panel.add(title);
        panel.add(text, "width 500");
        return panel;
    }

    private Component createOptions() {
        JPanel panel = new JPanel(new MigLayout("wrap 2,fillx", "[grow 0,fill][fill]", "[fill]"));
        panel.add(createTransitionOption());
        panel.add(createOtherOption());
        panel.add(createTestButton(), "span 2");
        panel.add(createExample(), "span 2");
        return panel;
    }

    private Component createTransitionOption() {
        JPanel panel = new JPanel(new MigLayout("wrap 4,insets n n n 30"));
        panel.setBorder(new TitledBorder("Transition type"));
        jrDefault = new JRadioButton("Default");
        jrBack = new JRadioButton("Back");
        jrForward = new JRadioButton("Forward", true);
        jrZoomIn = new JRadioButton("Zoom in");
        jrZoomOut = new JRadioButton("Zoom out");
        jrTopDown = new JRadioButton("Top down");
        jrDownTop = new JRadioButton("Down top");

        ButtonGroup group = new ButtonGroup();
        group.add(jrDefault);
        group.add(jrBack);
        group.add(jrForward);
        group.add(jrZoomIn);
        group.add(jrZoomOut);
        group.add(jrTopDown);
        group.add(jrDownTop);

        panel.add(jrDefault);
        panel.add(jrBack);
        panel.add(jrForward);
        panel.add(jrZoomIn);
        panel.add(jrZoomOut);
        panel.add(jrTopDown);
        panel.add(jrDownTop);

        return panel;
    }

    private Component createOtherOption() {
        JPanel panel = new JPanel(new MigLayout());
        panel.setBorder(new TitledBorder("Other option"));
        jrContainerSize = new JRadioButton("Container size");
        jrComponentPreferredSize = new JRadioButton("Component preferred size", true);

        ButtonGroup group = new ButtonGroup();
        group.add(jrContainerSize);
        group.add(jrComponentPreferredSize);

        panel.add(jrContainerSize);
        panel.add(jrComponentPreferredSize);
        return panel;
    }

    private Component createTestButton() {
        JPanel panel = new JPanel(new MigLayout());
        panel.setBorder(new TitledBorder("Show slide"));
        LabelButton lbTest1 = new LabelButton("Show slide-1");
        LabelButton lbTest2 = new LabelButton("Show slide-2");

        lbTest1.addOnClick(o -> {
            slidePane.addSlide(new SimpleInputForms(), getTransition());
        });
        lbTest2.addOnClick(o -> {
            slidePane.addSlide(new SimpleInputForms2(), getTransition());
        });

        panel.add(lbTest1);
        panel.add(lbTest2);
        return panel;
    }

    private Component createExample() {
        JPanel panel = new JPanel(new MigLayout("wrap"));
        panel.setBorder(new TitledBorder("Example"));
        // create slide layout size
        PanelSlider.PaneSliderLayoutSize layoutSize = (container, component) -> {
            if (jrComponentPreferredSize.isSelected()) {
                // when slide use the component preferred size.
                return component.getPreferredSize();
            } else {
                // when slide use the container slide pane as the size.
                return container.getSize();
            }
        };
        slidePane = new SlidePane(layoutSize);
        slidePane.addSlide(new SimpleInputForms());
        panel.add(slidePane);
        return panel;
    }

    private SlidePaneTransition.Type getTransition() {
        if (jrBack.isSelected()) {
            return SlidePaneTransition.Type.BACK;
        } else if (jrForward.isSelected()) {
            return SlidePaneTransition.Type.FORWARD;
        } else if (jrZoomIn.isSelected()) {
            return SlidePaneTransition.Type.ZOOM_IN;
        } else if (jrZoomOut.isSelected()) {
            return SlidePaneTransition.Type.ZOOM_OUT;
        } else if (jrTopDown.isSelected()) {
            return SlidePaneTransition.Type.TOP_DOWN;
        } else if (jrDownTop.isSelected()) {
            return SlidePaneTransition.Type.DOWN_TOP;
        }
        return SlidePaneTransition.Type.DEFAULT;
    }

    private SlidePane slidePane;

    // transition option
    private JRadioButton jrDefault;
    private JRadioButton jrBack;
    private JRadioButton jrForward;
    private JRadioButton jrZoomIn;
    private JRadioButton jrZoomOut;
    private JRadioButton jrTopDown;
    private JRadioButton jrDownTop;

    // other option
    private JRadioButton jrContainerSize;
    private JRadioButton jrComponentPreferredSize;

}
