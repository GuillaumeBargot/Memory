package com.example.guillaume.test.memory;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.CharacterPickerDialog;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.guillaume.test.memory.UIParts.LevelsButton;
import com.example.guillaume.test.memory.UIParts.MenuButton;
import com.example.guillaume.test.memory.UIParts.RecapFrame;

import java.io.File;
import java.util.logging.Level;

public class GameActivity extends AppCompatActivity {

    //Les élements graphiques
    private Chronometer chronometer;
    private LevelsButton pauseButton;
    private RelativeLayout greyScreen;
    private RelativeLayout winScreen;
    private LevelsButton continueButton;
    private RecapFrame recapFrame;
    private GridView gridView;
    private TextView highScore;

    //Le timer du côté de l'UI : quand on arrêtte le jeu pour n'importe quelle raison, le chronomètre s'arrêtte et le temps est stocké ici.
    private int timer = 0;

    //Pour savoir si le jeu est en pause ou non.
    private boolean isPaused = true;

    //Pour savoir si l'UI est lock ou non. (quand deux cartes différentes doivent attendre 1s avant de redevenir bleue)
    private boolean isLocked = false;

    //Pour savoir si la partie est gagnée. Si oui, on ne sauvegarde pas la partie quand l'utilisateur quitte l'activité
    private boolean isWon = false;

    //L'objet GameState qui stocke les positions des images, les états de ces images, le nombre total d'images et un timer en relation avec le stockage interne.
    private GameState gameState;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //Cette condition est pour quand l'utilisateur change l'orientation de son écran. L'actitivé revient en mode onCreate
        //et on doit s'occuper de récupérer le jeu : le timer et le GameState on été changé dans la fonction onSaveInstanceState qui apparait plus bas.
        if (savedInstanceState != null) {
            timer = savedInstanceState.getInt("timer");
            gameState = new GameState(savedInstanceState.getInt("nbImages"),savedInstanceState.getIntegerArrayList("images"),savedInstanceState.getBooleanArray("revelees"),timer);
            isWon = savedInstanceState.getBoolean("isWon");
        } else {
            //Si l'activité vient d'autre part qu'un changement d'orientation d'écran, on regarde d'où l'utilisateur vient:
            //-de l'activité Nouvelle Partie
            //-du bouton "Continuer" dans le menu principal
            Intent intent = getIntent();
            Intent intentMain = new Intent(getApplicationContext(), MainActivity.class);
            if(intent.getStringExtra(MainActivity.CONTINUE)!=null){
                //Si l'utilisateur vient du bouton "Continuer" de l'activité principale,
                //on charge le GameState du "internal storage". Le GameState contient le timer
                //qu'on a sauvegardé donc on doit mettre à jour celui de l'UI
                gameState = FileManaging.getGameStateFromFile(this);
                if(gameState!=null){
                    timer = gameState.getTimer();
                }else{
                    startActivity(intentMain);
                    finish();
                }

            } else if (intent.getIntExtra(NewGameActivity.SIZE,0)!=0){
                //Si l'utilisateur vient de l'activité de Nouvelle Partie, on créé un GameState avec
                //le nombre choisi d'images.
                gameState = new GameState(intent.getIntExtra(NewGameActivity.SIZE,0));
            } else {
                //Si l'utilisateur ne vient d'aucune de ces positions, on revient à l'activité Nouvelle Partie.
                startActivity(intentMain);
                finish();
            }
        }

        //On montre le meilleur score actuel pour cette catégorie
        highScore = (TextView)findViewById(R.id.game_text_high_score);
        String highScoreText = getResources().getString(R.string.best_time) + "  " + FileManaging.getFormatedHighScoreForNb(this,gameState.getNbImages());
        SpannableString spannableString = new SpannableString(highScoreText);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, 9, 0);
        highScore.setText(spannableString);


        //Trouver les parties de l'UI
        greyScreen = (RelativeLayout)findViewById(R.id.game_grey_screen);
        winScreen = (RelativeLayout)findViewById(R.id.game_win_screen);
        continueButton = (LevelsButton)findViewById(R.id.game_button_resume);
        gridView = (GridView)findViewById(R.id.game_gridview);
        chronometer = (Chronometer)findViewById(R.id.game_chronometer);
        pauseButton = (LevelsButton)findViewById(R.id.game_button_pause);
        recapFrame = (RecapFrame)findViewById(R.id.game_recap_frame);

        //S'occupe de ce qui arrive après que l'utilisateur appuie sur le bouton "Play" (visible uniquement quand le jeu est en pause)
        continueButton.setImageButton(R.drawable.ic_play_arrow_white_24dp);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPaused){
                    startTheChronometer();
                }
            }
        });

        //S'occupe de ce qui arrive quand l'utilisateur appuie sur "Nouvelle partie"
        MenuButton winButton = recapFrame.getButton();
        winButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //We link the gridView to the custom CardAdapter.
        //On lie la gridView au CardAdapter custom.
        gridView.setAdapter(new CardAdapter(this, gameState));


        //This is where we handle what happens when the user clicks on the cards:
        //If the game isn't paused and if the UI isn't locked because two cards have wait 1s and hide
        //Then if the card selected isn't already revealed
        //On s'occupe de ce qu'il se passe quand l'utilisateur clique sur n'importe laquelle des cartes:
        //Si le jeu n'est pas en pause et que l'UI n'est pas bloquée pour cause des deux cartes qui attendent 1s avant de se retourner,
        //Alors si la carte cliquée n'est pas déjà révélée,
        //Si une "firstClickedCard" alors on la met face visible et on attends le prochain click de l'utilisateur
        //Sinon, alors on compare celle que l'utilisateur vient de choisir avec la "firstClickedCard" et on décide de la suite:
        //Si les cartes sont identiques, alors leur état passe à "true" dans le GameState et elles restent face visible,
        //si c'était les deux dernières cartes, on finit la partie
        //sinon, on ne fait rien d'autre.
        //Si les cartes ne sont pas identiques, on les retourne face cachées et rien d'autre ne se passe.
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Card clickedCard = (Card)view;
                if(!isPaused) {
                    if(!isLocked){
                        if (gameState.isAlreadyRevealed(position)) {
                            //Ne rien faire
                        } else {
                            final int localFirstClickedCard = gameState.getFirstClickedPosition();
                            if (localFirstClickedCard == -1) {
                                //Il n'y a pas de carte à comparer
                                gameState.setFirstClickedPosition(position);
                                clickedCard.reveal();
                            } else {
                                //Il y a une carte à comparer, on montre la carte sélectionnée et on compare
                                clickedCard.reveal();
                                boolean isMatched = gameState.checkForMatch(position);
                                if (isMatched) {
                                    //Les deux sont identiques, on ne retourne aucune des deux cartes.
                                    //On regarde si c'est la fin de la partie.
                                    if (gameState.hasFinished()) {
                                        endGame();

                                    }
                                } else {
                                    //On laisse les cartes visibles 1s et on les retourne pour que l'utilisateur
                                    //puisse voir ce qu'il a retourné. On lock l'UI pour pas que l'utilisateur puisse
                                    //tout retourner à peu près en même temps si il est assez rapide et le "firstClickedCard" et le reste
                                    //de l'algorithme est faussé.
                                    isLocked = true;
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        public void run() {
                                            clickedCard.hide();
                                            Card otherCard = (Card) (gridView.getChildAt(localFirstClickedCard));
                                            otherCard.hide();
                                            isLocked = false;
                                        }
                                    }, 1000);

                                }
                            }
                        }
                    }
                }
            }
        });



        //On s'occupe de ce qu'il se passe quand l'utilisateur appuie sur le bouton pause en bas à droite.
        //Si c'est l'écran de Win et qu'on a changé d'orientation, il faut empêcher l'action sur le bouton.
        pauseButton.setImageButton(R.drawable.ic_pause_white_24dp);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isWon && !isPaused) {
                    stopTheChronometer();
                }
            }
        });


        //Par défaut, quand l'utilisateur revient à onCreate, c'est une pause, on veut donc que l'écran gris de pause soit visible.
        //Si c'est une win, on doit récupérer le score et le highscore pour l'afficher vu que l'utilisateur vient de tourner l'écran sur un WinScreen.
        //On utilise ce if pour éviter de retourner à une pause si la partie est gagnée et que l'utilisateur tourne l'écran quand même.
        if(isWon){
            winScreen.setVisibility(View.VISIBLE);
            recapFrame.setBesttime(FileManaging.getFormatedHighScoreForNb(this,gameState.getNbImages()));
            recapFrame.setTime(HighScoresActivity.formatTheScore(timer/1000));
        }else {
            greyScreen.setVisibility(View.VISIBLE);
        }

        //Le timer doit être mis à jour: soit c'est le début d'une partie et il sera à 0 soit on vient d'une des autres possibles voies et on actualise le chronomètre
        chronometer.setBase(SystemClock.elapsedRealtime() - timer);
    }

    //Le onPause arrive quand l'utilisateur interromp l'utilisation de l'activité, que ce soit en quittant l'activité, en répondant à un appel, etc.
    //Si la partie n'était pas en pause, on veut la mettre en pause, afin que l'utilisateur ne perde pas de temps.
    //C'est aussi ici qu'on sauvegarde l'état de la partie sur le stockage interne. Cela permet de sauvegarder plus ou moins souvent et aussi d'avoir sauvegardé
    //Si l'utilisateur décide de "kill" l'application dans sa liste d'application en cours d'utilisation
    @Override
    protected void onPause() {
        super.onPause();
        if(!isPaused) {
            stopTheChronometer();
        }
        if(!isWon){
            FileManaging.saveCurrentGame(this, gameState, timer / 1000);
        }
    }

    //C'est ici qu'on sauvegarde l'instance quand l'utlisateur change l'orientation de son téléphone.
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("timer", timer);
        outState.putInt("nbImages", gameState.getNbImages());
        outState.putIntegerArrayList("images", gameState.getImages());
        outState.putBooleanArray("revelees", gameState.getRevelees());
        outState.putBoolean("isWon", isWon);

        super.onSaveInstanceState(outState);
    }


    //Utilisée pour arrêtter le temps: on affiche l'écran de pause, on calcule le nombre de ms qu'indique le texte de l'objet Chronomètre.
    private void stopTheChronometer(){
        int stoppedMilliseconds = 0;

        chronometer.stop();
        isPaused = true;
        greyScreen.setVisibility(View.VISIBLE);
        String timerArray[] = chronometer.getText().toString().split(":");
        if(timerArray.length == 2){ //Si le timer est en dessous d'une heure. On laisse la possibilité que ça dure plus longtemps avec le prochain if.
            stoppedMilliseconds = Integer.parseInt(timerArray[0]) * 60 * 1000 + Integer.parseInt(timerArray[1]) * 1000;
        } else if(timerArray.length == 3){
            stoppedMilliseconds = Integer.parseInt(timerArray[0]) * 3600 * 1000 + Integer.parseInt(timerArray[1]) * 60 * 1000 + Integer.parseInt(timerArray[2]) * 1000;
        }

        timer = stoppedMilliseconds;
    }

    //Utilisée pour relancer le temps. On reprends là ou on s'était arrêté, on cache l'écran de pause et réactive
    //C'est aussi ici qu'on révèle les cartes qui étaient révélées si c'est une partie que l'on a chargé et qu'on reprends:
    //C'est l'endroit le plus simple ou le faire car on sait de manière certaine que la GridView et ses composants ont été créé.
    private void startTheChronometer(){
        chronometer.setBase(SystemClock.elapsedRealtime() - timer);
        chronometer.start();
        isPaused = false;
        greyScreen.setVisibility(View.GONE);
        boolean[] localRevelees = gameState.getRevelees();
        for(int i = 0; i< localRevelees.length;i++){
            if(localRevelees[i]){
                if(gridView!=null && gridView.getChildAt(i)!=null){
                    ((Card)(gridView.getChildAt(i))).reveal();
                }else{
                    Log.d("CONTINUE", "Problem with gridView");
                }

            }
        }
    }

    //Utilisée pour arrêtter le jeu: on affiche l'écran de fin, on gère si le highscore a été battu et on supprime la sauvegarde qui était
    //dans le stockage interne.
    private void endGame(){
        stopTheChronometer();
        isWon = true;
        FileManaging.removeCurrentGame(getApplicationContext());
        boolean gagne = FileManaging.checkAndPutNewHighScore(gameState.getNbImages(), timer / 1000, this);

        //Nouvelle façon de faire le winScreen
        recapFrame.setTime(HighScoresActivity.formatTheScore(timer/1000));
        recapFrame.setBesttime(FileManaging.getFormatedHighScoreForNb(this, gameState.getNbImages()));
        //Ici on ne récupère pas le besttime depuis la TextView qui l'affiche pendant la partie car il y a possibilité que l'utilisateur vienne de battre le meilleur score, donc on va
        //le chercher directement du fichier.

        winScreen.setVisibility(View.VISIBLE);
    }
}
