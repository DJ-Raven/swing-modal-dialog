package raven.swingpack.dropper;

import raven.swingpack.dropper.event.FileDropperEvent;
import raven.swingpack.dropper.event.FileDropperListener;
import raven.swingpack.dropper.event.FileDropperModalEvent;
import raven.swingpack.dropper.event.FileDropperModelListener;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

/**
 * @author Raven
 */
public class FileDropper extends JPanel implements FileDropperModelListener {

    private FileDropperModel model;
    private FileViewer fileViewer;
    private PanelDropper panelDropper;

    public FileDropper() {
        this(null);
    }

    public FileDropper(FileDropperModel model) {
        this.model = model;
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        panelDropper = new PanelDropper(this);
        fileViewer = new FileViewer();

        if (model == null) {
            model = createDefaultFileDropperModal();
        }
        setModel(model);

        installLayer(panelDropper);
        add(panelDropper);
    }

    private FileDropperModel createDefaultFileDropperModal() {
        return new FileDropperModel();
    }

    public FileDropperModel getModel() {
        return model;
    }

    public void setModel(FileDropperModel model) {
        if (this.model != null) {
            this.model.removeFileDropperModelListener(this);
        }
        this.model = model;
        if (this.model != null) {
            this.model.addFileDropperModelListener(this);
        }
        updateFileDropperComponent();
    }

    public FileViewer getFileViewer() {
        return fileViewer;
    }

    public void setFileViewer(FileViewer fileViewer) {
        this.fileViewer = fileViewer;
    }

    public Component getDropPlaceholder() {
        return panelDropper.getDropPlaceholder();
    }

    public void setDropPlaceholder(Component dropPlaceholder) {
        panelDropper.setDropPlaceholder(dropPlaceholder);
    }

    @Override
    public void fileDropperChanged(FileDropperModalEvent evt) {
        if (evt.getType() == FileDropperModalEvent.INSERT) {
            panelDropper.addFileAsDropped(evt.getBeforeOf(), evt.getFiles());
        } else if (evt.getType() == FileDropperModalEvent.DELETE) {
            panelDropper.removeFile(evt.getFiles());
        }
    }

    private void updateFileDropperComponent() {
        panelDropper.removeAllFile();

        if (model != null) {
            panelDropper.addFileAsDropped(null, model.getFiles());
        }
    }

    private void installLayer(JComponent component) {

        DropTarget dropTarget = new DropTarget(component, new DropTargetListener() {
            @Override
            public void dragEnter(DropTargetDragEvent dte) {
                if (dte.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    try {
                        dte.acceptDrag(DnDConstants.ACTION_COPY);
                        List<File> files = (List<File>) dte.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                        if (!files.isEmpty()) {
                            boolean added = panelDropper.prepareFile(files);
                            if (!added) {
                                dte.rejectDrag();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void dragOver(DropTargetDragEvent dte) {

            }

            @Override
            public void dropActionChanged(DropTargetDragEvent dte) {
            }

            @Override
            public void dragExit(DropTargetEvent dte) {
                panelDropper.exit();
            }

            @Override
            public void drop(DropTargetDropEvent dte) {
                panelDropper.drop(dte.getLocation());
            }
        });
    }

    public void clearAllFile() {
        getModel().removeAll();
    }

    public void addFileDropperListener(FileDropperListener listener) {
        listenerList.add(FileDropperListener.class, listener);
    }

    public void removeFileDropperListener(FileDropperListener listener) {
        listenerList.remove(FileDropperListener.class, listener);
    }

    public void addDropFilePlaceholderListener(ActionListener listener) {
        listenerList.add(ActionListener.class, listener);
    }

    public void removeDropFilePlaceholderListener(ActionListener listener) {
        listenerList.remove(ActionListener.class, listener);
    }

    public void fireFileDragEnter(FileDropperEvent event) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == FileDropperListener.class) {
                ((FileDropperListener) listeners[i + 1]).fileDragEnter(event);
            }
        }
    }

    public void fireFileDropped(FileDropperEvent event) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == FileDropperListener.class) {
                ((FileDropperListener) listeners[i + 1]).fileDropped(event);
            }
        }
    }

    public void fireFileDelete(FileDropperEvent event) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == FileDropperListener.class) {
                ((FileDropperListener) listeners[i + 1]).fileOnDelete(event);
            }
        }
    }

    public void fireFileView(FileDropperEvent event) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == FileDropperListener.class) {
                ((FileDropperListener) listeners[i + 1]).fileOnView(event);
            }
        }
    }

    public void fireDropPlaceholderSelected(ActionEvent event) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ActionListener.class) {
                ((ActionListener) listeners[i + 1]).actionPerformed(event);
            }
        }
    }
}
