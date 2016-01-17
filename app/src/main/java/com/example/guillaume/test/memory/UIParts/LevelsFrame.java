package com.example.guillaume.test.memory.UIParts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.example.guillaume.test.memory.R;

/**
 * Created by Guillaume on 15/01/2016.
 */
public class LevelsFrame extends RelativeLayout {

    private LayoutInflater inflater;
    private LevelsButton level2;
    private LevelsButton level3;
    private LevelsButton level4;
    private LevelsButton level5;
    private LevelsButton level6;
    private LevelsButton level7;
    private LevelsButton level8;

    public LevelsFrame(Context context) {
        super(context);
        inflater = LayoutInflater.from(context);
        init();
    }

    public LevelsFrame(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflater = LayoutInflater.from(context);
        init();
    }

    public LevelsFrame(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflater = LayoutInflater.from(context);
        init();
    }

    private void init(){
        inflater.inflate(R.layout.viewpart_levels_frame, this, true);
        level2 = (LevelsButton)findViewById(R.id.levels_frame_2);
        level3 = (LevelsButton)findViewById(R.id.levels_frame_3);
        level4 = (LevelsButton)findViewById(R.id.levels_frame_4);
        level5 = (LevelsButton)findViewById(R.id.levels_frame_5);
        level6 = (LevelsButton)findViewById(R.id.levels_frame_6);
        level7 = (LevelsButton)findViewById(R.id.levels_frame_7);
        level8 = (LevelsButton)findViewById(R.id.levels_frame_8);

        level2.setText("2");
        level3.setText("3");
        level4.setText("4");
        level5.setText("5");
        level6.setText("6");
        level7.setText("7");
        level8.setText("8");

    }

    public LevelsButton getLevel2(){
        return level2;
    }

    public LevelsButton getLevel3(){
        return level3;
    }

    public LevelsButton getLevel4(){
        return level4;
    }

    public LevelsButton getLevel5(){
        return level5;
    }

    public LevelsButton getLevel6(){
        return level6;
    }

    public LevelsButton getLevel7(){
        return level7;
    }

    public LevelsButton getLevel8(){
        return level8;
    }
}
