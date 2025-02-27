package raven.modal.utils;

import com.formdev.flatlaf.util.UIScale;
import raven.modal.component.DropShadowBorder;
import raven.modal.component.HeavyWeightModalController;
import raven.modal.component.ModalBackground;

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

    public ModalWindow createWindowBackground(ModalWindow modal) {
        if (!(modal.getContents() instanceof HeavyWeightModalController)) {
            throw new IllegalArgumentException("Can't create modal window");
        }
        return new ModalWindowBackground(modal, (HeavyWeightModalController) modal.getContents());
    }

    private boolean isShadowAndRoundBorderSupport() {
        // for windows-11, mac-os and linux not yet test
        // we can use native border provide by flatlaf (next update)
        return ModalUtils.isShadowAndRoundBorderSupport();
    }

    public abstract class AbstractModalBorder extends ModalWindow {

        public AbstractModalBorder(Component owner, Component contents, int x, int y) {
            super(owner, contents, x, y);
        }

        public abstract Rectangle getBorderSize();
    }

    protected class ModalWindowBackground extends AbstractModalBorder {

        private final ModalWindow delegate;
        private final HeavyWeightModalController controller;

        public ModalWindowBackground(ModalWindow delegate, HeavyWeightModalController controller) {
            super(delegate.getOwner(), new ModalBackground(controller), 0, 0);
            this.delegate = delegate;
            this.controller = controller;
            this.window.setBackground(new Color(0, true));
        }

        @Override
        public Component getContents() {
            return delegate.getContents();
        }

        @Override
        public Rectangle getBorderSize() {
            if (delegate instanceof DropShadowModalWindow) {
                return ((DropShadowModalWindow) delegate).getBorderSize();
            }
            return null;
        }

        @Override
        protected void setLocationImpl(int x, int y) {
            Rectangle ownerBounds = getOwnerBounds();
            window.setBounds(ownerBounds.x, ownerBounds.y, ownerBounds.width, ownerBounds.height);
            window.validate();
            delegate.setLocationImpl(x, y);
        }

        @Override
        protected void setBoundsImpl(int x, int y, int width, int height) {
            Rectangle ownerBounds = getOwnerBounds();
            window.setBounds(ownerBounds.x, ownerBounds.y, ownerBounds.width, ownerBounds.height);
            window.validate();
            delegate.setBoundsImpl(x, y, width, height);
        }

        @Override
        protected void showImpl() {
            super.showImpl();
            delegate.showImpl();
        }

        @Override
        protected void hideImpl() {
            super.hideImpl();
            delegate.hideImpl();
        }

        @Override
        protected void disposeImpl() {
            super.disposeImpl();
            delegate.disposeImpl();
        }

        private Rectangle getOwnerBounds() {
            Point point = getLocation(owner, 0, 0);
            Insets padding = UIScale.scale(controller.getOption().getLayoutOption().getBackgroundPadding());
            int width = owner.getWidth();
            int height = owner.getHeight();
            Insets insets;
            if (owner instanceof Frame) {
                insets = ((Frame) owner).getInsets();
            } else if (owner instanceof Window) {
                insets = ((Window) owner).getInsets();
            } else {
                JComponent component = (JComponent) owner;
                Rectangle visibleRec = component.getVisibleRect();
                point.x += visibleRec.x;
                point.y += visibleRec.y;
                width = visibleRec.width;
                height = visibleRec.height;
                insets = component.getInsets();
            }
            point.x += insets.left + padding.left;
            point.y += insets.top + padding.top;
            width -= (insets.left + insets.right + padding.left + padding.right);
            height -= (insets.top + insets.bottom + padding.top + padding.bottom);
            return new Rectangle(point.x, point.y, width, height);
        }
    }

    protected class DropShadowModalWindow extends AbstractModalBorder {

        private Window dropShadowWindow;
        private ShadowPanel dropShadowPanel;

        public DropShadowModalWindow(Component owner, Component contents, ModalWindowBorder border, int x, int y) {
            super(owner, contents, x, y);

            // create drop shadow component
            dropShadowPanel = new ShadowPanel(border);
            dropShadowPanel.setBackground(contents.getBackground());

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
            dropShadowWindow.validate();
        }

        @Override
        protected void setBoundsImpl(int x, int y, int width, int height) {
            super.setBoundsImpl(x, y, width, height);
            dropShadowWindow.setBounds(getDropShadowWindowBounds());
            dropShadowWindow.validate();
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

        private Rectangle getDropShadowWindowBounds() {
            Rectangle windowBounds = new Rectangle(window.getLocation(), window.getSize());
            Insets insets = dropShadowPanel.getInsets();
            return new Rectangle(
                    windowBounds.x -= insets.left,
                    windowBounds.y -= insets.top,
                    windowBounds.width += insets.left + insets.right,
                    windowBounds.height += insets.top + insets.bottom
            );
        }

        @Override
        public Rectangle getBorderSize() {
            Border border = dropShadowPanel.getBorder();
            if (border == null) return null;

            Insets insets = border.getBorderInsets(dropShadowPanel);

            return new Rectangle(insets.left, insets.top, insets.left + insets.right, insets.top + insets.bottom);
        }
    }

    /**
     * Fixes border reset after changed flatlaf themes
     * By create new border when ui updated
     */
    private class ShadowPanel extends JPanel {

        private final ModalWindowBorder border;

        private ShadowPanel(ModalWindowBorder border) {
            this.border = border;
            setOpaque(false);
            initBorder();
        }

        private void initBorder() {
            if (border != null) {
                setBorder(createDropShadowBorder(border));
            }
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

        @Override
        public void updateUI() {
            super.updateUI();
            initBorder();
        }
    }
}
