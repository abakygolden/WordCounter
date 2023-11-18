package challange.domain.service;

import challange.domain.exception.FileReaderIOException;
import challange.domain.exception.MaxAmountOfWordsException;
import challange.domain.exception.MinAmountOfWordsException;
import lombok.AllArgsConstructor;

import java.util.Map;
import java.util.TreeMap;

@AllArgsConstructor
public class WordCounter {
    private final FileReaderService fileReaderService;
    private final String INPUT_LOCATION = " src/main/resources/input/";
    private final String EXCLUDE_LOCATION = INPUT_LOCATION + "exclude.txt";

    public WordCounter() {
        this.fileReaderService = new FileReaderService();
    }

    public void countWords() {
        try {
            TreeMap<String, Long> excludeMap = fileReaderService.createExcludeMap(EXCLUDE_LOCATION);
            for (Map.Entry<String, Long> entry : excludeMap.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        } catch (FileReaderIOException e) {
            System.out.println("Error occurred while reading file. Program wills stop");
        } catch (MaxAmountOfWordsException | MinAmountOfWordsException e) {
            System.out.println("An error occurred " + e);
        }

    }
}
