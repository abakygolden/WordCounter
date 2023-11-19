package challange.domain.service;

import challange.domain.exception.FileWriterIOException;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import static challange.domain.Helper.clearStringBuilder;
import static challange.domain.Helper.removeLastTwoCharacters;

@NoArgsConstructor
@Service
public class FileWriterService {

    public void writeExcludeCount(String fileName, Map<String, Long> excludeMap) throws FileWriterIOException {
        try {
            if (excludeMap == null) writeFile(fileName, "");
            else {
                long totalAmountOfExclusion = excludeMap.values().stream().mapToLong(Long::longValue).sum();
                writeFile(fileName, "Total count of excluded words encountered " + totalAmountOfExclusion);
            }
        } catch (IOException e) {
            throw new FileWriterIOException("An error occurred while writing to exclude count file\n" + e.getMessage());
        }
    }

    public void writeWordsToFile(String filePreDirectory, TreeMap<String, Long> wordMap) throws FileWriterIOException {
        if (wordMap == null) return;
        char currentChar = wordMap.firstEntry().getKey().charAt(0);
        StringBuilder tmpString = new StringBuilder();
        for (Map.Entry<String, Long> word : wordMap.entrySet()) {
            if (word.getKey().charAt(0) != currentChar) {
                //Remove extra line from end of file
                removeLastTwoCharacters(tmpString);
                writeWordsToTxtFileGivenLastCharacter(filePreDirectory, tmpString.toString(), currentChar);
                currentChar = word.getKey().charAt(0);
                clearStringBuilder(tmpString);
            }
            tmpString.append(word.getKey()).append(" ").append(word.getValue()).append("\n");

        }
    }


    public void writeWordsToTxtFileGivenLastCharacter(String filePreDirectory, String words, char character) throws FileWriterIOException {
        String fileName = filePreDirectory + character + ".txt";
        try {
            writeFile(fileName, words);
        } catch (IOException e) {
            throw new FileWriterIOException("An error occurred while writing to " + fileName + "\n" + e.getMessage());
        }
    }

    private void writeFile(String fileName, String content) throws IOException {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(content);
        }
    }

}
