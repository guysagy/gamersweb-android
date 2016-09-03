package com.guysagy.gamersweb.games.tictactoe;

import com.guysagy.gamersweb.games.Player;

final class TicTacToePlayer extends Player
{
    @Override
    public int hashCode()
    {
        return 7 + (new StringBuilder(getName())).append(getType()).hashCode();
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

        final TicTacToePlayer other = (TicTacToePlayer)obj;
        return (this.getName() == other.getName() && this.getType() == other.getType());
    }
}