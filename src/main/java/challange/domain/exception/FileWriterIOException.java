package challange.domain.exception;

import java.io.IOException;

public class FileWriterIOException extends IOException {
    public FileWriterIOException(String message) {
        super(message);
    }
}