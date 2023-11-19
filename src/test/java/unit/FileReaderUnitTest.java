package unit;

import challange.Main;
import challange.domain.exception.FileReaderIOException;
import challange.domain.service.FileReaderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest(classes = Main.class)
@ExtendWith(SpringExtension.class)

public class FileReaderUnitTest {
    @Autowired
    private FileReaderService fileReaderService;

    @Test
    public void testReadFile_emptyFileLocation_throwsIOException() {
        System.out.println("Hello");
        assertThrows(FileReaderIOException.class, () -> {
            fileReaderService.createWordMap("", null, null);
        });
    }

    @Test
    private void testReadFile_wrongFileLocation_throwsIOException() {

    }

    @Test
    private void testReadFile_nullFileLocation_throwsIOException() {

    }

    @Test
    private void testReadFile_nullExcludeMap_() {

    }

    @Test
    private void testReadFile_emptyExcludeMap_() {

    }

    @Test
    private void testReadFile_nullWordMap_() {

    }

    @Test
    private void testReadFile_emptyWordMap_() {

    }

    @Test
    private void testReadFile_negativeMaxAmountWords_() {

    }

    @Test
    private void testReadFile_fileWordsExceedLimit_throwsMaxAmountOfWordsException() {

    }

    @Test
    private void testReadFile_fileWordsAllStartWithNonEnglishChar_wordMapEmpty() {

    }

    @Test
    private void testReadFile_fileWordsAllBelongToExcludeMap_wordMapEmpty() {

    }

}



