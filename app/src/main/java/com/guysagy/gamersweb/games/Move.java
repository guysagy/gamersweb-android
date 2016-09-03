package com.guysagy.gamersweb.games;

public interface Move
{
    Player getPlayer();
    Board getBoard();
    Location getLocation();
}
