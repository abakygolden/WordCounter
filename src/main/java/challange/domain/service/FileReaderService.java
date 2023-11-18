package challange.domain.service;

import challange.domain.exception.FileReaderIOException;
import challange.domain.exception.MaxAmountOfWordsException;
import challange.domain.exception.MinAmountOfWordsException;
import lombok.NoArgsConstructor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static challange.domain.Helper.splitIntoWords;

@NoArgsConstructor
public class FileReaderService {

    public TreeMap<String, Long> createExcludeMap(String fileLocation) throws FileReaderIOException, MinAmountOfWordsException, MaxAmountOfWordsException {
        TreeMap<String, Long> excludeMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        try {
            readInitialExcludeFile(fileLocation, excludeMap);
        } catch (IOException e) {
            throw new FileReaderIOException("IO Exception while reading file " + e.getMessage());
        }
        if (excludeMap.size() > 10) {
            throw new MaxAmountOfWordsException("Exclude list is larger than 10");
        } else if (excludeMap.size() < 10) {
            throw new MinAmountOfWordsException("Exclude list is smaller than 10");
        }
        return excludeMap;
}

    private void readInitialExcludeFile(String fileLocation, Map<String, Long> excludeMap) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileLocation))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = splitIntoWords(line); // splitting line into words
                for (String word : words) {
                    String uppercaseWord = word.toUpperCase();
                    excludeMap.put(uppercaseWord, 0L);
                }
            }
        }
    }
}
