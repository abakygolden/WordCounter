package integration;

import challange.domain.service.WordCounterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class WordCounterIntegrationTest {

    @Autowired
    private WordCounterService wordCounterService;

    private final String FILES_PATH = "src/test/resources/integration/";


    @Test
    public void testWordCounterService() {

        WordCounterService wordCounterService = new WordCounterService(FILES_PATH);

        wordCounterService.countWords();

    }
}