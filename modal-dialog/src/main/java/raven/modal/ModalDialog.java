package raven.modal;

import raven.modal.component.*;
import raven.modal.drawer.DrawerLayoutResponsive;
import raven.modal.drawer.DrawerPanel;
import raven.modal.layout.FrameModalLayout;
import raven.modal.layout.FrameToastLayout;
import raven.modal.option.Option;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Raven
 */
public class ModalDialog {

    private static ModalDialog instance;
    private final Integer LAYER = JLayeredPane.MODAL_LAYER + 1;
    private final Map<RootPaneContainer, ModalContainerLayer> map;
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

    public static void showModal(Component owner, Modal modal) {
        showModal(owner, modal, getDefaultOption());
    }

    public static void showModal(Component owner, Modal modal, String id) {
        showModal(owner, modal, getDefaultOption(), id);
    }

    public static void showModal(Component owner, Modal modal, Option option) {
        showModal(owner, modal, option, null);
    }

    public static void showModal(Component owner, Modal modal, Option option, String id) {
        if (getInstance().isIdExist(id)) {
            throw new IllegalArgumentException("id '" + id + "' already exist");
        }
        SwingUtilities.invokeLater(() -> {
            // need copy if the option is the default option
            Option copyOption = option == getDefaultOption() ? option.copy() : option;
            boolean isHeavyWeight = copyOption.isHeavyWeight();
            Component modelOwner = copyOption.getLayoutOption().isRelativeToOwner() ? owner : null;
            getInstance().getModalContainer(owner, isHeavyWeight).addModal(modelOwner, modal, copyOption, id);
        });
    }

    public static void pushModal(Modal modal, String id) {
        getInstance().getModalContainerById(id).pushModal(modal, id);
    }

    public static void popModel(String id) {
        getInstance().getModalContainerById(id).popModal(id);
    }

    public static void closeModal(String id) {
        getInstance().getModalContainerById(id).closeModal(id);
    }

    public static void closeAllModal() {
        getInstance().map.values().forEach(con -> {
            con.closeAllModal();
        });
        ModalHeavyWeight.getInstance().closeAllModal();
    }

    public static void closeModalImmediately(String id) {
        getInstance().getModalContainerById(id).closeModalImmediately(id);
    }

    public static void closeAllModalImmediately() {
        getInstance().map.values().forEach(con -> {
            con.closeAllModalImmediately();
        });
        ModalHeavyWeight.getInstance().closeAllModalImmediately();
    }

    public static boolean isIdExist(String id) {
        for (ModalContainerLayer con : getInstance().map.values()) {
            if (con.checkId(id)) {
                return true;
            }
        }
        return ModalHeavyWeight.getInstance().isIdExist(id);
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
        boolean isHeavyWeight = option.isHeavyWeight();
        Component modelOwner = option.getLayoutOption().isRelativeToOwner() ? rootPaneContainer.getRootPane() : null;
        return getInstance().getModalContainer(rootPaneContainer.getRootPane(), isHeavyWeight).addModalWithoutShowing(modelOwner, modal, option, id);
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

    private ModalContainerLayer getRootPaneContainerById(String id) {
        for (ModalContainerLayer con : map.values()) {
            if (con.checkId(id)) {
                return con;
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
            windowLayeredPane.add(modalContainerLayer.getLayeredPane(), LAYER);

            // init component orientation
            modalContainerLayer.initComponentOrientation(rootPaneContainer.getRootPane().getComponentOrientation());

            // create snapshot layered
            Component snapshot = modalContainerLayer.createLayeredSnapshot();

            windowLayeredPane.add(snapshot, LAYER - 1);

            // set custom layout to window layeredPane
            FrameModalLayout frameModalLayout = new FrameModalLayout(modalContainerLayer.getLayeredPane(), rootPaneContainer.getContentPane(), snapshot);
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

            // install property to windows
            modalContainerLayer.installWindowListener();
        }
        return modalContainerLayer;
    }

    private ModalContainerLayer createModalContainerLayered(RootPaneContainer rootPaneContainer) {
        ModalContainerLayer layeredPane = new ModalContainerLayer(map, rootPaneContainer, createSnapshotChangedCallback());
        layeredPane.getLayeredPane().setVisible(false);
        return layeredPane;
    }

    private BaseModalContainer getModalContainerById(String id) {
        ModalContainerLayer modalContainer = getRootPaneContainerById(id);
        if (modalContainer != null) {
            return modalContainer;
        }
        ModalHeavyWeightContainerLayer modalHeavyWeight = ModalHeavyWeight.getInstance().getModalHeavyWeightContainerById(id);
        if (modalHeavyWeight != null) {
            return modalHeavyWeight;
        }
        throw new IllegalArgumentException("id '" + id + "' not found");
    }

    private BaseModalContainer getModalContainer(Component owner, boolean isHeavyWeight) {
        if (isHeavyWeight) {
            return ModalHeavyWeight.getInstance().getModalHeavyWeightContainer(owner);
        }
        return getModalContainerLayered(getRootPaneContainer(owner));
    }

    private Consumer<Boolean> createSnapshotChangedCallback() {
        return show -> {
            // modal
            map.values().forEach(container -> container.setEnableHierarchy(show));

            // heavyweight modal
            ModalHeavyWeight.getInstance().setEnableHierarchy(show);

            // toast
            Toast.setEnableHierarchy(show);
        };
    }
}
