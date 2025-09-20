package raven.modal.demo.component.pagination;

import raven.modal.demo.component.pagination.event.PaginationModelEvent;
import raven.modal.demo.component.pagination.event.PaginationModelListener;

import javax.swing.event.EventListenerList;

public class DefaultPaginationModel implements PaginationModel {

    protected EventListenerList listenerList = new EventListenerList();
    private Page[] pagination;
    private int maxItem;
    private int selectedPage;
    private int pageSize;

    public DefaultPaginationModel() {
        this(0, 0);
    }

    public DefaultPaginationModel(int selectedPage, int pageSize) {
        this(7, selectedPage, pageSize);
    }

    public DefaultPaginationModel(int maxItem, int selectedPage, int pageSize) {
        if (maxItem > 0 && selectedPage >= 0 && selectedPage <= pageSize) {
            if (selectedPage == 0) {
                selectedPage = 1;
            }
            if (maxItem % 2 == 0) {
                maxItem++;
            }
            if (maxItem < 5) {
                maxItem = 5;
            }
            this.maxItem = maxItem;
            this.selectedPage = selectedPage;
            this.pageSize = pageSize;
            createPages();
        } else {
            throw new IllegalArgumentException("invalid page properties");
        }
    }

    private void createPages() {
        if (pageSize <= 0) {
            this.pagination = new Page[]{};
            return;
        }
        if (pageSize == 1) {
            this.pagination = new Page[]{new Page(1)};
            return;
        }

        int size = maxItem;
        int offset = (size - 1) / 2;
        boolean shouldAddDots = pageSize > size;
        int start = clamp(selectedPage - offset, 1, shouldAddDots ? pageSize - size + 1 : 1);
        int end = clamp(selectedPage + offset, size, pageSize);

        Page[] pages = getRange(start, end);
        boolean dotsPrevious = shouldAddDots && pages[0].getValue() != 1;
        boolean dotsNext = shouldAddDots && pages[pages.length - 1].getValue() != pageSize;

        int showPage = (size - 3) / 2;

        if (dotsPrevious) {
            int pagePrevious = Math.min(pageSize, pages[3].getValue() - showPage - 1);
            pages[0] = new Page(1);
            pages[1] = new Page(pagePrevious, Page.Type.ELLIPSIS);
        }
        if (dotsNext) {
            int pageNext = Math.min(pageSize, pages[pages.length - 3].getValue() + showPage);
            pages[pages.length - 1] = new Page(pageSize);
            pages[pages.length - 2] = new Page(pageNext, Page.Type.ELLIPSIS);
        }

        this.pagination = pages;
    }

    @Override
    public Page[] getPagination() {
        return pagination;
    }

    public int getMaxItem() {
        return maxItem;
    }

    public void setMaxItem(int maxItem) {
        // page size must be odd. The size will be increased by 1
        if (maxItem % 2 == 0) {
            maxItem++;
        }
        // minimum page size is 5
        if (maxItem < 5) {
            maxItem = 5;
        }
        if (this.maxItem != maxItem) {
            this.maxItem = maxItem;
            createPages();
            fireStateChanged(new PaginationModelEvent(this, false));
        }
    }

    @Override
    public int getSelectedPage() {
        return selectedPage;
    }

    @Override
    public void setSelectedPage(int selectedPage) {
        int newValue = Math.max(selectedPage, 1);
        newValue = Math.min(newValue, pageSize);
        setPageRange(newValue, pageSize);
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public void setPageSize(int pageSize) {
        int newValue = Math.max(pageSize, 0);
        setPageRange(selectedPage, newValue);
    }

    @Override
    public void setPageRange(int selectedPage, int pageSize) {
        if (selectedPage > pageSize) {
            selectedPage = pageSize;
        }

        if (selectedPage == 0) {
            selectedPage = 1;
        }

        boolean changed = this.selectedPage != selectedPage
                || this.pageSize != pageSize;

        if (changed) {
            boolean pageChanged = this.selectedPage != selectedPage;

            this.selectedPage = selectedPage;
            this.pageSize = pageSize;
            createPages();
            fireStateChanged(new PaginationModelEvent(this, pageChanged));
        }
    }

    @Override
    public boolean hasPrevious() {
        return selectedPage > 1 && pageSize > 0;
    }

    @Override
    public boolean hasNext() {
        return selectedPage < pageSize && pageSize > 0;
    }

    private int clamp(int value, int lower, int upper) {
        return Math.min(Math.max(value, lower), upper);
    }

    private Page[] getRange(int start, int end) {
        if (end < start) {
            throw new IllegalArgumentException("invalid range number");
        }
        int size = (end - start) + 1;
        Page[] pages = new Page[size];
        for (int i = 0; i < size; i++) {
            pages[i] = new Page(start + i);
        }
        return pages;
    }

    @Override
    public void addPaginationModelListener(PaginationModelListener listener) {
        listenerList.add(PaginationModelListener.class, listener);
    }

    @Override
    public void removePaginationModelListener(PaginationModelListener listener) {
        listenerList.remove(PaginationModelListener.class, listener);
    }

    protected void fireStateChanged(PaginationModelEvent event) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == PaginationModelListener.class) {
                ((PaginationModelListener) listeners[i + 1]).paginationModelChanged(event);
            }
        }
    }
}
