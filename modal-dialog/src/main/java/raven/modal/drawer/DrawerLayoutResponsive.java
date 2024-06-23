package raven.modal.drawer;

import com.formdev.flatlaf.util.UIScale;
import raven.modal.component.ModalContainer;
import raven.modal.layout.OptionLayoutUtils;
import raven.modal.utils.DynamicSize;

import java.awt.*;

/**
 * This class for responsive, show and hide drawer
 *
 * @author Raven
 */
public class DrawerLayoutResponsive {

    public void setModalContainer(ModalContainer modalContainer) {
        this.modalContainer = modalContainer;
    }

    public ModalContainer getModalContainer() {
        return modalContainer;
    }

    public DrawerPanel getDrawerPanel() {
        return drawerPanel;
    }

    public int getDrawerOpenAt() {
        return drawerOpenAt;
    }

    public boolean isOpened() {
        return opened;
    }

    public boolean isShowing() {
        return showing;
    }

    public void setShowing(boolean showing) {
        this.showing = showing;
        if (drawerPanel != null) {
            drawerPanel.setVisible(showing);
        }
        drawerPanel.revalidate();
    }

    private ModalContainer modalContainer;
    private DrawerPanel drawerPanel;
    private int drawerOpenAt;
    private boolean scale;
    private boolean opened = true;
    private boolean showing = true;

    public DrawerLayoutResponsive(ModalContainer modalContainer, DrawerPanel drawerPanel, int drawerOpenAt, boolean scale) {
        this.modalContainer = modalContainer;
        this.drawerPanel = drawerPanel;
        this.drawerOpenAt = drawerOpenAt;
        this.scale = scale;
    }

    public boolean check(Container container, int width) {
        boolean isOpen = width <= (scale ? UIScale.scale(drawerOpenAt) : drawerOpenAt);
        if (isOpen != opened) {
            // change layout
            if (isOpen) {
                container.remove(drawerPanel);
                if (modalContainer != null) {
                    if (modalContainer.getController() != null) {
                        modalContainer.getController().addModal();
                    }
                    modalContainer.setVisible(true);
                    modalContainer.checkLayerVisible();
                }
            } else {
                if (modalContainer != null) {
                    if (modalContainer.getController() != null) {
                        modalContainer.getController().removeModal();
                    }
                    modalContainer.setVisible(false);
                    modalContainer.checkLayerVisible();
                }
                drawerPanel.checkThemesChanged();
                container.add(drawerPanel);
                drawerPanel.setVisible(showing);
            }
            opened = isOpen;
        }
        return opened;
    }

    public Rectangle getDrawerLayout(Container parent) {
        return OptionLayoutUtils.getLayoutLocation(parent, drawerPanel, 1f, drawerPanel.getDrawerBuilder().getOption().getLayoutOption());
    }

    public boolean isHorizontalDrawer() {
        DynamicSize size = drawerPanel.getDrawerBuilder().getOption().getLayoutOption().getSize();
        return size.getY().floatValue() == 1f;
    }

    public void revalidateDrawer() {
        if (drawerPanel != null) {
            drawerPanel.setVisible(showing);
        }
    }
}
