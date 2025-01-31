package raven.modal.demo.component.flalaf;

import raven.modal.toast.PromiseIcon;
import raven.modal.toast.icon.RollingIcon;

import javax.swing.*;
import java.awt.event.HierarchyEvent;

public class LoadingButton extends JButton {

    private PromiseIcon promiseIcon;
    private boolean running;

    public LoadingButton() {
        this("");
    }

    public LoadingButton(String text) {
        super(text);
        init();
    }

    private void init() {
        promiseIcon = new RollingIcon(17);
        addHierarchyListener(e -> {
            if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0 || (e.getChangeFlags() & HierarchyEvent.DISPLAYABILITY_CHANGED) != 0) {
                if (e.getChanged().isShowing()) {
                    if (isShowing() && running) {
                        promiseIcon.start();
                    }
                } else {
                    if (!isShowing() && running) {
                        promiseIcon.stop();
                    }
                }
            }
        });
        start();
    }

    public void start() {
        promiseIcon.start();
        running = true;
        setIcon(promiseIcon);
    }

    public void stop() {
        promiseIcon.stop();
        running = false;
        setIcon(null);
    }

    public boolean isRunning() {
        return running;
    }
}
