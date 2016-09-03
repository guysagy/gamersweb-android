package com.guysagy.gamersweb.db;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import com.guysagy.gamersweb.settings.AppSettings;
import com.guysagy.gamersweb.user.Scores;

final public class ScoresTable
{
    public static final String DB_FIELD_PLAYER_1_NAME 	= "player1name";
    public static final String DB_FIELD_PLAYER_1_SCORES	= "player1scores";
    public static final String DB_FIELD_PLAYER_2_NAME 	= "player2name";
    public static final String DB_FIELD_PLAYER_2_SCORES	= "player2scores";
    
    public static final String DATABASE_TABLE_SCORES 	= "SCORES";
    public static final String DATABASE_CREATE_SCORES 	= "create table if not exists " + DATABASE_TABLE_SCORES + 
                                                                " (_id integer primary key autoincrement, " +
                                                                DB_FIELD_PLAYER_1_NAME  	+ " text not null, " +
                                                                DB_FIELD_PLAYER_1_SCORES  	+ " text not null, " +
                                                                DB_FIELD_PLAYER_2_NAME  	+ " text not null, " +
                                                                DB_FIELD_PLAYER_2_SCORES  	+ " text not null)";	

    public static final String CREATE_DEFAULT_ACCOUNT_SCORES = "INSERT INTO "
                                                                + DATABASE_TABLE_SCORES
                                                                + "VALUES ("
                                                                + "Guest"
                                                                + ","
                                                                + "0"
                                                                + ","
                                                                + "Android"
                                                                + ","
                                                                + "0"
                                                                + ")";
    
    static public boolean insert(Context context, String player1Name, Integer player1Scores, String player2Name, Integer player2Scores)
    {
        boolean status = false;
        try 
        {
            SQLiteDatabase writableUserTable = SQLiteDbHelper.getInstance(context).getWritableDatabase();
            ContentValues newRow = new ContentValues();
            newRow.put(DB_FIELD_PLAYER_1_NAME	, player1Name);         	
            newRow.put(DB_FIELD_PLAYER_1_SCORES	, player1Scores.toString());          
            newRow.put(DB_FIELD_PLAYER_2_NAME	, player2Name);          
            newRow.put(DB_FIELD_PLAYER_2_SCORES	, player2Scores.toString());   
            
            long rowId = writableUserTable.insert(DATABASE_TABLE_SCORES, null, newRow);
            status = (rowId != -1);	
            writableUserTable.close();
        } 
        catch (SQLiteException e) 
        {
            e.printStackTrace();
        }
        return status;
    }
    
    static public boolean update(Context context, String player1Name, Integer player1Scores, String player2Name, Integer player2Scores)
    {
        boolean status = false;
        try 
        {
            SQLiteDatabase writeableUserTable = SQLiteDbHelper.getInstance(context).getWritableDatabase();
            ContentValues newValues = new ContentValues();
            newValues.put(DB_FIELD_PLAYER_1_NAME	, player1Name);         	
            newValues.put(DB_FIELD_PLAYER_1_SCORES	, player1Scores.toString());          
            newValues.put(DB_FIELD_PLAYER_2_NAME	, player2Name);          
            newValues.put(DB_FIELD_PLAYER_2_SCORES	, player2Scores.toString());   
            
            String whereClause = DB_FIELD_PLAYER_1_NAME + "=? AND " + DB_FIELD_PLAYER_2_NAME + "=?";
            String[] whereArgs = new String[] {player1Name, player2Name};
            
            status = (writeableUserTable.update(DATABASE_TABLE_SCORES, newValues, whereClause, whereArgs) > 0);	
            writeableUserTable.close();
        } 
        catch (SQLiteException e) 
        {
            e.printStackTrace();
        }
        return status;
    }
    
    static public Scores[] getAllScores(Context context)
    {
        Scores[] scores = null;
        try 
        {
            SQLiteDatabase readableUserTable = SQLiteDbHelper.getInstance(context).getReadableDatabase();
            String whereClause 			= null;
            String[] whereArgs 			= null;			
            Cursor selectedRows 		= readableUserTable.query(DATABASE_TABLE_SCORES, null, 
                                                                        whereClause, whereArgs, 
                                                                        null, null, null, null);				

            scores = new Scores[selectedRows.getCount()];
            
            Integer player1NameIndex   = selectedRows.getColumnIndex(DB_FIELD_PLAYER_1_NAME);		
            Integer player1ScoresIndex = selectedRows.getColumnIndex(DB_FIELD_PLAYER_1_SCORES);
            Integer player2NameIndex   = selectedRows.getColumnIndex(DB_FIELD_PLAYER_2_NAME);
            Integer player2ScoresIndex = selectedRows.getColumnIndex(DB_FIELD_PLAYER_2_SCORES);
            
            if (player1NameIndex != -1 && player1ScoresIndex != -1 
                    && player2NameIndex != -1 && player2ScoresIndex != -1)
            {
                if (selectedRows.moveToFirst()) 
                {
                    
                    for (int i = 0 ; selectedRows.isAfterLast() == false ; ++i) 
                    {
                        String player1Name  = selectedRows.getString(player1NameIndex);
                        int player1Scores 	= Integer.parseInt(selectedRows.getString(player1ScoresIndex));
                        String player2Name  = selectedRows.getString(player2NameIndex);
                        int player2Scores 	= Integer.parseInt(selectedRows.getString(player2ScoresIndex));
                        
                        Scores playerScores = Scores.getInstance().setPlayer1Name(player1Name)
                                                                .setPlayer1Score(player1Scores)
                                                                .setPlayer2Name(player2Name)
                                                                .setPlayer2Score(player2Scores);
                        
                        scores[i] = playerScores;
                        selectedRows.moveToNext();
                    }
                }
            }
            else 
            {
                Log.e(AppSettings.DebugPrefix, "Scores retrieval failed: database does not contain expected fields");
            }
            
            selectedRows.close();
            readableUserTable.close();
        } 
        catch (SQLiteException e) 
        {
            e.printStackTrace();
        }
        
        return scores;
    }    
    
    static public Scores getScores(Context context, String player1, String player2)
    {
        Scores scores = null;
        try 
        {
            SQLiteDatabase readableUserTable = SQLiteDbHelper.getInstance(context).getReadableDatabase();
            String[] selectFields 	= null;
            String whereClause 		= DB_FIELD_PLAYER_1_NAME + "=? AND " + DB_FIELD_PLAYER_2_NAME + "=?";
            String[] whereArgs 		= new String[] {player1, player2};			
            Cursor selectedRows 		= readableUserTable.query(DATABASE_TABLE_SCORES, selectFields, 
                                                                        whereClause, whereArgs, 
                                                                        null, null, null, null);				

            Integer player1NameIndex   = selectedRows.getColumnIndex(DB_FIELD_PLAYER_1_NAME);		
            Integer player1ScoresIndex = selectedRows.getColumnIndex(DB_FIELD_PLAYER_1_SCORES);
            Integer player2NameIndex   = selectedRows.getColumnIndex(DB_FIELD_PLAYER_2_NAME);
            Integer player2ScoresIndex = selectedRows.getColumnIndex(DB_FIELD_PLAYER_2_SCORES);
            
            if (player1NameIndex != -1 && player1ScoresIndex != -1 
                    && player2NameIndex != -1 && player2ScoresIndex != -1)
            {
                if (selectedRows.moveToFirst()) 
                {
                    if (selectedRows.isAfterLast() == false) 
                    {
                        String player1Name  = selectedRows.getString(player1NameIndex);
                        int player1Scores 	= Integer.parseInt(selectedRows.getString(player1ScoresIndex));
                        String player2Name  = selectedRows.getString(player2NameIndex);
                        int player2Scores 	= Integer.parseInt(selectedRows.getString(player2ScoresIndex));
                        
                        scores	 			= Scores.getInstance().setPlayer1Name(player1Name)
                                                                    .setPlayer1Score(player1Scores)
                                                                    .setPlayer2Name(player2Name)
                                                                    .setPlayer2Score(player2Scores);
                    }
                }
            }
            else 
            {
                Log.e(AppSettings.DebugPrefix, "Scores retrieval failed: database does not contain expected fields");
            }
            
            selectedRows.close();
            readableUserTable.close();
        } 
        catch (SQLiteException e) 
        {
            e.printStackTrace();
        }
        
        return scores;
    }   
    
    static public boolean delete(Context context, String userName)
    {
        boolean retVal = false;
        try 
        {
            SQLiteDatabase writableUserTable = SQLiteDbHelper.getInstance(context).getWritableDatabase();
            String deleteUserSql = "delete from " + DATABASE_TABLE_SCORES + " where " 
                                        + DB_FIELD_PLAYER_1_NAME 
                                        + "='" + userName + "'";
            
            writableUserTable.execSQL(deleteUserSql);
            writableUserTable.close();
            retVal = true;
        } 
        catch (SQLiteException e) 
        {
            e.printStackTrace();
        }
        return retVal; // If exception been thrown, assume we cannot proceed with the database anyway.
    }    
}
