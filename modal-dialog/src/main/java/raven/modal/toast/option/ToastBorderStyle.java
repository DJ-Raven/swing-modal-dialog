package raven.modal.toast.option;

import raven.modal.toast.ToastBorder;
import raven.modal.toast.ToastPanel;
import raven.modal.utils.ModalUtils;

import javax.swing.border.Border;
import java.awt.*;

/**
 * @author Raven
 */
public class ToastBorderStyle {

    public static ToastBorderStyle getDefault() {
        return new ToastBorderStyle();
    }

    public BorderType getBorderType() {
        return borderType;
    }

    public float getRound() {
        return round;
    }

    public Insets getShadowSize() {
        return shadowSize;
    }

    public Color getShadowColor() {
        return shadowColor;
    }

    public float getShadowOpacity() {
        return shadowOpacity;
    }

    public int getLineSize() {
        return lineSize;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public Insets getPadding() {
        return padding;
    }

    private ToastBorderStyle(BorderType borderType, float round, Insets shadowSize, Color shadowColor, float shadowOpacity, int lineSize, int borderWidth, Insets padding) {
        this.borderType = borderType;
        this.round = round;
        this.shadowSize = shadowSize;
        this.shadowColor = shadowColor;
        this.shadowOpacity = shadowOpacity;
        this.lineSize = lineSize;
        this.borderWidth = borderWidth;
        this.padding = padding;
    }

    public ToastBorderStyle() {
    }

    private BorderType borderType = BorderType.NONE;
    private float round = 10;
    private Insets shadowSize = new Insets(0, 0, 10, 10);
    private Color shadowColor;
    private float shadowOpacity = -1;
    private int lineSize = 3;
    private int borderWidth = 1;
    private Insets padding = new Insets(0, 0, 0, 0);

    public ToastBorderStyle setBorderType(BorderType borderType) {
        this.borderType = borderType;
        return this;
    }

    public ToastBorderStyle setRound(float round) {
        this.round = round;
        return this;
    }

    public ToastBorderStyle setShadowSize(int shadowSize) {
        if (shadowSize < 0) {
            throw new IllegalArgumentException("shadow size must be >=0");
        }
        this.shadowSize = new Insets(shadowSize, shadowSize, shadowSize, shadowSize);
        return this;
    }

    public ToastBorderStyle setShadowSize(Insets shadowSize) {
        if (ModalUtils.minimumInsets(shadowSize) < 0) {
            throw new IllegalArgumentException("shadow size must be >=0");
        }
        this.shadowSize = new Insets(shadowSize.top, shadowSize.left, shadowSize.bottom, shadowSize.right);
        return this;
    }

    public ToastBorderStyle setShadowColor(Color shadowColor) {
        this.shadowColor = shadowColor;
        return this;
    }

    public ToastBorderStyle setShadowOpacity(float shadowOpacity) {
        this.shadowOpacity = shadowOpacity;
        return this;
    }

    public ToastBorderStyle setLineSize(int lineSize) {
        this.lineSize = lineSize;
        return this;
    }

    public ToastBorderStyle setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        return this;
    }

    public ToastBorderStyle setPadding(int top, int left, int bottom, int right) {
        this.padding = new Insets(top, left, bottom, right);
        return this;
    }

    public ToastBorderStyle setPadding(int padding) {
        this.padding = new Insets(padding, padding, padding, padding);
        return this;
    }

    public ToastBorderStyle setShadow(ToastBorderStyle.Shadow shadow) {
        shadow.apply(this);
        return this;
    }

    public Border createBorder(Component component, ToastPanel.ToastData toastData) {
        return new ToastBorder(component, toastData, shadowSize, shadowOpacity, shadowColor, round);
    }

    public ToastBorderStyle copy() {
        return new ToastBorderStyle(borderType, round, ModalUtils.copyInsets(shadowSize), shadowColor, shadowOpacity, lineSize, borderWidth, ModalUtils.copyInsets(padding));
    }

    public enum Shadow {
        NONE, SMALL, MEDIUM, LARGE, EXTRA_LARGE;

        private void apply(ToastBorderStyle option) {
            if (this == NONE) {
                option.setShadowSize(0);
            } else if (this == SMALL) {
                option.setShadowSize(new Insets(0, 0, 6, 6));
            } else if (this == MEDIUM) {
                option.setShadowSize(new Insets(0, 0, 10, 10));
            } else if (this == LARGE) {
                option.setShadowSize(new Insets(0, 0, 12, 12));
            } else {
                option.setShadowSize(new Insets(0, 0, 15, 15));
            }
        }
    }

    public enum BorderType {
        OUTLINE, TRAILING_LINE, LEADING_LINE, TOP_LINE, BOTTOM_LINE, NONE
    }
}
