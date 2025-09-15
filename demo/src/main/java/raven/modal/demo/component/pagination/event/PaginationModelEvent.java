package raven.modal.demo.component.pagination.event;

import java.util.EventObject;

public class PaginationModelEvent extends EventObject {

    private final boolean pageChanged;

    public PaginationModelEvent(Object source, boolean pageChanged) {
        super(source);
        this.pageChanged = pageChanged;
    }

    public boolean isPageChanged() {
        return pageChanged;
    }
}
