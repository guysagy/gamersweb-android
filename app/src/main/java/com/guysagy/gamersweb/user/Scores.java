package com.guysagy.gamersweb.user;

final public class Scores implements Cloneable
{
    private String 	mPlayer1Name;
    private int 	mPlayer1Score;
    private String 	mPlayer2Name;
    private int 	mPlayer2Score;
    
    public enum SORT_BY
    {
        NAME,
        SCORE,
    }
    
    private Scores()
    {
    }
    
    static public Scores getInstance()
    {
        return new Scores();
    }
    
    public Scores(String player1Name, int player1Score, String player2Name, int player2Score)
    {
        mPlayer1Name = player1Name;
        mPlayer1Score = player1Score;
        mPlayer2Name = player2Name;
        mPlayer2Score = player2Score;		
    }
    
    public Scores setPlayer1Name(String player1)
    {
        mPlayer1Name = player1;
        return this;
    }
    
    public Scores setPlayer1Score(int player1Score)
    {
        mPlayer1Score = player1Score;
        return this;
    }
    
    public Scores setPlayer2Name(String player2)
    {
        mPlayer2Name = player2;
        return this;
    }
    
    public Scores setPlayer2Score(int player2Score)
    {
        mPlayer2Score = player2Score;
        return this;
    }
    
    public String getPlayer1Name()
    {
        return mPlayer1Name;
    }
    
    public int getPlayer1Score()
    {
        return mPlayer1Score;
    }
    
    public String getPlayer2Name()
    {
        return mPlayer2Name;		
    }
    
    public int getPlayer2Score()
    {
        return mPlayer2Score;
    }	
    
    @Override
    public int hashCode()
    {
        return 7 + getPlayer1Name().hashCode() + getPlayer2Name().hashCode() + getPlayer1Score() ^ 3 + getPlayer2Score() ^ 5;
    }
    
    @Override
    public boolean equals(Object object)
    {
        if (object == null)
            return false;
        
        if (this.getClass() != object.getClass())
            return false;
        
        final Scores other = (Scores)object;
        
        if ((this.getPlayer1Name() == null) ? (other.getPlayer1Name() != null) : !this.getPlayer1Name().equals(other.getPlayer1Name()))
        {
            return false;
        }

        if ((this.getPlayer2Name() == null) ? (other.getPlayer2Name() != null) : !this.getPlayer2Name().equals(other.getPlayer2Name()))
        {
            return false;
        }
        
        if (this.getPlayer1Score() != other.getPlayer1Score() || this.getPlayer2Score() != other.getPlayer2Score())
        {
            return false;
        }
                
        return true;
    }
    
    public int compareBy(Scores scores, SORT_BY compareBy)
    {
        if (compareBy == SORT_BY.NAME)
        {
            return this.getPlayer1Name().compareTo(scores.getPlayer1Name());
        }
        else if (compareBy == SORT_BY.SCORE)
        {
            return this.getPlayer1Score() - scores.getPlayer1Score();
        }
        
        return 0; // Ureachable code.
    }
    
    @Override 
    public Scores clone()
    {
        Scores scores = null;
        try 
        {
            scores = (Scores)super.clone();
        } 
        catch (CloneNotSupportedException e) 
        {
        }
        return scores;
    }
}
