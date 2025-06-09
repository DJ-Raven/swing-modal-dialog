package raven.modal.utils;

import javax.swing.*;
import java.awt.*;

/**
 * @author Raven
 */
public class ModalWindow {

    protected Component owner;
    protected Component contents;

    protected Window window;

    protected ModalWindow(Component owner, Component contents, int x, int y) {
        this.owner = owner;
        this.contents = contents;

        window = createWindow(owner);

        if (window instanceof JWindow) {
            JWindow component = (JWindow) window;
            component.setBounds(new Rectangle(getLocation(owner, x, y), new Dimension(1, 1)));
            component.getContentPane().add(contents, BorderLayout.CENTER);
            component.setBackground(contents.getBackground());
            component.invalidate();
            component.validate();
        }
    }

    public Component getOwner() {
        return owner;
    }

    public Component getContents() {
        return contents;
    }

    public void setLocation(int x, int y) {
        if (window != null) {
            setLocationImpl(x, y);
        }
    }

    public void setBounds(int x, int y, int width, int height) {
        if (window != null) {
            setBoundsImpl(x, y, width, height);
        }
    }

    public void setVisible(boolean visible) {
        if (visible) {
            show();
        } else {
            hide();
        }
    }

    public void show() {
        if (window != null && !window.isVisible()) {
            showImpl();
        }
    }

    public void hide() {
        if (window != null && window.isVisible()) {
            hideImpl();
        }
    }

    public void dispose() {
        if (window != null) {
            disposeImpl();
        }
    }

    protected void setLocationImpl(int x, int y) {
        window.setLocation(getLocation(owner, x, y));
    }

    protected void setBoundsImpl(int x, int y, int width, int height) {
        Point location = getLocation(owner, x, y);
        window.setBounds(location.x, location.y, width, height);
        window.validate();
    }

    protected void showImpl() {
        window.setVisible(true);
    }

    protected void hideImpl() {
        window.setVisible(false);
    }

    protected void disposeImpl() {
        window.dispose();
    }

    protected Window createWindow(Component owner) {
        return new JWindow(getParentWindow(owner));
    }

    protected Window getParentWindow(Component owner) {
        Window window;
        if (owner instanceof Window) {
            window = (Window) owner;
        } else {
            window = SwingUtilities.getWindowAncestor(owner);
        }
        if (window == null) {
            window = new DefaultFrame();
        }
        return window;
    }

    protected Point getLocation(Component owner, int x, int y) {
        if (owner == null || !owner.isShowing()) {
            return new Point(x, y);
        }
        Point ownerLocation = owner.getLocationOnScreen();
        return new Point(ownerLocation.x + x, ownerLocation.y + y);
    }

    static class DefaultFrame extends Frame {
    }
}
