package unit;

import challange.Main;
import challange.domain.exception.FileReaderIOException;
import challange.domain.exception.MaxAmountOfWordsException;
import challange.domain.exception.MinAmountOfWordsException;
import challange.domain.service.FileReaderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(classes = Main.class)
@ExtendWith(SpringExtension.class)

public class FileReaderUnitTest {
    private final String FILE_LOCATION = "src/test/resources/unit/input/";
    private final String NO_EXCEPTION_ERROR_MESSAGE = "It did not throw an exception something went wrong";

    private final TreeMap<String, Long> excludeMap = getExcludeMap();
    @Autowired
    private FileReaderService fileReaderService;

    private TreeMap<String, Long> getEmptyMap() {
        return new TreeMap<>();
    }

    private TreeMap<String, Long> getExcludeMap() {
        TreeMap<String, Long> map = new TreeMap<>();
        map.put("Apple", 1L);
        map.put("Banana", 1L);
        map.put("Map", 1L);
        map.put("Put", 1L);
        map.put("You", 1L);
        map.put("One", 1L);
        map.put("Long", 1L);
        map.put("Two", 1L);
        map.put("Minutes", 1L);
        map.put("Ago", 1L);
        return map;
    }

    private String generateFileLocation(String fileName) {
        return FILE_LOCATION + fileName + ".txt";
    }

    @Test
    public void testCreateWordMap_emptyFileLocation_throwsIOException() {
        assertThrows(FileReaderIOException.class, () -> {
            fileReaderService.createWordMap("", excludeMap, getEmptyMap());
        }, NO_EXCEPTION_ERROR_MESSAGE);
    }

    @Test
    public void testCreateWordMap_wrongFileLocation_throwsIOException() {
        assertThrows(FileReaderIOException.class, () -> {
            fileReaderService.createWordMap(generateFileLocation("nonexistentfile.txt"), excludeMap, getEmptyMap());
        }, NO_EXCEPTION_ERROR_MESSAGE);
    }

    @Test
    public void testCreateWordMap_nullFileLocation_throwsIOException() {
        assertThrows(NullPointerException.class, () -> {
            fileReaderService.createWordMap(null, excludeMap, getEmptyMap());
        }, NO_EXCEPTION_ERROR_MESSAGE);
    }

    @Test
    public void testCreateWordMap_nullExcludeMap_throwsMinAmountOfWordsException() {
        assertThrows(MinAmountOfWordsException.class, () -> {
            fileReaderService.createWordMap(generateFileLocation("valid_file"), null, getEmptyMap());
        }, NO_EXCEPTION_ERROR_MESSAGE);

    }

    @Test
    public void testCreateWordMap_emptyExcludeMap_throwsMinAmountOfWordsException() {
        assertThrows(MinAmountOfWordsException.class, () -> {
            fileReaderService.createWordMap(generateFileLocation("valid_file"), getEmptyMap(), getEmptyMap());
        }, NO_EXCEPTION_ERROR_MESSAGE);
    }

    @Test
    public void testCreateWordMap_lessThan10EntriesExcludeMap_throwsMinAmountOfWordsException() {
        TreeMap<String, Long> map = getEmptyMap();
        map.put("A", 1L);
        assertThrows(MinAmountOfWordsException.class, () -> {
            fileReaderService.createWordMap(generateFileLocation("valid_file"), map, getEmptyMap());
        }, NO_EXCEPTION_ERROR_MESSAGE);
    }

    @Test
    public void testCreateWordMap_moreThan10EntriesExcludeMap_throwsMaxAmountOfWordsException() {
        TreeMap<String, Long> map = getExcludeMap();
        map.put("ZZZ", 1L);
        assertThrows(MaxAmountOfWordsException.class, () -> {
            fileReaderService.createWordMap(generateFileLocation("valid_file"), map, getEmptyMap());
        }, NO_EXCEPTION_ERROR_MESSAGE);
    }

    @Test
    public void testCreateWordMap_nullWordMapEmptyExcludeMapNormalInputFile_throwsMaxAmountOfWordsException() {
        assertThrows(MaxAmountOfWordsException.class, () -> {
            fileReaderService.createWordMap(generateFileLocation("valid_file"), getEmptyMap(), null);
        }, NO_EXCEPTION_ERROR_MESSAGE);
    }

    @Test
    public void testCreateWordMap_nullWordMapSmallInputFile_throwsMaxAmountOfWordsException() {
        assertThrows(MaxAmountOfWordsException.class, () -> {
            fileReaderService.createWordMap(generateFileLocation("exclude"), excludeMap, null);
        }, NO_EXCEPTION_ERROR_MESSAGE);
    }


    @Test
    public void testCreateWordMap_nullWordMapLargeInputFile_throwsMaxAmountOfWordsException() {

    }

    @Test
    public void testCreateWordMap_emptyWordMap_() {

    }

    @Test
    public void testCreateWordMap_negativeMaxAmountWords_() {

    }

    @Test
    public void testCreateWordMap_fileWordsExceedLimit_throwsMaxAmountOfWordsException() {

    }

    @Test
    public void testCreateWordMap_fileWordsAllStartWithNonEnglishChar_wordMapEmpty() {

    }

    @Test
    public void testCreateWordMap_fileWordsAllBelongToExcludeMap_wordMapEmpty() {

    }

    //
    @Test
    public void testCreateExcludeMap_nullWordMapEmptyExcludeMapSmallInputFile_fillsEmptyExcludeMap() throws FileReaderIOException, MinAmountOfWordsException, MaxAmountOfWordsException {
        TreeMap<String, Long> excludeMap = fileReaderService.createExcludeMap(generateFileLocation("exclude"));
        assertFalse(excludeMap.isEmpty(), "Exclude Map should contain 10 entries but it was empty");
        assertEquals(10, excludeMap.size(), String.format("Exclude map should contain 10 entries but it had only %s", excludeMap.size()));
        excludeMap.forEach((key, value) -> assertEquals(0, value, String.format("Value should be 0 but it was  %s", value)));

    }

}



