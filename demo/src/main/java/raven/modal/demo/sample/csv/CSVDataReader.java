package raven.modal.demo.sample.csv;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVDataReader {

    private final String[] columns;
    private final List<String[]> rows;

    private CSVDataReader(String[] columns, List<String[]> rows) {
        this.columns = columns;
        this.rows = rows;
    }

    public String[] getColumns() {
        return columns;
    }

    public ResponseCSV getData(int page, int limit) {
        int total = rows.size();
        int pageSize = (int) Math.ceil((double) total / limit);

        if (page > pageSize) {
            page = pageSize;
        }

        int start = Math.min((page - 1) * limit, total);
        int end = Math.min(start + limit, total);
        return new ResponseCSV(total, page, pageSize, limit, new ArrayList<>(rows.subList(start, end)));
    }

    public static CSVDataReader load(InputStream stream) throws IOException {
        return loadImpl(new BufferedReader(new InputStreamReader(stream)));
    }

    public static CSVDataReader load(File file) throws IOException {
        return loadImpl(new BufferedReader(new FileReader(file)));
    }

    private static CSVDataReader loadImpl(BufferedReader reader) throws IOException {
        String[] columns = null;
        List<String[]> rows = new ArrayList<>();
        String line;
        try (BufferedReader br = reader) {
            while ((line = br.readLine()) != null) {

                // parse the line as a CSV row
                String[] data = parseCSVLine(line);
                if (columns == null) {
                    columns = data;
                } else {
                    rows.add(data);
                }
            }
        }
        return new CSVDataReader(columns, rows);
    }

    /**
     * Code from ChatGPT
     * To parse the csv row to array
     */
    private static String[] parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);

            if (ch == '"') {
                // toggle the inQuotes flag unless it's an escaped quote
                if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    currentField.append(ch); // Add escaped quote
                    i++; // skip next quote
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (ch == ',' && !inQuotes) {
                // end of field
                result.add(currentField.toString());
                currentField.setLength(0); // reset the field
            } else {
                currentField.append(ch); // add character to the current field
            }
        }
        // add the last field
        result.add(currentField.toString());
        return result.toArray(new String[0]);
    }
}
