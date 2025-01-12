package raven.modal.toast.option;

import raven.modal.layout.OptionLayoutUtils;
import raven.modal.option.LayoutOption;
import raven.modal.utils.DynamicSize;

import java.awt.*;

/**
 * @author Raven
 */
public class ToastLayoutOption {

    public static ToastLayoutOption getDefault() {
        return new ToastLayoutOption();
    }

    public ToastLocation getLocation() {
        return location;
    }

    public DynamicSize getLocationSize() {
        return locationSize;
    }

    public boolean isRelativeToOwner() {
        return relativeToOwner;
    }

    public ToastDirection getDirection() {
        if (direction != null) {
            return direction;
        }
        return location.getDirection();
    }

    private ToastLayoutOption(ToastLocation location, DynamicSize locationSize, ToastDirection direction, boolean relativeToOwner) {
        this.location = location;
        this.locationSize = locationSize;
        this.direction = direction;
        this.relativeToOwner = relativeToOwner;
    }

    public ToastLayoutOption() {
    }

    private ToastLocation location = ToastLocation.TOP_CENTER;
    private DynamicSize locationSize;
    private ToastDirection direction;
    private boolean relativeToOwner;
    private Insets margin = new Insets(7, 7, 7, 7);

    public ToastLayoutOption setLocation(ToastLocation location) {
        this.location = location;
        this.locationSize = null;
        return this;
    }

    public ToastLayoutOption setLocation(Number x, Number y) {
        locationSize = new DynamicSize(x, y);
        return this;
    }

    public ToastLayoutOption setDirection(ToastDirection direction) {
        this.direction = direction;
        return this;
    }

    public ToastLayoutOption setRelativeToOwner(boolean relativeToOwner) {
        this.relativeToOwner = relativeToOwner;
        return this;
    }

    public ToastLayoutOption setMargin(int top, int left, int bottom, int right) {
        this.margin = new Insets(top, left, bottom, right);
        return this;
    }

    public ToastLayoutOption setMargin(int margin) {
        this.margin = new Insets(margin, margin, margin, margin);
        return this;
    }

    public LayoutOption createLayoutOption(Component parent, Component owner) {
        ToastDirection direction = getDirection();
        Insets insets = new Insets(margin.top, margin.left, margin.bottom, margin.right);
        if (isRelativeToOwner()) {
            insets = OptionLayoutUtils.getOwnerInsert(parent, owner, insets);
        }
        LayoutOption layoutOption = new LayoutOption()
                .setRelativeToOwner(isRelativeToOwner())
                .setMargin(insets.top, insets.left, insets.bottom, insets.right)
                .setAnimateDistance(direction.getValue().getX(), direction.getValue().getY());

        if (locationSize != null) {
            layoutOption.setLocation(locationSize.getX(), locationSize.getY());

        } else {
            layoutOption.setLocation(location.getHorizontalLocation(), location.getVerticalLocation());
        }
        return layoutOption;
    }

    public ToastLayoutOption copy() {
        return new ToastLayoutOption(location, locationSize, direction, relativeToOwner);
    }
}
