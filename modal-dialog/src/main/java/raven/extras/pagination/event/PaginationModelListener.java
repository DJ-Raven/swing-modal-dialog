package raven.extras.pagination.event;

import java.util.EventListener;

public interface PaginationModelListener extends EventListener {

    void paginationModelChanged(PaginationModelEvent event);
}
