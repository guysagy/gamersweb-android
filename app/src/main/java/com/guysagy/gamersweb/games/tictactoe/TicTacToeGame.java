package com.guysagy.gamersweb.games.tictactoe;

import java.util.ArrayList;

import android.os.Handler;
import com.guysagy.gamersweb.games.Board;
import com.guysagy.gamersweb.games.Game;
import com.guysagy.gamersweb.games.Location;
import com.guysagy.gamersweb.games.Player;
import com.guysagy.gamersweb.games.GameListener;
import com.guysagy.gamersweb.games.Rules;

final public class TicTacToeGame extends Game
{
    public TicTacToeGame(GameListener gameListener, Handler uiHandler)
    {
        super(gameListener, uiHandler);
    }

    protected Rules getRules()
    {
        return TicTacToeRules.getInstance();
    }
    
    protected ArrayList<Player> createGamePlayers()
    {
        // In Tic-Tac-Toe, there are always only 2 players, so the rules create the game players.
        // Not necessarily so in other games.
        return getRules().createGamePlayers();
    }
    
    protected Player getStartingPlayer()
    {
        return getPlayers() != null ? getPlayers().get(0) : null ;
    }
    
    protected Board createBoard()
    {
        return new TicTacToeBoard();
    }
    
    public TicTacToeBoard getBoard()
    {
        return (TicTacToeBoard)mBoard;
    }
    protected TicTacToeLocation createLocation(int location)
    {
        return new TicTacToeLocation(location);
    }
    
    protected TicTacToeMove createMove(Player player, Board board, Location location)
    {
        return new TicTacToeMove(player, board, location);
    }
    
    public int getBoardSize()
    {
        return mBoard.getWidth() * mBoard.getHeight();
    }

    public void setWinningStroke(int[] winningStroke)
    {
        mWinningStroke = winningStroke;
    }

    public int[] getWinningStroke()
    {
        return mWinningStroke;
    }
}