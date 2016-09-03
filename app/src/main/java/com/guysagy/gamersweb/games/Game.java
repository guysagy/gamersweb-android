package com.guysagy.gamersweb.games;

import java.security.InvalidParameterException;
import java.util.ArrayList;

import android.os.Handler;

public abstract class Game
{
    protected GameListener 					mGameListener;	
    protected Rules 						mRules;
    protected ArrayList<Player> 			mPlayers;
    protected Player 						mCurrentPlayer;
    protected Player 						mWinnerPlayer;
    protected Board 						mBoard;
    protected Move 							mLastMadeMove;
    protected int[]							mWinningStroke = new int[3];
    protected Handler 						mUiHandler;
    
    public final String 					mMoveAtLocationMessageKey = "moveAtLocation";
    
    protected abstract Rules				getRules();
    protected abstract ArrayList<Player> 	createGamePlayers();
    protected abstract Player 				getStartingPlayer();
    protected abstract Board 				createBoard();
    protected abstract Location				createLocation(int location);
    protected abstract Move					createMove(Player player, Board board, Location location);
    
    protected Game(GameListener gameListener, Handler uiHandler)
    {
        mGameListener 	= gameListener;
        mRules 			= getRules();
        mBoard 			= createBoard();
        mPlayers 		= createGamePlayers();
        mCurrentPlayer 	= getStartingPlayer();
        mWinnerPlayer 	= null;
        mUiHandler 		= uiHandler;
    }	
    
    public Handler getUiHandler()
    {
        return mUiHandler;
    }	
    
    public Move getLastMadeMove()
    {
        return mLastMadeMove;
    }
    
    public Player getCurrentPlayer()
    {
        return mCurrentPlayer;
    }
    
    public Player getWinnerPlayer()
    {
        return mWinnerPlayer;
    }
    
    public ArrayList<Player> getPlayers()
    {
        return mPlayers;
    }
    
    public int[] getWinningStroke()
    {
        return mWinningStroke;
    }
    
    public void start(Player startingPlayer)
    {
        if (startingPlayer != null)
            mCurrentPlayer = startingPlayer;
        
        // mWinnerPlayer 	= null; // TODO: test this change
        mBoard.initialize();
    }
    
    public void stop()
    {
        mCurrentPlayer = null;
    }
    
    public boolean isStopped()
    {
        return mCurrentPlayer == null;
    }
    
    public Board getBoard()
    {
        return mBoard;
    }
    
    public int getBoardSize()
    {
        return mBoard.getWidth()* mBoard.getHeight();
    }
    
    public boolean simulateMove(int location, Player player)
    {
        boolean winningMove = false;
        Location _location = createLocation(location);
        Move move = createMove(player, getBoard(), _location);

        if (mRules.isMoveLegal(move) == true)
        {
            mBoard.makeMove(move);
            int[] winningStroke = new int[3];
            winningMove = mRules.isGameWin(mBoard, winningStroke);
            mBoard.undoMove(move);
        }

        return winningMove;
    }
    
    public void makeMove(int location)
    {
        if (mCurrentPlayer == null) 
            throw new InvalidParameterException("There is no current player to make a move.");
            
        Location _location = createLocation(location);
        Move move = createMove(mCurrentPlayer, getBoard(), _location);

        if (mRules.isMoveLegal(move) == false)
        {
            mGameListener.notifyIllegalMove();
        }
        else
        {
            mBoard.makeMove(move);
            mLastMadeMove = move;
            mGameListener.notifyGameMove();
            
            if (mRules.isGameDraw(mBoard))
            {
                mWinnerPlayer = null;
                stop();
                mGameListener.notifyGameDraw();
            }
            else if (mRules.isGameWin(mBoard, mWinningStroke))
            {
                mWinnerPlayer = mCurrentPlayer;
                stop();
                mGameListener.notifyGameWin();
            }
            else
            {
                mCurrentPlayer = mRules.getNextPlayer(mPlayers, mCurrentPlayer);
                mGameListener.notifyCurrentPlayerChanged();
                
                if (mCurrentPlayer instanceof AIPlayer)
                {
                    AIPlayer aiPlayer = (AIPlayer)mCurrentPlayer;
                    aiPlayer.setGame(this);
                    Thread aiPlayerThread = new Thread(aiPlayer); 
                    aiPlayerThread.start();
                }
            }
        }
    }	
}