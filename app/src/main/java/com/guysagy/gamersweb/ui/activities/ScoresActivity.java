package com.guysagy.gamersweb.ui.activities;

import com.guysagy.gamersweb.db.ScoresTable;
import com.guysagy.gamersweb.R;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.guysagy.gamersweb.user.Scores;
import com.guysagy.gamersweb.user.UserListener;

public final class ScoresActivity extends BaseActivity implements UserListener
{
    private Scores[] mAllScores = null;
    private Scores lastLoggedInUserScores = null;
    public final static String BUNDLE_EXTRA_KEY_INITIAL_TAB = "initialTab";
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setActivityView(R.layout.scores, R.string.str_Scores, Color.WHITE);
        
        /*
         TabHost:
         Container for a tabbed window view. This object holds two children: a set of tab labels 
         that the user clicks to select a specific tab, and a FrameLayout object that displays the 
         contents of that page. The individual elements are typically controlled using this 
         container object, rather than setting values on the child elements themselves. 
        */
        TabHost host = (TabHost)findViewById(R.id.TabHost_Scores);
        host.setup();
        
        addTabSpecToHost(host, "byName", R.string.str_ByGamerName, R.id.ScrollView_ScoresByName);
        addTabSpecToHost(host, "byScores", R.string.str_ByGamerScores, R.id.ScrollView_ScoresByScore);
        
        Bundle bundle = getIntent().getExtras();
        int initialTab = bundle.getInt(BUNDLE_EXTRA_KEY_INITIAL_TAB);
        host.setCurrentTab(initialTab);
        
        mAllScores = ScoresTable.getAllScores(this); 
        DeleteUserAccountActivity.addUserListeners(this);
    }
    
    @Override
    protected void onNewIntent(Intent intent) 
    {
        super.onNewIntent(intent);
        Bundle bundle = intent.getExtras();
        int initialTab = bundle.getInt(BUNDLE_EXTRA_KEY_INITIAL_TAB);
        TabHost host = (TabHost)findViewById(R.id.TabHost_Scores);
        host.setCurrentTab(initialTab);
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
        resumeScoresDisplay();
    }

    @Override
    protected void onDestroy()
    {
        DeleteUserAccountActivity.removeUserListeners(this);
        super.onDestroy();
    }
    
    private void resumeScoresDisplay()
    {
        if (mLoggedInUserScores != null 
                && mLoggedInUser != null 
                && lastLoggedInUserScores != null 
                && lastLoggedInUserScores.equals(mLoggedInUserScores) == false)
        {
            for(Scores score : mAllScores)
            {
                if (score.getPlayer1Name().equals(mLoggedInUserScores.getPlayer1Name()))
                {
                    score.setPlayer1Score(mLoggedInUserScores.getPlayer1Score());
                    lastLoggedInUserScores = mLoggedInUserScores.clone();
                    populateScoresTables();
                }
            }
        } 
        else
        {
            populateScoresTables();
        }
    }
    
    public void onAccountDelete()
    {
        // User has deleted his account; we need to remove his scores.
        if (lastLoggedInUserScores != null)
        {
            for(int i = 0 ; i < mAllScores.length ; ++i)
            {
                if (mAllScores[i].getPlayer1Name().equals(lastLoggedInUserScores.getPlayer1Name()))
                {
                    mAllScores[i] = mAllScores[ mAllScores.length - 1];
                    mAllScores[ mAllScores.length - 1] = null;
                    lastLoggedInUserScores = null;
                    populateScoresTables();
                }
            }
        }  
    }
    
    private void populateScoresTables()
    {
        TableLayout scoresTable = (TableLayout) findViewById(R.id.TableLayout_ScoresByName);
        scoresTable.removeAllViews();
        
        scoresTable = (TableLayout) findViewById(R.id.TableLayout_ScoresByScore);
        scoresTable.removeAllViews(); 
      
        if (mAllScores.length > 0)
        {
            quickSort(mAllScores, 0, mAllScores.length - 1, Scores.SORT_BY.NAME ) ;
            populateTable(R.id.TableLayout_ScoresByName);
            
            quickSort(mAllScores, 0, mAllScores.length - 1, Scores.SORT_BY.SCORE ) ;
            populateTableReversed(R.id.TableLayout_ScoresByScore); 
        }
    }
    
    private void addTabSpecToHost(TabHost host, String tag, int labelId, int contentViewId)
    {
        TabSpec newTabSpec = host.newTabSpec(tag);
        newTabSpec.setIndicator(getString(labelId));
        newTabSpec.setContent(contentViewId);
        host.addTab(newTabSpec);	
    }

    private void populateTable(int tableId)
    {
        TableLayout scoresTable = (TableLayout) findViewById(tableId);
        insertHeaderRow(scoresTable);
        
        if (mAllScores != null)
        {
            for (int i = 0 ; i < mAllScores.length ; ++i)
            {
                insertScoreRow(scoresTable, mAllScores[i].getPlayer1Name()
                                            , mAllScores[i].getPlayer1Score()
                                            , mAllScores[i].getPlayer2Score());
            }
        }
    }
    
    private void populateTableReversed(int tableId)
    {
        TableLayout scoresTable = (TableLayout) findViewById(tableId);
        insertHeaderRow(scoresTable);
        
        if (mAllScores != null)
        {
            for (int i = mAllScores.length - 1 ; i >= 0 ; --i)
            {
                insertScoreRow(scoresTable, mAllScores[i].getPlayer1Name()
                                            , mAllScores[i].getPlayer1Score()
                                            , mAllScores[i].getPlayer2Score());
            }
        }
    }
    
    private void insertHeaderRow(final TableLayout scoreTable) 
    {
        int textColor = getResources().getColor(R.color.scores_table_title);
        float textSize = getResources().getDimension(R.dimen.scores_text_size);
        String player1NameTitle = getString(R.string.str_ScoresTableGamerNameColumnTitle);
        String player1ScoreTitle = getString(R.string.str_ScoresTableGamerScoreColumnTitle);
        String Player2Title = getString(R.string.str_Android); 
        insertRow(scoreTable, textColor, textSize, player1NameTitle, player1ScoreTitle, Player2Title);
    }
    
    private void insertScoreRow(final TableLayout scoreTable, String player1Name, int player1Score, int player2Score) 
    {
        final TableRow newRow = new TableRow(this);
        int textColor = getResources().getColor(R.color.scores_table_text);
        float textSize = getResources().getDimension(R.dimen.scores_text_size);
        insertRow(scoreTable, textColor, textSize, player1Name, ((Integer)player1Score).toString(), ((Integer)player2Score).toString());
        scoreTable.addView(newRow);
    }
    
    private void insertRow(final TableLayout table, int textColor, float textSize, 
                                String column1, String column2, String column3)
    {
        final TableRow newRow = new TableRow(this);
        insertRowItem(newRow, column1, textColor, textSize);
        insertRowItem(newRow, column2, textColor, textSize);
        insertRowItem(newRow, column3, textColor, textSize);
        table.addView(newRow);   	
    }    

    private void insertRowItem(final TableRow tableRow, String text, int textColor, float textSize) 
    {
        TextView textView = new TextView(this);
        // Let the system set the text size; color is set by theme style.
        // textView.setTextSize(textSize);
        // textView.setTextColor(0x0000FF); //textColor);
        textView.setText(text);
        tableRow.addView(textView);
    }
    
    int partition(Scores arr[], int left, int right, Scores.SORT_BY sortBy) 
    { 
        int i = left, j = right; 
        Scores tmp; 
        Scores pivot = arr[(left + right) / 2]; 
          
        while (i <= j) 
        { 
            while (arr[i].compareBy(pivot, sortBy) < 0) 
                i++; 

            while (arr[j].compareBy(pivot, sortBy) > 0) 
                j--; 

            if (i <= j) 
            { 
                tmp = arr[i]; 
                arr[i] = arr[j]; 
                arr[j] = tmp; 
                i++; 
                j--; 
             } 
          }; 
          
          return i; 
    } 

    void quickSort(Scores arr[], int left, int right, Scores.SORT_BY sortBy) 
    { 
        if (arr == null || arr.length == 0)
            return;
        
        int index = partition(arr, left, right, sortBy); 
        if (left < index - 1) 
            quickSort(arr, left, index - 1, sortBy);
        
        if (index < right) 
            quickSort(arr, index, right, sortBy); 
    }

    @Override
    public void onAccountCreate() 
    {
        // Nothing to do.
        
    }

    @Override
    public void onAccountLogin() 
    {
        // Nothing to do.
    }

    @Override
    public void onAccountLogout() 
    {
        // Nothing to do.
    }
}