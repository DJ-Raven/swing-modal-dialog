package raven.modal.option;

import java.awt.*;

/**
 * This class for SimpleModalBorder option
 *
 * @author Raven
 */
public class ModalBorderOption {

    public boolean isUseScroll() {
        return useScroll;
    }

    public Insets getPadding() {
        return padding;
    }

    private boolean useScroll;
    private Insets padding = PaddingType.EXTRA_LARGE.getValue();

    public ModalBorderOption setUseScroll(boolean useScroll) {
        this.useScroll = useScroll;
        return this;
    }

    public ModalBorderOption setPadding(int padding) {
        return setPadding(padding, padding, padding, padding);
    }

    public ModalBorderOption setPadding(PaddingType type) {
        this.padding = type.getValue();
        return this;
    }

    public ModalBorderOption setPadding(int top, int left, int bottom, int right) {
        this.padding = new Insets(top, left, bottom, right);
        return this;
    }

    public enum PaddingType {
        SMALL, MEDIUM, LARGE, EXTRA_LARGE;

        public Insets getValue() {
            if (this == SMALL) {
                return new Insets(2, 5, 2, 5);
            } else if (this == MEDIUM) {
                return new Insets(5, 10, 5, 10);
            } else if (this == LARGE) {
                return new Insets(8, 15, 8, 15);
            } else {
                return new Insets(18, 30, 18, 30);
            }
        }
    }
}
