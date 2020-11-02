package me.bbebawe.guessthenumber.service;

import me.bbebawe.guessthenumber.data.GameNotFoundException;
import me.bbebawe.guessthenumber.data.RoundsNotFoundException;
import me.bbebawe.guessthenumber.model.Game;
import me.bbebawe.guessthenumber.model.Round;

import java.util.List;

public interface GameService {
    Game startGame();

    List<Game> getAllGames();

    Game getGameById(int gameId) throws GameNotFoundException;

    List<Round> getGameRounds(int gameId) throws RoundsNotFoundException;

    Round makeGuess(Round round) throws GameNotFoundException;
}
