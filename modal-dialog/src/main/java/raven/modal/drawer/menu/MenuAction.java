package raven.modal.drawer.menu;

/**
 * @author Raven
 */
public class MenuAction {

    private boolean consume;

    protected boolean getConsume() {
        return consume;
    }

    public void consume() {
        consume = true;
    }
}
