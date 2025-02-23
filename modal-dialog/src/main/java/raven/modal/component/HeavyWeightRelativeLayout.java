package raven.modal.component;

import raven.modal.utils.ModalWindow;
import raven.modal.utils.ModalWindowFactory;

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

    private HierarchyListener hierarchyListener;
    private HierarchyBoundsListener hierarchyBoundsListener;
    private ComponentListener componentListener;

    public HeavyWeightRelativeLayout(Component owner) {
        this.owner = owner;
        modalWindows = new ArrayList<>();
        installOwner();
    }

    public void add(Component contents) {
        ModalWindow modal = ModalWindowFactory.getInstance().getWindow(owner, contents, 0, 0);
        modalWindows.add(modal);
        checkAndUpdateLayout();
        if (owner.isShowing()) {
            modal.show();
        }
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

    protected abstract void updateLayout();

    public void installOwner() {
        if (owner != null) {
            if (owner instanceof Window) {
                componentListener = new ComponentAdapter() {
                    @Override
                    public void componentResized(ComponentEvent e) {
                        System.out.println("componentResized");
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
                        checkAndUpdateLayout();
                    }

                    @Override
                    public void ancestorResized(HierarchyEvent e) {
                        System.out.println("ancestorResized");
                        checkAndUpdateLayout();
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
}
