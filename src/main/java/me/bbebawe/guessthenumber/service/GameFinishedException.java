package me.bbebawe.guessthenumber.service;

public class GameFinishedException extends RuntimeException {
    public GameFinishedException() {
    }

    public GameFinishedException(String message) {
        super(message);
    }

    public GameFinishedException(String message, Throwable cause) {
        super(message, cause);
    }
}
