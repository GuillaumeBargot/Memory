package com.example.guillaume.test.memory;

import android.content.Intent;
import android.support.annotation.IntegerRes;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Guillaume on 28/12/2015.
 */
public class GameState {

    //Le nombre d'images (indique la difficulté) de la partie.
    private int nbImages;

    //Le timer propre au GameState. Il n'est updaté que quand on a besoin de sauvegarder la partie ou quand
    //on charge une partie à partir d'un GameState.
    private int timerMs;

    //La liste d'images des cartes du jeu qui sont rangées dans l'ordre ou elles seront affichées.
    private Integer[] images;

    //L'état des cartes, si elles ont été trouvées ou non.
    private boolean[] revelees;

    //Variable qui permet de stocker la carte que l'on choisit en premier avant de la comparer avec une deuxième.
    private int firstClickedPosition=-1;

    //Les adresses resources des images disponibles. Le jeu est scalable si on a plus de 8 photos.
    private static Integer[] availableImages = {R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,
            R.drawable.sample_0, R.drawable.sample_1};

    //Constructeur pour nouvelle partie, on ne regarde que le nombre d'images
    public GameState(int nombreImages){
        //On créé deux tableaux avec nombreImagesx2 cases
        nbImages = nombreImages;
        images = new Integer[nbImages*2];
        revelees = new boolean[nbImages*2];

        //On remplit le tableau revelees avec que des false pour l'instant.
        for(int i=0;i<revelees.length;i++){
            revelees[i] = false;
        }

        //Fill the images with randomly generated images
        //For that, create an arrayList with twice each image:
        //On remplit celui des images avec des images générées aléatoirement.
        //Pour cela, on créé une ArrayList avec deux fois chaque image
        ArrayList<Integer> imageArray = new ArrayList<Integer>();
        for(int j=0;j<nbImages;j++){
            imageArray.add(availableImages[j]);
            imageArray.add(availableImages[j]);
        }

        //Maintenant on remplit le tableau images avec les images de l'arrayList récemment créée, et ce
        //de manière aléatoire.
        for(int k = 0;k<images.length;k++) {
            Integer randomImage = imageArray.get(new Random().nextInt(imageArray.size()));
            images[k] = randomImage;
            imageArray.remove(randomImage);
        }

    }

    //Constructeur utilisé quand l'utilisateur à changé l'orientation de son écran.
    //Il est différent du suivant car la Bundle des onSaveInstanceState n'accepte que des array list pour les Integer.
    public GameState(int nombreImages, ArrayList<Integer> imagesSaved, boolean[] reveleesSaved, int time){
        nbImages = nombreImages;
        images = new Integer[nbImages*2];
        revelees = new boolean[nbImages*2];
        for(int i = 0;i<reveleesSaved.length;i++){
            images[i] = imagesSaved.get(i);
        }
        revelees = reveleesSaved;
        timerMs = time*1000;
    }

    //Même que précedement mais pour quand l'utilisateur charge une partie précedente.
    public GameState(int nombreImages, Integer[] imagesSaved, boolean[] reveleesSaved, int time){
        nbImages = nombreImages;
        images = imagesSaved;
        revelees = reveleesSaved;
        timerMs = time*1000;
    }

    //Renvoie l'adresse resource de l'image à la position donnée.
    public Integer getImageAt(int position){
        return images[position];
    }

    //Renvoie le nombre d'images
    public int getNbImages(){
        return nbImages;
    }

    //Renvoie la position de la firstClickedCard
    public int getFirstClickedPosition(){
        return firstClickedPosition;
    }

    //Set la position de la firstClickedCard
    public void setFirstClickedPosition(int position){
        firstClickedPosition = position;
    }

    //Check si la carte à la position donnée est déjà retournée (càd si c'est la firstClickedCard ou si elle
    //fait partie d'une paire déjà trouvée)
    public boolean isAlreadyRevealed(int position){
        if(firstClickedPosition!=-1){
            if(firstClickedPosition == position){
                return true;
            } else{
                return (revelees[position]);
            }
        } else {
            return (revelees[position]);
        }
    }

    //Check si la carte à la position donnée et la firstClickedCard sont une paire.
    //Renvoie un boolean et met "true" dans le tableau "revelees" pour les deux cartes si
    //elles sont bonnes.
    public boolean checkForMatch(int position){
        boolean result = false;
        if(firstClickedPosition != -1){
            if(images[firstClickedPosition].equals(images[position])){
                //GOOD!
                revelees[position] = true;
                revelees[firstClickedPosition] = true;
                result = true;

            }else{
                //BAD!
                revelees[firstClickedPosition] = false;
                result = false;
            }
            firstClickedPosition=-1;
        }
        else{
            Log.d("GameState", "Problème: pas de firstClickedCard");
        }
        return result;
    }

    //Check si l'utilisateur a fini la partie
    public boolean hasFinished(){
        boolean result = true;
        for(int i = 0;i<revelees.length;i++){
            if(!revelees[i]){
                result=false;
            }
        }
        return result;
    }

    //Renvoie l'arrayList des adresse resource des images de cette partie
    public ArrayList<Integer> getImages(){
        ArrayList<Integer> result = new ArrayList<Integer>();
        for(int i=0;i<images.length;i++){
            result.add(images[i]);
        }
        return result;
    }

    //Renvoie l'état des cartes
    public boolean[] getRevelees(){
        return revelees;
    }

    //Renvoie le timer
    public int getTimer(){
        return timerMs;
    }
}
