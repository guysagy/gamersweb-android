package com.guysagy.gamersweb.games.tictactoe;

import com.guysagy.gamersweb.games.Location;

final class TicTacToeLocation implements Location
{
    protected int x;
    
    public TicTacToeLocation()
    {
    }	
    
    public TicTacToeLocation(int location)
    {
        this.x = location;
    }
    
    public int getLocation()
    {
        return x;
    }
    
    public TicTacToeLocation setLocation(int x)
    {
        this.x = x;
        return this;
    }
    
    public String toString()
    {
        return ((Integer)x).toString();
    }
    
    @Override
    public int hashCode()
    {
        return 7 + toString().hashCode();
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

        final TicTacToeLocation other = (TicTacToeLocation)obj;
        return (this.getLocation() == other.getLocation());
    }
}