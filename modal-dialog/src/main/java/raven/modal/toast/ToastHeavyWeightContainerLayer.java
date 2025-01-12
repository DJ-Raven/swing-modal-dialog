package raven.modal.toast;

import raven.modal.component.HeavyWeightWindowShared;
import raven.modal.component.HeavyWindowAction;
import raven.modal.component.ModalWindow;

import java.awt.*;
import java.util.Map;

/**
 * @author Raven
 */
public class ToastHeavyWeightContainerLayer extends AbstractToastContainerLayer implements HeavyWindowAction {
    private final Map<Window, ToastHeavyWeightContainerLayer> map;
    private final Window parentWindow;
    private ModalWindow window;

    public ToastHeavyWeightContainerLayer(Map<Window, ToastHeavyWeightContainerLayer> map, Window parentWindow) {
        this.map = map;
        this.parentWindow = parentWindow;
        init();
    }

    private void init() {
        window = HeavyWeightWindowShared.getInstance().getWindowShared(parentWindow);
        window.addToast(this);
        window.updateLayout();
    }

    @Override
    public void showContainer(boolean show) {
        layeredPane.setVisible(show);
        window.updateLayout();
        window.setShowWindowAndCheck(show);
    }

    @Override
    public void windowRemoved() {
        closeAllImmediately();
        map.remove(parentWindow);
    }

    @Override
    public boolean isEmptyItem() {
        return toastPanels.isEmpty();
    }
}
