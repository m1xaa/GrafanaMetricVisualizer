package org.example.exceptions;

public class GrafanaException extends RuntimeException {
    public GrafanaException(String message) {
        super(message);
    }
}