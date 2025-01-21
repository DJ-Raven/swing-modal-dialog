package raven.modal;

import raven.modal.layout.FrameModalLayout;
import raven.modal.layout.FrameToastLayout;
import raven.modal.toast.*;
import raven.modal.toast.option.ToastLocation;
import raven.modal.toast.option.ToastOption;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Raven
 */
public class Toast {

    private static Toast instance;
    private final Integer LAYER = JLayeredPane.POPUP_LAYER;
    private Map<RootPaneContainer, ToastContainerLayer> map;
    private Map<Type, ToastPanel.ThemesData> themesDataMap;
    private ToastOption defaultOption;
    private boolean reverseOrder;

    private static Toast getInstance() {
        if (instance == null) {
            instance = new Toast();
        }
        return instance;
    }

    private Toast() {
        map = new HashMap<>();
        initThemesData();
        defaultOption = ToastOption.getDefault();
    }

    private void initThemesData() {
        themesDataMap = new HashMap<>();
        themesDataMap.put(Type.SUCCESS, new ToastPanel.ThemesData("raven/modal/icon/toast_success.svg", new String[]{"#1EA97C", "#1EA97C"}));
        themesDataMap.put(Type.INFO, new ToastPanel.ThemesData("raven/modal/icon/toast_info.svg", new String[]{"#3B82F6", "#3B82F6"}));
        themesDataMap.put(Type.WARNING, new ToastPanel.ThemesData("raven/modal/icon/toast_warning.svg", new String[]{"#CC8925", "#CC8925"}));
        themesDataMap.put(Type.ERROR, new ToastPanel.ThemesData("raven/modal/icon/toast_error.svg", new String[]{"#FF5757", "#FF5757"}));
        themesDataMap.put(Type.DEFAULT, new ToastPanel.ThemesData(null, new String[]{"#64748b", "#64748b"}));
        themesDataMap.put(null, new ToastPanel.ThemesData(null, new String[]{"#64748b", "#64748b"}));
    }

    public static void show(Component owner, Type type, String message) {
        show(owner, type, message, getDefaultOption());
    }

    public static void show(Component owner, Type type, String message, ToastLocation location) {
        ToastOption option = createOption();
        option.getLayoutOption()
                .setLocation(location);
        show(owner, type, message, option);
    }

    public static void show(Component owner, Type type, String message, ToastLocation location, ToastOption option) {
        option.getLayoutOption()
                .setLocation(location);
        show(owner, type, message, option, null);
    }

    public static void show(Component owner, Type type, String message, ToastOption option) {
        show(owner, type, message, option, null);
    }

    public static void showPromise(Component owner, String message, ToastPromise promise) {
        showPromise(owner, message, getDefaultOption(), promise);
    }

    public static void showPromise(Component owner, String message, ToastLocation location, ToastPromise promise) {
        ToastOption option = createOption();
        option.getLayoutOption()
                .setLocation(location);
        showPromise(owner, message, option, promise);
    }

    public static void showPromise(Component owner, String message, ToastLocation location, ToastOption option, ToastPromise promise) {
        option.getLayoutOption()
                .setLocation(location);
        showPromise(owner, message, option, promise);
    }

    public static void showPromise(Component owner, String message, ToastOption option, ToastPromise promise) {
        show(owner, null, message, option, promise);
    }

    public static void showCustom(Component owner, Component component, ToastOption option) {
        show(owner, null, component, option, null);
    }

    private static void show(Component owner, Type type, Object object, ToastOption option, ToastPromise promise) {
        ToastPanel.ThemesData themesData = getInstance().themesDataMap.get(type);
        RootPaneContainer rootPaneContainer = ModalDialog.getRootPaneContainer(owner);
        AbstractToastContainerLayer toastContainerLayer = getInstance().getToastContainer(owner, option.isHeavyWeight());
        String message = object instanceof String ? object.toString() : null;
        Component toastOwner = option.getLayoutOption().isRelativeToOwner() ? owner : null;
        ToastPanel toastPanel = new ToastPanel(toastContainerLayer, toastOwner, new ToastPanel.ToastData(type, option, themesData, message));
        if (object instanceof Component) {
            toastContainerLayer.add(toastPanel.createToastCustom((Component) object));
        } else if (promise != null) {
            toastContainerLayer.add(toastPanel.createToastPromise(promise));
        } else {
            toastContainerLayer.add(toastPanel.createToast());
        }
        if (rootPaneContainer.getRootPane().getComponentOrientation().isLeftToRight() != toastPanel.getComponentOrientation().isLeftToRight()) {
            toastPanel.applyComponentOrientation(rootPaneContainer.getRootPane().getComponentOrientation());
        }
        toastContainerLayer.addToastPanel(toastPanel);
        toastPanel.start();
    }

    public static boolean checkPromiseId(String id) {
        if (id == null) {
            throw new IllegalArgumentException("id must not null");
        }
        for (ToastContainerLayer com : getInstance().map.values()) {
            if (com.checkPromiseId(id)) {
                return true;
            }
        }
        return ToastHeavyWeight.getInstance().checkPromiseId(id);
    }

    public static void closeAll() {
        getInstance().map.values().forEach(com -> {
            com.closeAll();
        });
        ToastHeavyWeight.getInstance().closeAll();
    }

    public static void closeAll(ToastLocation location) {
        getInstance().map.values().forEach(com -> {
            com.closeAll(location);
        });
        ToastHeavyWeight.getInstance().closeAll(location);
    }

    public static void closeAllImmediately() {
        getInstance().map.values().forEach(com -> {
            com.closeAllImmediately();
        });
        ToastHeavyWeight.getInstance().closeAllImmediately();
    }

    public static void setReverseOrder(boolean reverseOrder) {
        if (reverseOrder != isReverseOrder()) {
            getInstance().reverseOrder = reverseOrder;
            getInstance().updateLayout();
        }
    }

    public static boolean isReverseOrder() {
        return getInstance().reverseOrder;
    }

    public static ToastOption getDefaultOption() {
        return getInstance().defaultOption;
    }

    public static ToastOption createOption() {
        return getInstance().defaultOption.copy();
    }

    public static Map<Type, ToastPanel.ThemesData> getThemesData() {
        return getInstance().themesDataMap;
    }

    private void updateLayout() {
        for (ToastContainerLayer com : map.values()) {
            com.updateLayout();
        }
        ToastHeavyWeight.getInstance().updateLayout();
    }

    /**
     * Get toast container layered or create new for windows
     */
    private ToastContainerLayer getToastContainerLayered(RootPaneContainer rootPaneContainer) {
        ToastContainerLayer toastContainerLayer;
        if (map.containsKey(rootPaneContainer)) {
            toastContainerLayer = map.get(rootPaneContainer);
        } else {
            // get layeredPane from window
            JLayeredPane windowLayeredPane = rootPaneContainer.getLayeredPane();

            // create new toast container layered
            toastContainerLayer = createToastContainerLayered(rootPaneContainer);

            // add toast container layered to window layeredPane
            windowLayeredPane.add(toastContainerLayer.getLayeredPane(), LAYER);

            // init component orientation
            toastContainerLayer.initComponentOrientation(rootPaneContainer.getRootPane().getComponentOrientation());

            // set custom layout to window layeredPane
            LayoutManager layout = windowLayeredPane.getLayout();
            FrameToastLayout frameToastLayout = new FrameToastLayout(toastContainerLayer.getLayeredPane());
            if (layout != null && layout instanceof FrameModalLayout) {
                ((FrameModalLayout) layout).setOtherComponent(toastContainerLayer.getLayeredPane(), frameToastLayout);
            } else {
                // install toast layout
                windowLayeredPane.setLayout(frameToastLayout);
            }

            // store toast container layered to map
            map.put(rootPaneContainer, toastContainerLayer);

            // install property to windows
            toastContainerLayer.installWindowListener();
        }
        return toastContainerLayer;
    }

    private ToastContainerLayer createToastContainerLayered(RootPaneContainer rootPaneContainer) {
        ToastContainerLayer layeredPane = new ToastContainerLayer(map, rootPaneContainer);
        layeredPane.getLayeredPane().setVisible(false);
        return layeredPane;
    }

    private AbstractToastContainerLayer getToastContainer(Component owner, boolean isHeavyWeight) {
        if (isHeavyWeight) {
            return ToastHeavyWeight.getInstance().getToastHeavyWeightContainer(owner);
        }
        return getToastContainerLayered(ModalDialog.getRootPaneContainer(owner));
    }

    public static void setEnableHierarchy(boolean enable) {
        if (instance != null) {
            instance.map.values().forEach(container -> container.setEnableHierarchy(enable));
        }
        ToastHeavyWeight.getInstance().setEnableHierarchy(enable);
    }

    public enum Type {
        DEFAULT, SUCCESS, INFO, WARNING, ERROR
    }
}
