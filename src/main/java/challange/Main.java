package challange;

import challange.domain.service.WordCounterService;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class Main {
    static WordCounterService wordCounterService = new WordCounterService();

    public static void main(String[] args) {
    //Uncomment line below to run write to files
    //wordCounterService.countWords();
    //Uncomment line below to clear files
    //wordCounterService.cleanOutputFiles();
    }


}