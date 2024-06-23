package raven.modal.drawer.simple.header;

import javax.swing.*;

/**
 * @author Raven
 */
public class SimpleHeaderData {

    public Icon getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public SimpleHeaderStyle getSimpleHeaderStyle() {
        return simpleHeaderStyle;
    }

    protected Icon icon;
    protected String title;
    protected String description;

    protected SimpleHeaderStyle simpleHeaderStyle;

    public SimpleHeaderData setIcon(Icon icon) {
        this.icon = icon;
        return this;
    }

    public SimpleHeaderData setTitle(String title) {
        this.title = title;
        return this;
    }

    public SimpleHeaderData setDescription(String description) {
        this.description = description;
        return this;
    }

    public SimpleHeaderData setHeaderStyle(SimpleHeaderStyle simpleHeaderStyle) {
        this.simpleHeaderStyle = simpleHeaderStyle;
        return this;
    }
}
