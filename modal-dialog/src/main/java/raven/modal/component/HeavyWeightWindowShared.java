package raven.modal.component;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Raven
 */
public class HeavyWeightWindowShared {

    private static HeavyWeightWindowShared instance;
    private Map<Window, ModalWindow> map;

    public static HeavyWeightWindowShared getInstance() {
        if (instance == null) {
            instance = new HeavyWeightWindowShared();
        }
        return instance;
    }

    private HeavyWeightWindowShared() {
        map = new HashMap<>();
    }

    public ModalWindow getWindowShared(Window parentWindow) {
        if (map.containsKey(parentWindow)) {
            return map.get(parentWindow);
        }
        ModalWindow window = createWindow(parentWindow);
        map.put(parentWindow, window);
        return window;
    }

    public void remove(Window window) {
        map.remove(window);
    }

    private ModalWindow createWindow(Window parent) {
        ModalWindow window = new ModalWindow(parent);
        return window;
    }
}
