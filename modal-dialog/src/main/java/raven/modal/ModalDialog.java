package raven.modal;

import raven.modal.component.Modal;
import raven.modal.component.ModalContainer;
import raven.modal.component.ModalContainerLayer;
import raven.modal.drawer.DrawerLayoutResponsive;
import raven.modal.drawer.DrawerPanel;
import raven.modal.layout.FrameModalLayout;
import raven.modal.layout.FrameToastLayout;
import raven.modal.option.Option;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Raven
 */
public class ModalDialog {

    private static ModalDialog instance;
    public static final Integer LAYER = JLayeredPane.MODAL_LAYER + 1;
    private Map<RootPaneContainer, ModalContainerLayer> map;
    private Option defaultOption;

    private ModalDialog() {
        map = new HashMap<>();
        defaultOption = Option.getDefault();
    }

    private static ModalDialog getInstance() {
        if (instance == null) {
            instance = new ModalDialog();
        }
        return instance;
    }

    public static void showModal(Component parentComponent, Modal modal) {
        showModal(parentComponent, modal, getDefaultOption());
    }

    public static void showModal(Component parentComponent, Modal modal, String id) {
        showModal(parentComponent, modal, getDefaultOption(), id);
    }

    public static void showModal(Component parentComponent, Modal modal, Option option) {
        showModal(parentComponent, modal, option, null);
    }

    public static void showModal(Component parentComponent, Modal modal, Option option, String id) {
        if (getInstance().isIdExist(id)) {
            throw new IllegalArgumentException("id '" + id + "' already exist");
        }
        SwingUtilities.invokeLater(() -> {
            ModalContainerLayer modalContainerLayer = getInstance().getModalContainerLayered(getRootPaneContainer(parentComponent));
            modalContainerLayer.addModal(modal, option, id);
            modalContainerLayer.repaint();
            modalContainerLayer.revalidate();
        });
    }

    public static void pushModal(Modal modal, String id) {
        RootPaneContainer rootPaneContainer = getInstance().getRootPaneContainerById(id);
        if (rootPaneContainer == null) {
            throw new IllegalArgumentException("id '" + id + "' not found");
        }
        ModalContainerLayer modalContainerLayer = getInstance().getModalContainerLayered(rootPaneContainer);
        modalContainerLayer.pushModal(modal, id);
    }

    public static void popModel(String id) {
        RootPaneContainer rootPaneContainer = getInstance().getRootPaneContainerById(id);
        if (rootPaneContainer == null) {
            throw new IllegalArgumentException("id '" + id + "' not found");
        }
        ModalContainerLayer modalContainerLayer = getInstance().getModalContainerLayered(rootPaneContainer);
        modalContainerLayer.popModal(id);
    }

    public static void closeModal(String id) {
        RootPaneContainer rootPaneContainer = getInstance().getRootPaneContainerById(id);
        if (rootPaneContainer == null) {
            throw new IllegalArgumentException("id '" + id + "' not found");
        }
        ModalContainerLayer modalContainerLayer = getInstance().getModalContainerLayered(rootPaneContainer);
        modalContainerLayer.closeModal(id);
    }

    public static void closeAllModal() {
        for (Map.Entry<RootPaneContainer, ModalContainerLayer> entry : getInstance().map.entrySet()) {
            entry.getValue().closeAllModal();
        }
    }

    public static boolean isIdExist(String id) {
        for (Map.Entry<RootPaneContainer, ModalContainerLayer> entry : getInstance().map.entrySet()) {
            if (entry.getValue().checkId(id)) {
                return true;
            }
        }
        return false;
    }

    public static void setDefaultOption(Option option) {
        getInstance().defaultOption = option;
    }

    public static Option getDefaultOption() {
        return getInstance().defaultOption;
    }

    public static Option createOption() {
        return getInstance().defaultOption.copy();
    }

    protected static DrawerLayoutResponsive installDrawer(RootPaneContainer rootPaneContainer, DrawerPanel drawerPanel) {
        ModalContainerLayer modalContainerLayer = getInstance().getModalContainerLayered(rootPaneContainer);
        JLayeredPane windowLayeredPane = rootPaneContainer.getLayeredPane();
        if (windowLayeredPane.getLayout() instanceof FrameModalLayout) {
            DrawerLayoutResponsive drawerLayoutResponsive = new DrawerLayoutResponsive(null, drawerPanel);
            modalContainerLayer.setDrawerLayoutResponsive(drawerLayoutResponsive);
            ((FrameModalLayout) windowLayeredPane.getLayout()).setDrawerLayoutAction(drawerLayoutResponsive);
            return drawerLayoutResponsive;
        }
        return null;
    }

    protected static ModalContainer showDrawer(RootPaneContainer rootPaneContainer, Modal modal, Option option, String id) {
        if (getInstance().isIdExist(id)) {
            throw new IllegalArgumentException("id '" + id + "' already exist");
        }
        ModalContainerLayer modalContainerLayer = getInstance().getModalContainerLayered(rootPaneContainer);
        return modalContainerLayer.addModalWithoutShowing(modal, option, id);
    }

    protected static void closeModalAsRemove(String id) {
        RootPaneContainer rootPaneContainer = getInstance().getRootPaneContainerById(id);
        if (rootPaneContainer == null) {
            throw new IllegalArgumentException("id '" + id + "' not found");
        }
        ModalContainerLayer modalContainerLayer = getInstance().getModalContainerLayered(rootPaneContainer);
        modalContainerLayer.closeAsRemove(id);
    }

    protected static RootPaneContainer getRootPaneContainer(Component component) {
        if (component == null) {
            throw new IllegalArgumentException("parent component must not null");
        }
        if (component instanceof JFrame || component instanceof JDialog || component instanceof JInternalFrame) {
            return (RootPaneContainer) component;
        }
        return getRootPaneContainer(component.getParent());
    }

    private RootPaneContainer getRootPaneContainerById(String id) {
        for (Map.Entry<RootPaneContainer, ModalContainerLayer> entry : map.entrySet()) {
            if (entry.getValue().checkId(id)) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Get modal container layered or create new for windows
     */
    private ModalContainerLayer getModalContainerLayered(RootPaneContainer rootPaneContainer) {
        ModalContainerLayer modalContainerLayer;
        if (map.containsKey(rootPaneContainer)) {
            modalContainerLayer = map.get(rootPaneContainer);
        } else {
            // get layeredPane from window
            JLayeredPane windowLayeredPane = rootPaneContainer.getLayeredPane();

            // create new modal container layered
            modalContainerLayer = createModalContainerLayered(rootPaneContainer);

            // add modal container layered to window layeredPane
            windowLayeredPane.add(modalContainerLayer, LAYER);

            // create snapshot layered
            Component snapshot = modalContainerLayer.createLayeredSnapshot();

            windowLayeredPane.add(snapshot, LAYER - 1);

            // set custom layout to window layeredPane
            FrameModalLayout frameModalLayout = new FrameModalLayout(modalContainerLayer, rootPaneContainer.getContentPane(), snapshot);
            LayoutManager oldLayout = windowLayeredPane.getLayout();
            windowLayeredPane.setLayout(frameModalLayout);
            windowLayeredPane.doLayout();

            // check if toast layout install
            if (oldLayout != null && oldLayout instanceof FrameToastLayout) {
                // add toast container layout to other component
                frameModalLayout.setOtherComponent(((FrameToastLayout) oldLayout).getComponent(), oldLayout);
            }

            // store modal container layered to map
            map.put(rootPaneContainer, modalContainerLayer);

            // install property to remove the root pane map after windows removed
            modalContainerLayer.setPropertyData(installProperty(rootPaneContainer.getRootPane()));
        }
        return modalContainerLayer;
    }

    private Object installProperty(JRootPane rootPane) {
        PropertyChangeListener propertyChangeListener = e -> {
            if (e.getNewValue() == null && e.getOldValue() instanceof RootPaneContainer) {
                uninstall((RootPaneContainer) e.getOldValue());
            }
        };
        rootPane.addPropertyChangeListener("ancestor", propertyChangeListener);
        return propertyChangeListener;
    }

    private void uninstall(RootPaneContainer rootPaneContainer) {
        if (map.containsKey(rootPaneContainer)) {
            ModalContainerLayer modalContainerLayer = map.get(rootPaneContainer);
            JLayeredPane windowLayeredPane = rootPaneContainer.getLayeredPane();
            rootPaneContainer.getRootPane().removePropertyChangeListener("ancestor", (PropertyChangeListener) modalContainerLayer.getPropertyData());

            // uninstall layout
            LayoutManager oldLayout = windowLayeredPane.getLayout();
            if (oldLayout != null) {
                if (oldLayout instanceof FrameModalLayout) {
                    FrameModalLayout frameModalLayout = (FrameModalLayout) oldLayout;
                    if (frameModalLayout.getOldOtherComponentLayout() != null) {
                        windowLayeredPane.setLayout(frameModalLayout.getOldOtherComponentLayout());
                    } else {
                        windowLayeredPane.setLayout(null);
                    }
                }
            }

            // remove
            map.remove(rootPaneContainer);
            windowLayeredPane.remove(modalContainerLayer);
            Component componentSnapshot = modalContainerLayer.getComponentSnapshot();
            if (componentSnapshot != null) {
                windowLayeredPane.remove(modalContainerLayer.getComponentSnapshot());
            }
            modalContainerLayer.remove();
        }
    }

    private ModalContainerLayer createModalContainerLayered(RootPaneContainer rootPaneContainer) {
        ModalContainerLayer layeredPane = new ModalContainerLayer(rootPaneContainer);
        layeredPane.setVisible(false);
        return layeredPane;
    }
}
