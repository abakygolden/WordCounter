package challange.domain.service;

import challange.domain.exception.FileWriterIOException;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

        } // Make sure last one is added too
        removeLastTwoCharacters(tmpString);
        writeWordsToTxtFileGivenLastCharacter(filePreDirectory, tmpString.toString(), currentChar);
    }


    public void writeWordsToTxtFileGivenLastCharacter(String filePreDirectory, String words, char character) throws FileWriterIOException {
        String fileName = filePreDirectory + character + ".txt";
        try {
            writeFile(fileName, words);
        } catch (IOException e) {
            throw new FileWriterIOException("An error occurred while writing to " + fileName + "\n" + e.getMessage());
        }
    }

    public void writeFile(String filePath, String content) throws IOException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            throw new FileWriterIOException(String.format("Path specified (%s) does not correspond to a file", filePath));
        }

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(content);
        }
    }

}
