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

    public BackgroundType getBackgroundType() {
        return backgroundType;
    }

    public BorderType getBorderType() {
        return borderType;
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

    public int getLineSize() {
        return lineSize;
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

    public int getOutlineSize() {
        return 1;
    }

    public ToastStyle(BackgroundType backgroundType, BorderType borderType, boolean showLabel, boolean iconSeparateLine, boolean showCloseButton, int lineSize, String promiseLabel, Icon icon) {
        this.backgroundType = backgroundType;
        this.borderType = borderType;
        this.showLabel = showLabel;
        this.iconSeparateLine = iconSeparateLine;
        this.showCloseButton = showCloseButton;
        this.lineSize = lineSize;
        this.promiseLabel = promiseLabel;
        this.customIcon = icon;
    }

    public ToastStyle() {
    }

    private BackgroundType backgroundType = BackgroundType.DEFAULT;
    private BorderType borderType = BorderType.NONE;
    private boolean showLabel;
    private boolean iconSeparateLine;
    private boolean showCloseButton = true;
    private int lineSize = 3;
    private String promiseLabel = "Loading";
    private Icon customIcon;

    public ToastStyle setBackgroundType(BackgroundType backgroundType) {
        this.backgroundType = backgroundType;
        return this;
    }

    public ToastStyle setBorderType(BorderType borderType) {
        this.borderType = borderType;
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

    public ToastStyle setLineSize(int lineSize) {
        this.lineSize = lineSize;
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
        return new ToastStyle(backgroundType, borderType, showLabel, iconSeparateLine, showCloseButton, lineSize, promiseLabel, customIcon);
    }

    public enum BackgroundType {
        DEFAULT, GRADIENT, NONE
    }

    public enum BorderType {
        OUTLINE, TRAILING_LINE, LEADING_LINE, TOP_LINE, BOTTOM_LINE, NONE
    }
}
