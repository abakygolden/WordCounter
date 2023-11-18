package challange.domain;

import java.util.Arrays;

public  class Helper {
    public static String[] splitIntoWords(String input) {
        String[] words = input.split("[^a-zA-Z]+");

        return Arrays.stream(words)
                .filter(word -> !word.isEmpty())
                .toArray(String[]::new);
    }
}
