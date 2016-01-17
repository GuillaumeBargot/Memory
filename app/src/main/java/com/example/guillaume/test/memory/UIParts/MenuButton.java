package com.example.guillaume.test.memory.UIParts;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.guillaume.test.memory.R;

/**
 * Created by Guillaume on 15/01/2016.
 */
public class MenuButton extends RelativeLayout {

    private LayoutInflater inflater;
    private TextView textView;
    private RelativeLayout layout;

    public MenuButton(Context context) {
        super(context);
        inflater = LayoutInflater.from(context);
        init();
    }

    public MenuButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflater = LayoutInflater.from(context);
        init();
    }

    public MenuButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflater = LayoutInflater.from(context);
        init();
    }

    private void init(){
        inflater.inflate(R.layout.viewpart_menu_button, this, true);
        textView = (TextView)findViewById(R.id.menu_button_text);
        layout = (RelativeLayout)findViewById(R.id.menu_button_layout);
        layout.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                switch (arg1.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        layout.setAlpha((float)0.5);
                        callOnClick();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        layout.setAlpha((float)1);
                        break;
                    }
                    case MotionEvent.ACTION_CANCEL:{
                        layout.setAlpha((float)1);
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

    public void setGreen(){
        layout.setBackground(getResources().getDrawable(R.drawable.button_green));
    }
}
