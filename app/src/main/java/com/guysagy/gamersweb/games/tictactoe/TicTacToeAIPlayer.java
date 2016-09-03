package com.guysagy.gamersweb.games.tictactoe;

import android.os.Bundle;
import android.os.Message;
import com.guysagy.gamersweb.games.AIPlayer;
import com.guysagy.gamersweb.games.Board;
import com.guysagy.gamersweb.games.Location;

final class TicTacToeAIPlayer extends AIPlayer
{
    public void makeMove()
    {
        Board board = mGame.getBoard();
        Location location = new TicTacToeLocation();
        int moveTo = -1;
        
        // Check if TicTacToeAIPlayer can win.
        for (int i = 1; i <= mGame.getBoardSize() ; ++i)
        {
            boolean winningMove = mGame.simulateMove(i, this);
            if (winningMove)
            {
                moveTo = i;
                break;
            }
        }
        
        // Check if need to block other (human) player.
        if (moveTo == -1)
        {		
            TicTacToePlayer otherPlayer = new TicTacToePlayer();
            TicTacToePlayerType otherPlayerType = (getType() == TicTacToePlayerType.X) ? TicTacToePlayerType.O : TicTacToePlayerType.X;
            otherPlayer.setType(otherPlayerType);
            otherPlayer.setName("");
            
            for (int i = 1; i <= mGame.getBoardSize() ; ++i)
            {
                boolean winningMove = mGame.simulateMove(i, otherPlayer);
                if (winningMove)
                {
                    moveTo = i;
                    break;
                }
            }		
        }

        //
        // TODO: At this point, implement next move logic using recursion.
        // The following implementation is just a quick and dirty solution, as the AI is not the main purpose of this app.
        //

        // Put in center.
        if (moveTo == -1)
        {
            location.setLocation(5);
            if (board.getPieceByLocation(location) == null)
            {
                moveTo = 5;
            }
        }

        // Put in corner.
        if (moveTo == -1)
        {
            int[] corners = {1,3,7,9};
            for (int j : corners)
            {
                location.setLocation(j);
                if (board.getPieceByLocation(location) == null)
                {
                    moveTo = j;
                    break;
                }
            }
        }

        // Put in a non-corner location.
        if (moveTo == -1)
        {
            int[] corners = {2,4,6,8};
            for (int j : corners)
            {
                location.setLocation(j);
                if (board.getPieceByLocation(location) == null)
                {
                    moveTo = j;
                    break;
                }
            }
        }

        if (moveTo == -1)
            throw new IllegalStateException("Unexpected error - there is nowhere for Android to make a move!");
        
        try 
        {
            // Give the user an impression Android is really thinking ...
            Thread.sleep(500); 
        } 
        catch (InterruptedException e) 
        {
        }		
        
        Message moveMessage = new Message();
        Bundle moveData = new Bundle();
        moveData.putInt(mGame.mMoveAtLocationMessageKey, moveTo);
        moveMessage.setData(moveData);
        mGame.getUiHandler().sendMessage(moveMessage);
    }

    @Override
    public void run() 
    {
        makeMove();
    }
    
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

        final TicTacToeAIPlayer other = (TicTacToeAIPlayer)obj;
        return (this.getName() == other.getName() && this.getType() == other.getType());
    }	
}
