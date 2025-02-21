package raven.swingpack.dropper.event;

import raven.swingpack.dropper.FileProgress;

import java.io.File;
import java.util.EventObject;
import java.util.function.Consumer;

/**
 * @author Raven
 */
public class FileDropperEvent extends EventObject {

    public static final int REJECT = -1;

    protected int type;
    protected File file;
    protected Consumer<FileProgress> fileProgress;

    public FileDropperEvent(Object source, File file) {
        super(source);
        this.file = file;
    }

    public void reject() {
        type = REJECT;
    }

    public void createFileProgress(Consumer<FileProgress> fileProgress) {
        this.fileProgress = fileProgress;
    }

    public int getType() {
        return type;
    }

    public File getFile() {
        return file;
    }

    public Consumer<FileProgress> getFileProgress() {
        return fileProgress;
    }
}
