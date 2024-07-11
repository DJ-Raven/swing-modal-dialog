package raven.modal.demo.forms;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import raven.modal.demo.forms.other.Card;
import raven.modal.demo.layout.ResponsiveLayout;
import raven.modal.demo.model.ModelEmployee;
import raven.modal.demo.sample.SampleData;
import raven.modal.demo.system.Form;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class FormResponsiveLayout extends Form {

    public FormResponsiveLayout() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("wrap,fillx,insets 7 15 7 15", "[fill]"));
        add(createInfo());
        add(createOptions());
    }

    @Override
    public void formInit() {
        // add sample data
        panelCard.removeAll();
        for (ModelEmployee employee : SampleData.getSampleEmployeeData(true)) {
            panelCard.add(new Card(employee));
        }
        panelCard.repaint();
        panelCard.revalidate();
    }

    private JPanel createInfo() {
        JPanel panel = new JPanel(new MigLayout("fillx,wrap", "[fill]"));
        JLabel title = new JLabel("Responsive Layout");
        JTextArea text = new JTextArea();
        text.setText("A responsive layout in desktop applications ensures that\nthe user interface adapts seamlessly to different window sizes and screen resolutions.");
        text.setEditable(false);
        text.setBorder(BorderFactory.createEmptyBorder());
        title.putClientProperty(FlatClientProperties.STYLE, "" +
                "font:bold +3");

        panel.add(title);
        panel.add(text);
        return panel;
    }

    private Component createOptions() {
        JPanel panel = new JPanel(new MigLayout("wrap 2,fillx", "[grow 0,fill][fill]0", "[fill]"));
        panel.add(createLayoutOption(), "width 300::");
        panel.add(createOtherOption(), ",gapx 0 7");
        panel.add(createExample(), "span 2,gapx 0 2");
        return panel;
    }

    private Component createLayoutOption() {
        JPanel panel = new JPanel(new MigLayout("wrap 2"));
        panel.setBorder(new TitledBorder("Justify content"));

        JRadioButton jrStart = new JRadioButton("Start");
        JRadioButton jrEnd = new JRadioButton("End");
        JRadioButton jrCenter = new JRadioButton("Center");
        JRadioButton jrSpaceBetween = new JRadioButton("Space between");
        JRadioButton jrSpaceAround = new JRadioButton("Space around");
        JRadioButton jrSpaceEvenly = new JRadioButton("Space evenly");
        JRadioButton jrFitContent = new JRadioButton("Fit content");

        ButtonGroup group = new ButtonGroup();
        group.add(jrStart);
        group.add(jrEnd);
        group.add(jrCenter);
        group.add(jrSpaceBetween);
        group.add(jrSpaceAround);
        group.add(jrSpaceEvenly);
        group.add(jrFitContent);

        jrFitContent.setSelected(true);

        applyEventJustifyContent(jrStart, ResponsiveLayout.JustifyContent.START);
        applyEventJustifyContent(jrEnd, ResponsiveLayout.JustifyContent.END);
        applyEventJustifyContent(jrCenter, ResponsiveLayout.JustifyContent.CENTER);
        applyEventJustifyContent(jrSpaceBetween, ResponsiveLayout.JustifyContent.SPACE_BETWEEN);
        applyEventJustifyContent(jrSpaceAround, ResponsiveLayout.JustifyContent.SPACE_AROUND);
        applyEventJustifyContent(jrSpaceEvenly, ResponsiveLayout.JustifyContent.SPACE_EVENLY);
        applyEventJustifyContent(jrFitContent, ResponsiveLayout.JustifyContent.FIT_CONTENT);

        panel.add(jrStart);
        panel.add(jrEnd);
        panel.add(jrCenter);
        panel.add(jrSpaceBetween);
        panel.add(jrSpaceAround);
        panel.add(jrSpaceEvenly);
        panel.add(jrFitContent);

        return panel;
    }

    private void applyEventJustifyContent(JRadioButton jr, ResponsiveLayout.JustifyContent type) {
        jr.addActionListener(e -> {
            if (jr.isSelected()) {
                responsiveLayout.setJustifyContent(type);
                panelCard.revalidate();
            }
        });
    }

    private Component createOtherOption() {
        JPanel panel = new JPanel(new MigLayout("wrap"));
        panel.setBorder(new TitledBorder("Other option"));

        return panel;
    }

    private Component createExample() {
        responsiveLayout = new ResponsiveLayout(ResponsiveLayout.JustifyContent.FIT_CONTENT, new Dimension(-1, -1), 10, 10);
        panelCard = new JPanel(responsiveLayout);
        panelCard.putClientProperty(FlatClientProperties.STYLE, "" +
                "border:10,10,10,10;");
        JScrollPane scrollPane = new JScrollPane(panelCard);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(10);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        scrollPane.getHorizontalScrollBar().putClientProperty(FlatClientProperties.STYLE, "" +
                "trackArc:$ScrollBar.thumbArc;" +
                "thumbInsets:0,0,0,0;" +
                "width:5;");
        scrollPane.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE, "" +
                "trackArc:$ScrollBar.thumbArc;" +
                "thumbInsets:0,0,0,0;" +
                "width:5;");
        scrollPane.setBorder(new TitledBorder("Example"));
        JSplitPane splitPane = new JSplitPane();
        splitPane.setLeftComponent(scrollPane);
        splitPane.setRightComponent(Box.createGlue());
        splitPane.setResizeWeight(1);
        return splitPane;
    }

    private JPanel panelCard;
    private ResponsiveLayout responsiveLayout;
}
