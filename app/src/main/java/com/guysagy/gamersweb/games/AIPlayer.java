package com.guysagy.gamersweb.games;

public abstract class AIPlayer extends Player implements Runnable
{
    protected Game mGame;
    
    public void setGame(Game game)
    {
        mGame = game;
    }
}