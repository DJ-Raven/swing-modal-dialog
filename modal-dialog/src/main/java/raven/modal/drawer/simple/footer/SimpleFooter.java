package raven.modal.drawer.simple.footer;

import net.miginfocom.swing.MigLayout;
import raven.modal.utils.FlatLafStyleUtils;

import javax.swing.*;

/**
 * @author Raven
 */
public class SimpleFooter extends JPanel {

    private SimpleFooterData simpleFooterData;

    public SimpleFooter(SimpleFooterData simpleFooterData) {
        this.simpleFooterData = simpleFooterData;
        init();
    }

    private void init() {
        setLayout(new MigLayout("wrap,insets 5 20 10 20,fill,gap 3"));
        labelTitle = new JLabel(simpleFooterData.getTitle());
        labelDescription = new JLabel(simpleFooterData.getDescription());

        if (simpleFooterData.getSimpleFooterStyle() != null) {
            simpleFooterData.getSimpleFooterStyle().styleFooter(this);
            simpleFooterData.getSimpleFooterStyle().styleTitle(labelTitle);
            simpleFooterData.getSimpleFooterStyle().styleDescription(labelDescription);
        }

        FlatLafStyleUtils.appendStyleIfAbsent(this, "" +
                "background:null");
        FlatLafStyleUtils.appendStyleIfAbsent(labelDescription, "" +
                "font:-1;" +
                "foreground:$Label.disabledForeground;");

        add(labelTitle);
        add(labelDescription);
    }

    public SimpleFooterData getSimpleFooterData() {
        return simpleFooterData;
    }

    public void setSimpleFooterData(SimpleFooterData simpleFooterData) {
        this.simpleFooterData = simpleFooterData;
        labelTitle.setText(simpleFooterData.getTitle());
        labelDescription.setText(simpleFooterData.getDescription());
    }

    private JLabel labelTitle;
    private JLabel labelDescription;
}