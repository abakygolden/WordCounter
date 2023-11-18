package challange;

import challange.domain.service.WordCounter;

public class Main {
    private static WordCounter wordCounter = new WordCounter();

    public static void main(String[] args) {
        wordCounter.countWords();
    }


}