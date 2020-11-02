package me.bbebawe.guessthenumber.data;

public class GameNotFoundException extends RuntimeException{
    public GameNotFoundException() {
    }

    public GameNotFoundException(String message) {
        super(message);
    }

    public GameNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
