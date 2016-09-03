package com.guysagy.gamersweb.db;

import com.guysagy.gamersweb.settings.AppSettings;
import com.guysagy.gamersweb.user.User;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

final public class UserTable
{
    public static final String DB_FIELD_FIRSTNAME 		= "firstname";
    public static final String DB_FIELD_LASTNAME 		= "lastname";
    public static final String DB_FIELD_USERNAME 		= "username";
    public static final String DB_FIELD_EMAIL 			= "email";
    public static final String DB_FIELD_PASSWORD 		= "password";
    public static final String DB_FIELD_DOB 			= "dob";
    public static final String DB_FIELD_GENDER 			= "gender";
    public static final String DB_FIELD_AVATAR 			= "avatarfilename";
    
    public static final String DATABASE_TABLE_USER		= "USER";
    public static final String DATABASE_CREATE_USER 	= "create table if not exists " + DATABASE_TABLE_USER + 
                                                                " (_id integer primary key autoincrement, " +
                                                                DB_FIELD_FIRSTNAME 	+ " text not null, " +
                                                                DB_FIELD_LASTNAME 	+ " text not null, " +
                                                                DB_FIELD_USERNAME 	+ " text not null, " +
                                                                DB_FIELD_EMAIL  	+ " text not null, " +
                                                                DB_FIELD_PASSWORD  	+ " text not null, " +
                                                                DB_FIELD_DOB  		+ " text not null, " +
                                                                DB_FIELD_GENDER  	+ " text not null, " +
                                                                DB_FIELD_AVATAR  	+ " text not null)";
    

    static public boolean insert(Context context,String firstName, String lastName, String userName, String email, String password, String dob, String gender, String avatarFileName)
    {
        boolean status = false;
        try 
        {
            SQLiteDatabase writableUserTable = SQLiteDbHelper.getInstance(context).getWritableDatabase();
            ContentValues newRow = new ContentValues();
            newRow.put(DB_FIELD_FIRSTNAME	, firstName); 
            newRow.put(DB_FIELD_LASTNAME	, lastName); 
            newRow.put(DB_FIELD_USERNAME	, userName);         	
            newRow.put(DB_FIELD_EMAIL		, email);          
            newRow.put(DB_FIELD_PASSWORD	, password); // TODO: in a production env., only password hash is to be persisted.         
            newRow.put(DB_FIELD_DOB			, dob);   
            newRow.put(DB_FIELD_GENDER		, gender);   
            newRow.put(DB_FIELD_AVATAR		, avatarFileName);  
            
            long rowId = writableUserTable.insert(DATABASE_TABLE_USER, null, newRow);
            status = (rowId != -1);	
            writableUserTable.close();
        } 
        catch (SQLiteException e) 
        {
            e.printStackTrace();
        }
        return status;
    }
    
    static public boolean update(Context context, String firstName, String lastName, String userName, String email, String password, String dob, String gender, String avatarFileName)
    {
        boolean status = false;
        try 
        {
            SQLiteDatabase writeableUserTable = SQLiteDbHelper.getInstance(context).getWritableDatabase();
            ContentValues newValues = new ContentValues();
            newValues.put(DB_FIELD_FIRSTNAME, firstName);  
            newValues.put(DB_FIELD_LASTNAME	, lastName);  			
            newValues.put(DB_FIELD_EMAIL	, email);          
            newValues.put(DB_FIELD_PASSWORD	, password);  		       
            newValues.put(DB_FIELD_DOB		, dob);   
            newValues.put(DB_FIELD_GENDER	, gender);   
            newValues.put(DB_FIELD_AVATAR	, avatarFileName);  
            
            String whereClause = DB_FIELD_USERNAME + "=?";
            String[] whereArgs = new String[] {userName};
            
            status = (writeableUserTable.update(DATABASE_TABLE_USER, newValues, whereClause, whereArgs) > 0);	
            writeableUserTable.close();
        } 
        catch (SQLiteException e) 
        {
            e.printStackTrace();
        }
        return status;
    }
    
    static public User authenticate(Context context, String userName, String password)
    {
        User user 					= null;
        try 
        {
            SQLiteDatabase readableUserTable = SQLiteDbHelper.getInstance(context).getReadableDatabase();
            String whereClause 			 = DB_FIELD_USERNAME + "=?";
            String[] whereArgs 			 = new String[] {userName};				
            Cursor selectedRows 		 = readableUserTable.query(DATABASE_TABLE_USER, null, whereClause, whereArgs, null, null, null, null);				
            Integer userCount 			 = selectedRows.getCount();	

            Integer firstNameColumnIndex = selectedRows.getColumnIndex(DB_FIELD_FIRSTNAME);	
            Integer lastNameColumnIndex  = selectedRows.getColumnIndex(DB_FIELD_LASTNAME);	
            Integer emailColumnIndex 	 = selectedRows.getColumnIndex(DB_FIELD_EMAIL);		
            Integer passwordColumnIndex  = selectedRows.getColumnIndex(DB_FIELD_PASSWORD);
            Integer dobColumnIndex 		 = selectedRows.getColumnIndex(DB_FIELD_DOB);
            Integer genderColumnIndex 	 = selectedRows.getColumnIndex(DB_FIELD_GENDER);
            Integer avatarColumnIndex 	 = selectedRows.getColumnIndex(DB_FIELD_AVATAR);
            
            if (userCount == 1)
            {
                if (passwordColumnIndex != -1 && emailColumnIndex != -1 
                        && dobColumnIndex != -1 && genderColumnIndex != -1 && avatarColumnIndex != -1)
                {
                    if (selectedRows.moveToFirst()) 
                    {
                        String savedPassword = selectedRows.getString(passwordColumnIndex);
                        if (password.equals(savedPassword))
                        {
                            user = User.createUser()
                                            .setFirstName(selectedRows.getString(firstNameColumnIndex))
                                            .setLastName(selectedRows.getString(lastNameColumnIndex))
                                            .setUserName(userName)
                                            .setEmail(selectedRows.getString(emailColumnIndex))
                                            .setPassword(selectedRows.getString(passwordColumnIndex))
                                            .setDob(selectedRows.getString(dobColumnIndex))
                                            .setGender(selectedRows.getString(genderColumnIndex))
                                            .setAvatarFileName(selectedRows.getString(avatarColumnIndex));
                        }
                    }
                }
                else 
                {
                    Log.e(AppSettings.DebugPrefix, "Authentication failed: database does not contain expected fields");
                }
            }
            else // if userCount > 1 
            {
                Log.e(AppSettings.DebugPrefix, "Authentication failed due to database usage error : contains " + userCount + " accounts for user " + userName);
            }
            
            selectedRows.close();
            readableUserTable.close();
        } 
        catch (SQLiteException e) 
        {
            e.printStackTrace();
        }
        
        return user;
    }
    
    static public boolean userNameAlreadyUsed(Context context, String userName)
    {
        try 
        {
            SQLiteDatabase readableUserTable = SQLiteDbHelper.getInstance(context).getReadableDatabase();
            String[] selectFields 	= new String[] {DB_FIELD_USERNAME};		
            String whereClause 		= DB_FIELD_USERNAME + "=?";
            String[] whereArgs 		= new String[] {userName};				
            Cursor selectedRows 	= readableUserTable.query(DATABASE_TABLE_USER, selectFields, whereClause, whereArgs, null, null, null, null);				
            int userCount			= selectedRows.getCount();	
            selectedRows.close();
            readableUserTable.close();
            return userCount != 0;
        } 
        catch (SQLiteException e) 
        {
            e.printStackTrace();
        }
        return false; // If exception been thrown, assume we cannot proceed with the database anyway.
    }
    
    static public boolean delete(Context context, String userName)
    {
        boolean retVal = false;
        try 
        {
            SQLiteDatabase writableUserTable = SQLiteDbHelper.getInstance(context).getWritableDatabase();
            String deleteUserSql = "delete from " + DATABASE_TABLE_USER + " where " + DB_FIELD_USERNAME + "='" + userName + "'";
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