package test;

import net.miginfocom.swing.MigLayout;
import raven.extras.LightDarkButton;
import raven.modal.ModalDialog;
import raven.modal.Toast;
import raven.modal.component.SimpleModalBorder;
import raven.modal.demo.simple.SimpleMessageModal;
import raven.modal.option.BorderOption;
import raven.modal.option.Option;
import raven.modal.toast.option.ToastBorderStyle;
import raven.modal.toast.option.ToastOption;
import raven.modal.toast.option.ToastStyle;
import test.base.BaseFrame;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class TestModal extends BaseFrame {

    private int count = 1;

    public TestModal() {
        super("Test Modal");
        setLayout(new MigLayout("wrap,al center center", "", ""));

        // option
        installDefaultOption();

        JSplitPane splitPane = new JSplitPane();

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBorder(new LineBorder(new Color(23, 116, 150)));
        JPanel panelModal = new JPanel(new MigLayout());
        JPanel panelToast = new JPanel(new MigLayout());

        // modal
        JButton cmdShowModal = new JButton("Show modal");
        panelModal.add(cmdShowModal);
        cmdShowModal.addActionListener(e -> {
            if (!ModalDialog.isIdExist("modal_id")) {
                showCustomModal(panelModal, SimpleMessageModal.Type.SUCCESS);
            }
        });

        // toast
        JButton cmdToast = new JButton("Show toast");
        panelToast.add(cmdToast);
        cmdToast.addActionListener(e -> {
            Toast.show(panelToast, Toast.Type.WARNING, "Sets the default hide mode for the layout. " + (count++));
        });

        add(splitPane, "w 90%,h 80%");

        tabbedPane.addTab("Tabb 001", panelModal);
        tabbedPane.addTab("Tabb 002", panelToast);
        splitPane.setLeftComponent(tabbedPane);

        JButton cmdChanged = new JButton("Move tabb to here");
        splitPane.setRightComponent(cmdChanged);

        cmdChanged.addActionListener(e -> {
            Component rightComponent = splitPane.getRightComponent();
            splitPane.setLeftComponent(null);
            splitPane.setRightComponent(null);
            if (rightComponent == cmdChanged) {
                splitPane.setLeftComponent(cmdChanged);
                splitPane.setRightComponent(tabbedPane);
            } else {
                splitPane.setRightComponent(cmdChanged);
                splitPane.setLeftComponent(tabbedPane);
            }
        });

        splitPane.setResizeWeight(0.5);

        // other component
        LightDarkButton lightDarkButton = new LightDarkButton();
        lightDarkButton.installAutoLafChangeListener();
        add(lightDarkButton);

        // applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        JButton cmdCloseAll = new JButton("close all modal");

        cmdCloseAll.addActionListener(e -> {
            ModalDialog.closeAllModal();
        });
        add(cmdCloseAll);
    }

    private void showCustomModal(Component com, SimpleMessageModal.Type type) {
        Option option = ModalDialog.createOption();
        String message = "Hello! I hope you're having a wonderful day." +
                "\nI wanted to take a moment to check in and see how you're doing." +
                "\nWhether you've been working on any exciting projects," +
                "\nencountered any interesting challenges," +
                "\nor simply had a good time relaxing, I'd love to hear about it." +
                "\nIt's always great to catch up and share our experiences.";
        ModalDialog.showModal(com, new SimpleMessageModal(type, message, "This is a modal custom message", SimpleModalBorder.YES_NO_OPTION, (controller, action) -> {
            System.out.println("Action: " + action);
        }), option, "modal_id");
    }

    private void installDefaultOption() {
        // modal option
        Option modalOption = ModalDialog.getDefaultOption();

        modalOption
                .setHeavyWeight(true)
                .getBorderOption()
                .setShadow(BorderOption.Shadow.MEDIUM)
        ;
        modalOption.getLayoutOption()
                .setMovable(true)
                .setRelativeToOwner(true)
        ;
        modalOption.getBorderOption()
                .setBorderWidth(1);

        // toast option

        ToastOption toastOption = Toast.getDefaultOption();
        toastOption
                .setHeavyWeight(true)
                .setAutoClose(false)

                .getLayoutOption()
                .setRelativeToOwner(true)
        ;

        toastOption.getStyle()
                .setBackgroundType(ToastStyle.BackgroundType.GRADIENT)

                .getBorderStyle()
                .setBorderType(ToastBorderStyle.BorderType.LEADING_LINE)
        ;
    }

    public static void main(String[] args) {
        installLaf();
        EventQueue.invokeLater(() -> new TestModal().setVisible(true));
    }
}
