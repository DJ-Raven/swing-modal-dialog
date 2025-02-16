package raven.swingpack.dropper.event;

import java.util.EventListener;

/**
 * @author Raven
 */
public interface FileDropperListener extends EventListener {

    void fileDropped(FileDropperEvent fileDropperEvent);

    void fileOnDelete(FileDropperEvent fileDropperEvent);

    void fileOnView(FileDropperEvent fileDropperEvent);
}
