package com.guysagy.gamersweb.games.tictactoe;

import com.guysagy.gamersweb.games.Piece;
import com.guysagy.gamersweb.games.PlayerType;
import com.guysagy.gamersweb.games.Location;

final class TicTacToePiece implements Piece 
{
    private PlayerType mType;
    private Location mLocation;
    
    TicTacToePiece()
    {
    }
    
    public PlayerType getType()
    {
        return mType;
    }
    
    public void setType(PlayerType type)
    {
        mType = type;
    }
    
    public Location getLocation()
    {
        return mLocation;
    }
    
    public void setLocation(Location location)
    {
        mLocation = location;
    }	
    
    @Override
    public int hashCode()
    {
        return 7 + (new StringBuilder(getLocation().toString())).append(getType()).hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        
        if (getClass() != obj.getClass())
        {
            return false;
        }

        final TicTacToePiece other = (TicTacToePiece)obj;
        return (this.getLocation() == other.getLocation() && this.getType() == other.getType());
    }	
}