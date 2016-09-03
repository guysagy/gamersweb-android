package com.guysagy.gamersweb.ui;

import com.guysagy.gamersweb.games.tictactoe.TicTacToeGame;
import com.guysagy.gamersweb.R;
import android.content.Context;
import android.view.Gravity;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class TicTacToeBoardView extends LinearLayout implements View.OnClickListener
{
    private int 				mLastMoveLocation = 0;
    private OnGameMoveListener 	mGameMoveListener = null;
    private TicTacToeGame 		mGameModel = null;
    protected   ImageView[] 	mImageButtons;

    public TicTacToeBoardView(Context context, /*int boardLayoutId,*/ TicTacToeGame gameModel, OnGameMoveListener listener)
    {
        super(context);
        mGameModel = gameModel;
        mGameMoveListener = listener;
        // setId(boardLayoutId);
        setOrientation(LinearLayout.VERTICAL);
        setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        setBackgroundResource(R.drawable.tictactoe_board);
        setSoundEffectsEnabled(false);
        setGravity(Gravity.CENTER);	

        int locationId = 1;
        int rowId = 10;
        for (int i = 0 ; i < mGameModel.getBoard().getHeight() ; ++i)
        {
            LinearLayout rowLayout = new LinearLayout(context);
            rowLayout.setId(rowId++);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            rowLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            rowLayout.setSoundEffectsEnabled(false);
            addView(rowLayout);
            
            for (int j = 0; j < mGameModel.getBoard().getWidth() ; ++j)
            {
                ImageView square = new ImageView(context);
                square.setId(locationId++);
                square.setAdjustViewBounds(true);
                square.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                square.setSoundEffectsEnabled(true);
                rowLayout.addView(square);
            }
        }    	
    }

//	public void enableBoardClickSound(boolean enable)
//	{
//		for (int i = 1; i <= mImageButtons.length ; ++i)
//		{
//			mImageButtons[i-1].setSoundEffectsEnabled(enable);
//		}		
//	}
    
    public void initGameBoardView()
    {
        mImageButtons  = new ImageView[mGameModel.getBoardSize()];
        for (int i = 1; i <= mImageButtons.length ; ++i)
        {
            initBoardLocation(i);
        }
    }
    
    public void initBoardLocation(int uiPosition)
    {
        mImageButtons[uiPosition-1] = (ImageView)findViewById(uiPosition);
        mImageButtons[uiPosition-1].setOnClickListener(this);
        resetBoardPosition(uiPosition);
    }

    public void resetGameBoardView()
    {
        for (int i = 1; i <= mImageButtons.length ; ++i)
        {
            resetBoardPosition(i);
        }
    }
    
    public void resetBoardPosition(int uiPosition)
    {
        mImageButtons[uiPosition-1].setImageResource(R.drawable.e); // Needed to erase previous images at end of game.
    }

    public void setMove(int location, int drawableId)
    {
        mImageButtons[location-1].setImageResource(drawableId);
        mImageButtons[location-1].setSoundEffectsEnabled(true);
        mImageButtons[location-1].playSoundEffect(SoundEffectConstants.CLICK);   		
        mImageButtons[location-1].setSoundEffectsEnabled(false);
        mImageButtons[location-1].invalidate();		
    }
    
    public void getLocationOnScreen(int location, int[] screenLocation)
    {
        /* getLocationOnScreen() :
         * Computes the coordinates of this view on the physical screen. 
         * The argument must be an array of two integers. 
         * After the method returns, the array contains the x and y location in that order.
         */
        
        mImageButtons[location-1].getLocationOnScreen(screenLocation);
        screenLocation[2] 	= mImageButtons[location-1].getWidth();
        screenLocation[3]  	= mImageButtons[location-1].getHeight();		
    }

    @Override
    public void onClick(View view)
    {
        if (view.getId() >= 1 && view.getId() <= mGameModel.getBoardSize())
        {
            mLastMoveLocation = view.getId();
            mGameMoveListener.onGameMove();
        }
    }
    
    public int getLastMove()
    {
        return mLastMoveLocation;
    }
}
