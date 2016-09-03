package com.guysagy.gamersweb.ui.activities;

import java.util.ArrayList;
import java.io.InputStream;
import com.guysagy.gamersweb.R;
import com.guysagy.gamersweb.user.UserListener;
import com.guysagy.gamersweb.db.ScoresTable;
import com.guysagy.gamersweb.db.UserTable;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public final class DeleteUserAccountActivity extends BaseActivity 
{
    static private ArrayList<UserListener> mUserListeners = new ArrayList<UserListener>();
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setActivityView(R.layout.account_delete, R.string.str_deleteAccount, Color.WHITE);
        
        final Button accountDeleteButton = (Button)findViewById(R.id.Button_accountDelete);
        accountDeleteButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View theView) 
            {
                // Note: referential integrity is not yet enforced by the database.
                UserTable.delete(DeleteUserAccountActivity.this, mLoggedInUser.getUserName());
                ScoresTable.delete(DeleteUserAccountActivity.this, mLoggedInUser.getUserName());
                menusHandler(getString(R.string.menu_item_account_logout));
                notifyOnAccountDelete();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) 
    {
        super.onNewIntent(intent);
    }
    
    static public void addUserListeners(UserListener userListener)
    {
        mUserListeners.add(userListener);
    }
    
    static public void removeUserListeners(UserListener userListener)
    {
        mUserListeners.remove(userListener);
    }   
    
    public void notifyOnAccountDelete()
    {
        for (UserListener userListener : mUserListeners)
        {
            userListener.onAccountDelete();
        }
    }	
}
