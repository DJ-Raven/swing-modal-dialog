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

    public void removeAt(int index) {
        if (index < lists.size()) {
            lists.remove(index);
            fireFileDropperModelChanged(new FileDropperModalEvent(this, index, FileDropperModalEvent.DELETE));
        }
    }

    public void removeAll() {
        if (!lists.isEmpty()) {
            int startIndex = 0;
            int lastIndex = lists.size() - 1;
            lists.clear();
            fireFileDropperModelChanged(new FileDropperModalEvent(this, startIndex, lastIndex, FileDropperModalEvent.DELETE));
        }
    }

    public void add(File file) {
        lists.add(file);
        fireFileDropperModelChanged(new FileDropperModalEvent(this, lists.size() - 1, FileDropperModalEvent.INSERT));
    }

    public void add(int index, File file) {
        lists.add(index, file);
        fireFileDropperModelChanged(new FileDropperModalEvent(this, index, FileDropperModalEvent.INSERT));
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
