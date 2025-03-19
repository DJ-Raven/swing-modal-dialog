package raven.modal.component;

import raven.modal.option.Option;
import raven.modal.slider.PanelSlider;
import raven.modal.utils.ModalMouseMovableListener;
import raven.modal.utils.ModalWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;

/**
 * @author Raven
 */
public class HeavyWeightModalController extends AbstractModalController {

    public Component getOwner() {
        return owner;
    }

    public ModalWindow getModalWindow() {
        return modalWindow;
    }

    private final BaseModalContainer baseModalContainer;
    private final Component owner;
    private ModalWindow modalWindow;
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
    protected MouseAdapter createMouseMovableListener() {
        return new ModalMouseMovableListener(this) {
            @Override
            protected Container getParent() {
                return (Container) owner;
            }

            @Override
            protected Component getOwner() {
                return owner;
            }

            @Override
            protected void updateLayout() {
                baseModalContainer.updateLayout();
            }
        };
    }

    @Override
    protected void onModalComponentInstalled() {

    }

    @Override
    protected void onShowing() {
    }

    @Override
    public void popModal() {
        super.popModal();
        baseModalContainer.updateLayout();
    }

    @Override
    public void closeModal() {
        baseModalContainer.remove(this);
        uninstallOption();
    }

    public void setModalWindow(ModalWindow modalWindow) {
        this.modalWindow = modalWindow;
        installOption();
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
