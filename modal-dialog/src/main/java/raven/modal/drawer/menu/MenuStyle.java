package raven.modal.drawer.menu;

import raven.modal.drawer.renderer.DrawerCurvedLineStyle;
import raven.modal.drawer.renderer.AbstractDrawerLineStyleRenderer;

import javax.swing.*;

/**
 * @author Raven
 */
public class MenuStyle {

    public AbstractDrawerLineStyleRenderer getDrawerLineStyleRenderer() {
        return drawerLineStyleRenderer;
    }

    public void setDrawerLineStyleRenderer(AbstractDrawerLineStyleRenderer drawerLineStyleRenderer) {
        this.drawerLineStyleRenderer = drawerLineStyleRenderer;
    }

    private AbstractDrawerLineStyleRenderer drawerLineStyleRenderer = new DrawerCurvedLineStyle();

    public void styleMenu(JComponent component) {
    }

    public void styleMenuPanel(JPanel panel, int[] index) {
    }

    public void styleMenuItem(JButton menu, int[] index, boolean isMainItem) {
    }

    public void styleCompactMenuItem(JMenuItem menu, int[] index, boolean isMainItem) {
    }

    public void styleLabel(JLabel label) {
    }

    public void styleSeparator(JSeparator separator) {
    }
}
