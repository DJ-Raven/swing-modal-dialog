package raven.modal.demo.forms;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.modal.component.SimpleModalBorder;
import raven.modal.demo.component.LabelButton;
import raven.modal.demo.simple.SimpleInputForms;
import raven.modal.demo.simple.SimpleMessageModal;
import raven.modal.demo.system.Form;
import raven.modal.demo.utils.SystemForm;
import raven.modal.option.Location;
import raven.modal.option.Option;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

@SystemForm(name = "Modal", description = "modal dialog user interface element", tags = {"dialog", "popup"})
public class FormModal extends Form {

    public FormModal() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("wrap,fillx,insets 7 15 7 15", "[fill]"));
        add(createInfo());
        add(createOptions());
    }

    private JPanel createInfo() {
        JPanel panel = new JPanel(new MigLayout("fillx,wrap", "[fill]"));
        JLabel title = new JLabel("Modal");
        JTextArea text = new JTextArea();
        text.setText("A library modal dialog is a user interface element that appears on top of the main application window, requiring the user's interaction to proceed.");
        text.setEditable(false);
        text.setBorder(BorderFactory.createEmptyBorder());
        title.putClientProperty(FlatClientProperties.STYLE, "" +
                "font:bold +3");

        panel.add(title);
        panel.add(text);
        return panel;
    }

    private Component createOptions() {
        JPanel panel = new JPanel(new MigLayout("wrap 2,fillx", "[grow 0,fill][fill]"));
        panel.add(createHorizontalOption());
        panel.add(createVerticalOption());
        panel.add(createModalOptions());
        panel.add(createModalOptionsClickType());
        panel.add(createSampleModal(), "span 2");
        panel.add(createCustomModal(), "span 2");
        return panel;
    }

    private Component createHorizontalOption() {
        JPanel panel = new JPanel(new MigLayout());
        panel.setBorder(new TitledBorder("Horizontal option"));
        jrLeft = new JRadioButton("Left");
        jrLeading = new JRadioButton("Leading");
        jrCenter = new JRadioButton("Center");
        jrTrailing = new JRadioButton("Trailing");
        jrRight = new JRadioButton("Right");

        jrCenter.setSelected(true);

        ButtonGroup group = new ButtonGroup();
        group.add(jrLeft);
        group.add(jrLeading);
        group.add(jrCenter);
        group.add(jrTrailing);
        group.add(jrRight);

        panel.add(jrLeft);
        panel.add(jrLeading);
        panel.add(jrCenter);
        panel.add(jrTrailing);
        panel.add(jrRight);

        return panel;
    }

    private Component createVerticalOption() {
        JPanel panel = new JPanel(new MigLayout());
        panel.setBorder(new TitledBorder("Vertical option"));

        jrTop = new JRadioButton("Top");
        jrMiddle = new JRadioButton("Center");
        jrButton = new JRadioButton("Bottom");

        jrMiddle.setSelected(true);

        ButtonGroup group = new ButtonGroup();
        group.add(jrTop);
        group.add(jrMiddle);
        group.add(jrButton);

        panel.add(jrTop);
        panel.add(jrMiddle);
        panel.add(jrButton);

        return panel;
    }

    private Component createModalOptions() {
        JPanel panel = new JPanel(new MigLayout());
        panel.setBorder(new TitledBorder("Options"));
        chAnimation = new JCheckBox("Animation enable");
        chCloseOnPressedEscape = new JCheckBox("Close on pressed escape");
        chAnimation.setSelected(true);
        chCloseOnPressedEscape.setSelected(true);

        panel.add(chAnimation);
        panel.add(chCloseOnPressedEscape);

        return panel;
    }

    private Component createModalOptionsClickType() {
        JPanel panel = new JPanel(new MigLayout());
        panel.setBorder(new TitledBorder("Background click type"));

        jrClose = new JRadioButton("Close modal");
        jrBlock = new JRadioButton("Block");
        jrNone = new JRadioButton("None");

        ButtonGroup group = new ButtonGroup();
        group.add(jrClose);
        group.add(jrBlock);
        group.add(jrNone);

        jrClose.setSelected(true);

        panel.add(jrClose);
        panel.add(jrBlock);
        panel.add(jrNone);

        return panel;
    }

    private Component createSampleModal() {
        JPanel panel = new JPanel(new MigLayout());
        panel.setBorder(new TitledBorder("Sample modal"));
        LabelButton label = new LabelButton("Show modal");
        LabelButton slide = new LabelButton("Show slide modal");

        label.addOnClick(o -> showModal(getSelectedOption()));
        slide.addOnClick(o -> showModalSlide(getSelectedOption()));

        panel.add(label);
        panel.add(slide);
        return panel;
    }

    private Component createCustomModal() {
        JPanel panel = new JPanel(new MigLayout());
        panel.setBorder(new TitledBorder("Custom message modal"));

        LabelButton lbDefault = new LabelButton("Show default");
        LabelButton lbSuccess = new LabelButton("Show success");
        LabelButton lbInfo = new LabelButton("Show info");
        LabelButton lbWarning = new LabelButton("Show warning");
        LabelButton lbError = new LabelButton("Show error");

        lbDefault.addOnClick(o -> showCustomModal(SimpleMessageModal.Type.DEFAULT, getSelectedOption()));
        lbSuccess.addOnClick(o -> showCustomModal(SimpleMessageModal.Type.SUCCESS, getSelectedOption()));
        lbInfo.addOnClick(o -> showCustomModal(SimpleMessageModal.Type.INFO, getSelectedOption()));
        lbWarning.addOnClick(o -> showCustomModal(SimpleMessageModal.Type.WARNING, getSelectedOption()));
        lbError.addOnClick(o -> showCustomModal(SimpleMessageModal.Type.ERROR, getSelectedOption()));

        panel.add(lbDefault);
        panel.add(lbSuccess);
        panel.add(lbInfo);
        panel.add(lbWarning);
        panel.add(lbError);
        return panel;
    }

    private void showModal(Option option) {
        JTextArea txt = new JTextArea();
        txt.setEditable(false);
        txt.putClientProperty(FlatClientProperties.STYLE, "" +
                "border:5,35,5,35");
        txt.setText("Sets the default hide mode for the layout. This hide mode can be overridden by the component constraint." +
                "\nThe hide mode specified how the layout manager should handle a component that isn't visible.");
        ModalDialog.showModal(this, new SimpleModalBorder(txt, "Sample Message", SimpleModalBorder.YES_NO_OPTION, null), option);
    }

    private void showCustomModal(SimpleMessageModal.Type type, Option option) {
        String message = "Hello! I hope you're having a wonderful day." +
                "\nI wanted to take a moment to check in and see how you're doing." +
                "\nWhether you've been working on any exciting projects," +
                "\nencountered any interesting challenges," +
                "\nor simply had a good time relaxing, I'd love to hear about it." +
                "\nIt's always great to catch up and share our experiences.";
        ModalDialog.showModal(this, new SimpleMessageModal(type, message, "This is a modal custom message", SimpleModalBorder.YES_NO_OPTION, null), option);
    }

    private void showModalSlide(Option option) {
        option.getLayoutOption().setSize(-1, 1f)
                .setOnTop(true)
                .setAnimateDistance(0.7f, 0);
        ModalDialog.showModal(this, new SimpleModalBorder(
                new SimpleInputForms(), "Sample Input Forms", SimpleModalBorder.YES_NO_CANCEL_OPTION,
                (controller, action) -> {
                    controller.close();
                }), option);
    }

    private Option getSelectedOption() {
        Option option = ModalDialog.createOption();
        Location h = jrLeft.isSelected() ? Location.LEFT : jrRight.isSelected() ? Location.RIGHT : jrLeading.isSelected() ? Location.LEADING : jrTrailing.isSelected() ? Location.TRAILING : Location.CENTER;
        Location v = jrTop.isSelected() ? Location.TOP : jrMiddle.isSelected() ? Location.CENTER : Location.BOTTOM;
        Option.BackgroundClickType backgroundClickType = jrClose.isSelected() ? Option.BackgroundClickType.CLOSE_MODAL : jrBlock.isSelected() ? Option.BackgroundClickType.BLOCK : Option.BackgroundClickType.NONE;
        option.setAnimationEnabled(chAnimation.isSelected())
                .setCloseOnPressedEscape(chCloseOnPressedEscape.isSelected())
                .setBackgroundClickType(backgroundClickType);
        option.getLayoutOption().setLocation(h, v);
        return option;
    }

    // horizontal
    private JRadioButton jrLeft;
    private JRadioButton jrLeading;
    private JRadioButton jrCenter;
    private JRadioButton jrTrailing;
    private JRadioButton jrRight;

    // vertical
    private JRadioButton jrTop;
    private JRadioButton jrMiddle;
    private JRadioButton jrButton;

    // option
    private JCheckBox chAnimation;
    private JCheckBox chCloseOnPressedEscape;

    // background click option
    private JRadioButton jrClose;
    private JRadioButton jrBlock;
    private JRadioButton jrNone;
}
