package raven.modal.demo.sample.csv;

public class Pageable {

    public int getTotal() {
        return total;
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getLimit() {
        return limit;
    }

    public Pageable(int total, int page, int pageSize, int limit) {
        this.total = total;
        this.page = page;
        this.pageSize = pageSize;
        this.limit = limit;
    }

    private final int total;
    private final int page;
    private final int pageSize;
    private final int limit;

    public boolean hasPrevious() {
        return page > 1 && pageSize > 0;
    }

    public boolean hasNext() {
        return page < pageSize && pageSize > 0;
    }
}
