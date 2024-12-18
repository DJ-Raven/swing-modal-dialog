package raven.modal.toast.option;

import raven.modal.utils.DynamicSize;

/**
 * @author Raven
 */
public enum ToastDirection {

    TOP_TO_BOTTOM(new DynamicSize(0, -1f)),
    BOTTOM_TO_TOP(new DynamicSize(0, 1f)),
    LEFT_TO_RIGHT_TOP(new DynamicSize(-0.7f, 0)),
    LEFT_TO_RIGHT_BOTTOM(new DynamicSize(-0.7f, 0)),
    RIGHT_TO_LEFT_TOP(new DynamicSize(0.7f, 0)),
    RIGHT_TO_LEFT_BOTTOM(new DynamicSize(0.7f, 0));

    private final DynamicSize value;

    ToastDirection(DynamicSize value) {
        this.value = value;
    }

    public DynamicSize getValue() {
        return value;
    }

    public boolean isVerticalDirection() {
        return value.getY().floatValue() != 0f;
    }

    public boolean isToBottomDirection() {
        if (this == ToastDirection.LEFT_TO_RIGHT_BOTTOM || this == ToastDirection.RIGHT_TO_LEFT_BOTTOM) {
            return true;
        }
        if (this == ToastDirection.LEFT_TO_RIGHT_TOP || this == ToastDirection.RIGHT_TO_LEFT_TOP) {
            return false;
        }
        return this == ToastDirection.TOP_TO_BOTTOM;
    }
}
