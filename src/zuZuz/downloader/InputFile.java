package zuZuz.downloader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class InputFile {
    private Scanner file;

    InputFile (String fileName) throws FileNotFoundException {
        file = new Scanner(new File(fileName));
        file.useDelimiter(" ");
    }

    boolean hasNextRecord() {
        return file.hasNextLine();
    }

    Record getNextRecord() {
        if (file.hasNextLine()) {
            return new Record(file.nextLine());
        }
        else {
            return null;
        }
    }

    void close() {
        file.close();
    }
}