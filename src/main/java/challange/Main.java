package challange;

import challange.domain.service.WordCounterService;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class Main {
    private static WordCounterService wordCounter = new WordCounterService();

    public static void main(String[] args) {
        wordCounter.countWords();
    }


}