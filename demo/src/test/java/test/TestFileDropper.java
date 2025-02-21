package test;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import raven.extras.LightDarkButton;
import raven.swingpack.dropper.DefaultFileDropPlaceholder;
import raven.swingpack.dropper.FileDropper;
import raven.swingpack.dropper.FileProgress;
import raven.swingpack.dropper.event.FileDropperEvent;
import raven.swingpack.dropper.event.FileDropperListener;
import test.base.BaseFrame;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class TestFileDropper extends BaseFrame {

    public TestFileDropper() {
        super("Test File Dropper");
        setLayout(new MigLayout("wrap,al center center"));
        FileDropper fileDropper = new FileDropper();
        ((DefaultFileDropPlaceholder) fileDropper.getDropPlaceholder()).setDescription("JPEG, PNG, and MP4 formats, Max file size: 10MB");
        fileDropper.putClientProperty(FlatClientProperties.STYLE, "" +
                "border:3,3,3,3,$Component.borderColor,1,15;");

        add(fileDropper, "w 500,h 500");
        JButton cmdClear = new JButton("Clear");
        cmdClear.addActionListener(e -> {
            fileDropper.clearAllFile();
        });

        fileDropper.addFileDropperListener(new FileDropperListener() {

            @Override
            public void fileDragEnter(FileDropperEvent fileDropperEvent) {
                // reject on drag enter is file size bigger than 10MB
                if (fileDropperEvent.getFile().length() > 10485760) {
                    fileDropperEvent.reject();
                }
            }

            @Override
            public void fileDropped(FileDropperEvent fileDropperEvent) {
                // confirm file dropped bigger than 5MB
                if (fileDropperEvent.getFile().length() > 5242880) {
                    if (confirm("File size bigger than 5MB ?") == false) {
                        fileDropperEvent.reject();
                        return;
                    }
                }

                fileDropperEvent.createFileProgress(fileProgress -> {
                    Random random = new Random();
                    // random duration for create test progress
                    int duration = random.nextInt(20) + 10;
                    testUploadProgress(fileProgress, duration);
                });

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

        add(fileDropper2, "w 500,h 500");

        add(cmdClear);
        LightDarkButton lightDarkButton = new LightDarkButton();
        lightDarkButton.installAutoLafChangeListener();
        add(lightDarkButton);
    }

    private boolean confirm(String message) {
        return JOptionPane.showConfirmDialog(TestFileDropper.this, message, "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
    }

    private void testUploadProgress(FileProgress fileProgress, long duration) {
        new Thread(() -> {
            fileProgress.setMessage("Upload ...");
            for (int i = 0; i < 100; i++) {
                try {
                    Thread.sleep(duration);
                } catch (Exception e) {
                }
                fileProgress.setMessage(String.format("Upload (%d%%)", i));
                fileProgress.setValue(i / 100f);
            }
            fileProgress.complete();
        }).start();
    }

    public static void main(String[] args) {
        installLaf();
        EventQueue.invokeLater(() -> new TestFileDropper().setVisible(true));
    }
}
