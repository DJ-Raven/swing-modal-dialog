package raven.modal.event;

import java.awt.event.MouseEvent;
import java.util.EventListener;

/**
 * @author Raven
 */
public interface ToastListener extends EventListener {

    void mousePressed(MouseEvent event, ToastCallback callback);

    void mouseReleased(MouseEvent event, ToastCallback callback);
}
