package raven.modal.toast;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.Animator;
import com.formdev.flatlaf.util.CubicBezierEasing;
import net.miginfocom.swing.MigLayout;
import raven.modal.Toast;
import raven.modal.component.DropShadowBorder;
import raven.modal.component.ModalLineBorder;
import raven.modal.toast.icon.RollingIcon;
import raven.modal.toast.option.*;
import raven.modal.utils.ImageSnapshots;
import raven.modal.utils.ModalUtils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author Raven
 */
public class ToastPanel extends JPanel {

    public float getEasingAnimate() {
        ToastInterpolator interpolator = getOption().getInterpolator();
        if (interpolator == null) {
            return CubicBezierEasing.STANDARD_EASING.interpolate(animate);
        }
        return interpolator.interpolate(animate);
    }

    public float getAnimate() {
        return animate;
    }

    public Component getOwner() {
        return owner;
    }

    public ToastOption getOption() {
        return toastData.getOption();
    }

    private final BaseToastContainer baseToastContainer;
    private final Component owner;
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
    private Image snapshotContent;

    public ToastPanel(BaseToastContainer baseToastContainer, Component owner, ToastData toastData) {
        this.baseToastContainer = baseToastContainer;
        this.owner = owner;
        this.toastData = toastData;
        init();
    }

    private void init() {
        setLayout(new MigLayout("insets 0,fill", "fill", "fill"));
        if (!toastData.getOption().isHeavyWeight()) {
            setOpaque(false);
        }
        if (isAnimationSupport()) {
            animate = 0f;
        }
        putClientProperty(FlatClientProperties.STYLE, "" +
                "background:$TextArea.background;");
    }

    @Override
    public void updateUI() {
        super.updateUI();
        initBorder();
    }

    protected void initBorder() {
        if (toastData == null || content == null) {
            return;
        }
        Border border = new ToastBorder(toastData);
        ToastBorderStyle borderStyle = toastData.getOption().getStyle().getBorderStyle();
        int borderWidth = borderStyle.getBorderType() == ToastBorderStyle.BorderType.OUTLINE ? borderStyle.getBorderWidth() : 0;
        if (getOption().isHeavyWeight()) {
            if ((borderWidth > 0 && !ModalUtils.isShadowAndRoundBorderSupport()) || borderStyle.getRound() == 0) {
                // border width painted with round window border
                // but if windows round border not support we set the border width here
                setBorder(new CompoundBorder(new ModalLineBorder(borderWidth, toastData.getThemes().getColor(), 0), border));
            } else {
                setBorder(border);
            }
        } else {
            if (FlatUIUtils.isInsetsEmpty(borderStyle.getShadowSize()) && borderStyle.getRound() == 0 && borderWidth == 0) {
                setBorder(border);
            } else {
                Border shadow = new DropShadowBorder(borderStyle.getShadowSize(),
                        borderStyle.getShadowOpacity(),
                        borderStyle.getShadowColor(),
                        borderWidth,
                        toastData.getThemes().getColor(),
                        borderStyle.getRound());
                setBorder(new CompoundBorder(shadow, border));
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        float easingAnimate = getEasingAnimate();
        if (easingAnimate < 1) {
            Graphics2D g2 = (Graphics2D) g;
            if (easingAnimate < 0) {
                easingAnimate = 0;
            }
            g2.setComposite(AlphaComposite.SrcOver.derive(easingAnimate));
        }
        super.paint(g);
        if (snapshotContent != null) {
            int x = content.getX();
            int y = content.getY();
            g.drawImage(snapshotContent, x, y, null);
        }
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        if (promiseIcon != null) {
            promiseIcon.stop();
            promiseIcon = null;
        }
        removeSnapshot();
    }

    public ToastPanel createToast() {
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

    public ToastPanel createToastPromise(ToastPromise promise) {
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

    public ToastPanel createToastCustom(Component component) {
        content = new ToastContent(new MigLayout(getLayoutInsets()), toastData);
        if (component instanceof ToastCustom) {
            ToastCustom toastCustom = (ToastCustom) component;
            toastCustom.initToastAction(getCustomAction());
        }
        content.add(component);
        initBorder();
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
                        updateModalLayout();
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

    private String getLayoutInsets() {
        Insets padding = toastData.getOption().getStyle().getBorderStyle().getPadding();
        final int add = 7 + ModalUtils.getToastExtraBorderPadding(toastData.getOption());
        return String.format("insets %d %d %d %d", padding.top + add, padding.left + add, padding.bottom + add, padding.right + add);
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
        content = new ToastContent(new MigLayout("filly," + getLayoutInsets(), getLayoutColumn(themesData), "[center]"), toastData);
        content.add(createTextMessage());
        if (toastData.getOption().getStyle().isShowCloseButton()) {
            content.add(createCloseButton());
        }
        initBorder();
        installStyle(themesData);
        add(content);
    }

    private void installStyle(ThemesData themesData) {
        if (toastData.getOption().getStyle().getBackgroundType() == ToastStyle.BackgroundType.DEFAULT) {
            putClientProperty(FlatClientProperties.STYLE, "" +
                    "[light]background:mix(" + themesData.colors[0] + ",$TextArea.background,10%);" +
                    "[dark]background:mix(" + themesData.colors[1] + ",$TextArea.background,10%);");
        }
        if (textMessage != null && toastData.getOption().getStyle().isPaintTextColor()) {
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
                if (toastData.getOption().isPauseDelayOnHover() && toastData.getOption().isAutoClose() && !isCurrenPromise()) {
                    if (animator == null || !animator.isRunning()) {
                        delayStop();
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (hover && toastData.getOption().isCloseOnClick() && !isCurrenPromise()) {
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
        updateModalLayout();
    }

    private PromiseIcon createIconPromise(ToastPromise promise) {
        return new RollingIcon(promise, 20);
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

    private boolean isAnimationSupport() {
        return getOption().isAnimationEnabled() && !getOption().isHeavyWeight();
    }

    public void start() {
        if (showing) {
            return;
        }
        installMouseHover();
        if (isAnimationSupport()) {
            if (animator == null) {
                animator = new Animator(toastData.getOption().getDuration(), new Animator.TimingTarget() {
                    @Override
                    public void timingEvent(float v) {
                        animate = showing ? v : 1f - v;
                        baseToastContainer.updateLayout(owner);
                    }

                    @Override
                    public void begin() {
                        createSnapshot();
                    }

                    @Override
                    public void end() {
                        repaint();
                        removeSnapshot();
                        if (showing) {
                            defaultStop();
                        } else {
                            removeToast();
                        }
                    }
                });
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
                } catch (InterruptedException ignored) {
                }
            }
        });
        threadDelay.start();
    }

    public void stop() {
        if (!showing) {
            return;
        }
        if (isCurrenPromise() && !toastPromise.rejectAble()) {
            return;
        }
        if (isAnimationSupport()) {
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
        if (isAnimationSupport()) {
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
        baseToastContainer.remove(this);
        toastData = null;
        content = null;
        textMessage = null;
        labelIcon = null;
        promiseCallback = null;
        toastPromise = null;
        mouseListener = null;
        animator = null;
        threadDelay = null;
    }

    private void createSnapshot() {
        snapshotContent = ImageSnapshots.createSnapshotsImage(content, 0);
        content.setVisible(false);
    }

    private void removeSnapshot() {
        if (snapshotContent != null) {
            snapshotContent.flush();
            snapshotContent = null;
        }
        content.setVisible(true);
    }

    private void updateModalLayout() {
        if (toastData != null && toastData.getOption().isHeavyWeight()) {
            baseToastContainer.updateLayout(owner);
        }
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

    public boolean isClose() {
        return content == null;
    }

    public boolean checkSameLayout(ToastLayoutOption layoutOption) {
        ToastLayoutOption option = toastData.getOption().getLayoutOption();
        if (option.getLocationSize() == null && layoutOption.getLocationSize() == null) {
            return checkSameLayout(layoutOption.getLocation());
        }
        if (option.getLocationSize() != null && layoutOption.getLocationSize() != null) {
            return option.getLocationSize().getX().floatValue() == layoutOption.getLocationSize().getX().floatValue()
                    && option.getLocationSize().getY().floatValue() == layoutOption.getLocationSize().getY().floatValue();
        }
        return false;
    }

    public boolean checkSameLayout(ToastLocation location) {
        return toastData.getOption().getLayoutOption().getLocation().isSame(location);
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

        public Color getColor() {
            return Color.decode(FlatLaf.isLafDark() ? colors[1] : colors[0]);
        }
    }
}
