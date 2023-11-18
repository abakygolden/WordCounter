package challange.domain;

import java.util.Arrays;
import java.util.Map;

public class Helper {
    public static String[] splitIntoWords(String input) {
        String[] words = input.split("[^a-zA-Z]+");

        return Arrays.stream(words)
                .filter(word -> !word.isEmpty())
                .toArray(String[]::new);
    }

    public static void printMap(Map<String, Long> map) {
        for (Map.Entry<String, Long> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
