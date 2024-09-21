package raven.modal.drawer.menu;

import raven.modal.drawer.data.Item;

/**
 * @author Raven
 */
public class MenuAction {

    private boolean consume;
    private Item item;

    public MenuAction(Item item) {
        this.item = item;
    }

    protected boolean getConsume() {
        return consume;
    }

    public void consume() {
        consume = true;
    }

    public Item getItem() {
        return item;
    }
}
