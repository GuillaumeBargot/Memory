package com.example.guillaume.test.memory;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.CharacterPickerDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.guillaume.test.memory.UIParts.MenuButton;
import com.example.guillaume.test.memory.UIParts.MenuFrame;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.CharBuffer;

//Le menu principal

public class MainActivity extends AppCompatActivity {

    //Le bouton pour continuer une partie
    //private Button continueButton;
    private MenuFrame menuFrame;
    //Utilisé pour passer des données à GameActivity pour lui indiquer qu'on souhaite continuer une partie
    public static final String CONTINUE = "com.example.guillaume.test.memory.CONTINUE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Check si le fichier existe, sinon le créé avec des valeurs nulles
        if(!FileManaging.checkIfAlreadyFile(this,"data.txt")){
            FileManaging.createBasicFile(this);
        }

        menuFrame = (MenuFrame)findViewById(R.id.main_menu_frame);
        menuFrame.getContinueButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                String message = "continue";
                intent.putExtra(CONTINUE, message);
                startActivity(intent);
            }
        });
        menuFrame.getPlayButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewGameActivity.class);
                startActivity(intent);
            }
        });
        menuFrame.getStatsButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HighScoresActivity.class);
                startActivity(intent);
            }
        });

        //On teste si il existe une sauvegarde. Si oui, on affiche l'option de continuer.
        if(FileManaging.checkIfCurrentGame(this)){
            menuFrame.setContinuePossible(true);
        } else {
            menuFrame.setContinuePossible(false);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(FileManaging.checkIfCurrentGame(this)){
            menuFrame.setContinuePossible(true);
        } else {
            menuFrame.setContinuePossible(false);
        }
    }
}
