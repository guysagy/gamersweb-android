package com.guysagy.gamersweb.ui.activities;

import com.guysagy.gamersweb.settings.AppSettings;
import com.guysagy.gamersweb.ui.GlassView;
import com.guysagy.gamersweb.ui.OnGameMoveListener;
import com.guysagy.gamersweb.ui.TicTacToeBoardView;
import com.guysagy.gamersweb.R;
import com.guysagy.gamersweb.factories.GamesFactory;
import com.guysagy.gamersweb.games.AIPlayer;
import com.guysagy.gamersweb.games.Game;
import com.guysagy.gamersweb.games.GameListener;
import com.guysagy.gamersweb.games.Move;
import com.guysagy.gamersweb.games.Player;
import com.guysagy.gamersweb.games.tictactoe.TicTacToeGame;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.ViewSwitcher;

public final class TicTacToeActivity extends GameBaseActivity implements OnTouchListener, OnCompletionListener, OnGameMoveListener
{
    //private final static String gameLayoutId                = "20";
    private final static String currentPlayerSymbolViewId   = "21";
    //private final static String boardLayoutId               = "22";
    
    private final static int START_DRAGGING                 = 0;
    private final static int STOP_DRAGGING                  = 1;

    private TicTacToeBoardView 	mBoardView;
    
    // The current player symbol above the board.
    private ImageSwitcher   mCurrentPlayerSymbolImageSwitcher;
    
    // The current player draggable symbol.
    private ImageView       mDraggablePlayerSymbolImage;

    // The transparent view that will display the line stroke when there is a winner.
    private GlassView       mWinLineStrokeView;
    
    private int             mDragStatus;
    private MediaPlayer     mMediaPlayer;
    private Animation       mBoardAnimation;
    private boolean         mWinResourcesAllocated;
    
    protected Game createGameImpl(GameListener gameListener, Handler uiHandler)
    {
        return GamesFactory.createTicTacToeGameImpl(gameListener, uiHandler);
    }
    
    protected int getGameTitleId()
    {
        return R.string.str_TicTacToe;
    }
   
    protected int getPlayerDrawableId(Player player)
    {
        return player.getType().toString().equals("X") ? R.drawable.x : R.drawable.o ;  
    }

    protected int getOtherPlayerDrawableId(Player player)
    {
        return player.getType().toString().equals("X") ? R.drawable.o : R.drawable.x ;  
    }
    
    protected View getGameBoardView()
    {
        // XML-generated IDs start at 0x7f000000. 
        // Framework IDs are above 0x800.
        
        LinearLayout gameView = new LinearLayout(this);
        // gameView.setId(gameLayoutId);
        gameView.setOrientation(LinearLayout.VERTICAL);
        gameView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        gameView.setGravity(Gravity.CENTER);
        gameView.setSoundEffectsEnabled(false);    	

        mCurrentPlayerSymbolImageSwitcher = new ImageSwitcher(this);
        mCurrentPlayerSymbolImageSwitcher.setTag(currentPlayerSymbolViewId);
        mCurrentPlayerSymbolImageSwitcher.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        mCurrentPlayerSymbolImageSwitcher.setSoundEffectsEnabled(true);
        mCurrentPlayerSymbolImageSwitcher.setFactory(new DraggableImageSwitcherFactory());
        mCurrentPlayerSymbolImageSwitcher.setOnTouchListener(this);
        gameView.addView(mCurrentPlayerSymbolImageSwitcher);
                
        mBoardView = new TicTacToeBoardView(this, /* boardLayoutId, */ (TicTacToeGame)mGame, this);
        gameView.addView(mBoardView);
        return gameView;	
    }
    
    protected void initializeGame() 
    {
        super.initializeGame();
        mBoardAnimation = AnimationUtils.loadAnimation(this, R.anim.board_shake);
        mBoardAnimation.setDuration(1000);

        mBoardAnimation.setStartOffset(0);
        mBoardAnimation.setRepeatMode(Animation.RESTART);
        mBoardAnimation.setRepeatCount(Animation.INFINITE);
        
        mMediaPlayer = new MediaPlayer();
        
        mWinResourcesAllocated = false;
        
        mDraggablePlayerSymbolImage = new ImageView(this);
        mDraggablePlayerSymbolImage.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        mDraggablePlayerSymbolImage.setSoundEffectsEnabled(false);   
        mGameActivityFrameAnchor.addView(mDraggablePlayerSymbolImage);
        
        mWinLineStrokeView = new GlassView(this);
        mWinLineStrokeView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mGameActivityFrameAnchor.addView(mWinLineStrokeView);
        
        // This view implementation is based on the knowledge that the game implementation is 
        // such that the first player is always the one and only human player in this game.
        // This needs to be done in initializeGame(), after the mGame is instantiated.
        mDraggablePlayerSymbolImage.setImageResource(getPlayerDrawableId(mGame.getCurrentPlayer()));
        mDraggablePlayerSymbolImage.setVisibility(View.GONE);        
        
        initGameBoardView();
    }
    
    protected void startGame()
    {
        super.startGame();
        mCurrentPlayerSymbolImageSwitcher.setImageResource(getPlayerDrawableId(mGame.getCurrentPlayer()));
        mWinLineStrokeView.setVisibility(View.GONE); 
        stopWinningPyrotechnics();
        mRestartGameButton.setEnabled(false);
        mBoardView.resetGameBoardView();
    }
    
    protected void stopGame()
    {
        super.stopGame();
    }
    
    protected void initGameBoardView()
    {
        mBoardView.initGameBoardView();
    }
    
    @Override
    public void notifyGameMove()
    {
        super.notifyGameMove();
        
        Move move = mGame.getLastMadeMove();
        Player player = move.getPlayer();
        int drawableId = getPlayerDrawableId(player);
        mBoardView.setMove(move.getLocation().getLocation(), drawableId);
        mCurrentPlayerSymbolImageSwitcher.setImageResource(getOtherPlayerDrawableId(player));
        mRestartGameButton.setEnabled(true);
    }
    
    @Override	
    public void notifyGameWin()
    {
        super.notifyGameWin();
        
        mCurrentPlayerSymbolImageSwitcher.setImageResource(getPlayerDrawableId(mGame.getWinnerPlayer()));
        int[] winningStroke = mGame.getWinningStroke();
        
        int[] center1 = getBoardLocationCenterClientCoordinates(winningStroke[0]);
        int[] center2 = getBoardLocationCenterClientCoordinates(winningStroke[2]);
        
        /*
         * The origin (i.e. view coordinates (0,0)) of mWinLineStrokeView and
         * the Client Coordinates  of this activity coincide.
         */
        mWinLineStrokeView.setLineCoordinatesViewCoordinates(center1[0], center1[1], center2[0], center2[1]);
        int lineColor = getPlayerDrawableId(mGame.getWinnerPlayer()) == R.drawable.x ? Color.RED : Color.BLUE;
        mWinLineStrokeView.setLineColor(lineColor);
        mWinLineStrokeView.setVisibility(View.VISIBLE); 
        
        startWinningPyrotechnics();
    }
    
    @Override	
    public void notifyGameDraw()
    {
        super.notifyGameDraw();
        mCurrentPlayerSymbolImageSwitcher.setImageResource(R.drawable.e);
    }
    
    /*
     * Client coordinates means the (0,0) is on the top left corner below the activity title.
     */
    private int[] getBoardLocationCenterClientCoordinates(int boardLocation)
    {
        int[] center = new int[2];
        int[] viewLocation = new int[4];
        mBoardView.getLocationOnScreen(boardLocation, viewLocation);
        
        RelativeLayout titleLayout = (RelativeLayout)findViewById(R.id.activity_title);
        int[] titleLayoutLocation = new int[2];
        titleLayout.getLocationOnScreen(titleLayoutLocation);
        int titleLayoutHeight = titleLayout.getHeight();
        int titleLayoutBottom = titleLayoutLocation[1] + titleLayoutHeight;
        
        int viewX       = viewLocation[0];
        int viewY       = viewLocation[1] - titleLayoutBottom;
        int viewWidth   = viewLocation[2];
        int viewHeight  = viewLocation[3];
        
        center[0] = viewX + viewWidth/2;
        center[1] = viewY + viewHeight/2;
        
        return center;
    }
    
    @Override
    protected void onPause()
    {
        super.onPause();
        Log.v(AppSettings.DebugPrefix, "onPause " + toString());
        stopWinningPyrotechnics();
    }

    protected void onSaveInstanceState (Bundle outState) 
    {
        super.onSaveInstanceState(outState);
        // TODO: needs to be implemented. Need to save all member variable's state into Bundle.
        // This needs to be done recursively up the derivation hirarchy.
        // Restore side also needs implementation.
    }
    
    @Override
    public void onClick(View view)
    {
        super.onClick(view);
    }
    
    @Override
    public boolean onTouch(View view, MotionEvent me) 
    {
        /* TODO: the test for AIPlayer should throw an exception, not return */
        if (((String)view.getTag()).equals(currentPlayerSymbolViewId)
                && (mGame.getCurrentPlayer() instanceof AIPlayer) || mGame.isStopped())
            return true;
        
        final int draggedImageOffsetY = 75;
        
        if (me.getAction() == MotionEvent.ACTION_DOWN && ((String)view.getTag()).equals(currentPlayerSymbolViewId))
        {
            mDragStatus = START_DRAGGING;
            return true;
        }
        
        if (me.getAction() == MotionEvent.ACTION_UP && mDragStatus == START_DRAGGING) 
        {
            int screenCoordTouchX = (int)me.getRawX();
            int screenCoordTouchY = (int)me.getRawY() - draggedImageOffsetY;
            int[] viewLocation = new int[4];
            mBoardView.getLocationOnScreen(1, viewLocation);
            int x = (screenCoordTouchX - viewLocation[0])/viewLocation[2] + 1;
            int y = (screenCoordTouchY - viewLocation[1])/viewLocation[3] + 1;
            int moveLocation = x + (y - 1)*mGame.getBoard().getWidth();
            uiMoveAtLocation(moveLocation);
            mDraggablePlayerSymbolImage.setVisibility(View.GONE);
            mDragStatus = STOP_DRAGGING;
            return true;
        }
        
        if (me.getAction() == MotionEvent.ACTION_MOVE && mDragStatus == START_DRAGGING) 
        {
            Log.v(AppSettings.DebugPrefix, "Motion Event rawX = " + me.getRawX());
            Log.v(AppSettings.DebugPrefix, "Motion Event rawY = " + me.getRawY());
            
            /*
             * getRawX(), getRawY():
             * Returns the original raw X, Y coordinates of this event. 
             * For touch events on the screen, this is the original location of the event on the screen, 
             * before it had been adjusted for the containing window and views. 
             * (rawX, rawY) values (0,0) are at the top left corner of the physical screen:
             * i.e., above the line that displays the time.
             */
            
            RelativeLayout titleLayout = (RelativeLayout)findViewById(R.id.activity_title);
            int[] titleLayoutLocation = new int[2];
            titleLayout.getLocationOnScreen(titleLayoutLocation);
            int titleLayoutHeight = titleLayout.getHeight();
            int titleLayoutBottom = titleLayoutLocation[1] + titleLayoutHeight;
            int clientCoordTouchX = (int)me.getRawX();
            int clientCoordTouchY = (int)me.getRawY() - titleLayoutBottom;

            // TODO: replace the hard coded 25 value with a reading from the bitmap
            /*
             * clientCoordTouchX & Y are the coordinates of the touch; this value is shifted by 
             * 25 pixels in both x and y to center the image in the touch position 
             * (the width of the image is 50 pixels).
             * The y position is shifted by 75 pixels upwards to position the dragged image above the touching finger
             * so it will be viewable more easily. 75 pixels are an arbitrary value fitting my fingers. 
             */

            final int imageCenterOffset = 25; // = 50 / 2
            mDraggablePlayerSymbolImage.setPadding(clientCoordTouchX - imageCenterOffset, 
                                                    clientCoordTouchY - imageCenterOffset - draggedImageOffsetY, 
                                                    0, 0);
            mDraggablePlayerSymbolImage.setVisibility(View.VISIBLE);
            mDraggablePlayerSymbolImage.invalidate();
            return true;
        }
        
        return false;
    }
    
    // Called when the end of a media source is reached during playback.
    @Override
    public void onCompletion(MediaPlayer player) 
    {
        stopWinningPyrotechnics();
        mBoardView.invalidate();
    }
    
    // Allocate winner's animation and sound.
    private void startWinningPyrotechnics()
    {
        if (!mWinResourcesAllocated)
        {
            // Animation
            mBoardView.startAnimation(mBoardAnimation);
            mWinLineStrokeView.startAnimation(mBoardAnimation);
            
            // Media player
            mMediaPlayer = MediaPlayer.create(this, R.raw.happy_birthday_free);
            mMediaPlayer.setOnCompletionListener(this); 
            mMediaPlayer.start();
            
            mWinResourcesAllocated = true;
        }
    }
    
    // Stop winner's animation and sound.
    private void stopWinningPyrotechnics()
    {
        if (mWinResourcesAllocated)
        {
            // Animation
            mBoardView.clearAnimation();
            mWinLineStrokeView.clearAnimation();
            
            // Media player
            mMediaPlayer.stop();
            mMediaPlayer.release();

            mWinResourcesAllocated = false;
        }
    }
    
    class DraggableImageSwitcherFactory implements ViewSwitcher.ViewFactory
    {
        @Override
        public View makeView() 
        {
            ImageView imageView = new ImageView(TicTacToeActivity.this);
            imageView.setLayoutParams(new TextSwitcher.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            imageView.setAdjustViewBounds(true);
            imageView.setImageResource(R.drawable.e);
            return imageView;
        }
    }

    @Override
    public void onGameMove() 
    {
        uiMoveAtLocation(mBoardView.getLastMove());
    }
}