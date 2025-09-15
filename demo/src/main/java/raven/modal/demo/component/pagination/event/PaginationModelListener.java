package raven.modal.demo.component.pagination.event;

import java.util.EventListener;

public interface PaginationModelListener extends EventListener {

    void paginationModelChanged(PaginationModelEvent event);
}
