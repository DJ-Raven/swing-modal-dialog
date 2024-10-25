package raven.modal.drawer;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import raven.modal.component.Modal;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * @author Raven
 */
public class DrawerPanel extends Modal {

    private LookAndFeel oldThemes = UIManager.getLookAndFeel();
    private final DrawerBuilder drawerBuilder;

    public DrawerPanel(DrawerBuilder drawerBuilder) {
        this.drawerBuilder = drawerBuilder;

        // drawer we need install component. because drawer use auto responsive embed to the frame before show modal dialog
        setInstalled(true);
        installComponent();
    }

    @Override
    public void installComponent() {
        setLayout(new CardLayout());
        setOpaque(false);
        JPanel menuBackground = createMenuBackground();

        String layoutRow = "";
        if (drawerBuilder.getHeader() != null) {
            layoutRow = "[grow 0]";
            menuBackground.add(drawerBuilder.getHeader());
        }
        if (drawerBuilder.getHeaderSeparator() != null) {
            layoutRow += "[grow 0,2::]";
            menuBackground.add(drawerBuilder.getHeaderSeparator());
        }
        if (drawerBuilder.getMenu() != null) {
            layoutRow += "[fill]";
            menuBackground.add(drawerBuilder.getMenu());
        }
        if (drawerBuilder.getFooter() != null) {
            layoutRow += "[grow 0]";
            menuBackground.add(drawerBuilder.getFooter());
        }
        menuBackground.setLayout(new MigLayout("wrap,insets 0,fill", "fill", layoutRow));

        add(menuBackground);
    }

    public DrawerBuilder getDrawerBuilder() {
        return drawerBuilder;
    }

    public void checkThemesChanged() {
        if (oldThemes != UIManager.getLookAndFeel()) {
            SwingUtilities.updateComponentTreeUI(this);
            oldThemes = UIManager.getLookAndFeel();
        }
    }

    private JPanel createMenuBackground() {
        JPanel panel = new JPanel();
        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "background:null;");
        return panel;
    }

    @Override
    public void updateUI() {
        Border border = getBorder();
        super.updateUI();
        setBorder(border);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}
