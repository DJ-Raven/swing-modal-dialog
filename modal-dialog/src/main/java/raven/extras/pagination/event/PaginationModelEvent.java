package raven.extras.pagination.event;

import java.util.EventObject;

public class PaginationModelEvent extends EventObject {

    private final boolean pageChanged;
    private final int oldPage;
    private final int oldIndex;
    private final int newPage;
    private final int newIndex;

    public PaginationModelEvent(Object source, boolean pageChanged, int oldPage, int oldIndex, int newPage, int newIndex) {
        super(source);
        this.pageChanged = pageChanged;
        this.oldPage = oldPage;
        this.oldIndex = oldIndex;
        this.newPage = newPage;
        this.newIndex = newIndex;
    }

    public boolean isPageChanged() {
        return pageChanged;
    }


    public int getOldPage() {
        return oldPage;
    }

    public int getOldIndex() {
        return oldIndex;
    }

    public int getNewPage() {
        return newPage;
    }

    public int getNewIndex() {
        return newIndex;
    }
}
