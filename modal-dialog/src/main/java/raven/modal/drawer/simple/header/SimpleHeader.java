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
        layout = new MigLayout("hidemode 3,wrap 2,insets 15 15 5 15,gap 10");
        setLayout(layout);
        profile = new JLabel(simpleHeaderData.getIcon());
        labelTitle = new JLabel(simpleHeaderData.getTitle());
        labelDescription = new JLabel(simpleHeaderData.getDescription());
        int textGap = 3;

        if (simpleHeaderData.getSimpleHeaderStyle() != null) {
            simpleHeaderData.getSimpleHeaderStyle().styleComponent(profile, PROFILE_STYLE);
            simpleHeaderData.getSimpleHeaderStyle().styleComponent(labelTitle, LABEL_TITLE_STYLE);
            simpleHeaderData.getSimpleHeaderStyle().styleComponent(labelDescription, LABEL_DESCRIPTION_STYLE);
            textGap = simpleHeaderData.getSimpleHeaderStyle().getTextGap();
        }

        FlatLafStyleUtils.appendStyleIfAbsent(profile, "" +
                "background:$Component.borderColor");
        FlatLafStyleUtils.appendStyleIfAbsent(labelTitle, "" +
                "font:bold +1;");
        FlatLafStyleUtils.appendStyleIfAbsent(labelDescription, "" +
                "foreground:$Label.disabledForeground;");

        panel = new JPanel(new MigLayout("insets 0,wrap,al n center,gap " + textGap));
        panel.setOpaque(false);

        add(profile);
        panel.add(labelTitle);
        panel.add(labelDescription);
        add(panel);
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
        if (labelTitle == null || labelDescription == null) return;

        if (menuOpenMode == MenuOption.MenuOpenMode.FULL) {
            layout.setColumnConstraints(null);
            panel.setVisible(true);
        } else {
            layout.setColumnConstraints("[center]");
            panel.setVisible(false);
        }
    }

    protected JLabel profile;
    protected JLabel labelTitle;
    protected JLabel labelDescription;
    protected JPanel panel;
}
