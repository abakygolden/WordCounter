package unit;

import challange.Main;
import challange.domain.exception.FileReaderIOException;
import challange.domain.exception.MaxAmountOfWordsException;
import challange.domain.exception.MinAmountOfWordsException;
import challange.domain.service.FileReaderService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(classes = Main.class)

public class FileReaderUnitTest {
    private final String FILE_LOCATION = "src/test/resources/unit/input/";
    private final String NO_EXCEPTION_ERROR_MESSAGE = "It did not throw an exception something went wrong";

    private TreeMap<String, Long> excludeMap = getExcludeMap();
    @Autowired
    private FileReaderService fileReaderService;


    @AfterEach
    public void resetExcludeMap() {
        this.excludeMap = getExcludeMap();
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
    public void testCreateWordMap_nullWordMap_worksProperly() throws FileReaderIOException, MinAmountOfWordsException, MaxAmountOfWordsException {
        //Valid file is made of 200 different words
        // Three words : Long, One and You are part of exclude file
        // So end result map should have 197 entries each with value 1 and exclude Map should increase value of these entries
        TreeMap<String, Long> map =
                fileReaderService.createWordMap(generateFileLocation("valid_file"), excludeMap, null);

        assertFalse(map.isEmpty(), "Map should read something , but it was empty");
        assertEquals(197, map.size(), String.format("Map should contain 197 entries but it had only %s", map.size()));
        map.forEach((key, value) -> assertEquals(1, value, String.format("Value should be 1 but it was  %s", value)));
        assertExcludeMap(2L);


    }

    @Test
    public void testCreateWordMap_emptyWordMap_worksProperly() throws FileReaderIOException, MinAmountOfWordsException, MaxAmountOfWordsException {
        //Valid file is made of 200 different, with 3 of them in exclude file, so final result should be 197  entries each with value 1
        TreeMap<String, Long> map =
                fileReaderService.createWordMap(generateFileLocation("valid_file"), excludeMap, getEmptyMap());
        assertFalse(map.isEmpty(), "Map should read something , but it was empty");
        assertEquals(197, map.size(), String.format("Map should contain 197 entries but it had only %s", map.size()));
        map.forEach((key, value) -> assertEquals(1, value, String.format("Value should be 1 but it was  %s", value)));
        assertExcludeMap(2L);
    }

    @Test
    public void testCreateWordMap_alreadyProcessedMap_worksProperly() throws FileReaderIOException, MinAmountOfWordsException, MaxAmountOfWordsException {
        TreeMap<String, Long> map =
                fileReaderService.createWordMap(generateFileLocation("valid_file"), excludeMap, null);
        map = fileReaderService.createWordMap(generateFileLocation("valid_file"), excludeMap, map);
        assertFalse(map.isEmpty(), "Map should read something , but it was empty");
        assertEquals(197, map.size(), String.format("Map should contain 197 entries but it had  %s", map.size()));
        map.forEach((key, value) -> assertEquals(2, value, String.format("Value should be 2 but it was  %s", value)));
        assertExcludeMap(3L);


    }


    @Test
    public void testCreateWordMap_fileWordsAllStartWithNonEnglishChar_wordMapEmpty() throws FileReaderIOException, MinAmountOfWordsException, MaxAmountOfWordsException {
        TreeMap<String, Long> map =
                fileReaderService.createWordMap(generateFileLocation("japanese"), excludeMap, null);
        assertTrue(map.isEmpty(), "Map should be empty");
    }

    @Test
    public void testCreateWordMap_nullMapEmptyFile_worksProperly() throws FileReaderIOException, MinAmountOfWordsException, MaxAmountOfWordsException {
        TreeMap<String, Long> map =
                fileReaderService.createWordMap(generateFileLocation("empty_file"), excludeMap, null);
        assertTrue(map.isEmpty(), "Map should be empty");
    }

    @Test
    public void testCreateWordMap_fileWordsAllBelongToExcludeMap_wordMapEmpty() throws FileReaderIOException, MinAmountOfWordsException, MaxAmountOfWordsException {
        TreeMap<String, Long> map =
                fileReaderService.createWordMap(generateFileLocation("all_words_excluded"), excludeMap, null);
        assertTrue(map.isEmpty(), "Map should be empty");
    }

    @Test
    public void testCreateWordMap_tooLargeFile_throwsMaxAmountOfWordsException() {
        assertThrows(MaxAmountOfWordsException.class, () -> {
            fileReaderService.createWordMap(generateFileLocation("10k_file"), excludeMap, null);
        }, NO_EXCEPTION_ERROR_MESSAGE);

    }


    // Create Exclude Map
    @Test
    public void testCreateExcludeMap_correctFile_fillsEmptyExcludeMap() throws FileReaderIOException, MinAmountOfWordsException, MaxAmountOfWordsException {
        TreeMap<String, Long> tmpExcludeMap = fileReaderService.createExcludeMap(generateFileLocation("exclude"));
        assertFalse(tmpExcludeMap.isEmpty(), "Exclude Map should contain 10 entries but it was empty");
        assertEquals(10, tmpExcludeMap.size(), String.format("Exclude map should contain 10 entries but it had only %s", tmpExcludeMap.size()));
        tmpExcludeMap.forEach((key, value) -> assertEquals(0, value, String.format("Value should be 0 but it was  %s", value)));

    }

    @Test
    public void testCreateExcludeMap_emptyFile_throwsMinAmountException() {
        assertThrows(MinAmountOfWordsException.class, () -> {
            fileReaderService.createExcludeMap(generateFileLocation("empty_file"));
        }, NO_EXCEPTION_ERROR_MESSAGE);

    }

    @Test
    public void testCreateExcludeMap_nullFile_FileReaderIOException() {
        assertThrows(FileReaderIOException.class, () -> {
            fileReaderService.createExcludeMap(generateFileLocation(null));
        }, NO_EXCEPTION_ERROR_MESSAGE);
    }

    @Test
    public void testCreateExcludeMap_longFile_throwsMaxAmountException() {
        assertThrows(MaxAmountOfWordsException.class, () -> {
            fileReaderService.createExcludeMap(generateFileLocation("valid_file"));
        }, NO_EXCEPTION_ERROR_MESSAGE);
    }

    //Private functions

    private TreeMap<String, Long> getEmptyMap() {
        return new TreeMap<>();
    }

    private TreeMap<String, Long> getExcludeMap() {
        TreeMap<String, Long> map = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
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

    private void assertExcludeMap(long number) {
        String key = "You";
        assertEquals((long) excludeMap.get(key), number, String.format("For key %s value expected to be %s but it was  %s", key, number, excludeMap.get(key)));
        key = "Long";
        assertEquals((long) excludeMap.get(key), number, String.format("For key %s value expected to be  %s but it was  %s", key, number, excludeMap.get(key)));
        key = "One";
        assertEquals((long) excludeMap.get(key), number, String.format("For key %s value expected to be %s but it was  %s", key, number, excludeMap.get(key)));
    }
}



