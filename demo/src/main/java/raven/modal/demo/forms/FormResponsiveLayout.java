package raven.modal.demo.forms;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import raven.modal.demo.forms.other.Card;
import raven.modal.demo.layout.ResponsiveLayout;
import raven.modal.demo.model.ModelEmployee;
import raven.modal.demo.sample.SampleData;
import raven.modal.demo.system.Form;
import raven.modal.demo.utils.SystemForm;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.function.Consumer;

@SystemForm(name = "Responsive Layout", description = "responsive layout user interface", tags = {"card"})
public class FormResponsiveLayout extends Form {

    public FormResponsiveLayout() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("wrap,fill", "[fill]", "[grow 0][fill]"));
        add(createInfo());
        add(createOptions());
    }

    @Override
    public void formInit() {
        // add sample data
        panelCard.removeAll();
        for (ModelEmployee employee : SampleData.getSampleEmployeeData(true)) {
            panelCard.add(new Card(employee, createEventCard()));
        }
        panelCard.repaint();
        panelCard.revalidate();
    }

    private Consumer<ModelEmployee> createEventCard() {
        return e -> {
            JOptionPane.showMessageDialog(this, e.getProfile().getName());
        };
    }

    private JPanel createInfo() {
        JPanel panel = new JPanel(new MigLayout("fillx,wrap", "[fill]"));
        JLabel title = new JLabel("Responsive Layout");
        JTextPane text = new JTextPane();
        text.setText("A responsive layout in desktop applications ensures that\nthe user interface adapts seamlessly to different window sizes and screen resolutions.");
        text.setEditable(false);
        text.setBorder(BorderFactory.createEmptyBorder());
        title.putClientProperty(FlatClientProperties.STYLE, "" +
                "font:bold +3");

        panel.add(title);
        panel.add(text, "width 500");
        return panel;
    }

    private Component createOptions() {
        JPanel panel = new JPanel(new MigLayout("wrap 3,fill", "[grow 0,fill][grow 0,fill][fill]0", "[fill,grow 0][fill]"));
        panel.add(createLayoutOption());
        panel.add(createOtherOption());
        panel.add(createGapOption(), "gapx 0 7");
        panel.add(createExample(), "span 3,gapx 0 2");
        return panel;
    }

    private Component createLayoutOption() {
        JPanel panel = new JPanel(new MigLayout("wrap 2,width 300"));
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
        JPanel panel = new JPanel(new MigLayout("wrap 2,width 300"));
        panel.setBorder(new TitledBorder("Column and Item option"));

        JCheckBox chColumn = new JCheckBox("Set column");
        JCheckBox chWidth = new JCheckBox("Set item width");
        JCheckBox chHeight = new JCheckBox("Set item height");

        JSpinner spColumn = createSpinner(20, 1);
        JSpinner spWidth = createSpinner(1000, 300);
        JSpinner spHeight = createSpinner(1000, 150);

        JLabel lbColumnStatus = new JLabel("( auto wrap )");
        JLabel lbWidthStatus = new JLabel("( preferred size )");
        JLabel lbHeightStatus = new JLabel("( preferred size )");

        lbColumnStatus.putClientProperty(FlatClientProperties.STYLE, "" +
                "foreground:$Label.disabledForeground;");
        lbWidthStatus.putClientProperty(FlatClientProperties.STYLE, "" +
                "foreground:$Label.disabledForeground;");
        lbHeightStatus.putClientProperty(FlatClientProperties.STYLE, "" +
                "foreground:$Label.disabledForeground;");

        spColumn.setVisible(false);
        spWidth.setVisible(false);
        spHeight.setVisible(false);

        chColumn.addActionListener(e -> {
            if (chColumn.isSelected()) {
                responsiveLayout.setColumn(Integer.parseInt(spColumn.getValue().toString()));
                lbColumnStatus.setText("");
            } else {
                responsiveLayout.setColumn(-1);
                lbColumnStatus.setText("( auto wrap )");
            }
            spColumn.setVisible(chColumn.isSelected());
            panelCard.revalidate();
        });
        chWidth.addActionListener(e -> {
            if (chWidth.isSelected()) {
                lbWidthStatus.setText("");
                Dimension size = responsiveLayout.getSize();
                size.width = Integer.parseInt(spWidth.getValue().toString());
                responsiveLayout.setSize(size);
            } else {
                Dimension size = responsiveLayout.getSize();
                size.width = -1;
                responsiveLayout.setSize(size);
                lbWidthStatus.setText("( preferred size )");
            }
            spWidth.setVisible(chWidth.isSelected());
            panelCard.revalidate();
        });
        chHeight.addActionListener(e -> {
            if (chHeight.isSelected()) {
                lbHeightStatus.setText("");
                Dimension size = responsiveLayout.getSize();
                size.height = Integer.parseInt(spHeight.getValue().toString());
                responsiveLayout.setSize(size);
            } else {
                Dimension size = responsiveLayout.getSize();
                size.height = -1;
                responsiveLayout.setSize(size);
                lbHeightStatus.setText("( preferred size )");
            }
            spHeight.setVisible(chHeight.isSelected());
            panelCard.revalidate();
        });
        spColumn.addChangeListener(e -> {
            responsiveLayout.setColumn(Integer.parseInt(spColumn.getValue().toString()));
            panelCard.revalidate();
        });
        spWidth.addChangeListener(e -> {
            Dimension size = responsiveLayout.getSize();
            size.width = Integer.parseInt(spWidth.getValue().toString());
            responsiveLayout.setSize(size);
            panelCard.revalidate();
        });
        spHeight.addChangeListener(e -> {
            Dimension size = responsiveLayout.getSize();
            size.height = Integer.parseInt(spHeight.getValue().toString());
            responsiveLayout.setSize(size);
            panelCard.revalidate();
        });

        panel.add(chColumn);
        panel.add(lbColumnStatus, "split 2");
        panel.add(spColumn);

        panel.add(chWidth);
        panel.add(lbWidthStatus, "split 2");
        panel.add(spWidth);

        panel.add(chHeight);
        panel.add(lbHeightStatus, "split 2");
        panel.add(spHeight);

        return panel;
    }

    private Component createGapOption() {
        JPanel panel = new JPanel(new MigLayout("wrap 2"));
        panel.setBorder(new TitledBorder("Gap option"));

        JSpinner spHGap = createSpinner(500, 10);
        JSpinner spVGap = createSpinner(500, 10);

        spHGap.addChangeListener(e -> {
            responsiveLayout.setHorizontalGap(Integer.parseInt(spHGap.getValue().toString()));
            panelCard.revalidate();
        });
        spVGap.addChangeListener(e -> {
            responsiveLayout.setVerticalGap(Integer.parseInt(spVGap.getValue().toString()));
            panelCard.revalidate();
        });

        panel.add(new JLabel("Horizontal"));
        panel.add(spHGap);
        panel.add(new JLabel("Vertical"));
        panel.add(spVGap);

        return panel;
    }

    private JSpinner createSpinner(int max, int init) {
        JSpinner spinner = new JSpinner();
        spinner.setValue(init);
        SpinnerNumberModel numberModel = (SpinnerNumberModel) spinner.getModel();
        numberModel.setMinimum(1);
        numberModel.setMaximum(max);
        return spinner;
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
