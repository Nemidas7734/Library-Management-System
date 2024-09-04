package com.librarysystem.exception;

public class LibrarySystemException extends Exception {
    public LibrarySystemException(String message) {
        super(message);
    }

    public LibrarySystemException(String message, Throwable cause) {
        super(message, cause);
    }
}