package zuZuz.downloader;

import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.util.concurrent.Callable;

public class DownloadWorker implements Callable<Status> {
    private static final int MAX_BUFFER_SIZE = 2048;
    private static final int ERROR = -1;

    private RandomAccessFile file;
    private HttpURLConnection connection;
    private long size;

    DownloadWorker(RandomAccessFile file, HttpURLConnection connection, long size) {
        this.file = file;
        this.connection = connection;
        this.size = size;
    }

    public Status call() {
        try {
            InputStream stream = connection.getInputStream();

            long downloaded = 0;
            byte[] buffer;
            int buffer_size;
            int read;

            while (downloaded != size) {

                if (MAX_BUFFER_SIZE < (size - downloaded)) {
                    buffer_size = MAX_BUFFER_SIZE;
                } else {
                    buffer_size = (int) (size - downloaded);
                }

                buffer = new byte[buffer_size];
                read = stream.read(buffer);

                if (read == ERROR) {
                    break;
                }

                file.write(buffer, (int) downloaded, read);
                downloaded += read;
            }

            if (downloaded != size) {
                String message = "Incomplete content.";
                return new Status(Status.INCOMPLETE, message);
            }

            file.close();
        } catch (Exception e) {
            e.printStackTrace();
            return new Status(Status.IOERROR, e.getMessage());
        } finally {
            connection.disconnect();
        }

        return new Status(Status.OK);
    }
}