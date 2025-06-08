package raven.modal.component;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;
import raven.modal.listener.ModalCallback;
import raven.modal.listener.ModalController;
import raven.modal.option.ModalBorderOption;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

/**
 * This class contain with title, action button options and close button
 *
 * @author Raven
 */
public class SimpleModalBorder extends Modal implements ModalBorderAction {

    protected final Component component;
    protected JComponent header;

    protected final ModalBorderOption option;
    protected final String title;
    private final Option[] optionsType;
    private final ModalCallback callback;

    // options
    public static final int DEFAULT_OPTION = -2;
    public static final int YES_NO_OPTION = 0;
    public static final int YES_NO_CANCEL_OPTION = 1;
    public static final int OK_CANCEL_OPTION = 2;

    // return options
    public static final int YES_OPTION = 0;
    public static final int NO_OPTION = 1;
    public static final int CANCEL_OPTION = 2;
    public static final int OK_OPTION = 0;
    public static final int CLOSE_OPTION = -1;
    public static final int OPENED = 20;

    public SimpleModalBorder(Component component, String title) {
        this(component, title, new ModalBorderOption());
    }

    public SimpleModalBorder(Component component, String title, ModalBorderOption option) {
        this(component, title, option, DEFAULT_OPTION, null);
    }

    public SimpleModalBorder(Component component, String title, int optionType, ModalCallback callback) {
        this(component, title, new ModalBorderOption(), optionType, callback);
    }

    public SimpleModalBorder(Component component, String title, Option[] optionsType, ModalCallback callback) {
        this(component, title, new ModalBorderOption(), DEFAULT_OPTION, optionsType, callback);
    }

    public SimpleModalBorder(Component component, String title, ModalBorderOption option, int optionType, ModalCallback callback) {
        this(component, title, option, optionType, null, callback);
    }

    public SimpleModalBorder(Component component, String title, ModalBorderOption option, Option[] optionsType, ModalCallback callback) {
        this(component, title, option, DEFAULT_OPTION, optionsType, callback);
    }

    private SimpleModalBorder(Component component, String title, ModalBorderOption option, int optionType, Option[] optionsType, ModalCallback callback) {
        this.component = component;
        this.option = option;
        this.title = title;
        this.callback = callback;
        if (optionType == DEFAULT_OPTION) {
            this.optionsType = optionsType;
        } else {
            this.optionsType = createOptions(optionType);
        }
    }

    /**
     * This method work when show modal dialog
     * To be able custom new model border with new constructor argument and override the other method
     */
    @Override
    public void installComponent() {
        String insets = String.format("insets %d 0 %d 0", option.getPadding().top, option.getPadding().bottom);
        setLayout(new MigLayout("wrap,fillx," + insets, "[fill]", "[][fill,grow][]"));
        header = createHeader();
        add(header);
        if (option.isUseScroll()) {
            JScrollPane scrollPane = createContentScroll();
            scrollPane.setViewportView(component);
            add(scrollPane);
        } else {
            add(component);
        }
        Component optionButton = createOptionButton(optionsType);
        if (optionButton != null) {
            add(optionButton);
        }
    }

    protected JScrollPane createContentScroll() {
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        scrollPane.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE, "" +
                "trackInsets:0,3,0,3;" +
                "thumbInsets:0,3,0,3;" +
                "trackArc:$ScrollBar.thumbArc");
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        return scrollPane;
    }

    protected JComponent createHeader() {
        String insets = String.format("insets 0 %d 0 %d", option.getPadding().left, option.getPadding().right);
        JPanel panel = new JPanel(new MigLayout("novisualpadding,fill," + insets));
        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "background:null;");
        panel.add(createTitleComponent(title), "push");
        panel.add(createActionTitleComponent());
        return panel;
    }

    protected JComponent createTitleComponent(String title) {
        JLabel label = new JLabel(title);
        label.putClientProperty(FlatClientProperties.STYLE, "" +
                "font:+4");
        return label;
    }

    protected JComponent createActionTitleComponent() {
        JButton buttonClose = new JButton(new FlatSVGIcon("raven/modal/icon/close.svg", 0.4f));
        buttonClose.setFocusable(false);
        buttonClose.addActionListener(e -> {
            doAction(CLOSE_OPTION);
        });
        buttonClose.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:999;" +
                "margin:5,5,5,5;" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0;" +
                "background:null;");
        return buttonClose;
    }

    protected JComponent createOptionButton(Option[] optionsType) {
        if (optionsType == null || optionsType.length == 0) {
            return null;
        }
        String insets = String.format("insets 3 %d 3 %d", option.getPadding().left, option.getPadding().right);
        JPanel panel = new JPanel(new MigLayout(insets + ",al trailing"));
        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "background:null;");
        for (Option option : optionsType) {
            panel.add(createButtonOption(option));
        }
        return panel;
    }

    protected Option[] createOptions(int optionType) {
        checkOptionType(optionType);
        Option[] options = null;
        if (optionType == YES_NO_OPTION) {
            options = new Option[]{new Option("Yes", YES_OPTION), new Option("No", NO_OPTION)};
        } else if (optionType == YES_NO_CANCEL_OPTION) {
            options = new Option[]{new Option("Yes", YES_OPTION), new Option("No", NO_OPTION), new Option("Cancel", CANCEL_OPTION)};
        } else if (optionType == OK_CANCEL_OPTION) {
            options = new Option[]{new Option("Ok", OK_OPTION), new Option("Cancel", CANCEL_OPTION)};
        } else if (optionType == CLOSE_OPTION) {
            options = new Option[]{new Option("Close", CLOSE_OPTION)};
        }
        return options;
    }

    protected JButton createButtonOption(Option option) {
        JButton button = new JButton(option.text) {
            @Override
            public boolean isDefaultButton() {
                if (option.type == 0) {
                    return true;
                }
                return super.isDefaultButton();
            }
        };
        button.addActionListener(e -> {
            doAction(option.type);
        });
        return button;
    }

    protected void applyBackButton(Consumer<?> onBack) {
        Component backButton = createBackButton(onBack);
        if (header != null) {
            header.add(backButton, 0);
        }
    }

    protected JComponent createBackButton(Consumer<?> onBack) {
        JButton buttonClose = new JButton(new FlatSVGIcon("raven/modal/icon/back.svg", 0.4f));
        buttonClose.setFocusable(false);
        buttonClose.addActionListener(e -> onBack.accept(null));
        buttonClose.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:999;" +
                "margin:5,5,5,5;" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0;" +
                "background:null;");
        return buttonClose;
    }

    private void checkOptionType(int type) {
        if (type != DEFAULT_OPTION && type != YES_NO_OPTION && type != YES_NO_CANCEL_OPTION && type != OK_CANCEL_OPTION && type != CLOSE_OPTION) {
            throw new RuntimeException("SimpleModalBorder: option type must be one of" +
                    " SimpleModalBorder.DEFAULT_OPTION," +
                    " SimpleModalBorder.YES_NO_OPTION," +
                    " SimpleModalBorder.YES_NO_CANCEL_OPTION," +
                    " SimpleModalBorder.OK_CANCEL_OPTION" +
                    " or SimpleModalBorder.CLOSE_OPTION");
        }
    }

    private ModalController createController() {
        return new ModalController(this) {
            @Override
            public void close() {
                getController().closeModal();
            }
        };
    }

    @Override
    protected void modalOpened() {
        if (callback != null) {
            ModalController controller = createController();
            callback.action(controller, OPENED);
        }
    }

    @Override
    public void doAction(int action) {
        if (callback == null) {
            getController().closeModal();
        } else {
            ModalController controller = createController();
            callback.action(controller, action);
            if (!controller.getConsume()) {
                getController().closeModal();
            }
        }
    }

    @Override
    public Color getBackground() {
        if (component == null) {
            return super.getBackground();
        }
        return component.getBackground();
    }

    public static class Option {

        public String getText() {
            return text;
        }

        public int getType() {
            return type;
        }

        private final String text;
        private final int type;

        public Option(String text, int type) {
            this.text = text;
            this.type = type;
        }
    }
}
