package raven.extras.pagination;

public class Page {

    private final int value;
    private final Type type;

    public Page(int value, Type type) {
        this.value = value;
        this.type = type;
    }

    public Page(int value) {
        this(value, Type.PAGE);
    }

    public int getValue() {
        return value;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        PAGE, PREVIOUS, NEXT, ELLIPSIS
    }
}
