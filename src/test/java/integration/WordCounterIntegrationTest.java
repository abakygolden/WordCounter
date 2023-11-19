package integration;

import challange.Main;
import challange.domain.Helper;
import challange.domain.service.WordCounterService;
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
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(classes = Main.class)
@ExtendWith(SpringExtension.class)
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
    public void testWordCounterService_WhenCorrectFilePath_Success() throws IOException {
        assertDoesNotThrow(() -> {
            wordCounterService.countWords();
        });
        assertEquals(Files.readString(getExcludeCountPath(true)), Files.readString(getExcludeCountPath(false)));
        ArrayList<Path> expected = getAllFilesPath(true);
        ArrayList<Path> actual = getAllFilesPath(false);
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(Files.readString(expected.get(i)), Files.readString(actual.get(i)));
        }

    }


    public ArrayList<Path> getAllFilesPath(boolean expected) {
        String expectedPath = expected ? "expected/" : "";

        ArrayList<Path> paths = new ArrayList<>();
        Helper.getEnglishAlphabet().forEach(character -> {
            paths.add(Paths.get(FILES_PATH + "output/" + expectedPath + "file_" + character + ".txt"));
        });
        return paths;
    }

    public Path getExcludeCountPath(boolean expected) {
        String expectedPath = expected ? "expected/" : "";
        return Paths.get(FILES_PATH + "output/" + expectedPath + "exclude_count.txt");
    }
}