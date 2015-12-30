package com.example.guillaume.test.memory;

import android.content.Context;
import android.media.Image;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by Guillaume on 28/12/2015.
 */

//Element graphique personalisé qui contient l'image à matcher mais aussi un écran bleu qui la rends "cachée" quand elle n'est pas sélectionnée ou révélée.
public class Card extends RelativeLayout {

    //L'inflateur utilisé pour les constructeurs et les éléments graphiques
    private LayoutInflater inflater;
    private ImageView image;
    private ImageView back;

    //Les trois constructeurs nécessaires selon la version de l'OS de l'utilisateur
    public Card(Context context) {
        super(context);
        inflater = LayoutInflater.from(context);
        init();

    }

    public Card(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflater = LayoutInflater.from(context);
        init();
    }

    public Card(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflater = LayoutInflater.from(context);
        init();
    }

    //Inflate l'inflater et trouve les éléments graphiques pour pouvoir les utiliser
    private void init(){
        inflater.inflate(R.layout.viewpart_card, this, true);
        image = (ImageView)findViewById(R.id.card_image);
        back = (ImageView)findViewById(R.id.card_back_image);
    }

    //Utilisée pour mettre l'image souhaitée
    public void setImageResource(Integer resource){
        if(image!=null) {
            image.setImageResource(resource);
        }
    }

    //Utilisée pour choisir le type de Scaling de l'image. Les images des chiens que j'ai obtenues du
    //site dévelopeur Android ne sont pas toutes carrées et doivent être Scalées.
    public void setScaleType(ImageView.ScaleType scaleType){
        if(image!=null) {
            image.setScaleType(scaleType);
        }
    }

    //Fonction pour révéler la carte. On peut aussi mettre une animation ici.
    public void reveal(){
        back.setVisibility(GONE);
    }

    //Fonction pour cacher la carte.
    public void hide(){
        back.setVisibility(VISIBLE);
    }
}
