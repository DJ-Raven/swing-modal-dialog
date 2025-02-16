package raven.swingpack.dropper.event;

import java.util.EventListener;

/**
 * @author Raven
 */
public interface FileDropperModelListener extends EventListener {

    void fileDropperChanged(FileDropperModalEvent fileDropperModalEvent);
}
