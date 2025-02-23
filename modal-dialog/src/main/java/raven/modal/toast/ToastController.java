package raven.modal.toast;

import raven.modal.toast.option.ToastLocation;

import java.awt.*;

/**
 * @author Raven
 */
public interface ToastController {

    void add(ToastPanel toastPanel);

    void remove(ToastPanel toastPanel);

    void closeAll();

    void closeAllImmediately();

    void closeAll(ToastLocation location);

    boolean checkPromiseId(String id);

    void updateLayout();

    void updateLayout(Component owner);
}
