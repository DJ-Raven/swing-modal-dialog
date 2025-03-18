package raven.swingpack.dropper;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Raven
 */
public class FileViewer {

    private Set<String> IMAGE_SUPPORT_EXTENSION = new HashSet<>(
            Arrays.asList("jpg", "png", "jpeg")
    );

    public FileViewer() {
    }

    public boolean isSupported(File file) {
        String extension = getFileExtension(file.getName());
        if (extension == null) return false;

        return IMAGE_SUPPORT_EXTENSION.contains(extension.toLowerCase());
    }

    public String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex + 1);
        }
        return null;
    }

    public Icon getFileViewer(File file) {
        if (isSupported(file)) {
            return new ImageIcon(file.getAbsolutePath());
        }
        return getUnsupportedFileViewer(file);
    }

    public Icon getUnsupportedFileViewer(File file) {
        return new FlatSVGIcon("raven/swingpack/icons/file.svg", 10f);
    }
}
