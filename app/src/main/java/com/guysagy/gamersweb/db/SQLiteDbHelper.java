package com.guysagy.gamersweb.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDbHelper extends SQLiteOpenHelper 
{
    private static final String DATABASE_NAME 	 = "gamers.db";
    private static final int 	DATABASE_VERSION = 2;	
    
    static public SQLiteDbHelper getInstance(Context context)
    {
        return new SQLiteDbHelper(context);
    }
    
    private SQLiteDbHelper(Context context) 
    {        
        super(context, DATABASE_NAME, null, DATABASE_VERSION);    
    }
    
    @Override    
    public void onCreate(SQLiteDatabase db) 
    {        
        db.execSQL(UserTable.DATABASE_CREATE_USER);    
        db.execSQL(ScoresTable.DATABASE_CREATE_SCORES);   
    }	

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) 
    {
        // Do nothing.
    }	
}
