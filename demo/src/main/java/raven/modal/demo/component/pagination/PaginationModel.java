package raven.modal.demo.component.pagination;

import raven.modal.demo.component.pagination.event.PaginationModelListener;

public interface PaginationModel {

    int getSelectedPage();

    void setSelectedPage(int selectedPage);

    int getPageSize();

    void setPageSize(int pageSize);

    void setPageRange(int selectedPage, int pageSize);

    boolean hasPrevious();

    boolean hasNext();

    Page[] getPagination();

    void addPaginationModelListener(PaginationModelListener listener);

    void removePaginationModelListener(PaginationModelListener listener);
}
