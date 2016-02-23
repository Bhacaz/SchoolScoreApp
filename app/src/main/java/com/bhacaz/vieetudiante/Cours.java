package com.bhacaz.vieetudiante;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by bhacaz on 14-12-23.
 */
public class Cours implements Serializable {
    private ArrayList<Examen> examenArrayList = new ArrayList<Examen>();
    private Double moyenne = 0.0;
    private Double notePässage = 50.0;
    private String nomCours = "";
    private String numCours = "";
    private Double PointEvalue = 0.0;
    private int mCreditCoursl = 0;
    private double mResultatMin = 0.0;





    public Cours()
    {

    }

    public Cours(ArrayList<Examen> l, String nom, String num, Double n)
    {
        this.examenArrayList = l;
        this.notePässage = n;
        this.nomCours = nom;
        this.numCours = num;
        setPointEvalue();
        setMoyenne();
        setmResultatMin();
    }

    public Cours(String nom, String num, Double n)
    {
        //examenArrayList = listExamenByDefault();
        this.nomCours = nom;
        this.numCours = num;
        this.notePässage = n;
        setPointEvalue();
        setMoyenne();
        setmResultatMin();
    }

    public void recalcule()
    {
        setPointEvalue();
        setMoyenne();
        setmResultatMin();
    }


    public void setExamenArrayList(ArrayList<Examen> e)
    {
        this.examenArrayList = e;
    }

    public void setNumCours(String s)
    {
        this.numCours = s;
    }

    public void setNomCours(String s){this.nomCours = s;}

    private void setPointEvalue()
    {
        Double pt = 0.0;
        for(Examen e : examenArrayList)
        {
            if(e.getResultat() > 0)
                pt += e.getPonderation();
        }
        if(pt.isNaN())
            this.PointEvalue = 0.0;
        else
            this.PointEvalue = pt;
        //System.out.println(String.valueOf(pt) + String.valueOf(PointEvalue));
    }

    private void setMoyenne() {
        Double mo = 0.0;
        for(Examen e : examenArrayList)
        {
            if(e.getResultat() != 0)
                mo += (e.getResultat() * e.getPonderation()) / 100;
        }
        mo = (mo/PointEvalue) * 100;
        if(mo.isNaN())
            this.moyenne = 0.0;
        else
            this.moyenne = mo;
        //System.out.println(String.valueOf(mo) + String.valueOf(moyenne));
    }

    public void setNotePassage(Double notePässage) {
        this.notePässage = notePässage;
    }


    public void setmCreditCoursl(int c){this.mCreditCoursl = c;}

    private void setmResultatMin()
    {
        Double pointCum = (moyenne * PointEvalue) / 100;
        pointCum = ((notePässage - pointCum) / (100 - PointEvalue)) * 100;

        if(pointCum.isNaN() || pointCum.isInfinite())
            this.mResultatMin = 0.0;
        else
            this.mResultatMin = pointCum;
        //System.out.println(String.valueOf(pointCum) + String.valueOf(mResultatMin));

    }


    public ArrayList<Examen> getListElement() {
        return examenArrayList;
    }

    public Double getNotePassage() {
        return notePässage;
    }

    public Double getMoyenne() {
        return moyenne;
    }

    public String getNumCours(){return numCours;}

    public String getNomCours(){return nomCours;}

    public Double getPointEvalue(){return PointEvalue;}


    public int getmCreditCoursl(){return mCreditCoursl;}

    public double getmResultatMin(){return mResultatMin;}


    public boolean ajouterElement(Examen e)
    {
        if(e instanceof Examen)
            return this.examenArrayList.add(e);
        else
            return false;
    }



    private ArrayList<Examen> listExamenByDefault()
    {
        ArrayList<Examen> examenArrayListDefault = new ArrayList<Examen>();

        examenArrayListDefault.add(new Examen("Examen 1", 35.0, 87.4));
        examenArrayListDefault.add(new Examen("TP1", 10.0, 64.5));
        examenArrayListDefault.add(new Examen("Examen 2", 40.0 ,46.2));
        return examenArrayListDefault;
    }
}
