package zuZuz.downloader;

import com.google.common.io.Files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class Main {
    public static void main(String[] args) {
        // parse command-line parameters
        Parameters parameters = new Parameters(args);

        if (!parameters.parse() || !parameters.isCorrect()) {
            parameters.printHelp();
            System.exit(1);
        }

        int threads = parameters.getN();
        int limit = parameters.getL();
        String input = parameters.getF();
        String output = parameters.getO();

        // load <URL> <FileName> entries
        InputFile inputFile = null;

        try {
            inputFile = new InputFile(input);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        // download file
        Map<String, File> files = new HashMap<>();
        DownloadManager manager = new DownloadManager(limit, threads);

        while (inputFile.hasNextRecord()) {
            Record record = inputFile.getNextRecord();

            String url = record.getUrl();
            String filename = record.getFilename();

            System.out.print(filename + " ");

            if (files.containsKey(url)) {
                try {
                    System.out.print("(Copying): ");
                    Files.copy(files.get(url), new File(output, filename));
                    System.out.println("OK");
                    continue;
                } catch (IOException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }

            try {
                System.out.print("(Downloading): ");
                manager.set(url, output + File.separatorChar + filename);
                System.out.print(manager.getFileSize() + " bytes. ");
                Status status = manager.download();
                System.out.println(status.getMessage());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            files.put(url, new File(output, filename));
        }

        manager.shutdown();
        inputFile.close();
    }
}