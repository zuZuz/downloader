package zuZuz.downloader;

import java.net.HttpURLConnection;
import java.net.URL;

abstract class HttpConnection extends HttpURLConnection {
    static final int INFO = 1;
    static final int SUCCESS = 2;
    static final int REDIRECTION = 3;
    static final int CLIENTERROR = 4;
    static final int SERVERERROR = 5;

    protected HttpConnection(URL u) {
        super(u);
    }

    int getStatusClass() throws Exception {
        return this.getResponseCode() / 100;
    }
}
