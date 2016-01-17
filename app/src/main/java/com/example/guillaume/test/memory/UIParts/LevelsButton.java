package com.example.guillaume.test.memory.UIParts;

import android.content.Context;
import android.media.Image;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.guillaume.test.memory.R;

/**
 * Created by Guillaume on 15/01/2016.
 */
public class LevelsButton extends RelativeLayout {

    private LayoutInflater inflater;
    private TextView textView;
    private RelativeLayout layout;
    private ImageView imageView;

    public LevelsButton(Context context) {
        super(context);
        inflater = LayoutInflater.from(context);
        init();
    }

    public LevelsButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflater = LayoutInflater.from(context);
        init();
    }

    public LevelsButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflater = LayoutInflater.from(context);
        init();
    }

    private void init(){
        inflater.inflate(R.layout.viewpart_levels_button, this, true);
        textView = (TextView)findViewById(R.id.levels_button_text);
        imageView = (ImageView)findViewById(R.id.levels_button_image);
        layout = (RelativeLayout)findViewById(R.id.levels_button_layout);
        layout.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                switch (arg1.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        layout.setAlpha((float) 0.5);
                        callOnClick();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        layout.setAlpha((float) 1);
                        break;
                    }
                    case MotionEvent.ACTION_CANCEL: {
                        layout.setAlpha((float) 1);
                        break;
                    }
                }
                return true;
            }
        });
    }

    public void setText(String text){
        textView.setText(text);
    }

    public void setImageButton(Integer resource){
        imageView.setVisibility(VISIBLE);
        textView.setVisibility(GONE);
        imageView.setImageResource(resource);
        layout.setBackgroundResource(R.drawable.button_play_pause);
    }

    public void setGreen(){
        layout.setBackground(getResources().getDrawable(R.drawable.button_play_pause));
    }
}
