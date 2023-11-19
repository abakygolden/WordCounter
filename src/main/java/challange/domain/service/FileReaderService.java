package challange.domain.service;

import challange.domain.exception.FileReaderIOException;
import challange.domain.exception.MaxAmountOfWordsException;
import challange.domain.exception.MinAmountOfWordsException;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import static challange.domain.Helper.splitIntoWords;

@NoArgsConstructor
@Service
public class FileReaderService {
    private final int MAX_AMOUNT_WORDS = 10000;

    public TreeMap<String, Long> createExcludeMap(String fileLocation) throws FileReaderIOException, MinAmountOfWordsException, MaxAmountOfWordsException {
        TreeMap<String, Long> excludeMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        try {
            readFile(fileLocation, excludeMap, 0, null);
        } catch (IOException e) {
            throw new FileReaderIOException("IO Exception while reading file " + e.getMessage());
        }
        return excludeMap;
    }

    public TreeMap<String, Long> createWordMap(String fileLocation, TreeMap<String, Long> excludeMap, TreeMap<String, Long> wordMap) throws FileReaderIOException, MinAmountOfWordsException, MaxAmountOfWordsException {
        if (excludeMap == null) {
            throw new MinAmountOfWordsException("No words on exclude list");
        }
        wordMap = wordMap == null ? new TreeMap<>(String.CASE_INSENSITIVE_ORDER) : wordMap; //If map not there, intialize it
        try {
            readFile(fileLocation, excludeMap, MAX_AMOUNT_WORDS, wordMap);
        } catch (IOException e) {
            throw new FileReaderIOException("IO Exception while reading file " + e.getMessage());
        }
        if (excludeMap.size() < 10) {
            throw new MinAmountOfWordsException("Exclude list is smaller than 10");
        } else if (excludeMap.size() > 10) {
            throw new MaxAmountOfWordsException("Exclude list is larger than 10");
        }
        return wordMap;
    }

    //Change to public pga af test
    private void readFile(String fileLocation, Map<String, Long> excludeMap, int maxAmountWords, Map<String, Long> wordMap) throws IOException, MaxAmountOfWordsException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileLocation))) {
            String line;
            int count = 0;
            while ((line = reader.readLine()) != null) {
                String[] words = splitIntoWords(line); // splitting line into words
                count += words.length; // Add length to count
                for (String word : words) {
                    String uppercaseWord = word.toUpperCase();
                    if (wordMap == null) {// If no wordMap provided, means we are on Initial Exclude File Reading
                        putIntoInitialExcludeMap(excludeMap, uppercaseWord);
                    } else { // Else we are in normal input reading
                        if (count > maxAmountWords) {
                            throw new MaxAmountOfWordsException(String.format("Amount of words  %s  larger than the limit of %s ", count, maxAmountWords));
                        } else {
                            putIntoWordMap(wordMap, excludeMap, uppercaseWord);
                        }

                    }
                }
            }
        }
    }

    private void putIntoWordMap(Map<String, Long> wordMap, Map<String, Long> excludeMap, String uppercaseWord) {
        if (excludeMap.containsKey(uppercaseWord)) { // If it's an exlusion, add a count to it
            putIntoMapAndIncreaseValue(excludeMap, uppercaseWord);
        } else { //If not added to the WordMap
            putIntoMapAndIncreaseValue(wordMap, uppercaseWord);
        }
    }

    private void putIntoMapAndIncreaseValue(Map<String, Long> map, String key) {
        map.put(key, map.getOrDefault(key, 0L) + 1);
    }

    private void putIntoInitialExcludeMap(Map<String, Long> excludeMap, String uppercaseWord) {
        excludeMap.put(uppercaseWord, 0L);
    }


}
