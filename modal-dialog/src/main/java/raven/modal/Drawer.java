package raven.modal;

import raven.modal.component.ModalContainer;
import raven.modal.drawer.DrawerBuilder;
import raven.modal.drawer.DrawerLayoutResponsive;
import raven.modal.drawer.DrawerPanel;
import raven.modal.drawer.simple.SimpleDrawerBuilder;
import raven.modal.option.Option;

import javax.swing.*;
import java.awt.*;

/**
 * @author Raven
 */
public class Drawer {

    public static String DRAWER_ID = "drawer";

    private static Drawer instance;
    private DrawerPanel drawerPanel;
    private RootPaneContainer rootPaneContainer;
    private Option drawerOption;
    private DrawerLayoutResponsive drawerLayoutResponsive;

    private static void initInstance() {
        if (instance == null) {
            instance = new Drawer();
        }
    }

    private Drawer() {
    }

    public static void installDrawer(Component parentComponent, DrawerBuilder drawerBuilder) {
        initInstance();
        instance.rootPaneContainer = ModalDialog.getRootPaneContainer(parentComponent);
        instance.drawerOption = drawerBuilder.getOption();
        instance.drawerPanel = new DrawerPanel(drawerBuilder);
        drawerBuilder.build(instance.drawerPanel);
        if (drawerBuilder.getOpenDrawerAt() >= 0) {
            instance.drawerLayoutResponsive = ModalDialog.installDrawer(instance.rootPaneContainer, instance.drawerPanel);
        }
    }

    public static void showDrawer() {
        if (instance.drawerLayoutResponsive != null) {
            if (instance.drawerLayoutResponsive.isOpened()) {
                instance.drawerPanel.checkThemesChanged();
                ModalContainer modalContainer = ModalDialog.showDrawer(instance.rootPaneContainer, instance.drawerPanel, instance.drawerOption, DRAWER_ID);
                modalContainer.showModal();
                instance.drawerLayoutResponsive.setModalContainer(modalContainer);
            }
        } else {
            ModalDialog.showDrawer(instance.rootPaneContainer, instance.drawerPanel, instance.drawerOption, DRAWER_ID).showModal();
        }
        if (!isVisible()) {
            setVisible(true);
        }
    }

    public static void closeDrawer() {
        if (ModalDialog.isIdExist(DRAWER_ID)) {
            ModalDialog.closeModal(DRAWER_ID);
        }
    }

    public static void setVisible(boolean visible) {
        if (instance.drawerLayoutResponsive != null) {
            instance.drawerLayoutResponsive.setShowing(visible);
        }
        if (!visible) {
            if (ModalDialog.isIdExist(DRAWER_ID)) {
                ModalDialog.closeModalAsRemove(DRAWER_ID);
            }
        }
    }

    public static boolean isVisible() {
        return instance.drawerLayoutResponsive == null || instance.drawerLayoutResponsive.isShowing();
    }

    public static boolean isOpen() {
        return instance.drawerLayoutResponsive == null || instance.drawerLayoutResponsive.isOpened();
    }

    public static DrawerBuilder getDrawerBuilder() {
        return instance.drawerPanel.getDrawerBuilder();
    }

    public static void setSelectedItemClass(Class<?> itemClass) {
        SimpleDrawerBuilder drawerBuilder = (SimpleDrawerBuilder) instance.drawerPanel.getDrawerBuilder();
        drawerBuilder.getDrawerMenu().setMenuSelectedClass(itemClass);
    }
}
