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

import static org.junit.jupiter.api.Assertions.*;


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
        assertExpectedFilesWithActual(true);

    }

    @Test
    public void testWordCounterService_WhenWrongPath_NothingSaved() throws IOException {
        //In this case application will fail and nothing will happen , therefore output files shall be clean
        wordCounterService = new WordCounterService("Somethingwrong");
        assertDoesNotThrow(() -> {
            wordCounterService.countWords();
        });
        assertExpectedFilesWithActual(false);
    }

    private void assertExpectedFilesWithActual(boolean equals) throws IOException {
        String excludePathExpectedResult = Files.readString(getExcludeCountPath(true));
        String excludePathActualResult = Files.readString(getExcludeCountPath(false));
        if (equals) {
            assertEquals(excludePathExpectedResult,excludePathActualResult);
        } else {
            assertNotEquals(excludePathExpectedResult,excludePathActualResult);
        }
        ArrayList<Path> expected = getAllFilesPath(true);
        ArrayList<Path> actual = getAllFilesPath(false);
        for (int i = 0; i < expected.size(); i++) {
            if (equals) {
                assertEquals(Files.readString(expected.get(i)), Files.readString(actual.get(i)));
            } else {
                assertNotEquals(Files.readString(expected.get(i)), Files.readString(actual.get(i)));
            }
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