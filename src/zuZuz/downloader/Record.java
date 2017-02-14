package zuZuz.downloader;

class Record {
    private static final String SEPARATOR = " ";
    private String[] record;

    Record(String line) {
        record = line.split(SEPARATOR);
    }

    String getUrl() {
        return record[0];
    }

    String getFilename() {
        return record[1];
    }
}
