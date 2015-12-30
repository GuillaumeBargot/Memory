package com.example.guillaume.test.memory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NewGameActivity extends AppCompatActivity {

    //Utilisée pour passer le nombre d'images à la GameActivity
    public static final String SIZE = "com.example.guillaume.test.memory.SIZE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        //On trouve les boutons
        Button button2 = (Button)findViewById(R.id.new_game_button_2);
        Button button3 = (Button)findViewById(R.id.new_game_button_3);
        Button button4 = (Button)findViewById(R.id.new_game_button_4);
        Button button5 = (Button)findViewById(R.id.new_game_button_5);
        Button button6 = (Button)findViewById(R.id.new_game_button_6);
        Button button7 = (Button)findViewById(R.id.new_game_button_7);
        Button button8 = (Button)findViewById(R.id.new_game_button_8);

        //Et on créé leurs onClickListener
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                int nbImages = 2;
                intent.putExtra(SIZE, nbImages);
                startActivity(intent);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                int nbImages = 3;
                intent.putExtra(SIZE, nbImages);
                startActivity(intent);
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                int nbImages = 4;
                intent.putExtra(SIZE, nbImages);
                startActivity(intent);
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                int nbImages = 5;
                intent.putExtra(SIZE, nbImages);
                startActivity(intent);
            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                int nbImages = 6;
                intent.putExtra(SIZE, nbImages);
                startActivity(intent);
            }
        });

        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                int nbImages = 7;
                intent.putExtra(SIZE, nbImages);
                startActivity(intent);
            }
        });

        button8.setOnClickListener(new View.OnClickListener() {
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
