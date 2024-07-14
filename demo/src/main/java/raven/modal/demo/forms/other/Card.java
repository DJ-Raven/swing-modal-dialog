package raven.modal.demo.forms.other;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import raven.extras.AvatarIcon;
import raven.modal.demo.model.ModelEmployee;

import javax.swing.*;
import java.util.function.Consumer;

public class Card extends JPanel {

    private final ModelEmployee employee;
    private final Consumer<ModelEmployee> event;

    public Card(ModelEmployee employee, Consumer<ModelEmployee> event) {
        this.employee = employee;
        this.event = event;
        init();
    }

    private void init() {
        putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:30;" +
                "[light]background:darken($Panel.background,3%);" +
                "[dark]background:lighten($Panel.background,3%);");

        setLayout(new MigLayout("", "", "fill"));
        // create panel header
        panelHeader = createHeader();

        // create panel body
        panelBody = createBody();

        add(panelHeader);
        add(panelBody);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new MigLayout("fill,insets 0", "[fill]", "[top]"));
        header.putClientProperty(FlatClientProperties.STYLE, "" +
                "background:null");
        JLabel label = new JLabel(new AvatarIcon(employee.getProfile().getIcon(), 130, 130, 20));
        header.add(label);
        return header;
    }

    private JPanel createBody() {
        JPanel body = new JPanel(new MigLayout("wrap", "[150]", "[][]push[]"));
        body.putClientProperty(FlatClientProperties.STYLE, "" +
                "background:null");
        JLabel title = new JLabel(employee.getProfile().getName());
        title.putClientProperty(FlatClientProperties.STYLE, "" +
                "font:bold +1;");
        JTextPane description = new JTextPane();
        description.setEditable(false);
        description.putClientProperty(FlatClientProperties.STYLE, "" +
                "border:0,0,0,0;" +
                "background:null;" +
                "[light]foreground:tint($Label.foreground,30%);" +
                "[dark]foreground:shade($Label.foreground,30%)");
        description.setText(employee.getDescription());

        JButton button = new JButton("View");
        button.addActionListener(e -> event.accept(employee));
        button.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:999;" +
                "margin:3,25,3,25;" +
                "borderWidth:1;" +
                "focusWidth:0;" +
                "innerFocusWidth:0;" +
                "background:null;");

        body.add(title);
        body.add(description);
        body.add(button);
        return body;
    }

    private JPanel panelHeader;
    private JPanel panelBody;
}
