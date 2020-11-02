package me.bbebawe.guessthenumber.data;

public class NoGamesFoundException extends RuntimeException {
    public NoGamesFoundException() {
    }

    public NoGamesFoundException(String message) {
        super(message);
    }

    public NoGamesFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
