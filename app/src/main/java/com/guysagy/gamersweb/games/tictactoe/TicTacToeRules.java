package com.guysagy.gamersweb.games.tictactoe;

import com.guysagy.gamersweb.games.Rules;
import com.guysagy.gamersweb.games.Player;
import com.guysagy.gamersweb.games.Board;
import com.guysagy.gamersweb.games.Move;
import com.guysagy.gamersweb.games.Location;
import java.util.ArrayList;

final class TicTacToeRules implements Rules
{
    private static TicTacToeRules mRules;

    private TicTacToeRules()
    {
    }

    static TicTacToeRules getInstance()
    {
        if (mRules == null)
            mRules = new TicTacToeRules();
        return mRules;
    }

    public boolean isMoveLegal(Move move)
    {
        // TODO: remove the double calls for getLocation/Board.
        if (move.getPlayer() == null || move.getBoard() == null || move.getLocation() == null)
            return false;

        Location location = move.getLocation();
        Board board = (TicTacToeBoard)(move.getBoard());
        return !board.isLocationOccupied(location);
    }

    public Player getNextPlayer(ArrayList<Player> players, Player currentPlayer)
    {
        return (currentPlayer.getType() == TicTacToePlayerType.X) ? (TicTacToeAIPlayer)players.get(1) : (TicTacToePlayer)players.get(0);
    }

    public ArrayList<Player> createGamePlayers()
    {
        // Player index 0 is always 'X'.
        // Player index 1 is always 'O'.
        ArrayList<Player> players = new ArrayList<Player>(2);
        Player player = new TicTacToePlayer().setType(TicTacToePlayerType.X);
        players.add(player);
        player = new TicTacToeAIPlayer().setType(TicTacToePlayerType.O);
        players.add(player);
        return players;
    }

    public boolean isGameDraw(Board board)
    {
        int[] winningStroke = new int[3];
        if (!isGameWin(board, winningStroke))
        {
            for (int i = 1 ; i <= 9 ; ++i)
            {
                Location location = (new TicTacToeLocation()).setLocation(i);
                if (board.getPieceByLocation(location) == null )
                {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public boolean isGameWin(Board board, int[] winningStroke)
    {
        return isBoardLineComplete(board, 1,2,3, winningStroke)
                || isBoardLineComplete(board, 4,5,6, winningStroke)
                || isBoardLineComplete(board, 7,8,9, winningStroke)
                || isBoardLineComplete(board, 1,4,7, winningStroke)
                || isBoardLineComplete(board, 2,5,8, winningStroke)
                || isBoardLineComplete(board, 3,6,9, winningStroke)
                || isBoardLineComplete(board, 1,5,9, winningStroke)
                || isBoardLineComplete(board, 3,5,7, winningStroke);
    }

    // winningStroke is an out parameter returning the locations of the winning stroke.
    private boolean isBoardLineComplete(Board board, int location1, int location2, int location3, int[] winningStroke)
    {
        TicTacToeLocation gameLocation1 = new TicTacToeLocation(location1);
        TicTacToeLocation gameLocation2 = new TicTacToeLocation(location2);
        TicTacToeLocation gameLocation3 = new TicTacToeLocation(location3);

        TicTacToePiece piece1 = (TicTacToePiece)board.getPieceByLocation(gameLocation1);
        TicTacToePiece piece2 = (TicTacToePiece)board.getPieceByLocation(gameLocation2);
        TicTacToePiece piece3 = (TicTacToePiece)board.getPieceByLocation(gameLocation3);

        if (piece1 != null && piece2 != null && piece3 != null)
        {
            if (piece1.getType().equals(piece2.getType()) && piece2.getType().equals(piece3.getType()))
            {
                winningStroke[0] = location1;
                winningStroke[1] = location2;
                winningStroke[2] = location3;
                return true;
            }
        }

        return false;
    }
}
