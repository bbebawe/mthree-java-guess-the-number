package me.bbebawe.guessthenumber.data;

import me.bbebawe.guessthenumber.model.Round;

import java.util.List;

public interface RoundDao {
    List<Round> getGameRounds(int gameId) throws RoundsNotFoundException;

    Round getRoundById(int gameId, int roundId);

    Round addRound(Round round);

    int getNumberOfRoundsInGame(int gameId);

    void removeAllRounds();
}
