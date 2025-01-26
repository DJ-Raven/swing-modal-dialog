package raven.modal.demo.forms;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import raven.modal.Toast;
import raven.modal.demo.component.LabelButton;
import raven.modal.demo.simple.SimpleCustomToast;
import raven.modal.demo.system.Form;
import raven.modal.demo.utils.SystemForm;
import raven.modal.option.Location;
import raven.modal.toast.ToastPromise;
import raven.modal.toast.option.ToastBorderStyle;
import raven.modal.toast.option.ToastLocation;
import raven.modal.toast.option.ToastOption;
import raven.modal.toast.option.ToastStyle;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Random;

@SystemForm(name = "Toast", description = "toast notification message", tags = {"alert"})
public class FormToast extends Form {

    public FormToast() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("wrap,fillx", "[fill]"));
        add(createInfo());
        add(createOptions());
    }

    private JPanel createInfo() {
        JPanel panel = new JPanel(new MigLayout("fillx,wrap", "[fill]"));
        JLabel title = new JLabel("Toast");
        JTextPane text = new JTextPane();
        text.setText("A toast notification is a small, transient message that appears on the screen to provide feedback to the user.\nIt is commonly used to display brief, non-intrusive notifications.");
        text.setEditable(false);
        text.setBorder(BorderFactory.createEmptyBorder());
        title.putClientProperty(FlatClientProperties.STYLE, "" +
                "font:bold +3");

        panel.add(title);
        panel.add(text, "width 500");
        return panel;
    }

    private Component createOptions() {
        JPanel panel = new JPanel(new MigLayout("wrap 2,fillx", "[grow 0,fill][fill]", "[fill]"));
        panel.add(createHorizontalOption());
        panel.add(createVerticalOption());
        panel.add(createToastOption());
        panel.add(createBackgroundToastStyle(), "width 130::,split 3");
        panel.add(createBorderToastStyle(), "width 130::");
        panel.add(createOtherToastStyle());
        panel.add(createSampleToast(), "span 2");
        return panel;
    }

    private Component createHorizontalOption() {
        JPanel panel = new JPanel(new MigLayout());
        panel.setBorder(new TitledBorder("Horizontal option"));
        jrLeading = new JRadioButton("Leading");
        jrCenter = new JRadioButton("Center");
        jrTrailing = new JRadioButton("Trailing");

        jrCenter.setSelected(true);

        ButtonGroup group = new ButtonGroup();
        group.add(jrLeading);
        group.add(jrCenter);
        group.add(jrTrailing);

        panel.add(jrLeading);
        panel.add(jrCenter);
        panel.add(jrTrailing);

        return panel;
    }

    private Component createVerticalOption() {
        JPanel panel = new JPanel(new MigLayout());
        panel.setBorder(new TitledBorder("Vertical option"));

        jrTop = new JRadioButton("Top");
        jrButton = new JRadioButton("Bottom");

        jrTop.setSelected(true);

        ButtonGroup group = new ButtonGroup();
        group.add(jrTop);
        group.add(jrButton);

        panel.add(jrTop);
        panel.add(jrButton);

        return panel;
    }

    private Component createToastOption() {
        JPanel panel = new JPanel(new MigLayout("wrap"));
        panel.setBorder(new TitledBorder("Toast option"));

        chAnimation = new JCheckBox("Animation enable");
        chPauseDelayOnHover = new JCheckBox("Pause delay on hover");
        chAutoClose = new JCheckBox("Auto close");
        chCloseOnClick = new JCheckBox("Close on click");
        chHeavyWeight = new JCheckBox("Heavy weight");
        chRelativeToOwner = new JCheckBox("Relative to owner");
        JCheckBox chReverseOrder = new JCheckBox("Reverse order");

        chAnimation.setSelected(true);
        chPauseDelayOnHover.setSelected(true);
        chAutoClose.setSelected(true);

        chReverseOrder.addActionListener(e -> Toast.setReverseOrder(chReverseOrder.isSelected()));

        panel.add(chAnimation);
        panel.add(chPauseDelayOnHover);
        panel.add(chAutoClose);
        panel.add(chCloseOnClick);
        panel.add(chHeavyWeight);
        panel.add(chRelativeToOwner);
        panel.add(chReverseOrder);

        return panel;
    }

    private Component createBackgroundToastStyle() {
        JPanel panel = new JPanel(new MigLayout("wrap"));
        panel.setBorder(new TitledBorder("Background style"));

        jrBackgroundDefault = new JRadioButton("Default");
        jrBackgroundGradient = new JRadioButton("Gradient");
        jrBackgroundNone = new JRadioButton("None");

        jrBackgroundNone.setSelected(true);

        ButtonGroup group = new ButtonGroup();
        group.add(jrBackgroundDefault);
        group.add(jrBackgroundGradient);
        group.add(jrBackgroundNone);

        panel.add(jrBackgroundDefault);
        panel.add(jrBackgroundGradient);
        panel.add(jrBackgroundNone);

        return panel;
    }

    private Component createBorderToastStyle() {
        JPanel panel = new JPanel(new MigLayout("wrap"));
        panel.setBorder(new TitledBorder("Border style"));

        jrBorderOutline = new JRadioButton("Outline");
        jrBorderRightLine = new JRadioButton("Trailing line");
        jrBorderLeftLine = new JRadioButton("Leading line");
        jrBorderTopLine = new JRadioButton("Top line");
        jrBorderBottomLine = new JRadioButton("Bottom line");
        jrBorderNone = new JRadioButton("None");

        jrBorderNone.setSelected(true);

        ButtonGroup group = new ButtonGroup();
        group.add(jrBorderOutline);
        group.add(jrBorderRightLine);
        group.add(jrBorderLeftLine);
        group.add(jrBorderTopLine);
        group.add(jrBorderBottomLine);
        group.add(jrBorderNone);

        panel.add(jrBorderOutline);
        panel.add(jrBorderRightLine);
        panel.add(jrBorderLeftLine);
        panel.add(jrBorderTopLine);
        panel.add(jrBorderBottomLine);
        panel.add(jrBorderNone);

        return panel;
    }

    private Component createOtherToastStyle() {
        JPanel panel = new JPanel(new MigLayout("wrap"));
        panel.setBorder(new TitledBorder("Other style"));

        chShowLabel = new JCheckBox("Show label");
        chIconSeparateLine = new JCheckBox("Icon separate line");
        chShowCloseButton = new JCheckBox("Show close button");
        chPaintTextColor = new JCheckBox("Paint text color");

        chShowCloseButton.setSelected(true);

        panel.add(chShowLabel);
        panel.add(chIconSeparateLine);
        panel.add(chShowCloseButton);
        panel.add(chPaintTextColor);

        return panel;
    }

    private Component createSampleToast() {
        JPanel panel = new JPanel(new MigLayout());
        panel.setBorder(new TitledBorder("Sample toast"));
        LabelButton lbDefault = new LabelButton("Show default");
        LabelButton lbSuccess = new LabelButton("Show success");
        LabelButton lbInfo = new LabelButton("Show info");
        LabelButton lbWarning = new LabelButton("Show warning");
        LabelButton lbError = new LabelButton("Show error");
        LabelButton lbCustom = new LabelButton("Show custom");
        LabelButton lbPromise = new LabelButton("Show promise");

        LabelButton lbCloseAll = new LabelButton("Close all");
        lbCloseAll.putClientProperty(FlatClientProperties.STYLE, "" +
                "foreground:#D23333;");

        lbDefault.addOnClick(o -> showToast(Toast.Type.DEFAULT, getSelectedOption()));
        lbSuccess.addOnClick(o -> showToast(Toast.Type.SUCCESS, getSelectedOption()));
        lbInfo.addOnClick(o -> showToast(Toast.Type.INFO, getSelectedOption()));
        lbWarning.addOnClick(o -> showToast(Toast.Type.WARNING, getSelectedOption()));
        lbError.addOnClick(o -> showToast(Toast.Type.ERROR, getSelectedOption()));
        lbCustom.addOnClick(o -> showCustom(getSelectedOption()));
        lbPromise.addOnClick(o -> showPromise(getSelectedOption()));
        lbCloseAll.addOnClick(o -> Toast.closeAll());

        panel.add(lbDefault);
        panel.add(lbSuccess);
        panel.add(lbInfo);
        panel.add(lbWarning);
        panel.add(lbError);
        panel.add(lbCustom);
        panel.add(lbPromise);
        panel.add(lbCloseAll);
        return panel;
    }

    private int number;

    private void showToast(Toast.Type type, ToastOption option) {
        String text = "Simple swing toast notification";
        Toast.show(this, type, text, option);
    }

    private void showCustom(ToastOption option) {
        Toast.showCustom(this, new SimpleCustomToast(), option);
    }

    private void showPromise(ToastOption option) {
        if (Toast.checkPromiseId("raven")) {
            return;
        }
        Toast.showPromise(this, "Toast with promise please wait", option, getPromiseCallback());
    }

    private ToastPromise getPromiseCallback() {
        return new ToastPromise("raven") {
            @Override
            public void execute(PromiseCallback callback) {
                try {
                    for (int i = 0; i <= 100; i++) {
                        callback.update(String.format("Downloading %d%%", i));
                        Thread.sleep((new Random().nextInt(7) + 1) * 10);
                    }
                    int type = sleepAndRandomCallback(0);
                    if (type == 1) {
                        callback.done(Toast.Type.SUCCESS, "Promise has done with success");
                    } else if (type == 2) {
                        callback.done(Toast.Type.ERROR, "Promise has done with error");
                    } else if (type == 3) {
                        callback.done(Toast.Type.INFO, "Promise has done with info");
                    } else if (type == 4) {
                        callback.done(Toast.Type.WARNING, "Promise has done with warning");
                    } else {
                        callback.done(Toast.Type.DEFAULT, "Promise has done with default");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private int sleepAndRandomCallback(int m) throws InterruptedException {
        if (m > 0) {
            Thread.sleep(m);
        }
        Random ran = new Random();
        return ran.nextInt(5) + 1;
    }

    private ToastOption getSelectedOption() {
        ToastOption option = Toast.createOption();
        Location h = jrLeading.isSelected() ? Location.LEADING : jrTrailing.isSelected() ? Location.TRAILING : Location.CENTER;
        Location v = jrTop.isSelected() ? Location.TOP : Location.BOTTOM;
        ToastStyle.BackgroundType backgroundType = jrBackgroundDefault.isSelected() ? ToastStyle.BackgroundType.DEFAULT : jrBackgroundGradient.isSelected() ? ToastStyle.BackgroundType.GRADIENT : ToastStyle.BackgroundType.NONE;
        ToastBorderStyle.BorderType borderType = jrBorderOutline.isSelected() ? ToastBorderStyle.BorderType.OUTLINE : jrBorderRightLine.isSelected() ? ToastBorderStyle.BorderType.TRAILING_LINE : jrBorderLeftLine.isSelected() ? ToastBorderStyle.BorderType.LEADING_LINE : jrBorderTopLine.isSelected() ? ToastBorderStyle.BorderType.TOP_LINE : jrBorderBottomLine.isSelected() ? ToastBorderStyle.BorderType.BOTTOM_LINE : ToastBorderStyle.BorderType.NONE;
        option.setAnimationEnabled(chAnimation.isSelected())
                .setPauseDelayOnHover(chPauseDelayOnHover.isSelected())
                .setAutoClose(chAutoClose.isSelected())
                .setCloseOnClick(chCloseOnClick.isSelected())
                .setHeavyWeight(chHeavyWeight.isSelected());

        option.getLayoutOption()
                .setLocation(ToastLocation.from(h, v))
                .setRelativeToOwner(chRelativeToOwner.isSelected());
        option.getStyle().setBackgroundType(backgroundType)
                .setShowLabel(chShowLabel.isSelected())
                .setIconSeparateLine(chIconSeparateLine.isSelected())
                .setShowCloseButton(chShowCloseButton.isSelected())
                .setPaintTextColor(chPaintTextColor.isSelected())
                .setPromiseLabel("Saving...")
                .getBorderStyle()
                .setBorderType(borderType)
        ;
        return option;
    }

    // horizontal
    private JRadioButton jrLeading;
    private JRadioButton jrCenter;
    private JRadioButton jrTrailing;

    // vertical
    private JRadioButton jrTop;
    private JRadioButton jrButton;

    // option
    private JCheckBox chAnimation;
    private JCheckBox chPauseDelayOnHover;
    private JCheckBox chAutoClose;
    private JCheckBox chCloseOnClick;
    private JCheckBox chHeavyWeight;
    private JCheckBox chRelativeToOwner;

    // style background
    private JRadioButton jrBackgroundDefault;
    private JRadioButton jrBackgroundGradient;
    private JRadioButton jrBackgroundNone;

    // style border
    private JRadioButton jrBorderOutline;
    private JRadioButton jrBorderRightLine;
    private JRadioButton jrBorderLeftLine;
    private JRadioButton jrBorderTopLine;
    private JRadioButton jrBorderBottomLine;
    private JRadioButton jrBorderNone;

    // other style
    private JCheckBox chShowLabel;
    private JCheckBox chIconSeparateLine;
    private JCheckBox chShowCloseButton;
    private JCheckBox chPaintTextColor;
}
