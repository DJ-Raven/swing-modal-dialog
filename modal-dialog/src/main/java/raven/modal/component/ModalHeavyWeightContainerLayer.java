package raven.modal.component;

import java.awt.*;
import java.util.Map;

/**
 * @author Raven
 */
public class ModalHeavyWeightContainerLayer extends AbstractModalContainerLayer implements HeavyWindowAction {

    private final Map<Window, ModalHeavyWeightContainerLayer> map;
    private final Window parentWindow;
    private ModalWindow window;

    public ModalHeavyWeightContainerLayer(Map<Window, ModalHeavyWeightContainerLayer> map, Window parentWindow) {
        this.map = map;
        this.parentWindow = parentWindow;
        init();
    }

    private void init() {
        window = HeavyWeightWindowShared.getInstance().getWindowShared(parentWindow);
        window.addModal(this);
        window.updateLayout();
    }

    @Override
    public void showContainer(boolean show) {
        window.updateLayout();
        window.setShowWindowAndCheck(show);
    }

    @Override
    public void removeContainer(ModalContainer container) {
        super.removeContainer(container);
        if (containers.isEmpty()) {
            window.setShowWindowAndCheck(false);
        }
    }

    @Override
    public void windowRemoved() {
        closeAllModalImmediately();
        map.remove(parentWindow);
    }

    @Override
    public boolean isEmptyItem() {
        return containers.isEmpty();
    }
}
