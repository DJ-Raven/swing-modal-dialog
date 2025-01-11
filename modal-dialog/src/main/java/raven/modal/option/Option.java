package raven.modal.option;

import java.awt.*;

/**
 * This class for modal dialog option
 *
 * @author Raven
 */
public class Option {

    public static Option getDefault() {
        return new Option();
    }

    public LayoutOption getLayoutOption() {
        return layoutOption;
    }

    public BorderOption getBorderOption() {
        return borderOption;
    }

    public BackgroundClickType getBackgroundClickType() {
        return backgroundClickType;
    }

    public boolean isAnimationEnabled() {
        return animationEnabled;
    }

    public boolean isAnimationOnClose() {
        return animationOnClose;
    }

    public boolean isCloseOnPressedEscape() {
        return closeOnPressedEscape;
    }

    public boolean isHeavyWeight() {
        return heavyWeight;
    }

    public Color getBackgroundLight() {
        return backgroundLight;
    }

    public Color getBackgroundDark() {
        return backgroundDark;
    }

    public float getOpacity() {
        return opacity;
    }

    public int getDuration() {
        return duration;
    }

    public int getSliderDuration() {
        return sliderDuration;
    }

    private LayoutOption layoutOption = LayoutOption.getDefault();
    private BorderOption borderOption = BorderOption.getDefault();
    private BackgroundClickType backgroundClickType = BackgroundClickType.CLOSE_MODAL;
    private boolean animationEnabled = true;
    private boolean animationOnClose = true;
    private boolean closeOnPressedEscape = true;
    private boolean heavyWeight;
    private Color backgroundLight;
    private Color backgroundDark;
    private float opacity = 0.5f;
    private int duration = 200;
    private int sliderDuration = 400;

    private Option(LayoutOption layoutOption, BorderOption borderOption, BackgroundClickType backgroundClickType, boolean animationEnabled, boolean animationOnClose, boolean closeOnPressedEscape, boolean heavyWeight, Color backgroundLight, Color backgroundDark, float opacity, int duration, int sliderDuration) {
        this.layoutOption = layoutOption;
        this.borderOption = borderOption;
        this.backgroundClickType = backgroundClickType;
        this.animationEnabled = animationEnabled;
        this.animationOnClose = animationOnClose;
        this.closeOnPressedEscape = closeOnPressedEscape;
        this.heavyWeight = heavyWeight;
        this.backgroundLight = backgroundLight;
        this.backgroundDark = backgroundDark;
        this.opacity = opacity;
        this.duration = duration;
        this.sliderDuration = sliderDuration;
    }

    public Option() {
    }

    public Option setLayoutOption(LayoutOption layoutOption) {
        this.layoutOption = layoutOption;
        return this;
    }

    public Option setBackgroundClickType(BackgroundClickType backgroundClickType) {
        this.backgroundClickType = backgroundClickType;
        return this;
    }

    public Option setAnimationEnabled(boolean animationEnabled) {
        this.animationEnabled = animationEnabled;
        return this;
    }

    public Option setAnimationOnClose(boolean animationOnClose) {
        this.animationOnClose = animationOnClose;
        return this;
    }

    public Option setCloseOnPressedEscape(boolean closeOnPressedEscape) {
        this.closeOnPressedEscape = closeOnPressedEscape;
        return this;
    }

    public Option setHeavyWeight(boolean heavyWeight) {
        this.heavyWeight = heavyWeight;
        return this;
    }

    public Option setBackground(Color color) {
        this.backgroundLight = color;
        this.backgroundDark = color;
        return this;
    }

    public Option setBackground(Color colorLight, Color colorDark) {
        this.backgroundLight = colorLight;
        this.backgroundDark = colorDark;
        return this;
    }

    public Option setOpacity(float opacity) {
        this.opacity = opacity;
        return this;
    }

    public Option setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public Option setSliderDuration(int duration) {
        this.sliderDuration = duration;
        return this;
    }

    public enum BackgroundClickType {
        CLOSE_MODAL, BLOCK, NONE
    }

    public Option copy() {
        return new Option(layoutOption.copy(), borderOption.copy(), backgroundClickType, animationEnabled, animationOnClose, closeOnPressedEscape, heavyWeight, backgroundLight == null ? null : new Color(backgroundLight.getRGB()), backgroundDark == null ? null : new Color(backgroundDark.getRGB()), opacity, duration, sliderDuration);
    }
}
