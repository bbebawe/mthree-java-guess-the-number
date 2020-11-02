package me.bbebawe.guessthenumber.data;

import me.bbebawe.guessthenumber.model.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository
public class GameDaoDB implements GameDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public GameDaoDB(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Game> getAllGames() throws NoGamesFoundException {
        final String SELECT_ALL_GAMES = "SELECT g.game_id, g.answer, g.finished FROM game g";
        List<Game> gameList = jdbcTemplate.query(SELECT_ALL_GAMES, new GameMapper());
        if (gameList.isEmpty()) {
            throw new NoGamesFoundException("no games found");
        } else {
            return gameList;
        }
    }

    @Override
    public Game getGameById(int gameId) throws GameNotFoundException {
        try {
            final String SELECT_GAME_BY_ID = "SELECT g.game_id, g.answer, g.finished FROM game g WHERE g.game_id= ?";
            Game game = jdbcTemplate.queryForObject(SELECT_GAME_BY_ID, new GameMapper(), gameId);
            return game;
        } catch (DataAccessException ex) {
            throw new GameNotFoundException("invalid game id " + gameId + ", game not found");
        }
    }

    @Override
    public Game addGame(Game game) {
        final String INSERT_GAME = "INSERT INTO game(answer) VALUES (?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update((Connection conn) -> {
            PreparedStatement statement = conn.prepareStatement(
                    INSERT_GAME,
                    Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, game.getAnswer());
            return statement;
        }, keyHolder);
        game.setGameId(keyHolder.getKey().intValue());
        return game;
    }

    @Override
    public void updateGame(Game game) {
        final String UPDATE_GAME = "UPDATE game g SET g.finished = ? WHERE g.game_id = ?";
        jdbcTemplate.update(UPDATE_GAME, game.isFinished(), game.getGameId());
    }

    public void removeAllGames() {
        jdbcTemplate.update("DELETE FROM game");
        jdbcTemplate.update("ALTER TABLE game AUTO_INCREMENT = 1");
    }

    public static final class GameMapper implements RowMapper<Game> {

        @Override
        public Game mapRow(ResultSet rs, int index) throws SQLException {
            Game game = new Game();
            game.setGameId(rs.getInt("game_id"));
            game.setAnswer(rs.getString("answer"));
            game.setFinished(rs.getBoolean("finished"));
            return game;
        }
    }

}
