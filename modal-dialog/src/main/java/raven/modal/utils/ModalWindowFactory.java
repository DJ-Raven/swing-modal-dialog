package raven.modal.utils;

import com.formdev.flatlaf.ui.FlatDropShadowBorder;
import com.formdev.flatlaf.ui.FlatUIUtils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * @author Raven
 */
public class ModalWindowFactory {

    private static ModalWindowFactory instance;

    public static ModalWindowFactory getInstance() {
        if (instance == null) {
            instance = new ModalWindowFactory();
        }
        return instance;
    }

    private ModalWindowFactory() {
    }


    public ModalWindow getWindow(Component owner, Component contents, int x, int y) {

        return new DropShadowModalWindow(owner, contents, x, y);
    }

    private class DropShadowModalWindow extends ModalWindow {

        private Window dropShadowWindow;
        private JPanel dropShadowPanel;

        public DropShadowModalWindow(Component owner, Component contents, int x, int y) {
            super(owner, contents, x, y);

            // create drop shadow component
            dropShadowPanel = new JPanel();
            dropShadowPanel.setBorder(createDropShadowBorder());
            dropShadowPanel.setOpaque(false);

            // init preferred size
            Dimension prefSize = window.getPreferredSize();
            Insets insets = dropShadowPanel.getInsets();
            dropShadowPanel.setPreferredSize(new Dimension(
                    prefSize.width + insets.left + insets.right,
                    prefSize.height + insets.top + insets.bottom));

            dropShadowWindow = createWindow(owner);
            if (dropShadowWindow instanceof JWindow) {
                JWindow component = (JWindow) dropShadowWindow;
                component.getContentPane().add(dropShadowPanel, BorderLayout.CENTER);
                component.setBackground(new Color(0, true));
            }
        }

        @Override
        protected void setLocationImpl(int x, int y) {
            super.setLocationImpl(x, y);
            dropShadowWindow.setBounds(getDropShadowWindowBounds());
        }

        @Override
        protected void showImpl() {
            showDropShadowWindow();
            super.showImpl();
        }

        @Override
        protected void hideImpl() {
            dropShadowWindow.setVisible(false);
            super.hideImpl();
        }

        @Override
        protected void disposeImpl() {
            dropShadowWindow.dispose();
            super.disposeImpl();
        }

        private void showDropShadowWindow() {
            dropShadowWindow.setBounds(getDropShadowWindowBounds());
            dropShadowWindow.setVisible(true);
        }

        private Border createDropShadowBorder() {
            // todo
            return new FlatDropShadowBorder(
                    UIManager.getColor("Popup.dropShadowColor"),
                    // UIManager.getInsets("Popup.dropShadowInsets"),
                    new Insets(3, 3, 10, 10),
                    FlatUIUtils.getUIFloat("Popup.dropShadowOpacity", 0.5f));
        }

        private Rectangle getDropShadowWindowBounds() {
            Rectangle windowBounds = new Rectangle(window.getLocation(), window.getPreferredSize());
            Insets insets = dropShadowPanel.getInsets();
            return new Rectangle(
                    windowBounds.x -= insets.left,
                    windowBounds.y -= insets.top,
                    windowBounds.width += insets.left + insets.right,
                    windowBounds.height += insets.top + insets.bottom
            );
        }
    }
}
