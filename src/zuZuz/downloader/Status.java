package zuZuz.downloader;

public class Status {
    private int code;
    private String message = "OK";

    static final int OK = 0;
    static final int INCOMPLETE = 2;
    static final int UNAVAILABLE = 4;
    static final int IOERROR = 8;

    Status(int code, String message) {
        this.code = code;
        this.message = message;
    }

    Status(int code) {
        this.code = code;
    }

    int getCode() {
        return code;
    }

    String getMessage() {
        return message;
    }
}