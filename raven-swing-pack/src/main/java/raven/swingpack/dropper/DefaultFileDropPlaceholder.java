package raven.swingpack.dropper;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

/**
 * @author Raven
 */
public class DefaultFileDropPlaceholder extends JButton {

    private Icon dropIcon;
    private String title;
    private String description;

    public DefaultFileDropPlaceholder() {
        this("Choose a file or drag & drop it here.", "Support for a single or multiple files. JPEG, PNG, PDF, and Other file formats.");
    }

    public DefaultFileDropPlaceholder(String title, String description) {
        setLayout(new MigLayout("hidemode 3,insets 10 n 10 n,wrap,al center", "[center]"));
        putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]background:shade($Panel.background,5%);" +
                "[dark]background:tint($Panel.background,5%);" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0;");

        lbIcon = new JLabel();
        lbTitle = new JLabel();
        lbDescription = new JLabel();

        lbTitle.putClientProperty(FlatClientProperties.STYLE, "" +
                "font:+1;");
        lbDescription.putClientProperty(FlatClientProperties.STYLE, "" +
                "foreground:$Label.disabledForeground;");

        add(lbIcon);
        add(lbTitle);
        add(lbDescription);

        setDropIcon(createDefaultIcon());
        setTitle(title);
        setDescription(description);
    }

    public void setDropIcon(Icon icon) {
        this.dropIcon = icon;
        lbIcon.setVisible(icon != null);
        lbIcon.setIcon(icon);
    }

    public Icon getDropIcon() {
        return dropIcon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        if (title == null || title.isEmpty()) {
            lbTitle.setText("");
            lbTitle.setVisible(false);
        } else {
            lbTitle.setText(title);
            lbTitle.setVisible(true);
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        if (description == null || description.isEmpty()) {
            lbDescription.setText("");
            lbDescription.setVisible(false);
        } else {
            lbDescription.setText(description);
            lbDescription.setVisible(true);
        }
    }

    protected Icon createDefaultIcon() {
        return new FlatSVGIcon("raven/swingpack/icons/upload.svg", 0.6f)
                .setColorFilter(new FlatSVGIcon.ColorFilter(color -> UIManager.getColor("Component.accentColor")));
    }

    private JLabel lbIcon;
    private JLabel lbTitle;
    private JLabel lbDescription;
}
