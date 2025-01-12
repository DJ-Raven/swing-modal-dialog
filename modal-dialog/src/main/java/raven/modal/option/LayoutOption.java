package raven.modal.option;

import raven.modal.utils.DynamicSize;

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

    public DynamicSize getSize() {
        return size;
    }

    public DynamicSize getAnimateDistance() {
        return animateDistance;
    }

    public boolean isRelativeToOwner() {
        return relativeToOwner;
    }

    public float getAnimateScale() {
        return animateScale;
    }

    public boolean isOnTop() {
        return onTop;
    }

    private LayoutOption(DynamicSize location, Insets margin, DynamicSize size, DynamicSize animateDistance, boolean relativeToOwner, float animateScale, boolean onTop) {
        this.location = location;
        this.margin = margin;
        this.size = size;
        this.animateDistance = animateDistance;
        this.relativeToOwner = relativeToOwner;
        this.animateScale = animateScale;
        this.onTop = onTop;
    }

    public LayoutOption() {
    }

    // keep this horizontal location for component orientation (RTL or LTR)
    private Location horizontalLocation = Location.CENTER;
    private DynamicSize location = new DynamicSize(horizontalLocation.getValue(), Location.CENTER.getValue());
    private Insets margin = new Insets(7, 7, 7, 7);
    private DynamicSize size = new DynamicSize(-1, -1);
    private DynamicSize animateDistance = new DynamicSize(0, 20);
    private boolean relativeToOwner;
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

    public LayoutOption setSize(Number width, Number height) {
        this.size = new DynamicSize(width, height);
        return this;
    }

    public LayoutOption setAnimateDistance(Number x, Number y) {
        this.animateDistance = new DynamicSize(x, y);
        return this;
    }

    public LayoutOption setRelativeToOwner(boolean relativeToOwner) {
        this.relativeToOwner = relativeToOwner;
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

    public LayoutOption copy() {
        return new LayoutOption(location, new Insets(margin.top, margin.left, margin.bottom, margin.right), new DynamicSize(size), new DynamicSize(animateDistance), relativeToOwner, animateScale, onTop);
    }
}
