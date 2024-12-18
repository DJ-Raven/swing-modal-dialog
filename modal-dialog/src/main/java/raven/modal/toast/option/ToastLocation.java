package raven.modal.toast.option;

import raven.modal.option.Location;

/**
 * @author Raven
 */
public enum ToastLocation {

    TOP_LEADING(Location.LEADING, Location.TOP, ToastDirection.LEFT_TO_RIGHT_BOTTOM),
    TOP_CENTER(Location.CENTER, Location.TOP, ToastDirection.TOP_TO_BOTTOM),
    TOP_TRAILING(Location.TRAILING, Location.TOP, ToastDirection.RIGHT_TO_LEFT_BOTTOM),
    BOTTOM_LEADING(Location.LEADING, Location.BOTTOM, ToastDirection.LEFT_TO_RIGHT_TOP),
    BOTTOM_CENTER(Location.CENTER, Location.BOTTOM, ToastDirection.BOTTOM_TO_TOP),
    BOTTOM_TRAILING(Location.TRAILING, Location.BOTTOM, ToastDirection.RIGHT_TO_LEFT_TOP);

    ToastLocation(Location horizontalLocation, Location verticalLocation, ToastDirection direction) {
        this.horizontalLocation = horizontalLocation;
        this.verticalLocation = verticalLocation;
        this.direction = direction;
    }

    private Location horizontalLocation;
    private Location verticalLocation;
    private ToastDirection direction;

    public Location getHorizontalLocation() {
        return horizontalLocation;
    }

    public Location getVerticalLocation() {
        return verticalLocation;
    }

    public ToastDirection getDirection() {
        return direction;
    }

    public static ToastLocation from(Location horizontalLocation, Location verticalLocation) {
        for (ToastLocation v : values()) {
            if (v.horizontalLocation == horizontalLocation && v.verticalLocation == verticalLocation) {
                return v;
            }
        }
        return TOP_CENTER;
    }

    public boolean isSame(ToastLocation location) {
        return this.horizontalLocation.getValue() == location.horizontalLocation.getValue()
                && this.verticalLocation.getValue() == location.verticalLocation.getValue();
    }
}
