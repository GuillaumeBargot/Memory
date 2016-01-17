package com.example.guillaume.test.memory.UIParts;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.guillaume.test.memory.R;

/**
 * Created by Guillaume on 15/01/2016.
 */
public class MenuFrame extends RelativeLayout {

    private LayoutInflater inflater;
    private MenuButton continueButton;
    private MenuButton playButton;
    private MenuButton statsButton;
    private boolean isContinuePossible = false;

    public MenuFrame(Context context) {
        super(context);
        inflater = LayoutInflater.from(context);
        init();
    }

    public MenuFrame(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflater = LayoutInflater.from(context);
        init();
    }

    public MenuFrame(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflater = LayoutInflater.from(context);
        init();
    }

    private void init(){
        inflater.inflate(R.layout.viewpart_menu_frame, this, true);
        continueButton = (MenuButton)findViewById(R.id.menu_frame_continue_button);
        playButton = (MenuButton)findViewById(R.id.menu_frame_play_button);
        statsButton = (MenuButton)findViewById(R.id.menu_frame_stats_button);

        continueButton.setText("CONTINUE");
        if(isContinuePossible){
            Log.d("MenuFrame", "goes through init VISIBLE");
            continueButton.setVisibility(VISIBLE);
        }else{
            Log.d("MenuFrame", "goes through init GONE");
            continueButton.setVisibility(GONE);
        }

        playButton.setGreen();
        playButton.setText("PLAY");

        statsButton.setText("STATS");
    }

    public MenuButton getContinueButton(){
        return continueButton;
    }

    public MenuButton getPlayButton(){
        return playButton;
    }

    public MenuButton getStatsButton(){
        return statsButton;
    }

    public void setContinuePossible(boolean isIt){
        if(isIt){
            continueButton.setVisibility(VISIBLE);
            isContinuePossible = true;
        }else{
            continueButton.setVisibility(GONE);
            isContinuePossible = false;
        }
    }
}
