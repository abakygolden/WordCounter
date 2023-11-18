package challange;

import challange.domain.service.FileWriterService;
import challange.domain.service.WordCounter;

import java.io.IOException;

public class Main {
    static FileWriterService f = new FileWriterService();
    private static WordCounter wordCounter = new WordCounter();

    public static void main(String[] args) throws IOException {   // Creating a TreeMap with a custom comparator for case-insensitive sorting
        wordCounter.countWords();
    }


}