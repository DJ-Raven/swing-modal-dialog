package raven.modal.option;

import com.formdev.flatlaf.ui.FlatUIUtils;
import raven.modal.component.DropShadowBorder;
import raven.modal.utils.ModalUtils;

import javax.swing.border.Border;
import java.awt.*;

/**
 * @author Raven
 */
public class BorderOption {

    public static BorderOption getDefault() {
        return new BorderOption();
    }

    public int getRound() {
        return round;
    }

    public Insets getShadowSize() {
        return new Insets(shadowSize.top, shadowSize.left, shadowSize.bottom, shadowSize.right);
    }

    public Color getShadowColor() {
        return shadowColor;
    }

    public float getShadowOpacity() {
        return shadowOpacity;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    private BorderOption(int round, Insets shadowSize, Color shadowColor, float shadowOpacity, int borderWidth, Color borderColor) {
        this.round = round;
        this.shadowSize = shadowSize;
        this.shadowColor = shadowColor;
        this.shadowOpacity = shadowOpacity;
        this.borderWidth = borderWidth;
        this.borderColor = borderColor;
    }

    public BorderOption() {
    }

    private int round = 20;
    private Insets shadowSize = new Insets(0, 0, 0, 0);
    private Color shadowColor;
    private float shadowOpacity = -1;
    private int borderWidth = 0;
    private Color borderColor;

    public BorderOption setRound(int round) {
        this.round = round;
        return this;
    }

    public BorderOption setShadowSize(int shadowSize) {
        if (shadowSize < 0) {
            throw new IllegalArgumentException("shadow size must be >=0");
        }
        this.shadowSize = new Insets(shadowSize, shadowSize, shadowSize, shadowSize);
        return this;
    }

    public BorderOption setShadowSize(Insets shadowSize) {
        if (ModalUtils.minimumInsets(shadowSize) < 0) {
            throw new IllegalArgumentException("shadow size must be >=0");
        }
        this.shadowSize = new Insets(shadowSize.top, shadowSize.left, shadowSize.bottom, shadowSize.right);
        return this;
    }

    public BorderOption setShadowColor(Color shadowColor) {
        this.shadowColor = shadowColor;
        return this;
    }

    public BorderOption setShadowOpacity(float shadowOpacity) {
        this.shadowOpacity = shadowOpacity;
        return this;
    }

    public BorderOption setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        return this;
    }

    public BorderOption setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    public BorderOption setShadow(Shadow shadow) {
        shadow.apply(this);
        return this;
    }

    public BorderOption copy() {
        return new BorderOption(round, ModalUtils.copyInsets(shadowSize), shadowColor, shadowOpacity, borderWidth, borderColor);
    }

    public boolean isBorderAble() {
        return borderWidth > 0 || round > 0 || !FlatUIUtils.isInsetsEmpty(shadowSize);
    }

    public Border createBorder() {
        if (isBorderAble()) {
            return new DropShadowBorder(shadowSize, shadowOpacity, shadowColor, borderWidth, borderColor, round);
        }
        return null;
    }

    public enum Shadow {
        NONE, SMALL, MEDIUM, LARGE, EXTRA_LARGE, DOUBLE_EXTRA_LARGE;

        private void apply(BorderOption option) {
            if (this == NONE) {
                option.setShadowSize(0);
            } else if (this == SMALL) {
                option.setShadowSize(new Insets(2, 5, 6, 5));
            } else if (this == MEDIUM) {
                option.setShadowSize(new Insets(4, 7, 12, 7));
            } else if (this == LARGE) {
                option.setShadowSize(new Insets(6, 9, 18, 9));
            } else if (this == EXTRA_LARGE) {
                option.setShadowSize(new Insets(8, 13, 24, 13));
            } else {
                option.setShadowSize(new Insets(10, 15, 30, 15));
            }
        }
    }
}
