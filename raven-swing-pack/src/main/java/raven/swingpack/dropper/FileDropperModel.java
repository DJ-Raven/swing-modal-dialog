package raven.swingpack.dropper;

import raven.swingpack.dropper.event.FileDropperModalEvent;
import raven.swingpack.dropper.event.FileDropperModelListener;

import javax.swing.event.EventListenerList;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Raven
 */
public class FileDropperModel {

    protected EventListenerList listenerList = new EventListenerList();

    private final List<File> lists = new ArrayList<>();

    public File[] getFiles() {
        return lists.toArray(new File[lists.size()]);
    }

    public File getFile(int index) {
        return lists.get(index);
    }

    public int getSize() {
        return lists.size();
    }

    public synchronized void removeAt(int index) {
        if (index < lists.size()) {
            File file = lists.remove(index);
            if (file != null) {
                fireFileDropperModelChanged(new FileDropperModalEvent(this, FileDropperModalEvent.DELETE, null, file));
            }
        }
    }

    public void removeAt(File file) {
        int index = lists.indexOf(file);
        removeAt(index);
    }

    public synchronized void removeAll() {
        if (!lists.isEmpty()) {
            File[] files = getFiles();
            lists.clear();
            fireFileDropperModelChanged(new FileDropperModalEvent(this, FileDropperModalEvent.DELETE, null, files));
        }
    }

    public void add(File file) {
        add(lists.size(), file);
    }

    public synchronized void add(int index, File file) {
        lists.add(index, file);
        fireFileDropperModelChanged(new FileDropperModalEvent(this, FileDropperModalEvent.INSERT, getBeforeOf(index), file));
    }

    private File getBeforeOf(int index) {
        if (index + 1 < lists.size()) {
            return lists.get(index + 1);
        }
        return null;
    }

    public void addFileDropperModelListener(FileDropperModelListener listener) {
        listenerList.add(FileDropperModelListener.class, listener);
    }

    public void removeFileDropperModelListener(FileDropperModelListener listener) {
        listenerList.remove(FileDropperModelListener.class, listener);
    }

    public void fireFileDropperModelChanged(FileDropperModalEvent event) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == FileDropperModelListener.class) {
                ((FileDropperModelListener) listeners[i + 1]).fileDropperChanged(event);
            }
        }
    }
}
