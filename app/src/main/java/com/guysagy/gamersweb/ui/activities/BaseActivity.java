package com.guysagy.gamersweb.ui.activities;

import com.guysagy.gamersweb.settings.AppSettings;
import com.guysagy.gamersweb.R;
import com.guysagy.gamersweb.user.Scores;
import com.guysagy.gamersweb.user.User;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public abstract class BaseActivity extends Activity 
{
    protected 	static User 	mLoggedInUser;
    protected   static Scores 	mLoggedInUserScores;
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        Log.v(AppSettings.DebugPrefix, "onCreate " + toString());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    
    @Override
    protected void onNewIntent(Intent intent) 
    {
        super.onNewIntent(intent);
    }
    
    protected void setActivityView(int activityMainId, int titleResourceId, int backgroundColor)
    {
        View activityView = getLayoutInflater().inflate(activityMainId, null);
        setActivityView(activityView, titleResourceId, backgroundColor);
    }
    
    protected void setActivityView(View activityMain, int titleResourceId, int backgroundColor)
    {
        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        
        View activityTitle = getLayoutInflater().inflate(R.layout.activity_title, null);
        activityTitle.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        TextView textView = (TextView)activityTitle.findViewById(R.id.activity_title_string);
        textView.setText(getString(titleResourceId));
        rootLayout.addView(activityTitle);
        
        activityMain.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        rootLayout.addView(activityMain);
        
        setContentView(rootLayout);
    }
    
    @Override
    protected void onStart()
    {
        super.onStart();
        Log.v(AppSettings.DebugPrefix, "onStart " + toString());
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
        Log.v(AppSettings.DebugPrefix, "onResume " + toString());
    }
    
    @Override
    protected void onPause()
    {
        super.onPause();
        Log.v(AppSettings.DebugPrefix, "onPause " + toString());
    }    
    
    @Override
    protected void onStop()
    {
        super.onStop();
        Log.v(AppSettings.DebugPrefix, "onStop " + toString());
    }
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Log.v(AppSettings.DebugPrefix, "onDestroy " + toString());
    }    
    
    protected void onSaveInstanceState (Bundle outState) 
    {
        super.onSaveInstanceState(outState);
        Log.v(AppSettings.DebugPrefix, "onSaveInstanceState " + toString());
        // TODO: needs to be implemented. Need to save all member variable's state into Bundle.
        // This needs to be done recursively up the derivation hirarchy.
        // Restore side also needs implementation.
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle restoreBundle)
    {
        super.onRestoreInstanceState(restoreBundle);
        Log.v(AppSettings.DebugPrefix, "onRestoreInstanceState " + toString());
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.gameoptions, menu);
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem menuItem = menu.findItem(R.id.menu_item_account_login);
        CharSequence menuTitle = (mLoggedInUser == null) ? getString(R.string.menu_item_account_login) : getString(R.string.menu_item_account_logout) ;
        menuItem.setTitle(menuTitle);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        return menusHandler(menuItem.getTitle().toString());
    }
    
    protected boolean menusHandler(String menuItemText)
    {
        Intent intent = null;
                
        if (menuItemText.equals(getString(R.string.str_TicTacToe)))
        {
            intent = new Intent(this, TicTacToeActivity.class);
        }
        else if (menuItemText.equals(getString(R.string.menu_item_account_delete)))
        {
            if (mLoggedInUser == null)
            {
                makeLongToast(R.string.str_PleaseLogin);
            }  
            else
            {
                intent = new Intent(this, DeleteUserAccountActivity.class);
            }
        }    	
        else if (menuItemText.equals(getString(R.string.menu_item_account_create)))
        {
            if (mLoggedInUser != null)
            {
                makeLongToast(R.string.str_PleaseLogout);
            }  
            else
            {
                intent = new Intent(this, CreateUserAccountActivity.class);
            }
        }
        else if (menuItemText.equals(getString(R.string.menu_item_account_update)))
        {
            if (mLoggedInUser == null)
            {
                makeLongToast(R.string.str_PleaseLogin);
                intent = new Intent(this, LoginUserAccountActivity.class);
            }    
            else
            {
                intent = new Intent(this, UpdateUserAccountActivity.class);
            }
        }
        else if (menuItemText.equals(getString(R.string.menu_item_account_login)))
        {
            if (mLoggedInUser != null)
            {
                makeLongToast(R.string.str_PleaseLogout);
            }
            else
            {
                intent = new Intent(this, LoginUserAccountActivity.class);
            }
        }
        else if (menuItemText.equals(getString(R.string.menu_item_account_logout)))
        {
            // If the user is in Update Account activity , and then logs out, 
            // the Update Account activity needs to be exited.
            if (UpdateUserAccountActivity.isActivityRunning)
            {
                intent = new Intent(this, UpdateUserAccountActivity.class);
                intent.putExtra(UpdateUserAccountActivity.extraExit, UpdateUserAccountActivity.extraExit); 
            }
            mLoggedInUser = null;
            mLoggedInUserScores = null;
            Toast.makeText(this, getString(R.string.str_YouAreLoggedOut), Toast.LENGTH_SHORT).show();
        }
        else if (menuItemText.equals(getString(R.string.menu_item_info)))
        {
            intent = new Intent(this, AboutActivity.class);
        }
        else if (menuItemText.equals(getString(R.string.menu_item_welcome)))
        {
            intent = new Intent(this, WelcomeScreenActivity.class);
        }
        else if (menuItemText.equals(getString(R.string.menu_item_main_menu)))
        {
            intent = new Intent(this, MainMenuActivity.class);
        }    	
        else if (menuItemText.equals(getString(R.string.menu_item_scores_by_name)))
        {
            intent = new Intent(this, ScoresActivity.class);
            intent.putExtra(ScoresActivity.BUNDLE_EXTRA_KEY_INITIAL_TAB, 0);
        }  
        else if (menuItemText.equals(getString(R.string.menu_item_scores_by_score)))
        {
            intent = new Intent(this, ScoresActivity.class);
            intent.putExtra(ScoresActivity.BUNDLE_EXTRA_KEY_INITIAL_TAB, 1);
        }      	
        
        if (intent != null)
        {
            startActivity(intent);
            return true;
        }
        
        return false;
    }
    
    protected void makeShortToast(int stringId)
    {
        Toast.makeText(this, getString(stringId), Toast.LENGTH_SHORT).show();
    }
    
    protected void makeLongToast(int stringId)
    {
        Toast.makeText(this, getString(stringId), Toast.LENGTH_LONG).show();
    }   
}
