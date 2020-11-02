package me.bbebawe.guessthenumber.controller;

import me.bbebawe.guessthenumber.data.GameNotFoundException;
import me.bbebawe.guessthenumber.data.NoGamesFoundException;
import me.bbebawe.guessthenumber.data.RoundsNotFoundException;
import me.bbebawe.guessthenumber.service.GameFinishedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.xml.bind.ValidationException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;

@ControllerAdvice
@RestController
public class GameControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<Error> handleSqlException(
            SQLIntegrityConstraintViolationException ex,
            WebRequest request) {
        Error err = new Error("Invalid data entry");
        return new ResponseEntity<>(err, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Error> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            WebRequest request) {
        Error err = new Error(LocalDateTime.now(), ex.getName() + " should be of type " + ex.getRequiredType().getName());
        return new ResponseEntity<Error>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Error> handleNullPointerException(
            NullPointerException ex,
            WebRequest request) {
        Error err = new Error(LocalDateTime.now(), ex.getMessage());
        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Error> handleInvalidGameGuess(
            ValidationException ex,
            WebRequest request) {
        Error err = new Error(LocalDateTime.now(), ex.getMessage());
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoGamesFoundException.class)
    public ResponseEntity<Error> handleNoGameException(
            NoGamesFoundException ex,
            WebRequest request) {
        Error err = new Error(LocalDateTime.now(), ex.getMessage());
        return new ResponseEntity<Error>(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GameFinishedException.class)
    public ResponseEntity<Error> handleGameFinishedException(
            GameFinishedException ex,
            WebRequest request) {
        Error err = new Error(LocalDateTime.now(), ex.getMessage());
        return new ResponseEntity<Error>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GameNotFoundException.class)
    public ResponseEntity<Error> handleGameNotFoundException(
            GameNotFoundException ex,
            WebRequest request) {
        Error err = new Error(LocalDateTime.now(), ex.getMessage());
        return new ResponseEntity<Error>(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RoundsNotFoundException.class)
    public ResponseEntity<Error> handleRoundsNotFoundException(
            RoundsNotFoundException ex,
            WebRequest request) {
        Error err = new Error(LocalDateTime.now(), ex.getMessage());
        return new ResponseEntity<Error>(err, HttpStatus.NOT_FOUND);
    }

}
