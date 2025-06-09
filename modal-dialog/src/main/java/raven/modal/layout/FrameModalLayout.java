package raven.modal.layout;

import raven.modal.drawer.DrawerLayoutResponsive;
import raven.modal.drawer.DrawerPanel;

import java.awt.*;

/**
 * @author Raven
 */
public class FrameModalLayout implements LayoutManager {

    public LayoutManager getOldOtherComponentLayout() {
        return oldOtherComponentLayout;
    }

    public void setDrawerLayoutAction(DrawerLayoutResponsive drawerLayoutResponsive) {
        this.drawerLayoutResponsive = drawerLayoutResponsive;
    }

    public void setOtherComponent(Component otherComponent, LayoutManager oldOtherComponentLayout) {
        this.otherComponent = otherComponent;
        this.oldOtherComponentLayout = oldOtherComponentLayout;
    }

    private final Component component;
    private final Component contentPane;
    private final Component snapshot;

    private Component otherComponent;
    private LayoutManager oldOtherComponentLayout;

    private DrawerLayoutResponsive drawerLayoutResponsive;

    public FrameModalLayout(Component component, Component contentPane, Component snapshot) {
        this.component = component;
        this.contentPane = contentPane;
        this.snapshot = snapshot;
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
    }

    @Override
    public void removeLayoutComponent(Component comp) {
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
            return new Dimension(0, 0);
        }
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
            return new Dimension(0, 0);
        }
    }

    @Override
    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            int x = insets.left;
            int y = contentPane.getY();
            int width = parent.getWidth() - (insets.left + insets.right);
            int height = contentPane.getHeight();
            component.setBounds(x, y, width, height);
            snapshot.setBounds(x, y, width, height);
            if (drawerLayoutResponsive != null) {
                boolean isHorizontalDrawer = drawerLayoutResponsive.isHorizontalDrawer();
                boolean isOpen = drawerLayoutResponsive.check(parent, isHorizontalDrawer ? width : height);
                if (isOpen || !drawerLayoutResponsive.isShowing()) {
                    contentPane.setBounds(0, contentPane.getY(), width, height);
                    drawerLayoutResponsive.revalidateDrawer();
                } else {
                    Rectangle drawerRec = drawerLayoutResponsive.getDrawerLayout(parent);
                    Rectangle contentRec = calculate(new Rectangle(x, y, width, height), drawerRec, isHorizontalDrawer);

                    // adjust content pane with drawer insets left and right
                    // to make the shadow border paint over the content pane
                    Insets drawerInsets = getBorderInsets(drawerLayoutResponsive.getDrawerPanel());
                    if (drawerInsets != null && (drawerInsets.left > 0 || drawerInsets.right > 0)) {
                        boolean ltr = parent.getComponentOrientation().isLeftToRight();
                        if (ltr) {
                            contentRec.x -= drawerInsets.right;
                            contentRec.width += drawerInsets.right;
                        } else {
                            contentRec.width += drawerInsets.left;
                        }
                    }

                    contentPane.setBounds(contentRec);
                    drawerLayoutResponsive.getDrawerPanel().setBounds(x + drawerRec.x, y + drawerRec.y, drawerRec.width - x, drawerRec.height - y);
                }
            }
            // do layout other component
            if (otherComponent != null) {
                int fw = parent.getWidth() - (insets.left + insets.right);
                int fh = parent.getHeight() - (insets.top + insets.bottom);
                otherComponent.setBounds(insets.left, insets.top, fw, fh);
            }
        }
    }

    private Insets getBorderInsets(DrawerPanel drawerPanel) {
        return drawerPanel.getInsets();
    }

    private Rectangle calculate(Rectangle rec1, Rectangle rec2, boolean horizontal) {
        if (horizontal) {
            int x = (rec2.x == 0 ? rec2.width : rec1.x);
            int y = rec1.y;
            int w = rec1.width - rec2.width;
            int h = rec1.height;
            return new Rectangle(x, y, w, h);
        } else {
            int x = rec1.x;
            int y = (rec2.y == 0 ? rec2.height : rec1.y);
            int w = rec2.width;
            int h = rec1.height - rec2.height + rec1.y;
            return new Rectangle(x, y, w, h);
        }
    }
}
