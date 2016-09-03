package com.guysagy.gamersweb.games;

public interface Board
{
    void initialize();
    void makeMove(Move move);
    void undoMove(Move move);
    int getWidth();
    int getHeight();
    boolean isLocationOccupied(Location location);
    Piece getPieceByLocation(Location location);
    Location getLocationByPiece(Piece piece);
}