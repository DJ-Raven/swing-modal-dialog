package raven.modal.event;

import java.awt.event.MouseEvent;

/**
 * Abstract adapter class for ToastListener
 *
 * @author Raven
 */
public abstract class ToastAdapter implements ToastListener {

    @Override
    public void mousePressed(MouseEvent event, ToastCallback callback) {
    }

    @Override
    public void mouseReleased(MouseEvent event, ToastCallback callback) {
    }
}
