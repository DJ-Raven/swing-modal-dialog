package raven.swingpack.dropper.event;

import java.util.EventObject;

/**
 * @author Raven
 */
public class FileDropperModalEvent extends EventObject {

    public static final int INSERT = 1;
    public static final int UPDATE = 0;
    public static final int DELETE = -1;

    protected int type;
    protected int firstIndex;
    protected int lastIndex;

    public FileDropperModalEvent(Object source, int index, int type) {
        this(source, index, index, type);
    }

    public FileDropperModalEvent(Object source, int firstIndex, int lastIndex, int type) {
        super(source);
        this.firstIndex = firstIndex;
        this.lastIndex = lastIndex;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public int getFirstIndex() {
        return firstIndex;
    }

    public int getLastIndex() {
        return lastIndex;
    }
}
