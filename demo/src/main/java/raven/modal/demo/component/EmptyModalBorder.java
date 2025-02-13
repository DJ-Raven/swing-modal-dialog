package raven.modal.demo.component;

import net.miginfocom.swing.MigLayout;
import raven.modal.component.Modal;
import raven.modal.component.ModalBorderAction;
import raven.modal.listener.ModalCallback;
import raven.modal.listener.ModalController;

import java.awt.*;

public class EmptyModalBorder extends Modal implements ModalBorderAction {

    protected final Component component;
    public static final int OPENED = 20;
    private final ModalCallback callback;

    public EmptyModalBorder(Component component) {
        this(component, null);
    }

    public EmptyModalBorder(Component component, ModalCallback callback) {
        this.component = component;
        this.callback = callback;
        setLayout(new MigLayout("fill,insets 8 0 8 0", "[fill]", "[fill]"));
        add(component);
    }

    @Override
    protected void modalOpened() {
        if (callback != null) {
            callback.action(createController(), OPENED);
        }
    }

    @Override
    public void doAction(int action) {
        if (callback == null) {
            getController().closeModal();
        } else {
            ModalController controller = createController();
            callback.action(controller, action);
            if (!controller.getConsume()) {
                getController().closeModal();
            }
        }
    }

    @Override
    public Color getBackground() {
        if (component == null) {
            return super.getBackground();
        }
        return component.getBackground();
    }

    private ModalController createController() {
        return new ModalController(this) {
            @Override
            public void close() {
                getController().closeModal();
            }
        };
    }
}
