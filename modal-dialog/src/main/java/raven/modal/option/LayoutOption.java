package raven.modal.option;

import raven.modal.utils.DynamicSize;
import raven.modal.utils.ModalUtils;

import java.awt.*;

/**
 * This class for option layout
 *
 * @author Raven
 */
public class LayoutOption {

    public static LayoutOption getDefault() {
        return new LayoutOption();
    }

    public Location getHorizontalLocation() {
        return horizontalLocation;
    }

    public DynamicSize getLocation() {
        return location;
    }

    public Insets getMargin() {
        return margin;
    }

    public Insets getBackgroundPadding() {
        return backgroundPadding;
    }

    public DynamicSize getSize() {
        return size;
    }

    public DynamicSize getAnimateDistance() {
        return animateDistance;
    }

    public RelativeToOwnerType getRelativeToOwnerType() {
        return relativeToOwnerType;
    }

    public boolean isRelativeToOwner() {
        return relativeToOwner;
    }

    public boolean isOverflowAlignmentAuto() {
        return overflowAlignmentAuto;
    }

    public boolean isMovable() {
        return movable;
    }

    public float getAnimateScale() {
        return animateScale;
    }

    public boolean isOnTop() {
        return onTop;
    }

    private LayoutOption(Location horizontalLocation, DynamicSize location, Insets margin, Insets backgroundPadding, DynamicSize size, DynamicSize animateDistance, RelativeToOwnerType relativeToOwnerType, boolean relativeToOwner, boolean overflowAlignmentAuto, boolean movable, float animateScale, boolean onTop) {
        this.horizontalLocation = horizontalLocation;
        this.location = location;
        this.margin = margin;
        this.backgroundPadding = backgroundPadding;
        this.size = size;
        this.animateDistance = animateDistance;
        this.relativeToOwnerType = relativeToOwnerType;
        this.relativeToOwner = relativeToOwner;
        this.overflowAlignmentAuto = overflowAlignmentAuto;
        this.movable = movable;
        this.animateScale = animateScale;
        this.onTop = onTop;
    }

    public LayoutOption() {
    }

    // keep this horizontal location for component orientation (RTL or LTR)
    private Location horizontalLocation = Location.CENTER;
    private DynamicSize location = new DynamicSize(horizontalLocation.getValue(), Location.CENTER.getValue());
    private Insets margin = new Insets(7, 7, 7, 7);
    private Insets backgroundPadding = new Insets(0, 0, 0, 0);
    private DynamicSize size = new DynamicSize(-1, -1);
    private DynamicSize animateDistance = new DynamicSize(0, 20);
    private RelativeToOwnerType relativeToOwnerType = RelativeToOwnerType.RELATIVE_CONTAINED;
    private boolean relativeToOwner;
    private boolean overflowAlignmentAuto = true;
    private boolean movable;
    private float animateScale;
    private boolean onTop = false;

    public LayoutOption setLocation(Location horizontal, Location vertical) {
        this.location = new DynamicSize(horizontal.getValue(), vertical.getValue());
        this.horizontalLocation = horizontal;
        return this;
    }

    public LayoutOption setLocation(Location horizontal, Number y) {
        this.location = new DynamicSize(horizontal.getValue(), y);
        this.horizontalLocation = horizontal;
        return this;
    }

    public LayoutOption setLocation(Number x, Number y) {
        this.location = new DynamicSize(x, y);
        this.horizontalLocation = null;
        return this;
    }

    public LayoutOption setMargin(int top, int left, int bottom, int right) {
        this.margin = new Insets(top, left, bottom, right);
        return this;
    }

    public LayoutOption setMargin(int margin) {
        this.margin = new Insets(margin, margin, margin, margin);
        return this;
    }

    public LayoutOption setBackgroundPadding(int top, int left, int bottom, int right) {
        this.backgroundPadding = new Insets(top, left, bottom, right);
        return this;
    }

    public LayoutOption setBackgroundPadding(int padding) {
        this.backgroundPadding = new Insets(padding, padding, padding, padding);
        return this;
    }

    public LayoutOption setSize(Number width, Number height) {
        this.size = new DynamicSize(width, height);
        return this;
    }

    public LayoutOption setAnimateDistance(Number x, Number y) {
        this.animateDistance = new DynamicSize(x, y);
        return this;
    }

    public LayoutOption setRelativeToOwnerType(RelativeToOwnerType relativeToOwnerType) {
        this.relativeToOwnerType = relativeToOwnerType;
        return this;
    }

    public LayoutOption setRelativeToOwner(boolean relativeToOwner) {
        this.relativeToOwner = relativeToOwner;
        return this;
    }

    public LayoutOption setOverflowAlignmentAuto(boolean overflowAlignmentAuto) {
        this.overflowAlignmentAuto = overflowAlignmentAuto;
        return this;
    }

    public LayoutOption setMovable(boolean movable) {
        this.movable = movable;
        return this;
    }

    public LayoutOption setOnTop(boolean onTop) {
        this.onTop = onTop;
        return this;
    }

    public LayoutOption setAnimateScale(float animateScale) {
        if (animateScale < 0 || animateScale > 1) {
            throw new IllegalArgumentException("Scale value must be 0 to 1");
        }
        this.animateScale = animateScale;
        return this;
    }

    /**
     * RELATIVE_CONTAINED: Modal and background are confined to the owner's bounds and track the owner's visibility. (default)
     * RELATIVE_GLOBAL: Background spans the entire window and does not track the owner's visibility
     * RELATIVE_BOUNDLESS: Background covers the owner, but the modal can extend outside the owner. Tracks owner's visibility. (requires heavyWeight = true)
     */
    public enum RelativeToOwnerType {
        RELATIVE_CONTAINED, RELATIVE_GLOBAL, RELATIVE_BOUNDLESS
    }

    public LayoutOption copy() {
        return new LayoutOption(horizontalLocation, location, ModalUtils.copyInsets(margin), ModalUtils.copyInsets(backgroundPadding), new DynamicSize(size), new DynamicSize(animateDistance), relativeToOwnerType, relativeToOwner, overflowAlignmentAuto, movable, animateScale, onTop);
    }
}
