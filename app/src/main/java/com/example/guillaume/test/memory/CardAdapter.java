package com.example.guillaume.test.memory;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
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
            float pixels;
            if(getWidthDeviceDp(mContext) >= 360){
                 pixels = convertDpToPixel(75, mContext);
            }else{
                pixels = convertDpToPixel(62,mContext);
            }
            card.setLayoutParams(new GridView.LayoutParams((int) pixels, (int) pixels));
            card.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else{
            card = (Card) convertView;
        }
        card.setImageResource(gameState.getImageAt(position));

        return card;
    }

    //Convertit les dp en pixel
    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    //Convertit les pixels en dp
    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }

    //Renvoie la taille de la largeur(la plus petite mesure) de l'appareil
    public static int getWidthDeviceDp(Context context){
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return Math.min((int) convertPixelsToDp(dm.widthPixels, context), (int)convertPixelsToDp(dm.heightPixels,context));
    }


}
