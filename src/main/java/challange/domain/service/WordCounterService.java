package challange.domain.service;

import challange.domain.exception.FileReaderIOException;
import challange.domain.exception.FileWriterIOException;
import challange.domain.exception.MaxAmountOfWordsException;
import challange.domain.exception.MinAmountOfWordsException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import static challange.domain.Helper.getEnglishAlphabet;

@Service
public class WordCounterService {
    public final int AMOUNT_OF_INPUT_FILES = 4;
    private final FileReaderService fileReaderService;
    private final FileWriterService fileWriterService;

    public final String FILES_PATH, INPUT_LOCATION, OUTPUT_LOCATION, EXCLUDE_INPUT_LOCATION, EXCLUDE_OUTPUT_LOCATION;


    public WordCounterService(String filesPath) {
        FILES_PATH = filesPath;
        INPUT_LOCATION = FILES_PATH + "input/";
        OUTPUT_LOCATION = FILES_PATH + "output/";
        EXCLUDE_INPUT_LOCATION = INPUT_LOCATION + "exclude.txt";
        EXCLUDE_OUTPUT_LOCATION = OUTPUT_LOCATION + "exclude_count.txt";
        this.fileReaderService = new FileReaderService();
        this.fileWriterService = new FileWriterService();
    }
    public WordCounterService() {
        FILES_PATH = "src/main/resources/";
        INPUT_LOCATION = FILES_PATH + "input/";
        OUTPUT_LOCATION = FILES_PATH + "output/";
        EXCLUDE_INPUT_LOCATION = INPUT_LOCATION + "exclude.txt";
        EXCLUDE_OUTPUT_LOCATION = OUTPUT_LOCATION + "exclude_count.txt";
        this.fileReaderService = new FileReaderService();
        this.fileWriterService = new FileWriterService();
    }

    public void countWords() {
        try {
            SortedMap<String, Long> excludeMap = fileReaderService.createExcludeMap(EXCLUDE_INPUT_LOCATION);
            SortedMap<String, Long> wordMap = Collections.synchronizedSortedMap(new TreeMap<>(String.CASE_INSENSITIVE_ORDER));
            for (int i = 1; i <= AMOUNT_OF_INPUT_FILES; i++) {
                wordMap = fileReaderService.createWordMap(returnInputFileLocation(i), excludeMap, wordMap);
            }
            fileWriterService.writeExcludeCount(EXCLUDE_OUTPUT_LOCATION, excludeMap);

            fileWriterService.writeWordsToFile(OUTPUT_LOCATION + "file_", wordMap);

        } catch (FileReaderIOException e) {
            System.out.println("Error occurred while reading file. Program stopping");
        } catch (MaxAmountOfWordsException | MinAmountOfWordsException e) {
            System.out.println("An error occurred " + e.getMessage());
        } catch (FileWriterIOException e) {
            System.out.println(e.getMessage());
            System.out.println("All output files will be cleared");
            cleanOutputFiles();
        }

    }

    public void cleanOutputFiles() {
        try {
            fileWriterService.writeExcludeCount(EXCLUDE_OUTPUT_LOCATION, null);
            getEnglishAlphabet().forEach(character -> {
                try {
                    fileWriterService.writeWordsToTxtFileGivenLastCharacter(OUTPUT_LOCATION + "file_", "", character);
                } catch (FileWriterIOException e) {
                    System.out.print("An error occurred while clearing output files.\n" + e.getMessage());
                }
            });
        } catch (FileWriterIOException e) {
            System.out.print("An error occurred while clearing output files.\n" + e.getMessage());
        }
    }

    private String returnInputFileLocation(int number) {
        return INPUT_LOCATION + "input_" + number + ".txt";
    }

}
