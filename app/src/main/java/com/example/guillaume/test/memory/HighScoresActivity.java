package com.example.guillaume.test.memory;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

//Activité dans laquelle on voit les highscore pour toutes les catégories
public class HighScoresActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        //On trouve les éléments graphiques
        TextView score2 = (TextView)findViewById(R.id.high_scores_2_content);
        TextView score3 = (TextView)findViewById(R.id.high_scores_3_content);
        TextView score4 = (TextView)findViewById(R.id.high_scores_4_content);
        TextView score5 = (TextView)findViewById(R.id.high_scores_5_content);
        TextView score6 = (TextView)findViewById(R.id.high_scores_6_content);
        TextView score7 = (TextView)findViewById(R.id.high_scores_7_content);
        TextView score8 = (TextView)findViewById(R.id.high_scores_8_content);

        //On obtient les highscore depuis le fichier et on les formate façon mm:ss
        int[] storedScores = FileManaging.getHighScores(this);
        String[] formatedScores = {"","","","","","",""};
        for(int i = 0;i < 7;i++) {
            formatedScores[i] = formatTheScore(storedScores[i]);
        }

        //On inscrit les scores dans l'UI.
        score2.setText(formatedScores[0]);
        score3.setText(formatedScores[1]);
        score4.setText(formatedScores[2]);
        score5.setText(formatedScores[3]);
        score6.setText(formatedScores[4]);
        score7.setText(formatedScores[5]);
        score8.setText(formatedScores[6]);
    }

    //Fonction de formatage des scores. Utilisée aussi dans FileManaging d'ou le "public"
    public static String formatTheScore(int i){
        String result = "";
        if(i == -1){
            result = "No score";
        }else {
            result = String.format("%02d", i / 3600) + ":" + String.format("%02d", (i % 3600) / 60) + ":" + String.format("%02d", (i % 3600) % 60);
        }
        return result;
    }
}
