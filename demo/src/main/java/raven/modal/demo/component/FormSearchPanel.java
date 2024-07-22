package raven.modal.demo.component;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.icons.FlatMenuArrowIcon;
import net.miginfocom.swing.MigLayout;
import raven.modal.demo.layout.ResponsiveLayout;

import javax.swing.*;
import java.awt.*;

public class FormSearchPanel extends JPanel {

    public FormSearchPanel() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("fillx,insets 0,wrap", "[fill,500]"));
        textSearch = new JTextField();
        panelResult = new JPanel(new ResponsiveLayout(ResponsiveLayout.JustifyContent.FIT_CONTENT, new Dimension(-1, -1), 10, 0, 1));
        panelResult.putClientProperty(FlatClientProperties.STYLE, "" +
                "border:0,10,0,10;");
        textSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search...");
        textSearch.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, new FlatSVGIcon("raven/modal/demo/icons/search.svg", 0.4f));
        textSearch.putClientProperty(FlatClientProperties.STYLE, "" +
                "border:3,20,3,20;" +
                "background:null;");
        add(textSearch);
        add(new JSeparator(), "height 2");
        JScrollPane scrollPane = new JScrollPane(panelResult);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        panelResult.add(new Item("Test 1"));
        panelResult.add(new Item("Test 2"));
        panelResult.add(new Item("Test 3"));
        panelResult.add(new Item("Test 4"));

        add(scrollPane);
    }

    private JTextField textSearch;
    private JPanel panelResult;

    private static class Item extends JButton {

        private String text;

        public Item(String text) {
            this.text = text;
            init();
        }

        private void init() {
            setHorizontalAlignment(JButton.LEADING);
            setLayout(new MigLayout("insets 3 3 3 0,filly,gapy 2", "[]push[]"));
            putClientProperty(FlatClientProperties.STYLE, "" +
                    "background:null;" +
                    "arc:10;" +
                    "borderWidth:0;" +
                    "focusWidth:0;" +
                    "innerFocusWidth:0;");
            JLabel labelDescription = new JLabel("description");
            labelDescription.putClientProperty(FlatClientProperties.STYLE, "" +
                    "foreground:$Label.disabledForeground;");
            add(new JLabel(text), "cell 0 0");
            add(labelDescription, "cell 0 1");
            add(new JLabel(new FlatMenuArrowIcon()), "cell 1 0,span 1 2");
        }
    }
}
