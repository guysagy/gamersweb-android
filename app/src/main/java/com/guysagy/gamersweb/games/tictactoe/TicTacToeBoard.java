package com.guysagy.gamersweb.games.tictactoe;

import com.guysagy.gamersweb.games.Location;
import com.guysagy.gamersweb.games.Board;
import com.guysagy.gamersweb.games.Piece;
import com.guysagy.gamersweb.games.Move;
import com.guysagy.gamersweb.games.Player;
import java.util.HashMap;

final public class TicTacToeBoard implements Board
{
    protected TicTacToeBoard()
    {
        super();
    }
    
    protected HashMap<Location, Piece> mPieceByLocation = null;
    protected HashMap<Piece, Location> mLocationByPiece = null;

    public int getWidth()
    {
        return 3;
    }
    
    public int getHeight()
    {
        return 3;
    }
    
    public void initialize()
    {
        mPieceByLocation = new HashMap<Location, Piece>(getWidth()*getHeight());
        mLocationByPiece = new HashMap<Piece, Location>(getWidth()*getHeight());
    }
    
    public void reInitialize(HashMap<Location, Piece> pieceByLocation)
    {
        mPieceByLocation = pieceByLocation;
        // TODO: Need to rebuild mLocationByPiece = locationByPiece;
    }	
    
    public boolean isLocationOccupied(Location location)
    {
        boolean isOccupied = ( mPieceByLocation.get(location) != null );
        return isOccupied;
    }
    
    public void makeMove(Move move)
    {
    	/* TODO: reorder the function calls;
        Player player = move.getPlayer();
        Location location = move.getLocation();
        
        Piece piece = new TicTacToePiece();
        piece.setType(player.getType());
        piece.setLocation(location);
        
        mPieceByLocation.put(location, piece);
        mLocationByPiece.put(piece, location);
		*/
		
        Player player = move.getPlayer();
        Piece piece = new TicTacToePiece();
        Location location = move.getLocation();
        piece.setType(player.getType());
        piece.setLocation(location);
        mPieceByLocation.put(location, piece);
        mLocationByPiece.put(piece, location);
    }
    
    public void undoMove(Move move)
    {
    	/* TODO: reorder the function calls;
        Player player = move.getPlayer();
        Location location = move.getLocation();
        
        Piece piece = new TicTacToePiece();
        piece.setType(player.getType());
        piece.setLocation(location);
        
        mPieceByLocation.put(location, null);
        mLocationByPiece.put(piece, null);
        */

        Player player = move.getPlayer();
        Piece piece = new TicTacToePiece();
        Location location = move.getLocation();
        piece.setType(player.getType());
        piece.setLocation(location);
        mPieceByLocation.put(location, null);
        mLocationByPiece.put(piece, null);
    }
    
    public Piece getPieceByLocation(Location location)
    {
        return mPieceByLocation.get(location);
    }	
    
    public Location getLocationByPiece(Piece piece)
    {
        return mLocationByPiece.get(piece);
    }
}