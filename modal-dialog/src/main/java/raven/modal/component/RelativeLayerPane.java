package raven.modal.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @author Raven
 */
public class RelativeLayerPane extends JLayeredPane {

    public Component getOwner() {
        return owner;
    }

    public boolean isControlVisibility() {
        return controlVisibility;
    }

    public boolean isFixedLayout() {
        return fixedLayout;
    }

    private final Component owner;
    private final boolean controlVisibility;
    private final boolean fixedLayout;
    private HierarchyListener hierarchyListener;
    private HierarchyBoundsListener hierarchyBoundsListener;
    private ComponentListener componentListener;

    public RelativeLayerPane(Component owner, boolean controlVisibility, boolean fixedLayout) {
        this.owner = owner;
        this.controlVisibility = controlVisibility;
        this.fixedLayout = fixedLayout;
    }

    protected void installOwnerListener() {
        if (owner != null) {
            componentListener = new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    repaint();
                    revalidate();
                }
            };
            hierarchyBoundsListener = new HierarchyBoundsAdapter() {
                @Override
                public void ancestorMoved(HierarchyEvent e) {
                    repaint();
                    revalidate();
                }
            };
            owner.addComponentListener(componentListener);
            owner.addHierarchyBoundsListener(hierarchyBoundsListener);
            if (controlVisibility) {
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
                owner.addHierarchyListener(hierarchyListener);
                setVisible(owner.isShowing());
            }
        }
    }

    protected void uninstallOwnerListener() {
        if (owner != null) {
            if (componentListener != null) {
                owner.removeComponentListener(componentListener);
                componentListener = null;
            }
            if (hierarchyBoundsListener != null) {
                owner.removeHierarchyBoundsListener(hierarchyBoundsListener);
                hierarchyBoundsListener = null;
            }
            if (hierarchyListener != null) {
                owner.removeHierarchyListener(hierarchyListener);
                hierarchyListener = null;
            }
        }
    }

    @Override
    public void addNotify() {
        super.addNotify();
        installOwnerListener();
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        uninstallOwnerListener();
    }
}
