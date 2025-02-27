package raven.modal.component;

import raven.modal.option.Option;
import raven.modal.slider.PanelSlider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * @author Raven
 */
public class HeavyWeightModalController extends AbstractModalController {

    public Component getOwner() {
        return owner;
    }

    private final BaseModalContainer baseModalContainer;
    private final Component owner;
    private ActionListener escapeAction;

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
        installOption();
    }

    @Override
    public void closeModal() {
        baseModalContainer.remove(this);
        uninstallOption();
    }

    private void installOption() {
        if (option.isCloseOnPressedEscape()) {
            escapeAction = e -> closeModal();
            registerKeyboardAction(escapeAction, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        }
    }

    private void uninstallOption() {
        if (escapeAction != null) {
            unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
        }
    }
}
