package com.guysagy.gamersweb.games;

public interface Piece 
{
    Location getLocation();
    void setLocation(Location location);
    PlayerType getType();
    void setType(PlayerType type);
}
