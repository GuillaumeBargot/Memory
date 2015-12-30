package com.example.guillaume.test.memory;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by Guillaume on 28/12/2015.
 */
public class CardAdapter extends BaseAdapter {

    //Données utilisées pour créer l'adapteur
    private Context mContext;
    private GameState gameState;

    //Le constructeur
    public CardAdapter(Context c, GameState gameState) {
        mContext = c;
        this.gameState = gameState;
    }

    //Ce sont des fonctions obligatoires quand on créé un adapter. Je ne les utilise pas
    //par habitude.
    public int getCount() {
        return gameState.getNbImages()*2;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    //Utilisée pour créer la vue d'une case en particulier.
    //On utilise le widget que j'ai créé qui s'appelle Card.
    public View getView(int position, View convertView, ViewGroup parent){
        Card card;

        if(convertView == null){
            card = new Card(mContext);
            card.setLayoutParams(new GridView.LayoutParams(200, 200));
            card.setScaleType(ImageView.ScaleType.CENTER_CROP);
            card.setPadding(8, 8, 8, 8);
        } else{
            card = (Card) convertView;
        }
        card.setImageResource(gameState.getImageAt(position));
        return card;
    }


}
