package me.bbebawe.guessthenumber.service;

import me.bbebawe.guessthenumber.data.GameDao;
import me.bbebawe.guessthenumber.data.GameNotFoundException;
import me.bbebawe.guessthenumber.data.RoundDao;
import me.bbebawe.guessthenumber.data.RoundsNotFoundException;
import me.bbebawe.guessthenumber.model.Game;
import me.bbebawe.guessthenumber.model.Round;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class GameServiceImpl implements GameService {

    private GameDao gameDao;
    private RoundDao roundDao;

    @Autowired
    public GameServiceImpl(GameDao gameDao, RoundDao roundDao) {
        this.gameDao = gameDao;
        this.roundDao = roundDao;
    }

    @Override
    public Game startGame() {
        return gameDao.addGame(this.createGame());
    }

    @Override
    public List<Game> getAllGames() {
        List<Game> allGames = gameDao.getAllGames();
        allGames.forEach(game -> {
            if (!game.isFinished()) {
                game.setAnswer("----");
            }
        });
        return allGames;
    }

    @Override
    public Game getGameById(int gameId) throws GameNotFoundException {
        Game game = gameDao.getGameById(gameId);
        if (game != null && !game.isFinished()) {
            game.setAnswer("----");
        }
        return game;
    }

    @Override
    public List<Round> getGameRounds(int gameId) throws RoundsNotFoundException {
        return roundDao.getGameRounds(gameId);
    }

    @Override
    public Round makeGuess(Round round) throws GameNotFoundException {
        Game foundGame = gameDao.getGameById(round.getGameId());
        if (foundGame.isFinished()) {
            throw new GameFinishedException("game " + foundGame.getGameId() + " is finished, " + "answer " + foundGame.getAnswer() + ", try different game");
        }
        String answer = foundGame.getAnswer();
        round.setGuessResult(determineGuess(round.getGuess(), answer));
        if (round.getGuess().equals(answer)) {
            Game game = getGameById(round.getGameId());
            game.setFinished(true);
            updateGame(game);
        }
        return roundDao.addRound(round);
    }

    private String determineGuess(String guess, String answer) {
        char[] guessChars = guess.toCharArray();
        char[] answerChars = answer.toCharArray();
        System.out.println(guessChars);

        int exactGuess = 0;
        int partialGuess = 0;
        for (int i = 0; i < guessChars.length; i++) {
            // if index > -1 then char exists
            if (answer.indexOf(guessChars[i]) > -1) {
                if (guessChars[i] == answerChars[i]) {
                    exactGuess++;
                } else {
                    partialGuess++;
                }
            }
        }
        String result = "e:" + exactGuess + ":p:" + partialGuess;
        return result;
    }

    private Game createGame() {
        Game game = new Game();
        game.setAnswer(this.generateAnswer());
        return game;
    }

    private String generateAnswer() {
        // create list of numbers between 0 ,9
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            numbers.add(i);
        }
        // shuffle list of numbers to randomly change their order
        Collections.shuffle(numbers);
        String result = "";
        for (int i = 0; i < 4; i++) {
            result += numbers.get(i).toString();
        }
        return result;
    }

    private void updateGame(Game game) {
        gameDao.updateGame(game);
    }
}
