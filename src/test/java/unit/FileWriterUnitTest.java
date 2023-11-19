package unit;

import challange.Main;
import challange.domain.exception.FileWriterIOException;
import challange.domain.service.FileWriterService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Main.class)
@ExtendWith(SpringExtension.class)
public class FileWriterUnitTest {

    @Autowired
    private FileWriterService fileWriterService;

    private final String OUTPUT_PATH = "src/test/resources/unit/output/";
    private Path tempFile, tempFileA, tempFileB, tempFileC, tempFileK, tempFileD;
    private final TreeMap<String, Long> map = getMap();

    @BeforeEach
    public void createTmpFile() throws IOException {
        tempFile = Files.createTempFile("temp_test_file", ".txt");
        tempFileA = Paths.get(generateFileLocation("output_A"));
        tempFileB = Paths.get(generateFileLocation("output_B"));
        tempFileC = Paths.get(generateFileLocation("output_C"));
        tempFileD = Paths.get(generateFileLocation("output_D"));
        tempFileK = Paths.get(generateFileLocation("output_K"));

    }

    @AfterEach
    public void clearOutputFile() throws IOException {
        Files.deleteIfExists(tempFile);
        fileWriterService.writeFile(generateFileLocation("output_A"), "");
        fileWriterService.writeFile(generateFileLocation("output_B"), "");
        fileWriterService.writeFile(generateFileLocation("output_C"), "");
        fileWriterService.writeFile(generateFileLocation("output_D"), "");
        fileWriterService.writeFile(generateFileLocation("output_K"), "");

    }


    @Test
    public void testWriteExcludeCount_NullExcludeMap_Success() throws IOException {
        assertDoesNotThrow(() -> {
            fileWriterService.writeExcludeCount(tempFile.toString(), null);
        });
        String result = Files.readString(tempFile);
        assertEquals("", result);
    }

    @Test
    public void
    testWriteExcludeCount_NonNullExcludeMap_Success() throws IOException {
        assertDoesNotThrow(() -> {
            fileWriterService.writeExcludeCount(tempFile.toString(), map);
        });
        String result = Files.readString(tempFile);
        long totalAmountOfExclusion = map.values().stream().mapToLong(Long::longValue).sum();
        assertEquals("Total count of excluded words encountered " + totalAmountOfExclusion, result);

    }

    @Test
    public void testWriteWordsToFile_NullWordMap_NoException() throws IOException {
        assertDoesNotThrow(() -> {
            fileWriterService.writeWordsToFile(tempFile.toString(), null);
        });
        String result = Files.readString(tempFile);
        assertEquals("", result);
    }

    @Test
    public void testWriteWordsToFile_defaultWordMapWhenPathExist_Success() throws IOException {
        assertDoesNotThrow(() -> {
            fileWriterService.writeWordsToFile(OUTPUT_PATH + "output_", map);
        });
        String resultA = Files.readString(tempFileA);
        String resultB = Files.readString(tempFileB);
        String resultC = Files.readString(tempFileC);
        String resultD = Files.readString(tempFileD);
        String resultK = Files.readString(tempFileK);

        assertEquals("APPLE 1", resultA);
        assertEquals("BANANA 2", resultB);
        assertEquals("CHERRY 3", resultC);
        assertEquals("", resultD);
        assertEquals("KIWI 4", resultK);

    }

    @Test
    public void testWriteWordsToFile_extraEntryInWordMapWhenPathExist_Success() throws IOException {
        map.put("Appelsin", 5L);
        assertDoesNotThrow(() -> {
            fileWriterService.writeWordsToFile(OUTPUT_PATH + "output_", map);
        });
        String resultA = Files.readString(tempFileA);
        String resultB = Files.readString(tempFileB);
        String resultC = Files.readString(tempFileC);
        String resultD = Files.readString(tempFileD);
        String resultK = Files.readString(tempFileK);

        assertEquals("APPLE 1\nAppelsin 5", resultA);
        assertEquals("BANANA 2", resultB);
        assertEquals("CHERRY 3", resultC);
        assertEquals("", resultD);
        assertEquals("KIWI 4", resultK);

    }

    @Test
    public void testWriteWordsToFile_DefaultWordMapWhenPathDontExist_ThrowsException() {
        assertThrows(FileWriterIOException.class, () -> {
            fileWriterService.writeWordsToFile(OUTPUT_PATH + "no-correct", map);
        });
    }

    @Test
    public void testWriteFile_Success() {
        assertDoesNotThrow(() -> {
            fileWriterService.writeFile(tempFile.toString(), "test");
            assertEquals("test", Files.readString(tempFile));
        });
    }

    @Test
    public void testWriteFile_NullString_throwsNullPointerException() {
        assertThrows((NullPointerException.class), () -> {
            fileWriterService.writeFile(generateFileLocation("output"), null);
        });
    }

    @Test
    public void testWriteFile_WrongPath_ThrowsFileFileWriterIOException() {
        assertThrows((FileWriterIOException.class), () -> {
            fileWriterService.writeFile(generateFileLocation("wrong"), null);
        });
    }

    private String generateFileLocation(String fileName) {
        return OUTPUT_PATH + fileName + ".txt";
    }

    private TreeMap<String, Long> getMap() {
        TreeMap<String, Long> map = new TreeMap<>();
        map.put("APPLE", 1L);
        map.put("BANANA", 2L);
        map.put("CHERRY", 3L);
        map.put("KIWI", 4L);
        return map;
    }

}
