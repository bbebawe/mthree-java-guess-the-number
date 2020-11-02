package me.bbebawe.guessthenumber.data;

import me.bbebawe.guessthenumber.model.Round;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class RoundDaoDB implements RoundDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public RoundDaoDB(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Round> getGameRounds(int gameId) throws RoundsNotFoundException {
        try {
            final String SELECT_ROUNDS_BY_GAME_ID = "SELECT r.round_id, r.game_id, r.guess_time, r.guess, r.result "
                    + "FROM round r "
                    + "WHERE r.game_id = ? ORDER BY r.guess_time";
            List<Round> gameRounds = jdbcTemplate.query(SELECT_ROUNDS_BY_GAME_ID, new RoundMapper(), gameId);
            if (gameRounds.isEmpty()) {
                throw new RoundsNotFoundException("no rounds found for game id " + gameId);
            }
            return gameRounds;
        } catch (DataAccessException ex) {
            throw new RoundsNotFoundException("no rounds found for game id " + gameId);
        }
    }

    @Override
    public Round getRoundById(int roundId, int gameId) {
        final String SELECT_GAME_ROUND = "SELECT r.round_id, r.game_id, r.guess_time, r.guess, r.result " +
                "FROM round r WHERE r.round_id=? AND r.game_id=?";
        return jdbcTemplate.queryForObject(SELECT_GAME_ROUND, new RoundMapper(), roundId, gameId);
    }

    @Override
    public Round addRound(Round round) {
        int max = getNumberOfRoundsInGame(round.getGameId());
        round.setRoundId(max + 1);
        final String INSERT_ROUND = "INSERT INTO round(round_id, game_id, guess, result) VALUES(?,?,?,?)";
        jdbcTemplate.update(INSERT_ROUND, round.getRoundId(), round.getGameId(), round.getGuess(), round.getGuessResult());
        Round addedRound = getRoundById(round.getRoundId(), round.getGameId());
        return addedRound;
    }

    public int getNumberOfRoundsInGame(int gameId) {
        final String SELECT_MAX_ROUND = "SELECT CASE WHEN max(r.round_id) IS null THEN 0 ELSE max(r.round_id) " +
                "END AS max FROM round r WHERE r.game_id = ?";
        return jdbcTemplate.queryForObject(SELECT_MAX_ROUND, Integer.class, gameId);
    }

    public void removeAllRounds() {
        jdbcTemplate.update("DELETE FROM round");
        jdbcTemplate.update("ALTER TABLE round AUTO_INCREMENT = 1");
    }

    public static final class RoundMapper implements RowMapper<Round> {

        @Override
        public Round mapRow(ResultSet rs, int index) throws SQLException {
            Round round = new Round();
            round.setRoundId(rs.getInt("round_id"));
            round.setGameId(rs.getInt("game_id"));
            round.setGuess(rs.getString("guess"));
            Timestamp timestamp = rs.getTimestamp("guess_time");
            round.setGuessTime(timestamp.toLocalDateTime());
            round.setGuessResult(rs.getString("result"));
            return round;
        }
    }
}
