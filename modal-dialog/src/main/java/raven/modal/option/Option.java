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

    public BackgroundBlur getBackgroundBlur() {
        return backgroundBlur;
    }

    public boolean isAnimationEnabled() {
        return animationEnabled;
    }

    public boolean isCloseOnPressedEscape() {
        return closeOnPressedEscape;
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
    public BackgroundBlur backgroundBlur = BackgroundBlur.NONE;
    private boolean animationEnabled = true;
    private boolean closeOnPressedEscape = true;
    private Color backgroundLight;
    private Color backgroundDark;
    private float opacity = 0.5f;
    private int duration = 350;
    private int sliderDuration = 400;

    private Option(LayoutOption layoutOption, BorderOption borderOption, BackgroundClickType backgroundClickType, BackgroundBlur backgroundBlur, boolean animationEnabled, boolean closeOnPressedEscape, Color backgroundLight, Color backgroundDark, float opacity, int duration, int sliderDuration) {
        this.layoutOption = layoutOption;
        this.borderOption = borderOption;
        this.backgroundClickType = backgroundClickType;
        this.backgroundBlur = backgroundBlur;
        this.animationEnabled = animationEnabled;
        this.closeOnPressedEscape = closeOnPressedEscape;
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

    public Option setBackgroundBlur(BackgroundBlur backgroundBlur) {
        this.backgroundBlur = backgroundBlur;
        return this;
    }

    public Option setAnimationEnabled(boolean animationEnabled) {
        this.animationEnabled = animationEnabled;
        return this;
    }

    public Option setCloseOnPressedEscape(boolean closeOnPressedEscape) {
        this.closeOnPressedEscape = closeOnPressedEscape;
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

    public enum BackgroundBlur {
        NONE(0),
        SMALL(2),
        MEDIUM(3),
        LARGE(4),
        EXTRA_LARGE(5),
        DOUBLE_EXTRA_LARGE(6);

        private final int radius;

        BackgroundBlur(int radius) {
            this.radius = radius;
        }

        public int getRadius() {
            return radius;
        }

        public boolean isBlur() {
            return radius > 1;
        }
    }

    public Option copy() {
        return new Option(layoutOption.copy(), borderOption.copy(), backgroundClickType, backgroundBlur, animationEnabled, closeOnPressedEscape, backgroundLight == null ? null : new Color(backgroundLight.getRGB()), backgroundDark == null ? null : new Color(backgroundDark.getRGB()), opacity, duration, sliderDuration);
    }
}
