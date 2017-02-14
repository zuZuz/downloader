package zuZuz.downloader;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class DownloadManager {
    private ExecutorService pool;
    private ExecutorCompletionService<Status> workers;

    private URL url;
    private String filename;

    private int limit;
    private int threads;
    private long fileSize = -1;

    DownloadManager(int limit, int threads) {
        this.limit = limit;
        this.threads = threads;

        pool = Executors.newFixedThreadPool(threads);
        workers = new ExecutorCompletionService<>(pool);
    }

    private HttpURLConnection getConnection() throws IOException {
        return (HttpURLConnection) url.openConnection();
    }

    private void setFileSize() throws  IOException {
        fileSize = getConnection().getContentLength();
    }

    Status download() {
        HttpURLConnection connection;
        RandomAccessFile file;

        long chunkSize = fileSize / threads;
        long size;
        long left;
        long right;

        try {

            if (getConnection().getResponseCode() / 100 != HttpConnection.SUCCESS) {
                throw new MalformedURLException("Resource is unavailable.");
            }

            for (int i = 0; i < threads; i++) {
                left = i * chunkSize;

                if (i != (threads - 1)) {
                    size = chunkSize;
                    right = left + chunkSize - 1;
                } else {
                    size = fileSize - chunkSize * i;
                    right = fileSize;
                }

                connection = getConnection();
                connection.setRequestProperty("Range", "bytes=" + left + "-" + right);
                connection.connect();

                file = new RandomAccessFile(filename, "rw");
                file.seek(i * chunkSize);

                workers.submit(new DownloadWorker(file, connection, size));
            }

            for (int i = 0; i < threads; i++) {
                Status status = workers.take().get();
                if (status.getCode() != Status.OK) {
                    return status;
                }
            }

        } catch (MalformedURLException e) {
            return new Status(Status.UNAVAILABLE, e.getMessage());
        } catch (IOException e) {
            return new Status(Status.IOERROR, e.getMessage());
        } catch (Exception e) {
            return new Status(Status.INCOMPLETE, e.getMessage());
        }

        return new Status(Status.OK);
    }

    void set(String address, String filename) throws Exception {
        this.url = new URL(address);
        this.filename = filename;

        setFileSize();
    }

    void shutdown() {
        pool.shutdownNow();
    }

    long getFileSize() {
        return fileSize;
    }
}