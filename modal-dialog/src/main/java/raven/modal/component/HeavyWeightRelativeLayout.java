package raven.modal.component;

import raven.modal.utils.ModalWindow;
import raven.modal.utils.ModalWindowBorder;
import raven.modal.utils.ModalWindowFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Raven
 */
public abstract class HeavyWeightRelativeLayout {

    protected Component owner;
    protected List<ModalWindow> modalWindows;
    private boolean enableHierarchy = true;

    private HierarchyListener hierarchyListener;
    private HierarchyBoundsListener hierarchyBoundsListener;
    private ComponentListener componentListener;

    public HeavyWeightRelativeLayout(Component owner) {
        this.owner = owner;
        modalWindows = new ArrayList<>();
        installOwner();
    }

    protected ModalWindow createModalWindow(Component contents) {
        return ModalWindowFactory.getInstance().getWindow(owner, contents, getModalWindowBorder(contents), 0, 0);
    }

    public ModalWindow add(Component contents) {
        ModalWindow modal = createModalWindow(contents);
        modalWindows.add(modal);
        checkAndUpdateLayout();
        if (owner.isShowing()) {
            modal.show();
        }
        return modal;
    }

    public void remove(Component contents) {
        for (int i = 0; i < modalWindows.size(); i++) {
            ModalWindow modal = modalWindows.get(i);
            if (modal.getContents() == contents) {
                modal.dispose();
                modalWindows.remove(modal);
                checkAndUpdateLayout();
                return;
            }
        }
    }

    public boolean isEmpty() {
        return modalWindows.isEmpty();
    }

    public List<ModalWindow> getModalWindows() {
        return modalWindows;
    }

    public Component getOwner() {
        return owner;
    }

    protected void checkAndUpdateLayout() {
        if (owner.isShowing()) {
            updateLayout();
        }
    }

    protected void setVisible(boolean visible) {
        for (int i = 0; i < modalWindows.size(); i++) {
            modalWindows.get(i).setVisible(visible);
        }
        checkAndUpdateLayout();
    }

    protected abstract ModalWindowBorder getModalWindowBorder(Component contents);

    protected abstract void updateLayout();

    public static Rectangle getModalBorderSize(ModalWindow modal) {
        if (modal instanceof ModalWindowFactory.AbstractModalBorder) {
            return ((ModalWindowFactory.AbstractModalBorder) modal).getBorderSize();
        }
        return null;
    }

    public void installOwner() {
        if (owner != null) {
            if (owner instanceof Window) {
                componentListener = new ComponentAdapter() {
                    @Override
                    public void componentResized(ComponentEvent e) {
                        checkAndUpdateLayout();
                    }

                    @Override
                    public void componentMoved(ComponentEvent e) {
                        checkAndUpdateLayout();
                    }
                };
                owner.addComponentListener(componentListener);
            } else {
                hierarchyListener = e -> {
                    if (!enableHierarchy) return;
                    if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0 || (e.getChangeFlags() & HierarchyEvent.DISPLAYABILITY_CHANGED) != 0) {
                        if (e.getChanged().isShowing()) {
                            if (owner.isShowing()) {
                                setVisible(true);
                            }
                        } else {
                            if (!owner.isShowing()) {
                                setVisible(false);
                            }
                        }
                    }
                };
                hierarchyBoundsListener = new HierarchyBoundsAdapter() {
                    @Override
                    public void ancestorMoved(HierarchyEvent e) {
                        SwingUtilities.invokeLater(() -> checkAndUpdateLayout());
                    }

                    @Override
                    public void ancestorResized(HierarchyEvent e) {
                        SwingUtilities.invokeLater(() -> checkAndUpdateLayout());
                    }
                };
                owner.addHierarchyListener(hierarchyListener);
                owner.addHierarchyBoundsListener(hierarchyBoundsListener);
            }
        }
    }

    public void uninstallOwner() {
        if (owner != null) {
            if (componentListener != null) {
                owner.removeComponentListener(componentListener);
                componentListener = null;
            }
            if (hierarchyListener != null) {
                owner.removeHierarchyListener(hierarchyListener);
                hierarchyListener = null;
            }
            if (hierarchyBoundsListener != null) {
                owner.removeHierarchyBoundsListener(hierarchyBoundsListener);
                hierarchyBoundsListener = null;
            }
        }
    }

    public void setEnableHierarchy(boolean enableHierarchy) {
        this.enableHierarchy = enableHierarchy;
    }
}
