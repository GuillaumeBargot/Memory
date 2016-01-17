package com.example.guillaume.test.memory.UIParts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.guillaume.test.memory.R;

/**
 * Created by Guillaume on 17/01/2016.
 */
public class RecapFrame extends RelativeLayout {

    private LayoutInflater inflater;
    private TextView time;
    private TextView besttime;
    private MenuButton button;

    public RecapFrame(Context context) {
        super(context);
        inflater = LayoutInflater.from(context);
        init();
    }

    public RecapFrame(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflater = LayoutInflater.from(context);
        init();
    }

    public RecapFrame(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflater = LayoutInflater.from(context);
        init();
    }

    private void init(){
        inflater.inflate(R.layout.viewpart_recap_frame, this, true);
        time = (TextView)findViewById(R.id.recap_frame_time);
        besttime = (TextView)findViewById(R.id.recap_frame_best_time);
        button = (MenuButton)findViewById(R.id.recap_frame_new_game_button);
        button.setText("PLAY");
        button.setGreen();
    }

    public void setTime(String text){
        time.setText(text);
    }

    public void setBesttime(String text){
        besttime.setText(text);
    }

    public MenuButton getButton(){
        return button;
    }
}
