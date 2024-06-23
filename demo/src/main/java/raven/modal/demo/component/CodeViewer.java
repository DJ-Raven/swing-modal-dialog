package raven.modal.demo.component;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;

public class CodeViewer extends JPanel {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
        textArea.setText(code);
    }

    private String code = "";

    public CodeViewer(String code) {
        this.code = code;
        init(false);
    }

    public CodeViewer(String code, boolean useScroll) {
        this.code = code;
        init(useScroll);
    }

    private void init(boolean useScroll) {
        setLayout(new BorderLayout());
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(new MigLayout("fill", "[fill]"));
        textArea = new RSyntaxTextArea(code);
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        textArea.setEditable(false);
        textArea.setPopupMenu(null);
        textArea.setHighlightCurrentLine(false);
        putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:20;" +
                "border:3,3,3,3;" +
                "background:$TextArea.background;");
        JButton buttonCopy = new JButton(new FlatSVGIcon("raven/modal/demo/icons/copy.svg", 0.4f));
        buttonCopy.addActionListener(e -> copy());
        buttonCopy.setToolTipText("Copy");
        buttonCopy.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_TOOLBAR_BUTTON);
        layeredPane.setLayer(buttonCopy, JLayeredPane.MODAL_LAYER);
        layeredPane.add(buttonCopy, "pos 1al 10");
        if (useScroll) {
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.getVerticalScrollBar().setUnitIncrement(10);
            scrollPane.getHorizontalScrollBar().setUnitIncrement(10);
            scrollPane.getViewport().putClientProperty(FlatClientProperties.STYLE, "" +
                    "background:$TextArea.background;");
            scrollPane.getViewport().setOpaque(false);
            scrollPane.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE, "" +
                    "trackArc:$ScrollBar.thumbArc;" +
                    "trackInsets:3,3,3,3;" +
                    "thumbInsets:3,3,3,3;" +
                    "background:$TextArea.background;");
            scrollPane.getHorizontalScrollBar().putClientProperty(FlatClientProperties.STYLE, "" +
                    "trackArc:$ScrollBar.thumbArc;" +
                    "trackInsets:3,3,3,3;" +
                    "thumbInsets:3,3,3,3;" +
                    "background:$TextArea.background;");
            layeredPane.add(scrollPane);
        } else {
            layeredPane.add(textArea);
        }
        add(layeredPane);
    }

    private void copy() {
        StringSelection stringSelection = new StringSelection(code);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    public void changeThemes() {
        try {
            String themeName = FlatLaf.isLafDark() ? "dark.xml" : "vs.xml";
            Theme theme = Theme.load(getClass().getResourceAsStream(
                    "/org/fife/ui/rsyntaxtextarea/themes/" + themeName));
            theme.bgColor = UIManager.getColor("TextArea.background");
            theme.apply(textArea);
            textArea.setFont(UIManager.getFont("TextArea.font"));
        } catch (IOException e) {
        }
    }

    @Override
    public void updateUI() {
        super.updateUI();
        if (textArea != null) {
            changeThemes();
        }
    }

    private RSyntaxTextArea textArea;
}
