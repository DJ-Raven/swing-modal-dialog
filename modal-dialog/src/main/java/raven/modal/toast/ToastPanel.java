package raven.modal.toast;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.util.Animator;
import com.formdev.flatlaf.util.CubicBezierEasing;
import net.miginfocom.swing.MigLayout;
import raven.modal.Toast;
import raven.modal.option.LayoutOption;
import raven.modal.toast.icon.RollingIcon;
import raven.modal.toast.option.ToastOption;
import raven.modal.toast.option.ToastStyle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author Raven
 */
public class ToastPanel extends JPanel {

    public float getAnimate() {
        return animate;
    }

    private final ToastContainerLayer toastContainerLayer;
    private ToastData toastData;
    private ToastContent content;
    private JTextArea textMessage;
    private JLabel labelTitle;
    private MouseListener mouseListener;
    private Animator animator;
    private boolean showing;
    private float animate = 1f;
    private Thread threadDelay;
    private boolean hover;
    private JLabel labelIcon;
    private JSeparator separator;
    private PromiseIcon promiseIcon;
    private ToastPromise toastPromise;
    private ToastPromise.PromiseCallback promiseCallback;
    private boolean available = true;

    public ToastPanel(ToastContainerLayer toastContainerLayer, ToastData toastData) {
        this.toastContainerLayer = toastContainerLayer;
        this.toastData = toastData;
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        setOpaque(false);
        putClientProperty(FlatClientProperties.STYLE, "" +
                "background:$TextArea.background;");
    }

    @Override
    public void updateUI() {
        super.updateUI();
        setBorder(new ToastBorder(content, toastData));
    }

    @Override
    public void paint(Graphics g) {
        if (animate >= 0 && animate < 1f) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setComposite(AlphaComposite.SrcOver.derive(animate));
        }
        super.paint(g);
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        if (promiseIcon != null) {
            promiseIcon.stop();
            promiseIcon = null;
        }
    }

    public Component createToast() {
        ThemesData themesData = toastData.getThemes();
        installDefault(themesData);
        Icon icon = createIcon(themesData);
        if (icon != null) {
            content.add(new JLabel(icon), 0);
            if (toastData.getOption().getStyle().isIconSeparateLine()) {
                separator = new JSeparator(JSeparator.VERTICAL);
                content.add(separator, "height 50%", 1);
            }
        }
        return this;
    }

    public Component createToastPromise(ToastPromise promise) {
        this.toastPromise = promise;
        ThemesData themesData = toastData.getThemes();
        installDefault(themesData);
        promiseIcon = createIconPromise(promise);
        labelIcon = new JLabel(promiseIcon);
        content.add(labelIcon, 0);
        if (toastData.getOption().getStyle().isIconSeparateLine()) {
            separator = new JSeparator(JSeparator.VERTICAL);
            content.add(separator, "height 50%", 1);
        }
        return this;
    }

    public Component createToastCustom(Component component) {
        content = new ToastContent(new MigLayout(), toastData);
        if (component instanceof ToastCustom) {
            ToastCustom toastCustom = (ToastCustom) component;
            toastCustom.initToastAction(getCustomAction());
        }
        content.add(component);
        setBorder(new ToastBorder(content, toastData));
        installStyle(toastData.themes);
        add(content);
        return this;
    }

    private ToastCustom.Action getCustomAction() {
        return () -> {
            stop();
        };
    }

    private ToastPromise.PromiseCallback getPromiseCallback() {
        if (promiseCallback == null) {
            promiseCallback = new ToastPromise.PromiseCallback() {
                @Override
                public void update(String message) {
                    if (available && !toastPromise.isDone()) {
                        textMessage.setText(message);
                    }
                }

                @Override
                public void done(Toast.Type type, String message) {
                    if (available && !toastPromise.isDone()) {
                        toastPromise.setDone(true);
                        promiseIcon.stop();
                        toastPromise = null;
                        changeType(type, message);
                        if (toastData.getOption().isAutoClose()) {
                            delayStop();
                        }
                    }
                }
            };
        }
        return promiseCallback;
    }

    private String getLayoutColumn(ThemesData themesData) {
        String columnLayout;
        boolean isShowCloseButton = toastData.getOption().getStyle().isShowCloseButton();
        String closeButtonLayout = isShowCloseButton ? "[]" : "8";
        if (themesData.icon == null && toastPromise == null && toastData.getOption().getStyle().getCustomIcon() == null) {
            columnLayout = "10[grow]";
        } else {
            if (toastData.getOption().getStyle().isIconSeparateLine()) {
                columnLayout = "[][center][grow]";
            } else {
                columnLayout = "[][grow]";
            }
        }
        columnLayout += closeButtonLayout;
        return columnLayout;
    }

    private void installDefault(ThemesData themesData) {
        content = new ToastContent(new MigLayout("filly", getLayoutColumn(themesData), "[center]"), toastData);
        content.add(createTextMessage());
        if (toastData.getOption().getStyle().isShowCloseButton()) {
            content.add(createCloseButton());
        }
        setBorder(new ToastBorder(content, toastData));
        installStyle(themesData);
        add(content);
    }

    private void installStyle(ThemesData themesData) {
        if (toastData.getOption().getStyle().getBackgroundType() == ToastStyle.BackgroundType.DEFAULT) {
            putClientProperty(FlatClientProperties.STYLE, "" +
                    "[light]background:mix(" + themesData.colors[0] + ",$TextArea.background,10%);" +
                    "[dark]background:mix(" + themesData.colors[1] + ",$TextArea.background,10%);");
        }
        if (textMessage != null) {
            textMessage.putClientProperty(FlatClientProperties.STYLE, "" +
                    "[light]foreground:" + themesData.colors[0] + ";" +
                    "[dark]foreground:" + themesData.colors[1] + ";");
        }
        if (labelTitle != null) {
            labelTitle.putClientProperty(FlatClientProperties.STYLE, "" +
                    "font:bold;" +
                    "[light]foreground:" + themesData.colors[0] + ";" +
                    "[dark]foreground:" + themesData.colors[1] + ";");
        }
    }

    private void installMouseHover() {
        mouseListener = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hover = true;
                if (toastData.getOption().isPauseDelayOnHover() && toastData.getOption().isAutoClose()) {
                    if (threadDelay != null) {
                        threadDelay.interrupt();
                    }
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hover = false;
                if (toastData.getOption().isPauseDelayOnHover() && toastData.getOption().isAutoClose() && isCurrenPromise() == false) {
                    if (animator == null || !animator.isRunning()) {
                        delayStop();
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (hover && toastData.getOption().isCloseOnClick() && isCurrenPromise() == false) {
                        stop();
                    }
                }
            }
        };
        content.addMouseListener(mouseListener);
        if (textMessage != null) {
            textMessage.addMouseListener(mouseListener);
        }
    }

    private void changeType(Toast.Type type, String message) {
        if (type == null) {
            type = Toast.Type.DEFAULT;
        }
        content.remove(labelIcon);
        ThemesData data = Toast.getThemesData().get(type);
        toastData.themes = data;
        Icon icon = createIcon(data);
        if (icon != null) {
            content.add(new JLabel(icon), 0);
        } else {
            if (separator != null) {
                content.remove(separator);
            }
            MigLayout layout = (MigLayout) content.getLayout();
            layout.setColumnConstraints(getLayoutColumn(data));
        }
        if (labelTitle != null) {
            labelTitle.setText(toastData.getOption().getStyle().getLabelText(type));
        }
        if (message != null) {
            textMessage.setText(message);
        }
        installStyle(data);
        repaint();
        content.revalidate();
    }

    private PromiseIcon createIconPromise(ToastPromise promise) {
        return new RollingIcon(promise, 24);
    }

    private Icon createIcon(ThemesData themesData) {
        if (toastData.getOption().getStyle().getCustomIcon() != null) {
            return toastData.getOption().getStyle().getCustomIcon();
        }
        if (toastData.getThemes().icon == null) {
            return null;
        }
        FlatSVGIcon svgIcon = new FlatSVGIcon(themesData.icon, 0.5f);
        FlatSVGIcon.ColorFilter colorFilter = new FlatSVGIcon.ColorFilter();
        colorFilter.add(Color.decode("#969696"), Color.decode(themesData.colors[0]), Color.decode(themesData.colors[1]));
        svgIcon.setColorFilter(colorFilter);
        return svgIcon;
    }

    private JComponent createTextMessage() {
        JTextArea text = new JTextArea();
        text.setOpaque(false);
        text.setFocusable(false);
        text.setWrapStyleWord(true);
        text.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        text.setBorder(BorderFactory.createEmptyBorder());
        text.setText(toastData.getMessage());
        textMessage = text;
        if (toastData.getOption().getStyle().isShowLabel()) {
            JPanel panel = new JPanel(new MigLayout("insets 0,wrap,gap 0"));
            panel.setOpaque(false);
            labelTitle = new JLabel(toastData.getOption().getStyle().getLabelText(toastData.type));
            panel.add(labelTitle);
            panel.add(text);
            return panel;
        } else {
            return text;
        }
    }

    private JButton createCloseButton() {
        JButton buttonClose = new JButton(new FlatSVGIcon("raven/modal/icon/close.svg", 0.3f));
        buttonClose.setFocusable(false);
        buttonClose.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        buttonClose.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:999;" +
                "margin:5,5,5,5;" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0;" +
                "background:null;");
        buttonClose.addActionListener(e -> stop());
        return buttonClose;
    }

    public void start() {
        if (showing) {
            return;
        }
        installMouseHover();
        if (toastData.getOption().isAnimationEnabled()) {
            if (animator == null) {
                animator = new Animator(toastData.getOption().getDuration(), new Animator.TimingTarget() {
                    @Override
                    public void timingEvent(float v) {
                        animate = showing ? v : 1f - v;
                        toastContainerLayer.revalidate();
                    }

                    @Override
                    public void end() {
                        repaint();
                        if (showing) {
                            defaultStop();
                        } else {
                            removeToast();
                        }
                    }
                });
                animator.setInterpolator(CubicBezierEasing.STANDARD_EASING);
            }
            showing = true;
            animate = 0f;
            animator.start();
        } else {
            showing = true;
            defaultStop();
        }
        if (toastPromise != null) {
            toastPromise.start(getPromiseCallback());
        }
    }

    private void defaultStop() {
        if (toastData.getOption().isAutoClose() && toastPromise == null) {
            delayStop();
        } else {
            if (toastPromise != null) {
                promiseIcon.start();
            }
        }
    }

    public void delayStop() {
        if (toastData.getOption().isPauseDelayOnHover() && hover) {
            return;
        }
        threadDelay = new Thread(() -> {
            if (showing) {
                try {
                    Thread.sleep(toastData.getOption().getDelay());
                    stop();
                } catch (InterruptedException e) {
                }
            }
        });
        threadDelay.start();
    }

    public void stop() {
        if (!showing) {
            return;
        }
        if (isCurrenPromise() && toastPromise.rejectAble() == false) {
            return;
        }
        if (toastData.getOption().isAnimationEnabled()) {
            if (animator.isRunning()) {
                animator.stop();
            }
            showing = false;
            animator.start();
        } else {
            showing = false;
            removeToast();
        }
    }

    protected void close() {
        available = false;
        showing = false;
        if (threadDelay != null && threadDelay.isAlive()) {
            threadDelay.interrupt();
        }
        if (toastData.getOption().isAnimationEnabled()) {
            if (animator != null && animator.isRunning()) {
                animator.stop();
            } else {
                removeToast();
            }
        } else {
            removeToast();
        }
    }

    private void removeToast() {
        toastContainerLayer.removeToastPanel(this);
        if (mouseListener != null) {
            content.removeMouseListener(mouseListener);
            if (textMessage != null) {
                textMessage.removeMouseListener(mouseListener);
            }
        }

        // check promise and stop it
        if (isCurrenPromise()) {
            toastPromise.setDone(true);
            toastPromise.reject();
        }
        toastData = null;
        toastContainerLayer.remove(this);
        toastContainerLayer.repaint();
        toastContainerLayer.revalidate();
        content = null;
        textMessage = null;
        labelIcon = null;
        promiseCallback = null;
        toastPromise = null;
        mouseListener = null;
        animator = null;
        threadDelay = null;
    }

    protected boolean isCurrenPromise() {
        return toastPromise != null && !toastPromise.isDone();
    }

    protected boolean checkPromiseId(String id) {
        return isCurrenPromise() && toastPromise.getId() != null && toastPromise.getId().equals(id);
    }

    public ToastData getToastData() {
        return toastData;
    }

    public boolean checkSameLayout(LayoutOption option) {
        return option.getHorizontalLocation() == toastData.getOption().getLayoutOption().getHorizontalLocation()
                && option.getVerticalLocation() == toastData.getOption().getLayoutOption().getVerticalLocation();
    }

    public static class ToastData {

        public Toast.Type getType() {
            return type;
        }

        public ToastOption getOption() {
            return option;
        }

        public ThemesData getThemes() {
            return themes;
        }

        public String getMessage() {
            return message;
        }

        public ToastData(Toast.Type type, ToastOption option, ThemesData themes, String message) {
            this.type = type;
            this.option = option;
            this.themes = themes;
            this.message = message;
        }

        private Toast.Type type;
        private ToastOption option;
        private ThemesData themes;
        private String message;
    }

    public static class ThemesData {

        public String getIcon() {
            return icon;
        }

        public String[] getColors() {
            return colors;
        }

        public ThemesData(String icon, String[] colors) {
            this.icon = icon;
            this.colors = colors;
        }

        private String icon;
        private String[] colors;
    }
}
