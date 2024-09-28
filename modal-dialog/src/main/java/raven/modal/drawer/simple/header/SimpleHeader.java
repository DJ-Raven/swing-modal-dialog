package raven.modal.drawer.simple.header;

import net.miginfocom.swing.MigLayout;
import raven.modal.drawer.menu.AbstractMenuElement;
import raven.modal.drawer.menu.MenuOption;
import raven.modal.utils.FlatLafStyleUtils;

import javax.swing.*;

/**
 * @author Raven
 */
public class SimpleHeader extends AbstractMenuElement {

    private SimpleHeaderData simpleHeaderData;

    public SimpleHeader(SimpleHeaderData simpleHeaderData) {
        this.simpleHeaderData = simpleHeaderData;
        init();
    }

    private void init() {
        setLayout(new MigLayout("hidemode 3,wrap,insets 15 20 5 20,fill,gap 3"));
        profile = new JLabel(simpleHeaderData.getIcon());
        labelTitle = new JLabel(simpleHeaderData.getTitle());
        labelDescription = new JLabel(simpleHeaderData.getDescription());

        if (simpleHeaderData.getSimpleHeaderStyle() != null) {
            simpleHeaderData.getSimpleHeaderStyle().styleHeader(this);
            simpleHeaderData.getSimpleHeaderStyle().styleProfile(profile);
            simpleHeaderData.getSimpleHeaderStyle().styleTitle(labelTitle);
            simpleHeaderData.getSimpleHeaderStyle().styleDescription(labelDescription);
        }
        FlatLafStyleUtils.appendStyleIfAbsent(this, "" +
                "background:null");
        FlatLafStyleUtils.appendStyleIfAbsent(profile, "" +
                "background:$Component.borderColor");
        FlatLafStyleUtils.appendStyleIfAbsent(labelDescription, "" +
                "font:-1;" +
                "foreground:$Label.disabledForeground;");
        add(profile);
        add(labelTitle);
        add(labelDescription);
    }

    public SimpleHeaderData getSimpleHeaderData() {
        return simpleHeaderData;
    }

    public void setSimpleHeaderData(SimpleHeaderData simpleHeaderData) {
        this.simpleHeaderData = simpleHeaderData;
        profile.setIcon(simpleHeaderData.getIcon());
        labelTitle.setText(simpleHeaderData.getTitle());
        labelDescription.setText(simpleHeaderData.getDescription());
    }

    @Override
    protected void layoutOptionChanged(MenuOption.MenuOpenMode menuOpenMode) {
        if (menuOpenMode == MenuOption.MenuOpenMode.FULL) {
            setLayout(new MigLayout("hidemode 3,wrap,insets 15 20 5 20,fill,gap 3"));
            labelTitle.setVisible(true);
            labelDescription.setVisible(true);
        } else {
            setLayout(new MigLayout("hidemode 3,wrap,insets 15 5 5 5,fill,gap 3", "[center]"));
            labelTitle.setVisible(false);
            labelDescription.setVisible(false);
        }
    }

    private JLabel profile;
    private JLabel labelTitle;
    private JLabel labelDescription;
}
