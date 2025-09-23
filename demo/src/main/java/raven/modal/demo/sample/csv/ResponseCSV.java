package raven.modal.demo.sample.csv;

import java.util.List;

public class ResponseCSV extends ResponsePageable<List<String[]>> {

    public ResponseCSV(int total, int page, int pageSize, int limit, List<String[]> data) {
        super(total, page, pageSize, limit, data);
    }
}
