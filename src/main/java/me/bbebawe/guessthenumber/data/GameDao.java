package me.bbebawe.guessthenumber.data;

import me.bbebawe.guessthenumber.model.Game;

import java.util.List;

public interface GameDao {
    List<Game> getAllGames() throws NoGamesFoundException;

    Game getGameById(int gameId) throws GameNotFoundException;

    Game addGame(Game game);

    void updateGame(Game game);

    void removeAllGames();
}
