package raven.modal.toast;

/**
 * @author Raven
 */
public interface ToastCustom {

    void initToastAction(Action action);

    interface Action {
        void close();
    }
}
