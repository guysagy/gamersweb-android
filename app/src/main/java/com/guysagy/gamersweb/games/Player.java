package com.guysagy.gamersweb.games;

public abstract class Player 
{
    protected PlayerType mType;
    protected String mName;

    public Player setType(PlayerType type)
    {
        mType = type;
        return this;
    }

    public PlayerType getType()
    {
        return mType;
    }

    public Player setName(String name)
    {
        mName = name;
        return this;
    }

    public String getName()
    {
        return mName;
    }
}