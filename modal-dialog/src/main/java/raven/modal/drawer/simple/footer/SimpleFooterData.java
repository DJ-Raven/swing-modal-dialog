package raven.modal.drawer.simple.footer;

/**
 * @author Raven
 */
public class SimpleFooterData {

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public SimpleFooterStyle getSimpleFooterStyle() {
        return simpleFooterStyle;
    }

    protected String title;
    protected String description;
    protected SimpleFooterStyle simpleFooterStyle;

    public SimpleFooterData setTitle(String title) {
        this.title = title;
        return this;
    }

    public SimpleFooterData setDescription(String description) {
        this.description = description;
        return this;
    }

    public SimpleFooterData setFooterStyle(SimpleFooterStyle simpleFooterStyle) {
        this.simpleFooterStyle = simpleFooterStyle;
        return this;
    }
}
