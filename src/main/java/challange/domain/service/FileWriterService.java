package challange.domain.service;

import challange.domain.exception.FileWriterIOException;
import lombok.NoArgsConstructor;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

@NoArgsConstructor
public class FileWriterService {

    public void writeExcludeCount(String fileName, Map<String, Long> excludeMap) throws FileWriterIOException {
        try {
            long totalAmountOfExclusion = excludeMap.values().stream().mapToLong(Long::longValue).sum();
            writeFile(fileName, "Total count of excluded words encountered " + totalAmountOfExclusion);
        } catch (IOException e) {
            throw new FileWriterIOException("An error occurred while writing to exclude count file\n" + e.getMessage());
        }
    }

    public void writeWordsToFile(String filePreDirectory, TreeMap<String, Long> wordMap) throws FileWriterIOException {
        char currentChar = wordMap.firstEntry().getKey().charAt(0);
        StringBuilder tmpString = new StringBuilder();
        for (Map.Entry<String, Long> word : wordMap.entrySet()) {
            if (word.getKey().charAt(0) != currentChar) {
                //Remove extra line from end of file
                tmpString.delete(tmpString.length() - 2, tmpString.length());
                writeWordsToSpecificFile(filePreDirectory, tmpString.toString(), currentChar);
                currentChar = word.getKey().charAt(0);
                tmpString.setLength(0);
            }
            tmpString.append(word.getKey()).append(" ").append(word.getValue()).append("\n");

        }
    }

    private void writeWordsToSpecificFile(String filePreDirectory, String words, char character) throws FileWriterIOException {
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
