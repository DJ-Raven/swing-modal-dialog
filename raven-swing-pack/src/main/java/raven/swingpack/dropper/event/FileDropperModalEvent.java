package raven.swingpack.dropper.event;

import java.io.File;
import java.util.EventObject;

/**
 * @author Raven
 */
public class FileDropperModalEvent extends EventObject {

    public static final int INSERT = 1;
    public static final int UPDATE = 0;
    public static final int DELETE = -1;

    protected int type;
    protected File[] files;
    protected File beforeOf;

    public FileDropperModalEvent(Object source, int type, File beforeOf, File... files) {
        super(source);
        this.type = type;
        this.beforeOf = beforeOf;
        this.files = files;
    }

    public int getType() {
        return type;
    }

    public File getBeforeOf() {
        return beforeOf;
    }

    public File[] getFiles() {
        return files;
    }

    public File getFile() {
        if (files == null || files.length == 0) {
            return null;
        }
        return files[0];
    }
}
