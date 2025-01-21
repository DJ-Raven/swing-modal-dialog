package raven.modal.demo.system;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;
import raven.modal.Drawer;
import raven.modal.demo.Demo;
import raven.modal.demo.component.FormSearchButton;
import raven.modal.demo.component.MemoryBar;
import raven.modal.demo.component.RefreshLine;
import raven.modal.demo.icons.SVGIconUIColor;

import javax.swing.*;
import java.awt.*;

public class MainForm extends JPanel {

    public MainForm() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("fillx,wrap,insets 0,gap 0", "[fill]", "[][][fill,grow][]"));
        add(createHeader());
        add(createRefreshLine(), "height 3!");
        add(createMain());
        add(new JSeparator(), "height 2!");
        add(createFooter());
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel(new MigLayout("insets 3", "[]push[]push", "[fill]"));
        JToolBar toolBar = new JToolBar();
        JButton buttonDrawer = new JButton(new FlatSVGIcon("raven/modal/demo/icons/menu.svg", 0.5f));
        buttonUndo = new JButton(new FlatSVGIcon("raven/modal/demo/icons/undo.svg", 0.5f));
        buttonRedo = new JButton(new FlatSVGIcon("raven/modal/demo/icons/redo.svg", 0.5f));
        buttonRefresh = new JButton(new FlatSVGIcon("raven/modal/demo/icons/refresh.svg", 0.5f));
        buttonDrawer.addActionListener(e -> {
            if (Drawer.isOpen()) {
                Drawer.showDrawer();
            } else {
                Drawer.toggleMenuOpenMode();
            }
        });
        buttonUndo.addActionListener(e -> FormManager.undo());
        buttonRedo.addActionListener(e -> FormManager.redo());
        buttonRefresh.addActionListener(e -> FormManager.refresh());

        toolBar.add(buttonDrawer);
        toolBar.add(buttonUndo);
        toolBar.add(buttonRedo);
        toolBar.add(buttonRefresh);
        panel.add(toolBar);
        panel.add(createSearchBox(), "gapx n 135");
        return panel;
    }

    private JPanel createFooter() {
        JPanel panel = new JPanel(new MigLayout("insets 1 n 1 n,al trailing center,gapx 10,height 30!", "[]push[][]", "fill"));
        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]background:tint($Panel.background,20%);" +
                "[dark]background:tint($Panel.background,5%);");

        // demo version
        JLabel lbDemoVersion = new JLabel("Demo: v" + Demo.DEMO_VERSION);
        lbDemoVersion.putClientProperty(FlatClientProperties.STYLE, "" +
                "foreground:$Label.disabledForeground;");
        lbDemoVersion.setIcon(new SVGIconUIColor("raven/modal/demo/icons/git.svg", 1f, "Label.disabledForeground"));
        panel.add(lbDemoVersion);

        // java version
        String javaVendor = System.getProperty("java.vendor");
        if (javaVendor.equals("Oracle Corporation")) {
            javaVendor = "";
        }
        String java = javaVendor + " v" + System.getProperty("java.version").trim();
        String st = "Running on: Java %s";
        JLabel lbJava = new JLabel(String.format(st, java));
        lbJava.putClientProperty(FlatClientProperties.STYLE, "" +
                "foreground:$Label.disabledForeground;");
        lbJava.setIcon(new SVGIconUIColor("raven/modal/demo/icons/java.svg", 1f, "Label.disabledForeground"));
        panel.add(lbJava);

        panel.add(new JSeparator(JSeparator.VERTICAL));

        // memory
        MemoryBar memoryBar = new MemoryBar();
        panel.add(memoryBar);
        return panel;
    }

    private JPanel createSearchBox() {
        JPanel panel = new JPanel(new MigLayout("fill", "[fill,center,200:250:]", "[fill]"));
        FormSearchButton button = new FormSearchButton();
        button.addActionListener(e -> FormSearch.getInstance().showSearch());
        panel.add(button);
        return panel;
    }

    private JPanel createRefreshLine() {
        refreshLine = new RefreshLine();
        return refreshLine;
    }

    private Component createMain() {
        mainPanel = new JPanel(new BorderLayout());
        return mainPanel;
    }

    public void setForm(Form form) {
        mainPanel.removeAll();
        mainPanel.add(form);
        mainPanel.repaint();
        mainPanel.revalidate();

        // check button
        buttonUndo.setEnabled(FormManager.FORMS.isUndoAble());
        buttonRedo.setEnabled(FormManager.FORMS.isRedoAble());
        // check component orientation and update
        if (mainPanel.getComponentOrientation().isLeftToRight() != form.getComponentOrientation().isLeftToRight()) {
            applyComponentOrientation(mainPanel.getComponentOrientation());
        }
    }

    public void refresh() {
        refreshLine.refresh();
    }

    private JPanel mainPanel;
    private RefreshLine refreshLine;

    private JButton buttonUndo;
    private JButton buttonRedo;
    private JButton buttonRefresh;
}
