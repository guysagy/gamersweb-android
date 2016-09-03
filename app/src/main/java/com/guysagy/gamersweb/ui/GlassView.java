package com.guysagy.gamersweb.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/*
 * GlassView is designed to be a transparent view laid over the 'client' area of the activities' view,
 * i.e., it is laid over the the layout view below the activity title.
 * The purpose is to allow for intermittent views to be drawn over the client area.
 */
public class GlassView extends View 
{
    private int[] mLineCoordinates = new int[4];
    private int mLineColor = Color.BLUE;
    
    public GlassView(Context context) 
    {
        super(context);
    }
    
    @Override
    protected void onDraw(Canvas canvas)
    {
        Paint paint = new Paint();
        paint.setColor(mLineColor);
        paint.setStrokeWidth(3);
        // Note: (0, 0) == (top, left) corner of the view.
        canvas.drawLine(mLineCoordinates[0], mLineCoordinates[1], mLineCoordinates[2], mLineCoordinates[3], paint);
    }
    
    public void setLineCoordinatesViewCoordinates(int topX, int leftY, int bottomX, int rightY)
    {
        // Note: topX = 0 and topY = 0 is at the bottom left corner of the Tic Tac Toe game title.
        mLineCoordinates[0] = topX;
        mLineCoordinates[1] = leftY;
        mLineCoordinates[2] = bottomX;
        mLineCoordinates[3] = rightY;
    }
    
    public void setLineColor(int lineColor)
    {
        mLineColor = lineColor;
    }
}
