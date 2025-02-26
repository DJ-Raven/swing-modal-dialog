package raven.modal.component;

import raven.modal.option.Option;
import raven.modal.slider.PanelSlider;

import java.awt.*;

/**
 * @author Raven
 */
public class HeavyWeightModalController extends AbstractModalController {

    public Component getOwner() {
        return owner;
    }

    private final BaseModalContainer baseModalContainer;
    private final Component owner;

    public HeavyWeightModalController(BaseModalContainer baseModalContainer, Component owner, Option option) {
        super(option);
        this.baseModalContainer = baseModalContainer;
        this.owner = owner;
    }

    @Override
    public ModalContainer getModalContainer() {
        return null;
    }

    @Override
    protected PanelSlider.PaneSliderLayoutSize createSliderLayoutSize() {
        return (container, component) -> container.getSize();
    }

    @Override
    protected void onModalComponentInstalled() {
    }

    @Override
    protected void onShowing() {
    }

    @Override
    public void closeModal() {
        baseModalContainer.remove(this);
    }
}
