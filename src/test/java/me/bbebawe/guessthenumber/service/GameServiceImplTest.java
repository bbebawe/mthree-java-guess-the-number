package me.bbebawe.guessthenumber.service;

import me.bbebawe.guessthenumber.TestApplicationConfiguration;
import me.bbebawe.guessthenumber.data.GameDao;
import me.bbebawe.guessthenumber.data.RoundDao;
import me.bbebawe.guessthenumber.model.Game;
import me.bbebawe.guessthenumber.model.Round;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
public class GameServiceImplTest {
    private GameService gameService;
    private GameDao gameDao;
    private RoundDao roundDao;

    @Autowired
    public GameServiceImplTest(GameService gameService, GameDao gameDao, RoundDao roundDao) {
        this.gameService = gameService;
        this.gameDao = gameDao;
        this.roundDao = roundDao;
    }

    @Before
    public void setUp() {
        roundDao.removeAllRounds();
        gameDao.removeAllGames();
    }

    @Test
    public void testGameLogic_toMakeGuess() {
        // add game to db
        Game newGame = new Game("1234");
        Game addedGame = gameDao.addGame(newGame);
        // round object with guess and gameId
        Round round = new Round(1, "1345");
        Round guessResult = gameService.makeGuess(round); // make a guess
        assertEquals("e:1:p:2", guessResult.getGuessResult(), "should have one exact match and 2 partial match");
        // assert game is not marked as finished
        assertFalse(gameDao.getGameById(round.getGameId()).isFinished(), "game should not be finished");
    }

    @Test
    public void testGameLogic_toMakeGameAsFinished() {
        // add game to db
        Game newGame = new Game("1234");
        Game addedGame = gameDao.addGame(newGame);
        // round object with guess and gameId
        Round round = new Round(1, "1234");
        Round guessResult = gameService.makeGuess(round); // make a guess
        assertEquals("e:4:p:0", guessResult.getGuessResult(), "should have four exact match");
        // assert game is not marked as finished
        assertTrue(gameDao.getGameById(round.getGameId()).isFinished(), "game should be finished");
    }
}