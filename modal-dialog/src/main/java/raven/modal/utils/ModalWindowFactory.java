package raven.modal.utils;

import com.formdev.flatlaf.util.SystemInfo;
import raven.modal.component.DropShadowBorder;

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
        return getWindow(owner, contents, null, x, y);
    }

    public ModalWindow getWindow(Component owner, Component contents, ModalWindowBorder border, int x, int y) {

        if (border == null) {
            border = ModalWindowBorder.getDefault();
        }
        if (!border.isCreatedAble()) {
            return new ModalWindow(owner, contents, x, y);
        }

        if (!isShadowAndRoundBorderSupport()) {
            ModalWindow modalWindow = new ModalWindow(owner, contents, x, y);
            return modalWindow;
        }
        return new DropShadowModalWindow(owner, contents, border, x, y);
    }

    private boolean isShadowAndRoundBorderSupport() {
        // for windows-11, mac-os and linux not yet test
        // we can use native border provide by flatlaf (next update)
        return SystemInfo.isWindows && SystemInfo.isWindows_11_orLater == false;
    }

    private class DropShadowModalWindow extends ModalWindow {

        private Window dropShadowWindow;
        private JPanel dropShadowPanel;

        public DropShadowModalWindow(Component owner, Component contents, ModalWindowBorder border, int x, int y) {
            super(owner, contents, x, y);

            // create drop shadow component
            dropShadowPanel = new JPanel();
            dropShadowPanel.setBackground(contents.getBackground());
            dropShadowPanel.setBorder(createDropShadowBorder(border));
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

        private Border createDropShadowBorder(ModalWindowBorder border) {
            return new DropShadowBorder(
                    border.getShadowSize(),
                    border.getShadowOpacity(),
                    border.getShadowColor(),
                    border.getBorderWidth(),
                    border.getBorderColor(),
                    border.getRound());
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
