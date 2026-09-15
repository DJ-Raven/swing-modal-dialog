package test.internalframe;

import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.modal.Toast;
import raven.modal.component.SimpleModalBorder;
import raven.modal.demo.simple.SimpleMessageModal;
import raven.modal.option.Option;
import test.base.BaseFrame;

import javax.swing.*;
import java.awt.*;

/**
 * Demonstrates showing {@link ModalDialog} and {@link Toast} from a component
 * that lives inside a {@link JInternalFrame}.
 * <p>
 * The library walks up the component hierarchy until it finds a
 * {@link javax.swing.RootPaneContainer} (a {@code JFrame}, {@code JDialog} or
 * {@code JInternalFrame}) and attaches the modal/toast layer there. That means a
 * modal or toast shown from inside a {@code JInternalFrame} is confined to that
 * internal frame, while one shown from the {@code JDesktopPane} (or the outer
 * {@code JFrame}) covers the whole window.
 */
public class TestInternalFrame extends BaseFrame {

    private final JDesktopPane desktopPane;
    private int internalFrameCount = 0;

    public TestInternalFrame() {
        super("Test Modal / Toast inside JInternalFrame");
        Option.getDefault().setHeavyWeight(true);

        Toast.getDefaultOption().setHeavyWeight(true);
        setLayout(new BorderLayout());

        desktopPane = new JDesktopPane();
        add(desktopPane, BorderLayout.CENTER);

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        JButton cmdAddInternalFrame = new JButton("Add internal frame");
        cmdAddInternalFrame.addActionListener(e -> addInternalFrame());
        toolBar.add(cmdAddInternalFrame);

        JButton cmdShowFrameModal = new JButton("Show modal on desktop pane");
        cmdShowFrameModal.addActionListener(e -> showModal(desktopPane, "desktop_modal"));
        toolBar.add(cmdShowFrameModal);

        JButton cmdShowFrameToast = new JButton("Show toast on desktop pane");
        cmdShowFrameToast.addActionListener(e -> showToast(desktopPane));
        toolBar.add(cmdShowFrameToast);

        JButton cmdCloseAllModal = new JButton("Close all modal");
        cmdCloseAllModal.addActionListener(e -> ModalDialog.closeAllModal());
        toolBar.add(cmdCloseAllModal);

        add(toolBar, BorderLayout.NORTH);

        addInternalFrame();
        addInternalFrame();
    }

    private void addInternalFrame() {
        internalFrameCount++;
        int index = internalFrameCount;

        JInternalFrame internalFrame = new JInternalFrame(
                "Internal Frame " + index, true, true, true, true);
        internalFrame.setLayout(new MigLayout("wrap,al center center", "", ""));
        internalFrame.setSize(320, 220);
        internalFrame.setLocation(20 + (index - 1) * 30, 20 + (index - 1) * 30);

        JButton cmdShowModal = new JButton("Show modal");
        cmdShowModal.addActionListener(e -> showModal(internalFrame, "modal_" + index));
        internalFrame.add(cmdShowModal);

        JButton cmdShowToast = new JButton("Show toast");
        cmdShowToast.addActionListener(e -> showToast(internalFrame));
        internalFrame.add(cmdShowToast);

        desktopPane.add(internalFrame);
        internalFrame.setVisible(true);
        try {
            internalFrame.setSelected(true);
        } catch (java.beans.PropertyVetoException ignored) {
        }
    }

    private void showModal(Component owner, String id) {
        if (ModalDialog.isIdExist(id)) {
            return;
        }
        Option option = ModalDialog.createOption();
        option.setHeavyWeight(true);
        ModalDialog.showModal(owner, new SimpleMessageModal(
                SimpleMessageModal.Type.INFO,
                "This modal is confined to its nearest JInternalFrame (or the desktop pane, if shown from there).",
                "Modal inside JInternalFrame",
                SimpleModalBorder.YES_NO_OPTION,
                (controller, action) -> System.out.println("Modal [" + id + "] action: " + action)
        ), option, id);
    }

    private void showToast(Component owner) {
        Toast.show(owner, Toast.Type.INFO, "Toast anchored to " +
                (owner instanceof JInternalFrame ? ((JInternalFrame) owner).getTitle() : "the desktop pane"));
    }

    public static void main(String[] args) {
        installLaf();
        EventQueue.invokeLater(() -> new TestInternalFrame().setVisible(true));
    }
}
