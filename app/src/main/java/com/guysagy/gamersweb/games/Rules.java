package com.guysagy.gamersweb.games;

import java.util.ArrayList;

public interface Rules
{
    boolean isMoveLegal(Move move);
    Player getNextPlayer(ArrayList<Player> players, Player currentPlayer);
    ArrayList<Player> createGamePlayers();
    boolean isGameDraw(Board board);
    boolean isGameWin(Board board, int[] winningStroke);
}
