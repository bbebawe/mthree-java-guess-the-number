package me.bbebawe.guessthenumber.data;


import me.bbebawe.guessthenumber.TestApplicationConfiguration;
import me.bbebawe.guessthenumber.model.Game;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
public class GameDaoDBTest {
    private GameDao gameDao;
    private RoundDao roundDao;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public GameDaoDBTest(GameDao gameDao, RoundDao roundDao, JdbcTemplate jdbcTemplate) {
        this.gameDao = gameDao;
        this.roundDao = roundDao;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Before
    public void setUp() {
        roundDao.removeAllRounds();
        gameDao.removeAllGames();
    }

    @Test
    @DisplayName("Test game dao to throw exception when no games are found")
    public void testGameDao_toThrow_noGamesFoundException() {
        assertThrows(NoGamesFoundException.class, () -> gameDao.getAllGames(), "should throw exception because no games in database");
    }

    @Test
    @DisplayName("Test game dao to throw game not found exception when invalid id")
    public void testGameDao_toThrow_GameNotFoundException() {
        // test with empty db
        assertThrows(GameNotFoundException.class, () -> gameDao.getGameById(1), "should throw exception because no game with id 1 in database");
        // add game to db
        Game newGame = new Game("1234");
        Game addedGame = gameDao.addGame(newGame);
        assertDoesNotThrow(() -> gameDao.getGameById(1), "should not throw exception because game id 1 exits");
        assertThrows(GameNotFoundException.class, () -> gameDao.getGameById(2), "should throw exception because no game with id 2 in database");

    }

    @Test
    @DisplayName("Test games dao to add new game to database")
    public void testGameDao_toAddGame() throws GameNotFoundException {
        Game newGame = new Game("1234");
        Game addedGame = gameDao.addGame(newGame);
        assertEquals(addedGame, gameDao.getGameById(addedGame.getGameId()), "two games should be equal");
        assertEquals(1, addedGame.getGameId(), "id should be 1");
        assertFalse(gameDao.getGameById(addedGame.getGameId()).isFinished(), "should be false because game is not finished yet");
    }

    @Test
    @DisplayName("Test games dao to get all games from database")
    public void tesGameDao_toGetAllGames() {
        // add games
        Game newGame = new Game("1234");
        Game addedGame = gameDao.addGame(newGame);
        Game newGame2 = new Game("5678");
        Game addedGame2 = gameDao.addGame(newGame2);
        Game newGame3 = new Game("1054");
        Game addedGame3 = gameDao.addGame(newGame3);
        // get all games
        List<Game> games = gameDao.getAllGames();
        assertEquals(3, games.size(), "should be three, database has three games");
        assertTrue(games.contains(newGame));
        assertTrue(games.contains(newGame2));
        assertTrue(games.contains(newGame3));
    }

    @Test
    @DisplayName("Test games dao return game by Id")
    public void tesGameDao_toGetGameById() throws GameNotFoundException {
        Game newGame = new Game("1234");
        Game addedGame = gameDao.addGame(newGame);
        assertNotNull(gameDao.getGameById(1), "should not be null, game id 1 exists in database");
        assertEquals(addedGame, gameDao.getGameById(addedGame.getGameId()), "two games should be equal");
        assertThrows(GameNotFoundException.class, () -> gameDao.getGameById(2), "should throw exception because no game with id 2 in database");
    }

    @Test
    @DisplayName("Test games dao to update game status")
    public void tesGameDao_toUpdateGameStatus() throws GameNotFoundException {
        Game newGame = new Game("1234");
        Game addedGame = gameDao.addGame(newGame);
        assertFalse(gameDao.getGameById(addedGame.getGameId()).isFinished(), "should be false because game is not finished yet");
        newGame.setFinished(true);
        gameDao.updateGame(newGame);
        assertTrue(gameDao.getGameById(addedGame.getGameId()).isFinished(), "should be true because game status changed");
    }
}