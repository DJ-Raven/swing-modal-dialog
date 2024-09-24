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

    public BackgroundClickType getBackgroundClickType() {
        return backgroundClickType;
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

    public float getRound() {
        return round;
    }

    public float getBorderWidth() {
        return borderWidth;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public float getOpacity() {
        return opacity;
    }

    public int getDuration() {
        return duration;
    }

    private LayoutOption layoutOption = LayoutOption.getDefault();
    private BackgroundClickType backgroundClickType = BackgroundClickType.CLOSE_MODAL;
    private boolean animationEnabled = true;
    private boolean closeOnPressedEscape = true;
    private Color backgroundLight;
    private Color backgroundDark;
    private float round = 20;
    private float borderWidth = 0;
    private Color borderColor;
    private float opacity = 0.5f;
    private int duration = 350;

    private Option(LayoutOption layoutOption, BackgroundClickType backgroundClickType, boolean animationEnabled, boolean closeOnPressedEscape, Color backgroundLight, Color backgroundDark, float round, float borderWidth, Color borderColor, float opacity, int duration) {
        this.layoutOption = layoutOption;
        this.backgroundClickType = backgroundClickType;
        this.animationEnabled = animationEnabled;
        this.closeOnPressedEscape = closeOnPressedEscape;
        this.backgroundLight = backgroundLight;
        this.backgroundDark = backgroundDark;
        this.round = round;
        this.borderWidth = borderWidth;
        this.borderColor = borderColor;
        this.opacity = opacity;
        this.duration = duration;
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

    public Option setRound(float round) {
        this.round = round;
        return this;
    }

    public Option setBorderWidth(float borderWidth) {
        this.borderWidth = borderWidth;
        return this;
    }

    public Option setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
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

    public enum BackgroundClickType {
        CLOSE_MODAL, BLOCK, NONE
    }

    public Option copy() {
        return new Option(layoutOption.copy(), backgroundClickType, animationEnabled, closeOnPressedEscape, backgroundLight == null ? null : new Color(backgroundLight.getRGB()), backgroundDark == null ? null : new Color(backgroundDark.getRGB()), round, borderWidth, borderColor, opacity, duration);
    }
}
