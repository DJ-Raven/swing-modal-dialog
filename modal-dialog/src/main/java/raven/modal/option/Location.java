package raven.modal.option;

/**
 * This class for option location
 *
 * @author Raven
 */
public enum Location {

    TOP(0),
    CENTER(0.5f),
    BOTTOM(1f),
    LEFT(0f),
    RIGHT(1f),
    LEADING(0f),
    TRAILING(1f);

    private final float value;

    Location(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }
}