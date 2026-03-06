package raven.modal.toast;

import raven.modal.toast.option.ToastLocation;

import java.awt.*;

/**
 * @author Raven
 */
public interface BaseToastContainer {

    void add(ToastPanel toastPanel);

    void remove(ToastPanel toastPanel);

    void closeAll();

    void closeAllImmediately();

    boolean close(String id);

    boolean closeImmediately(String id);

    void closeAll(ToastLocation location);

    boolean checkPromiseId(String id);

    boolean checkId(String id);

    void updateLayout();

    void updateLayout(Component owner);
}
