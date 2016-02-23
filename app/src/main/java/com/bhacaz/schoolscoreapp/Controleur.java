package com.bhacaz.schoolscoreapp;

import com.bhacaz.vieetudiante.*;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by bhacaz on 15-05-04.
 */
public class Controleur implements Serializable{

    ArrayList<Cours> coursArrayList = new ArrayList<Cours>();

    public Controleur()
    {

    }

    public Controleur(ArrayList<Cours> l)
    {
        this.coursArrayList = l;
    }

    public void setCoursArrayList(ArrayList<Cours> l){this.coursArrayList = l;}

    public ArrayList<Cours> getCoursArrayList(){return this.coursArrayList;}

    public void addCours(Cours c){coursArrayList.add(c);}
    public Cours getCours(int i) {return coursArrayList.get(i);}

    public void update()
    {
        for(Cours c :coursArrayList)
            c.recalcule();
    }
}
