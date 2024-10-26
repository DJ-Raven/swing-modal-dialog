package raven.modal.option;

import com.formdev.flatlaf.ui.FlatUIUtils;
import raven.modal.component.OutlineBorder;
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

    public float getRound() {
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

    public float getBorderWidth() {
        return borderWidth;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    private BorderOption(float round, Insets shadowSize, Color shadowColor, float shadowOpacity, float borderWidth, Color borderColor) {
        this.round = round;
        this.shadowSize = shadowSize;
        this.shadowColor = shadowColor;
        this.shadowOpacity = shadowOpacity;
        this.borderWidth = borderWidth;
        this.borderColor = borderColor;
    }

    public BorderOption() {
    }

    private float round = 20;
    private Insets shadowSize = new Insets(0, 0, 0, 0);
    private Color shadowColor;
    private float shadowOpacity = -1;
    private float borderWidth = 0;
    private Color borderColor;

    public BorderOption setRound(float round) {
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

    public BorderOption setBorderWidth(float borderWidth) {
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
        Insets newShadowSize = new Insets(shadowSize.top, shadowSize.left, shadowSize.bottom, shadowSize.right);
        return new BorderOption(round, newShadowSize, shadowColor, shadowOpacity, borderWidth, borderColor);
    }

    public boolean isBorderAble() {
        return borderWidth > 0 || round > 0 || !FlatUIUtils.isInsetsEmpty(shadowSize);
    }

    public Border createBorder() {
        if (isBorderAble()) {
            return new OutlineBorder(shadowSize, shadowOpacity, shadowColor, borderWidth, borderColor, round);
        }
        return null;
    }

    public enum Shadow {
        NONE, SMALL, MEDIUM, LARGE, EXTRA_LARGE, DOUBLE_EXTRA_LARGE;

        private void apply(BorderOption option) {
            if (this == NONE) {
                option.setShadowSize(0);
            } else if (this == SMALL) {
                option.setShadowSize(new Insets(4, 4, 6, 6));
            } else if (this == MEDIUM) {
                option.setShadowSize(new Insets(8, 8, 12, 12));
            } else if (this == LARGE) {
                option.setShadowSize(new Insets(12, 12, 18, 18));
            } else if (this == EXTRA_LARGE) {
                option.setShadowSize(new Insets(16, 16, 24, 24));
            } else {
                option.setShadowSize(new Insets(20, 20, 30, 30));
            }
        }
    }
}
