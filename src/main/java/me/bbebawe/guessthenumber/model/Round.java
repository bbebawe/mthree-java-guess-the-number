package me.bbebawe.guessthenumber.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Round {
    private int roundId;
    private int gameId;
    private LocalDateTime guessTime;
    private String guess;
    private String guessResult;

    public Round() {
    }

    public Round(int gameId, String guess) {
        this.setGameId(gameId);
        this.setGuess(guess);
    }

    public Round(int roundId, int gameId, LocalDateTime guessTime, String guess, String guessResult) {
        this.roundId = roundId;
        this.gameId = gameId;
        this.guessTime = guessTime;
        this.guess = guess;
        this.guessResult = guessResult;
    }

    public int getRoundId() {
        return roundId;
    }

    public void setRoundId(int roundId) {
        this.roundId = roundId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public LocalDateTime getGuessTime() {
        return guessTime;
    }

    public void setGuessTime(LocalDateTime guessTime) {
        this.guessTime = guessTime;
    }

    public String getGuess() {
        return guess;
    }

    public void setGuess(String guess) {
        this.guess = guess;
    }

    public String getGuessResult() {
        return guessResult;
    }

    public void setGuessResult(String guessResult) {
        this.guessResult = guessResult;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + this.roundId;
        hash = 29 * hash + this.gameId;
        hash = 29 * hash + Objects.hashCode(this.guessTime);
        hash = 29 * hash + Objects.hashCode(this.guess);
        hash = 29 * hash + Objects.hashCode(this.guessResult);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Round other = (Round) obj;
        if (this.roundId != other.roundId) {
            return false;
        }
        if (this.gameId != other.gameId) {
            return false;
        }
        if (!Objects.equals(this.guess, other.guess)) {
            return false;
        }
        if (!Objects.equals(this.guessResult, other.guessResult)) {
            return false;
        }
        if (!Objects.equals(this.guessTime, other.guessTime)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Round{" + "roundId=" + roundId + ", gameId=" + gameId + ", guessTime=" + guessTime + ", guess=" + guess + ", guessResult=" + guessResult + '}';
    }

}
