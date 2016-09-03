package com.guysagy.gamersweb.factories;

import android.os.Handler;
import com.guysagy.gamersweb.games.GameListener;
import com.guysagy.gamersweb.games.tictactoe.TicTacToeGame;

public class GamesFactory 
{
    public static TicTacToeGame createTicTacToeGameImpl(GameListener gameListener ,Handler uiHandler)
    {
        return new TicTacToeGame(gameListener, uiHandler);
    }
}
