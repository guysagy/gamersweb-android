package com.guysagy.gamersweb.games.tictactoe;

import com.guysagy.gamersweb.games.Move;
import com.guysagy.gamersweb.games.Board;
import com.guysagy.gamersweb.games.Player;
import com.guysagy.gamersweb.games.Location;

final class TicTacToeMove implements Move
{
    private Player mPlayer;
    private Board 	mBoard;	
    private Location mLocation;
    
    public TicTacToeMove(Player player, Board board, Location location)
    {
        mPlayer = player;
        mBoard = board;
        mLocation = location;
    }

    public Location getLocation()
    {
        return mLocation;
    }
    
    public Player getPlayer()
    {
        return mPlayer;
    }
    
    public Board getBoard()
    {
        return mBoard;
    }
}
