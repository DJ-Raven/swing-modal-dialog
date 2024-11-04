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

    public Location getVerticalLocation() {
        return verticalLocation;
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

    public float getAnimateScale() {
        return animateScale;
    }

    public boolean isOnTop() {
        return onTop;
    }

    private LayoutOption(Location horizontalLocation, Location verticalLocation, Insets margin, DynamicSize size, DynamicSize animateDistance, float animateScale, boolean onTop) {
        this.horizontalLocation = horizontalLocation;
        this.verticalLocation = verticalLocation;
        this.margin = margin;
        this.size = size;
        this.animateDistance = animateDistance;
        this.animateScale = animateScale;
        this.onTop = onTop;
    }

    public LayoutOption() {
    }

    private Location horizontalLocation = Location.CENTER;
    private Location verticalLocation = Location.CENTER;
    private Insets margin = new Insets(7, 7, 7, 7);
    private DynamicSize size = new DynamicSize(-1, -1);
    private DynamicSize animateDistance = new DynamicSize(0, 20);
    private float animateScale;
    private boolean onTop = false;

    public LayoutOption setLocation(Location horizontal, Location vertical) {
        this.horizontalLocation = horizontal;
        this.verticalLocation = vertical;
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
        return new LayoutOption(horizontalLocation, verticalLocation, new Insets(margin.top, margin.left, margin.bottom, margin.right), new DynamicSize(size), new DynamicSize(animateDistance), animateScale, onTop);
    }
}
