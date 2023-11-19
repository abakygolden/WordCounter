package challange.domain.service;

import challange.domain.exception.FileReaderIOException;
import challange.domain.exception.FileWriterIOException;
import challange.domain.exception.MaxAmountOfWordsException;
import challange.domain.exception.MinAmountOfWordsException;

import java.util.TreeMap;

import static challange.domain.Helper.getEnglishAlphabet;
import static challange.domain.Helper.printMap;

public class WordCounter {
    private final FileReaderService fileReaderService;
    private final FileWriterService fileWriterService;

    public static final String SRC_MAIN_RESOURCES = "src/main/resources/";

    private final String INPUT_LOCATION = SRC_MAIN_RESOURCES + "input/";
    private final String OUTPUT_LOCATION = SRC_MAIN_RESOURCES + "output/";

    private final String EXCLUDE_INPUT_LOCATION = INPUT_LOCATION + "exclude.txt";
    private final String EXCLUDE_OUTPUT_LOCATION = OUTPUT_LOCATION + "exclude_count.txt";


    public WordCounter() {
        this.fileReaderService = new FileReaderService();
        this.fileWriterService = new FileWriterService();

    }

    public void countWords() {
        try {
            TreeMap<String, Long> excludeMap = fileReaderService.createExcludeMap(EXCLUDE_INPUT_LOCATION);
            TreeMap<String, Long> wordMap = null;
            for (int i = 1; i <= 4; i++) {
                wordMap = fileReaderService.createWordMap(returnInputFileLocation(i), excludeMap, wordMap);
            }
            System.out.println("WordMap");
            printMap(wordMap);
            System.out.println("ExcludeMap");
            printMap(excludeMap);
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

    private void cleanOutputFiles() {
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
