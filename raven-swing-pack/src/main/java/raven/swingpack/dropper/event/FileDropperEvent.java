package raven.swingpack.dropper.event;

import java.io.File;
import java.util.EventObject;

/**
 * @author Raven
 */
public class FileDropperEvent extends EventObject {

    public static final int REJECT = -1;

    private int type;
    private File file;

    public FileDropperEvent(Object source, File file) {
        super(source);
        this.file = file;
    }

    public void reject() {
        type = REJECT;
    }

    public int getType() {
        return type;
    }

    public File getFile() {
        return file;
    }
}
