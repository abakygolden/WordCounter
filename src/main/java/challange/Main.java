package challange;

import challange.domain.service.FileReaderService;
import challange.domain.service.FileWriterService;
import challange.domain.service.WordCounter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import static challange.domain.Helper.splitIntoWords;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
static FileWriterService f = new FileWriterService();
    private static WordCounter wordCounter = new WordCounter();
    public static void main(String[] args) throws IOException {   // Creating a TreeMap with a custom comparator for case-insensitive sorting
         wordCounter.countWords();
    }



}