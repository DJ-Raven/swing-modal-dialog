package raven.modal.toast;

import javax.swing.*;

/**
 * Use for custom toast
 * It's provide method like close toast ...
 *
 * @author Raven
 */
public class ToastCustomPanel extends JPanel implements ToastCustom {

    @Override
    public void initToastAction(Action toastAction) {
        this.toastAction = toastAction;
    }

    protected ToastCustom.Action toastAction;
}
