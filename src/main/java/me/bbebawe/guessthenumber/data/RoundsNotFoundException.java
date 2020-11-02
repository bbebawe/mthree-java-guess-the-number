package me.bbebawe.guessthenumber.data;

public class RoundsNotFoundException extends RuntimeException {
    public RoundsNotFoundException() {
    }

    public RoundsNotFoundException(String message) {
        super(message);
    }

    public RoundsNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
