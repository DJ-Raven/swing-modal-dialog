package raven.modal;

import raven.modal.layout.FrameModalLayout;
import raven.modal.layout.FrameToastLayout;
import raven.modal.option.LayoutOption;
import raven.modal.option.Location;
import raven.modal.toast.option.ToastLocation;
import raven.modal.toast.option.ToastOption;
import raven.modal.toast.ToastContainerLayer;
import raven.modal.toast.ToastPanel;
import raven.modal.toast.ToastPromise;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;
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
        defaultOption.getLayoutOption()
                .setLocation(Location.CENTER, Location.TOP)
                .setAnimateDistance(0, -1f);
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
        ToastOption option = createOption().setLayoutOption(location.getLayout());
        show(owner, type, message, option);
    }

    public static void show(Component owner, Type type, String message, ToastLocation location, ToastOption option) {
        LayoutOption l = location.getLayout();
        option.getLayoutOption()
                .setLocation(l.getHorizontalLocation(), l.getVerticalLocation())
                .setAnimateDistance(l.getAnimateDistance().getX(), l.getAnimateDistance().getY());
        show(owner, type, message, option, null);
    }

    public static void show(Component owner, Type type, String message, ToastOption option) {
        show(owner, type, message, option, null);
    }

    public static void showPromise(Component owner, String message, ToastPromise promise) {
        showPromise(owner, message, getDefaultOption(), promise);
    }

    public static void showPromise(Component owner, String message, ToastLocation location, ToastPromise promise) {
        ToastOption option = createOption().setLayoutOption(location.getLayout());
        showPromise(owner, message, option, promise);
    }

    public static void showPromise(Component owner, String message, ToastLocation location, ToastOption option, ToastPromise promise) {
        LayoutOption l = location.getLayout();
        option.getLayoutOption()
                .setLocation(l.getHorizontalLocation(), l.getVerticalLocation())
                .setAnimateDistance(l.getAnimateDistance().getX(), l.getAnimateDistance().getY());
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
        ToastContainerLayer toastContainerLayer = getInstance().getToastContainerLayered(rootPaneContainer);
        String message = object instanceof String ? object.toString() : null;
        ToastPanel toastPanel = new ToastPanel(toastContainerLayer, new ToastPanel.ToastData(type, option, themesData, message));
        if (object instanceof Component) {
            toastContainerLayer.add(toastPanel.createToastCustom((Component) object), 0);
        } else if (promise != null) {
            toastContainerLayer.add(toastPanel.createToastPromise(promise), 0);
        } else {
            toastContainerLayer.add(toastPanel.createToast(), 0);
        }
        if (rootPaneContainer.getRootPane().getComponentOrientation().isLeftToRight() != toastPanel.getComponentOrientation().isLeftToRight()) {
            toastPanel.applyComponentOrientation(rootPaneContainer.getRootPane().getComponentOrientation());
        }
        toastContainerLayer.setVisible(true);
        toastPanel.start();
        toastContainerLayer.revalidate();
        toastContainerLayer.addToastPanel(toastPanel);
    }

    public static boolean checkPromiseId(String id) {
        if (id == null) {
            throw new IllegalArgumentException("id must not null");
        }
        for (Map.Entry<RootPaneContainer, ToastContainerLayer> entry : getInstance().map.entrySet()) {
            if (entry.getValue().checkPromiseId(id)) {
                return true;
            }
        }
        return false;
    }

    public static void closeAll() {
        for (Map.Entry<RootPaneContainer, ToastContainerLayer> entry : getInstance().map.entrySet()) {
            entry.getValue().closeAll();
        }
    }

    public static void closeAll(ToastLocation location) {
        for (Map.Entry<RootPaneContainer, ToastContainerLayer> entry : getInstance().map.entrySet()) {
            entry.getValue().closeAll(location.getLayout());
        }
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
        for (Map.Entry<RootPaneContainer, ToastContainerLayer> entry : map.entrySet()) {
            entry.getValue().revalidate();
        }
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
            windowLayeredPane.add(toastContainerLayer, LAYER);

            // check layout right to left
            if (rootPaneContainer.getRootPane().getComponentOrientation().isLeftToRight() != toastContainerLayer.getComponentOrientation().isLeftToRight()) {
                toastContainerLayer.applyComponentOrientation(rootPaneContainer.getRootPane().getComponentOrientation());
            }

            // set custom layout to window layeredPane
            LayoutManager layout = windowLayeredPane.getLayout();
            FrameToastLayout frameToastLayout = new FrameToastLayout(toastContainerLayer);
            if (layout != null && layout instanceof FrameModalLayout) {
                ((FrameModalLayout) layout).setOtherComponent(toastContainerLayer, frameToastLayout);
            } else {
                // install toast layout
                windowLayeredPane.setLayout(frameToastLayout);
            }

            // store toast container layered to map
            map.put(rootPaneContainer, toastContainerLayer);

            // install property to remove the root pane map after windows removed
            toastContainerLayer.setPropertyData(installProperty(rootPaneContainer.getRootPane()));
        }
        return toastContainerLayer;
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
            ToastContainerLayer toastContainerLayer = map.get(rootPaneContainer);
            JLayeredPane windowLayeredPane = rootPaneContainer.getLayeredPane();
            rootPaneContainer.getRootPane().removePropertyChangeListener("ancestor", (PropertyChangeListener) toastContainerLayer.getPropertyData());

            // uninstall layout
            LayoutManager oldLayout = windowLayeredPane.getLayout();
            if (oldLayout != null) {
                if (oldLayout instanceof FrameToastLayout) {
                    windowLayeredPane.setLayout(null);
                } else if (oldLayout instanceof FrameModalLayout) {
                    ((FrameModalLayout) oldLayout).setOtherComponent(null, null);
                }
            }

            // remove
            map.remove(rootPaneContainer);
            windowLayeredPane.remove(toastContainerLayer);
            toastContainerLayer.remove();
        }
    }

    private ToastContainerLayer createToastContainerLayered(RootPaneContainer rootPaneContainer) {
        ToastContainerLayer layeredPane = new ToastContainerLayer();
        layeredPane.setVisible(false);
        return layeredPane;
    }

    public enum Type {
        DEFAULT, SUCCESS, INFO, WARNING, ERROR
    }
}
