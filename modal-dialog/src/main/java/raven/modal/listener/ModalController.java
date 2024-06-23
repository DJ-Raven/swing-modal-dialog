package raven.modal.listener;

import java.util.EventObject;

/**
 * @author Raven
 */
public abstract class ModalController extends EventObject {

    private boolean consume;

    public ModalController(Object source) {
        super(source);
    }

    public boolean getConsume() {
        return consume;
    }

    public void consume() {
        consume = true;
    }

    public abstract void close();
}
