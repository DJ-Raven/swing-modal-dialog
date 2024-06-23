package raven.modal.drawer.menu;

/**
 * @author Raven
 */
public class MenuAction {

    private boolean consume;

    private boolean selected;

    protected boolean getConsume() {
        return consume;
    }

    public void consume() {
        consume = true;
    }

    protected boolean getSelected() {
        return selected;
    }

    public void selected() {
        selected = true;
    }
}
