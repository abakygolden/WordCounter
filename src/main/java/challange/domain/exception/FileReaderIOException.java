package challange.domain.exception;

import java.io.IOException;

public class FileReaderIOException extends IOException {
    public FileReaderIOException(String message) {
        super(message);
    }
}