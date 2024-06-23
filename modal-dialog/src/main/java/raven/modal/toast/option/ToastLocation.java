package raven.modal.toast.option;

import raven.modal.Toast;
import raven.modal.option.LayoutOption;
import raven.modal.option.Location;
import raven.modal.utils.DynamicSize;

/**
 * @author Raven
 */
public enum ToastLocation {

    TOP_LEADING(Location.LEADING, Location.TOP, new DynamicSize(-0.7f, 0)),
    TOP_CENTER(Location.CENTER, Location.TOP, new DynamicSize(0, -1f)),
    TOP_TRAILING(Location.TRAILING, Location.TOP, new DynamicSize(0.7f, 0)),
    BOTTOM_LEADING(Location.LEADING, Location.BOTTOM, new DynamicSize(-0.7f, 0)),
    BOTTOM_CENTER(Location.CENTER, Location.BOTTOM, new DynamicSize(0, 1f)),
    BOTTOM_TRAILING(Location.TRAILING, Location.BOTTOM, new DynamicSize(0.7f, 0));

    private ToastLocation(Location horizontalLocation, Location verticalLocation, DynamicSize animateDistance) {
        this.horizontalLocation = horizontalLocation;
        this.verticalLocation = verticalLocation;
        this.animateDistance = animateDistance;
    }

    private Location horizontalLocation;
    private Location verticalLocation;
    private DynamicSize animateDistance;

    public LayoutOption getLayout() {
        return Toast.createOption().getLayoutOption()
                .setLocation(horizontalLocation, verticalLocation)
                .setAnimateDistance(animateDistance.getX(), animateDistance.getY());
    }

    public static ToastLocation from(Location horizontalLocation, Location verticalLocation) {
        for (ToastLocation v : values()) {
            if (v.horizontalLocation == horizontalLocation && v.verticalLocation == verticalLocation) {
                return v;
            }
        }
        return TOP_CENTER;
    }
}
