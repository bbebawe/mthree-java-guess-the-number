package me.bbebawe.guessthenumber.controller;

import java.time.LocalDateTime;

public class Error {
    private LocalDateTime timestamp = LocalDateTime.now();
    private String message;

    public Error() {
    }

    public Error(String message) {
        this.message = message;
    }

    public Error(LocalDateTime timestamp, String message) {
        this.timestamp = timestamp;
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
