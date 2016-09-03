package com.guysagy.gamersweb.ui.activities;

import com.guysagy.gamersweb.R;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public final class WelcomeScreenActivity extends BaseActivity implements Button.OnClickListener
{
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setActivityView(R.layout.welcome, R.string.str_Welcome, Color.WHITE);
    }
    
    @Override
    protected void onNewIntent(Intent intent) 
    {
        super.onNewIntent(intent);
    }
    
    public void onStart()
    {
        super.onStart();
        
        TextView    title               = (TextView)findViewById(R.id.activity_title_1);
        TextView    versionText         = (TextView)findViewById(R.id.version_text);
        ImageView   animatedGameIcon    = (ImageView)findViewById(R.id.ImageView_animatedSplash);
        Button      goButton           = (Button)findViewById(R.id.Button_mainMenu);

        goButton.setOnClickListener(this);
        animatedGameIcon.setOnClickListener(this);
        
        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        title.startAnimation(fadeInAnimation);  
        goButton.startAnimation(fadeInAnimation);
        versionText.startAnimation(fadeInAnimation);
        
        Animation spinin = AnimationUtils.loadAnimation(this, R.anim.spin);
        animatedGameIcon.startAnimation(spinin);
    }
    
    protected void onPause()
    {
        super.onPause();
 
        TextView topTitle = (TextView)findViewById(R.id.activity_title_1);
        topTitle.clearAnimation();  
        
        ImageView animatedGameIcon 	= (ImageView)findViewById(R.id.ImageView_animatedSplash);
        animatedGameIcon.clearAnimation();
    }

    @Override
    public void onClick(View clickView) 
    {
        Intent intent = null;
        
        switch(clickView.getId())
        {
        case(R.id.Button_mainMenu):
            intent = new Intent(this, MainMenuActivity.class);
            break;
            
        case(R.id.ImageView_animatedSplash):
            intent = new Intent(this, TicTacToeActivity.class);
            break;
        }
        
        if (intent != null)
            startActivity(intent);
    }
}