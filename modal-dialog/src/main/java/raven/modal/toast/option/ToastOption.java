package raven.modal.toast.option;

/**
 * This class for toast option
 *
 * @author Raven
 */
public class ToastOption {

    public static ToastOption getDefault() {
        return new ToastOption();
    }

    public ToastLayoutOption getLayoutOption() {
        return layoutOption;
    }

    public ToastStyle getStyle() {
        return style;
    }

    public boolean isAnimationEnabled() {
        return animationEnabled;
    }

    public boolean isPauseDelayOnHover() {
        return pauseDelayOnHover;
    }

    public boolean isAutoClose() {
        return autoClose;
    }

    public boolean isCloseOnClick() {
        return closeOnClick;
    }

    public int getDuration() {
        return duration;
    }

    public int getDelay() {
        return delay;
    }

    public ToastOption(ToastLayoutOption layoutOption, ToastStyle style, boolean animationEnabled, boolean pauseDelayOnHover, boolean autoClose, boolean closeOnClick, int duration, int delay) {
        this.layoutOption = layoutOption;
        this.style = style;
        this.animationEnabled = animationEnabled;
        this.pauseDelayOnHover = pauseDelayOnHover;
        this.autoClose = autoClose;
        this.closeOnClick = closeOnClick;
        this.duration = duration;
        this.delay = delay;
    }

    public ToastOption() {
    }

    private ToastLayoutOption layoutOption = ToastLayoutOption.getDefault();
    private ToastStyle style = ToastStyle.getDefault();
    private boolean animationEnabled = true;
    private boolean pauseDelayOnHover = true;
    private boolean autoClose = true;
    private boolean closeOnClick;
    private int duration = 350;
    private int delay = 3000;

    public ToastOption setLayoutOption(ToastLayoutOption layoutOption) {
        this.layoutOption = layoutOption;
        return this;
    }

    public ToastOption setStyle(ToastStyle style) {
        this.style = style;
        return this;
    }

    public ToastOption setAnimationEnabled(boolean animationEnabled) {
        this.animationEnabled = animationEnabled;
        return this;
    }

    public ToastOption setPauseDelayOnHover(boolean pauseDelayOnHover) {
        this.pauseDelayOnHover = pauseDelayOnHover;
        return this;
    }

    public ToastOption setAutoClose(boolean autoClose) {
        this.autoClose = autoClose;
        return this;
    }

    public ToastOption setCloseOnClick(boolean closeOnClick) {
        this.closeOnClick = closeOnClick;
        return this;
    }

    public ToastOption setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public ToastOption setDelay(int delay) {
        this.delay = delay;
        return this;
    }

    public ToastOption copy() {
        return new ToastOption(layoutOption.copy(), style.copy(), animationEnabled, pauseDelayOnHover, autoClose, closeOnClick, duration, delay);
    }
}
