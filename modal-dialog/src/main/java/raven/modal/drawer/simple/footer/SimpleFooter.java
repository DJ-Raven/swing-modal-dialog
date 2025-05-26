package raven.modal.drawer.simple.footer;

import net.miginfocom.swing.MigLayout;
import raven.modal.drawer.menu.AbstractMenuElement;
import raven.modal.drawer.menu.MenuOption;
import raven.modal.utils.FlatLafStyleUtils;

import javax.swing.*;

/**
 * @author Raven
 */
public class SimpleFooter extends AbstractMenuElement {

    public static final int LABEL_TITLE_STYLE = 0;
    public static final int LABEL_DESCRIPTION_STYLE = 1;

    private SimpleFooterData simpleFooterData;

    protected MigLayout layout;

    public SimpleFooter(SimpleFooterData simpleFooterData) {
        this.simpleFooterData = simpleFooterData;
        initDefault();
        initComponent();
    }

    protected void initDefault() {
        if (simpleFooterData.getSimpleFooterStyle() != null) {
            simpleFooterData.getSimpleFooterStyle().styleFooter(this);
        }

        FlatLafStyleUtils.appendStyleIfAbsent(this, "" +
                "background:null;");
    }

    protected void initComponent() {
        labelTitle = new JLabel(simpleFooterData.getTitle());
        labelDescription = new JLabel(simpleFooterData.getDescription());
        int gap = 3;
        if (simpleFooterData.getSimpleFooterStyle() != null) {
            simpleFooterData.getSimpleFooterStyle().styleComponent(labelTitle, LABEL_TITLE_STYLE);
            simpleFooterData.getSimpleFooterStyle().styleComponent(labelDescription, LABEL_DESCRIPTION_STYLE);
            gap = simpleFooterData.getSimpleFooterStyle().getTextGap();
        }

        layout = new MigLayout("hidemode 3,wrap,insets 5 20 10 20,fill,gap " + gap);
        setLayout(layout);
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

        if (labelTitle != null)
            labelTitle.setText(simpleFooterData.getTitle());
        if (labelDescription != null)
            labelDescription.setText(simpleFooterData.getDescription());
    }

    @Override
    protected void layoutOptionChanged(MenuOption.MenuOpenMode menuOpenMode) {
        if (labelTitle == null || labelDescription == null) return;

        if (menuOpenMode == MenuOption.MenuOpenMode.FULL) {
            labelTitle.setVisible(true);
            labelDescription.setVisible(true);
        } else {
            labelTitle.setVisible(false);
            labelDescription.setVisible(false);
        }
    }

    protected JLabel labelTitle;
    protected JLabel labelDescription;
}