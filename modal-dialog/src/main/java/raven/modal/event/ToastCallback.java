package raven.modal.event;

/**
 * @author Raven
 */
public interface ToastCallback {

    void close();

    void closeAllImmediately();

    boolean isCurrenPromise();
}
