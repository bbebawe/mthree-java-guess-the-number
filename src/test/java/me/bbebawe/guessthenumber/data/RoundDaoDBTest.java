package me.bbebawe.guessthenumber.data;

import me.bbebawe.guessthenumber.TestApplicationConfiguration;
import me.bbebawe.guessthenumber.model.Game;
import me.bbebawe.guessthenumber.model.Round;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
public class RoundDaoDBTest {
    private RoundDao roundDao;
    private GameDao gameDao;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public RoundDaoDBTest(RoundDao roundDao, GameDao gameDao, JdbcTemplate jdbcTemplate) {
        this.roundDao = roundDao;
        this.gameDao = gameDao;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Before
    public void setUp() {
        roundDao.removeAllRounds();
        gameDao.removeAllGames();
    }

    @Test
    @DisplayName("Test round dao to add and return game rounds")
    public void tesRoundDao_toAddRounds() {
        Game game = new Game("1234");
        // add game to db
        gameDao.addGame(game);
        int numRounds = roundDao.getNumberOfRoundsInGame(1);
        assertEquals(0, numRounds, "there are no rounds for game ");
        // add round to game
        Round round = new Round(1, "1345");
        Round addedRound = roundDao.addRound(round);
        // assert round is added
        assertEquals(1, roundDao.getNumberOfRoundsInGame(1), "there is one round for this game ");
        // add second round
        Round round2 = new Round(1, "1467");
        Round addedRound2 = roundDao.addRound(round2);
        // assert round is added
        assertEquals(2, roundDao.getNumberOfRoundsInGame(1), "there is two rounds for this game ");
    }

    @Test
    @DisplayName("Test round dao to get all rounds for game")
    public void tesRoundDao_toGetGameRounds() throws RoundsNotFoundException {
        Game game = new Game("1234");
        // add game to db
        gameDao.addGame(game);
        // add round to game
        Round round = new Round(1, "1345");
        Round addedRound = roundDao.addRound(round);
        // add second round
        Round round2 = new Round(1, "1467");
        Round addedRound2 = roundDao.addRound(round2);

        // get rounds
        List<Round> gameRounds = roundDao.getGameRounds(1);
        // assert round is added
        assertEquals(2, gameRounds.size(), "there is two rounds for this game ");
    }


    @Test
    @DisplayName("Test round dao to get round by id")
    public void tesRoundDao_toGetRoundById() throws RoundsNotFoundException {
        Game game = new Game("1234");
        // add game to db
        gameDao.addGame(game);
        // add round to game
        Round round = new Round(1, "1345");
        Round addedRound = roundDao.addRound(round);
        // add second round
        Round round2 = new Round(1, "1467");
        Round addedRound2 = roundDao.addRound(round2);

        // get rounds
        List<Round> gameRounds = roundDao.getGameRounds(1);
        // assert round is added
        assertNotNull(roundDao.getRoundById(1, 1), "there is round with id 1 for game 1");
        assertEquals(round.getGuess(), roundDao.getRoundById(1, 1).getGuess(), "should be 1345");
    }
}