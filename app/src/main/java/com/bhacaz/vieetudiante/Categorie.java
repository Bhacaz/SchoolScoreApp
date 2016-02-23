package com.bhacaz.vieetudiante;

import android.app.Application;

import java.util.ArrayList;

/**
 * Created by bhacaz on 15-05-04.
 */
public class Categorie extends Application {
    ArrayList<String> catArrayList = new ArrayList<String>();

    public Categorie()
    {
        catArrayList.add("Examen");
        catArrayList.add("Laboratoire");
        catArrayList.add("Travaux pratique");
    }

    public void addCategorie(String c)
    {
        catArrayList.add(c);
    }

    public ArrayList<String> getCategorie()
    {
        return this.catArrayList;
    }


}
