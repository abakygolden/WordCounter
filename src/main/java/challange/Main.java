package challange;

import challange.domain.service.FileReaderService;
import challange.domain.service.WordCounter;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import static challange.domain.Helper.splitIntoWords;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {

    private static WordCounter wordCounter = new WordCounter();
    public static void main(String[] args) {   // Creating a TreeMap with a custom comparator for case-insensitive sorting
        wordCounter.countWords();
    }



}