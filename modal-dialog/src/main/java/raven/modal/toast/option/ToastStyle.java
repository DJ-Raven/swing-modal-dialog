package raven.modal.toast.option;

import raven.modal.Toast;

import javax.swing.*;

/**
 * @author Raven
 */
public class ToastStyle {

    public static ToastStyle getDefault() {
        return new ToastStyle();
    }

    public ToastBorderStyle getBorderStyle() {
        return borderStyle;
    }

    public BackgroundType getBackgroundType() {
        return backgroundType;
    }

    public boolean isShowLabel() {
        return showLabel;
    }

    public boolean isIconSeparateLine() {
        return iconSeparateLine;
    }

    public boolean isShowCloseButton() {
        return showCloseButton;
    }

    public boolean isPaintTextColor() {
        return paintTextColor;
    }

    public String getPromiseLabel() {
        return promiseLabel;
    }

    public Icon getCustomIcon() {
        return customIcon;
    }

    public String getLabelText(Toast.Type type) {
        if (type == null) {
            return promiseLabel;
        } else {
            String text = type.toString().toLowerCase();
            return text.substring(0, 1).toUpperCase() + text.substring(1);
        }
    }

    private ToastStyle(ToastBorderStyle borderStyle, BackgroundType backgroundType, boolean showLabel, boolean iconSeparateLine, boolean showCloseButton, boolean paintTextColor, String promiseLabel, Icon icon) {
        this.borderStyle = borderStyle;
        this.backgroundType = backgroundType;
        this.showLabel = showLabel;
        this.iconSeparateLine = iconSeparateLine;
        this.showCloseButton = showCloseButton;
        this.paintTextColor = paintTextColor;
        this.promiseLabel = promiseLabel;
        this.customIcon = icon;
    }

    public ToastStyle() {
    }

    private ToastBorderStyle borderStyle = ToastBorderStyle.getDefault();
    private BackgroundType backgroundType = BackgroundType.DEFAULT;
    private boolean showLabel;
    private boolean iconSeparateLine;
    private boolean showCloseButton = true;
    private boolean paintTextColor;
    private String promiseLabel = "Loading";
    private Icon customIcon;

    public ToastStyle setBorderStyle(ToastBorderStyle borderStyle) {
        this.borderStyle = borderStyle;
        return this;
    }

    public ToastStyle setBackgroundType(BackgroundType backgroundType) {
        this.backgroundType = backgroundType;
        return this;
    }

    public ToastStyle setShowLabel(boolean showLabel) {
        this.showLabel = showLabel;
        return this;
    }

    public ToastStyle setIconSeparateLine(boolean iconSeparateLine) {
        this.iconSeparateLine = iconSeparateLine;
        return this;
    }

    public ToastStyle setShowCloseButton(boolean showCloseButton) {
        this.showCloseButton = showCloseButton;
        return this;
    }

    public ToastStyle setPaintTextColor(boolean paintTextColor) {
        this.paintTextColor = paintTextColor;
        return this;
    }

    public ToastStyle setPromiseLabel(String promiseLabel) {
        this.promiseLabel = promiseLabel;
        return this;
    }

    public ToastStyle setCustomIcon(Icon icon) {
        this.customIcon = icon;
        return this;
    }

    public ToastStyle copy() {
        return new ToastStyle(borderStyle.copy(), backgroundType, showLabel, iconSeparateLine, showCloseButton, paintTextColor, promiseLabel, customIcon);
    }

    public enum BackgroundType {
        DEFAULT, GRADIENT, NONE
    }
}
