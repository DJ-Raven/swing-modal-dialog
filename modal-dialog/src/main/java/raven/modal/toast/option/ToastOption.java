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

    public ToastEvent getEvent() {
        return event;
    }

    public ToastLayoutOption getLayoutOption() {
        return layoutOption;
    }

    public ToastStyle getStyle() {
        return style;
    }

    public ToastInterpolator getInterpolator() {
        return interpolator;
    }

    public boolean isAnimationEnabled() {
        return animationEnabled;
    }

    public boolean isHeavyWeight() {
        return heavyWeight;
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

    public boolean isHtmlEnabled() {
        return htmlEnabled;
    }

    public int getDuration() {
        return duration;
    }

    public int getDelay() {
        return delay;
    }

    private ToastOption(ToastEvent event, ToastLayoutOption layoutOption, ToastStyle style, ToastInterpolator interpolator, boolean animationEnabled, boolean heavyWeight, boolean pauseDelayOnHover, boolean autoClose, boolean closeOnClick, boolean htmlEnabled, int duration, int delay) {
        this.event = event;
        this.layoutOption = layoutOption;
        this.style = style;
        this.interpolator = interpolator;
        this.animationEnabled = animationEnabled;
        this.heavyWeight = heavyWeight;
        this.pauseDelayOnHover = pauseDelayOnHover;
        this.autoClose = autoClose;
        this.closeOnClick = closeOnClick;
        this.htmlEnabled = htmlEnabled;
        this.duration = duration;
        this.delay = delay;
    }

    public ToastOption() {
    }

    private ToastEvent event = ToastEvent.getDefault();
    private ToastLayoutOption layoutOption = ToastLayoutOption.getDefault();
    private ToastStyle style = ToastStyle.getDefault();
    private ToastInterpolator interpolator;
    private boolean animationEnabled = true;
    private boolean heavyWeight;
    private boolean pauseDelayOnHover = true;
    private boolean autoClose = true;
    private boolean closeOnClick;
    private boolean htmlEnabled;
    private int duration = 350;
    private int delay = 3000;

    public ToastOption setEvent(ToastEvent event) {
        this.event = event;
        return this;
    }

    public ToastOption setLayoutOption(ToastLayoutOption layoutOption) {
        this.layoutOption = layoutOption;
        return this;
    }

    public ToastOption setStyle(ToastStyle style) {
        this.style = style;
        return this;
    }

    public ToastOption setInterpolator(ToastInterpolator interpolator) {
        this.interpolator = interpolator;
        return this;
    }

    public ToastOption setAnimationEnabled(boolean animationEnabled) {
        this.animationEnabled = animationEnabled;
        return this;
    }

    public ToastOption setHeavyWeight(boolean heavyWeight) {
        this.heavyWeight = heavyWeight;
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

    public ToastOption setHtmlEnabled(boolean htmlEnabled) {
        this.htmlEnabled = htmlEnabled;
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
        return new ToastOption(event.copy(), layoutOption.copy(), style.copy(), interpolator, animationEnabled, heavyWeight, pauseDelayOnHover, autoClose, closeOnClick, htmlEnabled, duration, delay);
    }
}
