package edu.sjsu.cmpe295b.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DocumentNotFound extends Exception{
    public DocumentNotFound() {
        super();
    }

    public DocumentNotFound(String message) {
        super(message);
    }

    public DocumentNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public DocumentNotFound(Throwable cause) {
        super(cause);
    }

    protected DocumentNotFound(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
