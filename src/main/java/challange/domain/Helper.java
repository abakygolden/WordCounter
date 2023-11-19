package challange.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Helper {
    public static String[] splitIntoWords(String input) {
        String[] words = input.split("[^a-zA-Z]+");

        return Arrays.stream(words).filter(word -> !word.isEmpty()).toArray(String[]::new);
    }

    public static void printMap(Map<String, Long> map) {
        for (Map.Entry<String, Long> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    public static void removeLastTwoCharacters(StringBuilder stringBuilder) {
        //\n counts as one character so needed to fix this after test :)
        stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
    }

    public static void clearStringBuilder(StringBuilder stringBuilder) {
        stringBuilder.setLength(0);
    }

    public static List<Character> getEnglishAlphabet() {
        List<Character> alphabetList = new ArrayList<>();
        for (char c = 'A'; c <= 'Z'; c++) {
            alphabetList.add(c);
        }
        return alphabetList;
    }
}
