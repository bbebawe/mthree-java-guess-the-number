package me.bbebawe.guessthenumber.controller;

import me.bbebawe.guessthenumber.data.GameNotFoundException;
import me.bbebawe.guessthenumber.data.NoGamesFoundException;
import me.bbebawe.guessthenumber.data.RoundsNotFoundException;
import me.bbebawe.guessthenumber.model.Game;
import me.bbebawe.guessthenumber.model.Round;
import me.bbebawe.guessthenumber.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.ValidationException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class GameController {

    private GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/game")
    public List<Game> getAllGames() throws NoGamesFoundException {
        return gameService.getAllGames();
    }

    @GetMapping("/game/{gameId}")
    public ResponseEntity<Game> getGameById(@PathVariable int gameId) throws GameNotFoundException {
        Game result = gameService.getGameById(gameId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/begin")
    public ResponseEntity<Integer> createGame() {
        Game game = gameService.startGame();
        if (game == null) {
            return new ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity(game.getGameId(), HttpStatus.CREATED); // return gameId only
        }
    }

    @PostMapping("/guess")
    public ResponseEntity<Round> makeGuess(@RequestBody Round round) throws GameNotFoundException, ValidationException {
        if (round.getGuess().length() != 4 ) {
            throw new ValidationException("game guess must be only 4 digits");
        }

        if(round.getGameId() == 0 || round.getGuess() == null ) {
            throw new ValidationException("game guess must have valid game id and guess");
        }
        try {
            Integer.parseInt(round.getGuess());
        } catch (NumberFormatException ex){
            throw new ValidationException("game guess must be only 4 digits");
        }

        Round gameRound = gameService.makeGuess(round);
        if (gameRound.getGuessResult().equals("e:4:p:0")) {
            Game game = gameService.getGameById(round.getGameId());
            return new ResponseEntity(game, HttpStatus.OK);
        } else {
            return ResponseEntity.ok(gameRound);
        }
    }

    @GetMapping("/rounds/{gameId}")
    public ResponseEntity<List<Round>> getGameRounds(@PathVariable int gameId) throws RoundsNotFoundException {
        List<Round> result = gameService.getGameRounds(gameId);
        return ResponseEntity.ok(result);
    }
}
