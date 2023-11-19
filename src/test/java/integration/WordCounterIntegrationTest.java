package integration;

import challange.domain.Helper;
import challange.domain.service.WordCounterService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
public class WordCounterIntegrationTest {

    @Autowired
    private WordCounterService wordCounterService;

    private final String FILES_PATH = "src/test/resources/integration/";

    @BeforeEach()
    public void startUpServiceRightPath() {
        wordCounterService = new WordCounterService(FILES_PATH);
    }

    @AfterEach
    public void clearOutputFiles() {
        wordCounterService.cleanOutputFiles();
    }

    @Test
    public void testWordCounterService_WhenCorrectFilePath_Success() {
        assertDoesNotThrow(() -> {
            wordCounterService.countWords();
        });
        assertEquals();
    }


    public ArrayList<Path> getAllFilesPath() {
        ArrayList<Path> paths = new ArrayList<>();
        Helper.getEnglishAlphabet().forEach(character -> {
            paths.add(Paths.get(FILES_PATH + "file_" + character + ".txt"));
        });
        return paths;
    }

    public Path getExcludeCountPath() {
        return Paths.get(FILES_PATH + "exclude_count.txt");
    }
}