package test;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import raven.extras.LightDarkButton;
import raven.swingpack.dropper.FileDropper;
import raven.swingpack.dropper.event.FileDropperEvent;
import raven.swingpack.dropper.event.FileDropperListener;
import test.base.BaseFrame;

import javax.swing.*;
import java.awt.*;

public class TestFileDropper extends BaseFrame {

    public TestFileDropper() {
        super("Test File Dropper");
        setLayout(new MigLayout("wrap,al center center"));
        FileDropper fileDropper = new FileDropper();
        fileDropper.putClientProperty(FlatClientProperties.STYLE, "" +
                "border:3,3,3,3,$Component.borderColor,1,15;");

        add(fileDropper, "w 500,h 250");
        JButton cmdClear = new JButton("Clear");
        cmdClear.addActionListener(e -> {
            fileDropper.clearAllFile();
        });

        fileDropper.addFileDropperListener(new FileDropperListener() {

            @Override
            public void fileDragEnter(FileDropperEvent fileDropperEvent) {
                if (fileDropperEvent.getFile().length() > 204800) {
                    fileDropperEvent.reject();
                }
            }

            @Override
            public void fileDropped(FileDropperEvent fileDropperEvent) {
                if (fileDropperEvent.getFile().length() > 204800) {
                    if (confirm("File size bigger than 200KB ?") == false) {
                        fileDropperEvent.reject();
                    }
                }
            }

            @Override
            public void fileOnDelete(FileDropperEvent fileDropperEvent) {
                if (confirm("Are you sure to delete ?") == false) {
                    fileDropperEvent.reject();
                }
            }

            @Override
            public void fileOnView(FileDropperEvent fileDropperEvent) {
                try {
                    Desktop.getDesktop().open(fileDropperEvent.getFile());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        fileDropper.addDropFilePlaceholderListener(actionEvent -> {
            JFileChooser chooser = new JFileChooser();
            chooser.showOpenDialog(TestFileDropper.this);
        });

        // dropper 2
        FileDropper fileDropper2 = new FileDropper(fileDropper.getModel());
        fileDropper2.putClientProperty(FlatClientProperties.STYLE, "" +
                "border:5,5,5,5,$Component.borderColor,1,15;");
        fileDropper2.setDropPlaceholder(null);

        add(fileDropper2, "w 500,h 250");

        add(cmdClear);
        LightDarkButton lightDarkButton = new LightDarkButton();
        lightDarkButton.installAutoLafChangeListener();
        add(lightDarkButton);
    }

    private boolean confirm(String message) {
        return JOptionPane.showConfirmDialog(TestFileDropper.this, message, "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
    }

    public static void main(String[] args) {
        installLaf();
        EventQueue.invokeLater(() -> new TestFileDropper().setVisible(true));
    }
}
