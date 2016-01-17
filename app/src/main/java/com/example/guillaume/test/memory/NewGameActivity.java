package com.example.guillaume.test.memory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.guillaume.test.memory.UIParts.LevelsFrame;

public class NewGameActivity extends AppCompatActivity {

    //Utilisée pour passer le nombre d'images à la GameActivity
    public static final String SIZE = "com.example.guillaume.test.memory.SIZE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        LevelsFrame levelsFrame = (LevelsFrame)findViewById(R.id.new_game_levels_frame);
        levelsFrame.getLevel2().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                int nbImages = 2;
                intent.putExtra(SIZE, nbImages);
                startActivity(intent);
            }
        });

        levelsFrame.getLevel3().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                int nbImages = 3;
                intent.putExtra(SIZE, nbImages);
                startActivity(intent);
            }
        });

        levelsFrame.getLevel4().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                int nbImages = 4;
                intent.putExtra(SIZE, nbImages);
                startActivity(intent);
            }
        });

        levelsFrame.getLevel5().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                int nbImages = 5;
                intent.putExtra(SIZE, nbImages);
                startActivity(intent);
            }
        });

        levelsFrame.getLevel6().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                int nbImages = 6;
                intent.putExtra(SIZE, nbImages);
                startActivity(intent);
            }
        });

        levelsFrame.getLevel7().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                int nbImages = 7;
                intent.putExtra(SIZE, nbImages);
                startActivity(intent);
            }
        });

        levelsFrame.getLevel8().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                int nbImages = 8;
                intent.putExtra(SIZE, nbImages);
                startActivity(intent);
            }
        });
    }
}
