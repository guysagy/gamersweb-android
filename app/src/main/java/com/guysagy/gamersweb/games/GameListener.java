package com.guysagy.gamersweb.games;

public interface GameListener 
{
    void notifyGameMove();
    void notifyGameDraw();
    void notifyGameWin();
    void notifyIllegalMove();
    void notifyCurrentPlayerChanged();
}
