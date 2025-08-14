package raven.modal.toast.option;

import raven.modal.event.ToastCallback;
import raven.modal.event.ToastListener;

import javax.swing.event.EventListenerList;
import java.awt.event.MouseEvent;
import java.util.EventListener;

/**
 * This class for toast event
 *
 * @author Raven
 */
public class ToastEvent {

    public static ToastEvent getDefault() {
        return new ToastEvent();
    }

    private ToastEvent(EventListenerList listenerList) {
        this.listenerList = listenerList;
    }

    public ToastEvent() {
    }

    private EventListenerList listenerList = new EventListenerList();

    public ToastEvent addListener(ToastListener listener) {
        listenerList.add(ToastListener.class, listener);
        return this;
    }

    public ToastEvent removeListener(ToastListener listener) {
        listenerList.remove(ToastListener.class, listener);
        return this;
    }

    public void fireMousePressed(MouseEvent event, ToastCallback callback) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ToastListener.class) {
                ((ToastListener) listeners[i + 1]).mousePressed(event, callback);
            }
        }
    }

    public void fireMouseReleased(MouseEvent event, ToastCallback callback) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ToastListener.class) {
                ((ToastListener) listeners[i + 1]).mouseReleased(event, callback);
            }
        }
    }

    public ToastEvent copy() {
        return new ToastEvent(copyEvent(listenerList));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private EventListenerList copyEvent(EventListenerList original) {
        EventListenerList copy = new EventListenerList();
        Object[] listeners = original.getListenerList();

        for (int i = 0; i < listeners.length; i += 2) {
            Class<? extends EventListener> listenerType =
                    (Class<? extends EventListener>) listeners[i];
            EventListener listener = (EventListener) listeners[i + 1];
            copy.add((Class) listenerType, listener);
        }
        return copy;
    }
}
