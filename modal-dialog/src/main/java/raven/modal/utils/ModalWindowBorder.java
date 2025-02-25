package raven.modal.utils;

import com.formdev.flatlaf.ui.FlatUIUtils;

import java.awt.*;

/**
 * @author Raven
 */
public class ModalWindowBorder {

    public static ModalWindowBorder getDefault() {
        return new ModalWindowBorder(new Insets(8, 8, 12, 12), -1, null, 0, null, 20);
    }

    public Insets getShadowSize() {
        return shadowSize;
    }

    public float getShadowOpacity() {
        return shadowOpacity;
    }

    public Color getShadowColor() {
        return shadowColor;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public int getRound() {
        return round;
    }

    public ModalWindowBorder(Insets shadowSize, float shadowOpacity, Color shadowColor, int borderWidth, Color borderColor, int round) {
        this.shadowSize = shadowSize;
        this.shadowOpacity = shadowOpacity;
        this.shadowColor = shadowColor;
        this.borderWidth = borderWidth;
        this.borderColor = borderColor;
        this.round = round;
    }

    private Insets shadowSize;
    private float shadowOpacity;
    private Color shadowColor;
    private int borderWidth;
    private Color borderColor;
    private int round;

    public boolean isCreatedAble() {
        return !(FlatUIUtils.isInsetsEmpty(shadowSize) && borderWidth == 0 && round == 0);
    }
}
