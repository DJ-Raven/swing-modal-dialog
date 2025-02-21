package raven.swingpack.dropper;

/**
 * @author Raven
 */
public class FileProgress {

    protected final ProgressCallback callback;

    protected float value;

    protected FileProgress(ProgressCallback callback) {
        this.callback = callback;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
        if (callback != null) {
            callback.valueChanged(value);
        }
    }

    public void setMessage(String message) {
        if (callback != null) {
            callback.messageChanged(message);
        }
    }

    public void error(String message) {
        if (callback != null) {
            callback.error(message);
        }
    }

    public void complete() {
        if (callback != null) {
            callback.complete();
        }
    }

    protected interface ProgressCallback {

        void valueChanged(float value);

        void messageChanged(String message);

        void error(String message);

        void complete();
    }
}
