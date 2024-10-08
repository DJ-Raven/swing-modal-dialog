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

    public static final int PROFILE_STYLE = 0;
    public static final int LABEL_TITLE_STYLE = 1;
    public static final int LABEL_DESCRIPTION_STYLE = 2;

    private SimpleHeaderData simpleHeaderData;
    protected MigLayout layout;

    public SimpleHeader(SimpleHeaderData simpleHeaderData) {
        this.simpleHeaderData = simpleHeaderData;
        initDefault();
        initComponent();
    }

    protected void initDefault() {
        if (simpleHeaderData.getSimpleHeaderStyle() != null) {
            simpleHeaderData.getSimpleHeaderStyle().styleHeader(this);
        }
        FlatLafStyleUtils.appendStyleIfAbsent(this, "" +
                "background:null");

    }

    protected void initComponent() {
        layout = new MigLayout("hidemode 3,wrap,insets 15 20 5 20,fill,gap 3");
        setLayout(layout);
        profile = new JLabel(simpleHeaderData.getIcon());
        labelTitle = new JLabel(simpleHeaderData.getTitle());
        labelDescription = new JLabel(simpleHeaderData.getDescription());

        if (simpleHeaderData.getSimpleHeaderStyle() != null) {
            simpleHeaderData.getSimpleHeaderStyle().styleComponent(profile, PROFILE_STYLE);
            simpleHeaderData.getSimpleHeaderStyle().styleComponent(labelTitle, LABEL_TITLE_STYLE);
            simpleHeaderData.getSimpleHeaderStyle().styleComponent(labelDescription, LABEL_DESCRIPTION_STYLE);
        }

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

        if (profile != null)
            profile.setIcon(simpleHeaderData.getIcon());
        if (labelTitle != null)
            labelTitle.setText(simpleHeaderData.getTitle());
        if (labelDescription != null)
            labelDescription.setText(simpleHeaderData.getDescription());
    }

    @Override
    protected void layoutOptionChanged(MenuOption.MenuOpenMode menuOpenMode) {
        if (labelTitle == null || labelTitle == null) return;

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

    protected JLabel profile;
    protected JLabel labelTitle;
    protected JLabel labelDescription;
}
