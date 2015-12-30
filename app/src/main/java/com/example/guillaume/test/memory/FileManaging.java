package com.example.guillaume.test.memory;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by Guillaume on 29/12/2015.
 */
public class FileManaging {

    //Les fonctions qui ont à voir avec les sauvegardes dans le stockage interne ("internal storage" selon developers.android)
    //Le fichier créé pour effectuer ces opération est dans un storage privé à l'application et est détruit quand l'application est désinstallée

    //Ecrit dans le fichier data.txt
    public static void writeToFile(String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("data.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    //Lit le ficher data.txt
    public static String readFromFile(Context context) {

        String ret = "";
        try {
            InputStream inputStream = context.openFileInput("data.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    //Utilisé pour transformer le contenant du fichier en JSON.
    public static JSONArray getJSONFromString(String data){
        JSONArray jarray = null;
        try {
            jarray = new JSONArray(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jarray;
    }

    //Renvoie true si le high score a été battu et update le fichier avec le nouveau record.
    //Si c'est au début de l'utilisation de l'application, on check aussi si les records sont nuls
    public static boolean checkAndPutNewHighScore(int nbImages, int seconds, Context context){
        boolean result = false;
        String content = FileManaging.readFromFile(context);
        JSONArray mainArray = getJSONFromString(content);
        try {
            JSONObject highScoreObject = mainArray.getJSONObject(0);
            int storedScore = highScoreObject.getInt(getKeyForNbImages(nbImages));
            if(storedScore == -1 || storedScore > seconds){ //WIN
                result = true;
                //Met le nouveau socre dans le highScoreObject
                highScoreObject.put(getKeyForNbImages(nbImages),seconds);
                //Met le highScoreObject dans la mainArray
                mainArray.put(0,highScoreObject);
                //Réécrit le fichier
                writeToFile(mainArray.toString(), context);
            }else{ //LOSS
                //On ne fait rien, le booléen va être faux.
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return result;
    }

    //Renvoie la clé JSON associé avec le nombre ("2images" pour 2)
    private static String getKeyForNbImages(int nbImages){
        return nbImages + "images";
    }

    //Check si le fichier data.txt existe ou non
    public static boolean checkIfAlreadyFile(Context context, String name){
        File file = context.getFileStreamPath(name);
        return file.exists();
    }

    //Créé un ficher vide pour la première utilisation de l'application
    public static void createBasicFile(Context context){
        writeToFile("[{\"2images\":-1, \"3images\":-1, \"4images\":-1, \"5images\":-1, \"6images\":-1, \"7images\":-1, \"8images\":-1}]", context);
        //The second part when a game is present: {"nbImages":4,"images":[0,0,1,1,2,2,3,3],"revelees":[false,false,true,true,false,false,false,false],"time":3600}
    }

    //Check si il y a une sauvegarde de jeu dans le fichier.
    public static boolean checkIfCurrentGame(Context context){
        boolean result = false;
        String content = FileManaging.readFromFile(context);
        JSONArray mainArray = getJSONFromString(content);
        try {
            if(mainArray.getJSONObject(1)!=null){
                result = true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    //Renvoie les high scores dans un tableau d'entier
    public static int[] getHighScores(Context context){
        int[] result = {0,0,0,0,0,0,0};
        String content = FileManaging.readFromFile(context);
        JSONArray mainArray = getJSONFromString(content);
        try {
            JSONObject highScoreObject = mainArray.getJSONObject(0);
            for(int i=2;i<=8;i++){
                result[i-2] = highScoreObject.getInt(getKeyForNbImages(i));
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return result;
    }

    //Renvoie la partie sauvegardée dans le fichier sous forme de GameState
    public static GameState getGameStateFromFile(Context context){
        if(checkIfCurrentGame(context)) {
            GameState result = null;
            String content = FileManaging.readFromFile(context);
            JSONArray mainArray = getJSONFromString(content);
            try {
                JSONObject currentGameObject = mainArray.getJSONObject(1);
                int nbImages = currentGameObject.getInt("nbImages");
                Integer[] images = convertImagesIntToIntegers(currentGameObject.getJSONArray("images"));
                boolean[] revelees = convertJSONToBooleanArray(currentGameObject.getJSONArray("revelees"));
                int timer = currentGameObject.getInt("time");
                result = new GameState(nbImages,images,revelees,timer);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            return result;
        }else{
            return null;
        }
    }

    //Convertis les numéros d'images en adresse de "res" Android (sous forme d'Integer)
    private static Integer[] convertImagesIntToIntegers(JSONArray array){
        Integer[] result = new Integer[array.length()];

        for(int i = 0; i < array.length();i++){
            int stored = -1;
            try {
                stored = array.getInt(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(stored>-1 && stored < 8){
                result[i]=convertSingleImageToInteger(stored);
            }
        }

        return result;
    }

    //Même fonction mais pour un seul entier. Est utilisée dans la fonction précedente.
    private static Integer convertSingleImageToInteger(int i){
        Integer result = null;
        switch (i){
            case 0:
                result = R.drawable.sample_0;
                break;
            case 1:
                result = R.drawable.sample_1;
                break;
            case 2:
                result = R.drawable.sample_2;
                break;
            case 3:
                result = R.drawable.sample_3;
                break;
            case 4:
                result = R.drawable.sample_4;
                break;
            case 5:
                result = R.drawable.sample_5;
                break;
            case 6:
                result = R.drawable.sample_6;
                break;
            case 7:
                result = R.drawable.sample_7;
                break;
        }
        return result;
    }

    //Conversion entre les array JSON dans le fichier et les arrays que l'on veut pour le GameState
    public static boolean[] convertJSONToBooleanArray(JSONArray array){
        boolean[] result = new boolean[array.length()];

        for(int i = 0; i < array.length() ; i++){
            try {
                result[i] = array.getBoolean(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    //Sauvegarder la partie dans le fichier. On utilise le GameState et le timer de l'UI.
    public static void saveCurrentGame(Context context, GameState gameState, int timer){
        String content = FileManaging.readFromFile(context);
        JSONArray mainArray = getJSONFromString(content);
        JSONObject currentGameObject = new JSONObject();
        try {
            currentGameObject.put("nbImages", gameState.getNbImages());
            JSONArray imagesArray = getJSONImagesFromArray(gameState.getImages());
            currentGameObject.put("images",imagesArray);
            JSONArray booleanArray = getJSONReveleesFromArray(gameState.getRevelees());
            currentGameObject.put("revelees",booleanArray);
            currentGameObject.put("time",timer);
            mainArray.put(1,currentGameObject);
            writeToFile(mainArray.toString(), context);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Trois fonctions qui permettent de jongler entre les données utilisées dans le fichier et celles utilisées dans le GameState
    private static JSONArray getJSONImagesFromArray(ArrayList<Integer> array){
        JSONArray result = new JSONArray();
        for(Integer i : array){
            result.put(convertSingleIntegerToInt(i));
        }
        return result;
    }

    private static Integer convertSingleIntegerToInt(Integer i){
        int result = 0;
        switch (i){
            case R.drawable.sample_0:
                result = 0;
                break;
            case R.drawable.sample_1:
                result = 1;
                break;
            case R.drawable.sample_2:
                result = 2;
                break;
            case R.drawable.sample_3:
                result = 3;
                break;
            case R.drawable.sample_4:
                result = 4;
                break;
            case R.drawable.sample_5:
                result = 5;
                break;
            case R.drawable.sample_6:
                result = 6;
                break;
            case R.drawable.sample_7:
                result = 7;
                break;
        }
        return result;
    }

    private static JSONArray getJSONReveleesFromArray(boolean[] array){
        JSONArray result = new JSONArray();
        for(boolean i : array){
            result.put(i);
        }
        return result;
    }

    //Supprime la sauvegarde de jeu dans le fichier.
    public static void removeCurrentGame(Context context){
        String content = FileManaging.readFromFile(context);
        JSONArray mainArray = getJSONFromString(content);
        JSONArray newArray = new JSONArray();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mainArray.remove(1);
            newArray = mainArray;
        }else{
            try {
                newArray.put(mainArray.get(0));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        writeToFile(newArray.toString(), context);
    }

    //Récupère une version formatée mm:ss du highscore pour une certaine catégorie
    public static String getFormatedHighScoreForNb(Context context, int nbImages){
        int[] table = getHighScores(context);
        int highScore = table[nbImages-2];
        return HighScoresActivity.formatTheScore(highScore);
    }
}
