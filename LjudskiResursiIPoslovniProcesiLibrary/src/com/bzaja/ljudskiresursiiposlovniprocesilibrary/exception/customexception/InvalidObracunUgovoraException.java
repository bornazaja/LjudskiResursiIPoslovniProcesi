package com.bzaja.ljudskiresursiiposlovniprocesilibrary.exception.customexception;

public class InvalidObracunUgovoraException extends RuntimeException {

    public InvalidObracunUgovoraException() {
    }

    public InvalidObracunUgovoraException(String message) {
        super(message);
    }

    public InvalidObracunUgovoraException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidObracunUgovoraException(Throwable cause) {
        super(cause);
    }

    public InvalidObracunUgovoraException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
