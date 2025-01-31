package raven.modal.toast;

import raven.modal.Toast;

/**
 * @author Raven
 */
public abstract class ToastPromise implements PromiseIcon.PromiseIconAction {

    @Override
    public boolean isDone() {
        return done;
    }

    protected void setDone(boolean done) {
        this.done = done;
    }

    protected String getId() {
        return id;
    }

    private Thread thread;
    private boolean done;
    private String id;

    public ToastPromise() {
    }

    public ToastPromise(String id) {
        this.id = id;
    }

    public abstract void execute(PromiseCallback callback);

    public boolean rejectAble() {
        return false;
    }

    public boolean useThread() {
        return true;
    }

    protected final void start(PromiseCallback callback) {
        if (useThread()) {
            thread = new Thread(() -> execute(callback));
            thread.start();
        } else {
            execute(callback);
        }
    }

    protected final void reject() {
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
        }
    }

    public abstract static class PromiseCallback {
        public abstract void update(String message);

        public abstract void done(Toast.Type type, String message);
    }
}
