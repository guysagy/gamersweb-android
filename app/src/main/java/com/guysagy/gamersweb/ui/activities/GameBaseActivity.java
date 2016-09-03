package com.guysagy.gamersweb.ui.activities;

import java.util.ArrayList;
import com.guysagy.gamersweb.db.ScoresTable;
import com.guysagy.gamersweb.settings.AppSettings;
import com.guysagy.gamersweb.R;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import com.guysagy.gamersweb.games.AIPlayer;
import com.guysagy.gamersweb.games.Game;
import com.guysagy.gamersweb.games.GameListener;
import com.guysagy.gamersweb.games.Player;

/*
 * Serves as the base activity for games of two players.
 * Player (0) is always user.
 * Player (1) is always Android.
 */
public abstract class GameBaseActivity extends BaseActivity implements GameListener, Handler.Callback, View.OnClickListener
{
    protected   Game            mGame;
    protected   View            mGameViewLayout;
    protected   WebView         mGameTitle1;
    protected   WebView         mGameTitle2;
    protected   Handler         mAiPlayerMoveHandler;	
    protected   LinearLayout    mGameActivityFrameAnchor;
    protected   Button          mRestartGameButton;
    
    // The following must be implemented by the UI class - which provides the UI for a specific game.
    protected abstract int 	getGameTitleId();
    protected abstract int 	getPlayerDrawableId(Player player); 
    protected abstract Game createGameImpl(GameListener gameListener, Handler uiHandler);
    protected abstract View getGameBoardView();
    protected abstract void initGameBoardView();
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setActivityView(R.layout.game, getGameTitleId(), Color.WHITE);
        initializeGame();
        startGame();
    }
    
    @Override
    protected void onNewIntent(Intent intent) 
    {
        super.onNewIntent(intent);
    }
    
    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.btn_restart_game)
        {
            stopGame();
            startGame();
            mGameViewLayout.invalidate();
        }
    }	
    
    @Override
    protected void onResume()
    {
        super.onResume();
        Log.v(AppSettings.DebugPrefix, "onResume " + toString());
        updateGamePlayersNames();
    }   
    
    protected void onSaveInstanceState (Bundle outState) 
    {
        super.onSaveInstanceState(outState);
        // TODO: needs to be implemented. Need to save all member variable's state into Bundle.
        // This needs to be done recursively up the derivation hirarchy.
        // Restore side also needs implementation.
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        boolean retVal = super.onOptionsItemSelected(menuItem);
        if (menuItem.getTitle().toString().equals(getResources().getString(R.string.menu_item_account_logout)))
        {
            updateGamePlayersNames();
        }
        return retVal;
    } 
    
    @Override
    public boolean handleMessage(Message aiPlayerMessage) 
    {
        int aiMoveLocation = aiPlayerMessage.getData().getInt(mGame.mMoveAtLocationMessageKey);
        moveAtLocation(aiMoveLocation);
        return true;
    }
    
    protected void updateGamePlayersNames()
    {
        Player player = mGame.getCurrentPlayer();
        if ( player == null )
            return;
        
        ArrayList<Player> players = mGame.getPlayers();

        // 1) Update the game view - title 1.
        String player1Name = (mLoggedInUser == null) ? getString(R.string.str_Guest) : mLoggedInUser.getUserName();
        String player2Name = players.get(1).getName();
        
        String player1Type = players.get(0).getType().toString();
        String player2Type = players.get(1).getType().toString();
        setGameTitle1(player1Name, player1Type, player2Name, player2Type);

        // 2) Update the game model.
        if (player1Name.equals(players.get(0).getName()) == false)
        {
            players.get(0).setName(player1Name);
        }   
        
        mLoggedInUserScores = ScoresTable.getScores(this, player1Name, player2Name);
        
        // 3) Update the game view - title 2, after updating the game model (player 1).
        StringBuilder message = new StringBuilder(player.getName()).append(getString(R.string.str_IsNowPlaying));
        setGameTitle2(message.toString());        
    }
    
    protected void setGameTitle1(String player1Name, String player1Type, String player2Name, String player2Type)
    {
        String vs = getString(R.string.str_VS);
        
        String gameTitle1 = "<html>"
                                + "<head><STYLE type=\"text/css\">body{ text-align: center ; font-size: 20pt;}</STYLE></head>"
                                + "<body>"
                                + player1Name
                                + "<span style=\"color:red;\">"
                                + " " + player1Type  
                                + "</span>"
                                + " " + vs + " " 
                                + player2Name 
                                + "<span style=\"color:blue;\">"
                                + " " + player2Type
                                + "</span>"
                                + "</body></html>";
        
        mGameTitle1.loadData(gameTitle1, "text/html", "utf-8");
        mGameTitle1.invalidate();
    }
    
    protected void setGameTitle2(String title2)
    {
        title2 = "<html>" 
            + "<head><STYLE type=\"text/css\">body{ text-align: center ; font-size: 20pt;}</STYLE></head>"
            + "<body>"
            + title2
            + "</body>"
            + "</html>";
        
        mGameTitle2.loadData(title2, "text/html", "utf-8");
        mGameTitle2.invalidate();
    }
    
    protected void initializeGame()
    {
        // The UI class layout must supply the following two views.
        mGameTitle1 = (WebView)findViewById(R.id.player_text_switcher_1);
        mGameTitle2 = (WebView)findViewById(R.id.player_text_switcher_2);
        
        mRestartGameButton = ((Button)findViewById(R.id.btn_restart_game));
        mRestartGameButton.setOnClickListener(this); 
        
        mGameActivityFrameAnchor = (LinearLayout)findViewById(R.id.game_frame_layout_anchor);
        mAiPlayerMoveHandler = new Handler(this);
        mGame = createGameImpl((GameListener)this, mAiPlayerMoveHandler);  
        
        LinearLayout boardParentLayout = (LinearLayout)findViewById(R.id.board_image_container);
        mGameViewLayout = getGameBoardView();
        boardParentLayout.addView(mGameViewLayout);
    }
    
    protected void startGame()
    {
        ArrayList<Player> players = mGame.getPlayers();
        String userName = (mLoggedInUser == null) ? getString(R.string.str_Guest) : mLoggedInUser.getUserName();
        players.get(0).setName(userName);	
        players.get(1).setName(getString(R.string.str_Android));
        mGame.start(players.get(0)); 
        updateGamePlayersNames();
    }	

    protected void stopGame()
    {
        mGame.stop();
    }
    
    protected void moveAtLocation(int location)
    {
        if (mGame.isStopped())
            return;
        
        mGame.makeMove(location);
    }

    protected void uiMoveAtLocation(int location)
    {
        if (mGame.getCurrentPlayer() instanceof AIPlayer)
            return; /* TODO: should this not be an exception thrown ? */
        
        moveAtLocation(location);
    }
    
    public void notifyGameMove()
    {
        // Do nothing, let concrete implementations change UI display as needed.
    }
    
    public void notifyGameWin()
    {
        stopGame();
        Player winnerPlayer = mGame.getWinnerPlayer();
        
        if (mLoggedInUserScores != null) // Guests' don't count scores.
        {
            if (winnerPlayer.getName().equals(mLoggedInUserScores.getPlayer1Name()) == true) // Winner is player.
                mLoggedInUserScores.setPlayer1Score(mLoggedInUserScores.getPlayer1Score()+1);
            else // Winner is android.
                mLoggedInUserScores.setPlayer2Score(mLoggedInUserScores.getPlayer2Score()+1);
            
            ScoresTable.update(this, mLoggedInUserScores.getPlayer1Name(), mLoggedInUserScores.getPlayer1Score(), 
                                    mLoggedInUserScores.getPlayer2Name(), mLoggedInUserScores.getPlayer2Score());
        }
        
        StringBuilder message = new StringBuilder();
        message.append(winnerPlayer.getName()).append(" ").append(getString(R.string.str_HasWonWith));
        setGameTitle2(message.toString());
    }
    
    public void notifyGameDraw()
    {
        stopGame();
        StringBuilder message = new StringBuilder();
        message.append(getString(R.string.str_GameEndedWithADraw));
        setGameTitle2(message.toString());
    }
    
    public void notifyIllegalMove()
    {
        // Do nothing, let concrete implementations alert user if needed.
    }	
    
    public void notifyCurrentPlayerChanged()
    {
        updateGamePlayersNames();
    }
    
    class PlayerTextSwitcherFactory implements ViewSwitcher.ViewFactory
    {
        @Override
        public View makeView() 
        {
            TextView textView = new TextView(GameBaseActivity.this);
            textView.setGravity(Gravity.CENTER);
            textView.setLayoutParams(new TextSwitcher.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            textView.setTextSize(getResources().getDimension(R.dimen.game_title));
            textView.setTextColor(getResources().getColor(R.color.game_text));	
            textView.setText("");
            return textView;
        }
    }
}