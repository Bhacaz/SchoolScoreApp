package com.bhacaz.vieetudiante;

import java.io.Serializable;

/**
 * Created by bhacaz on 14-12-23.
 */
public class Examen implements Serializable {
    double resultatEx = 0.0;
    double ponderationEx;
//    double ex_moyenne
    String nomEx = "";
    String categorie = "";

    public Examen()
    {

    }

    public Examen(double r, double p)
    {
        this.resultatEx = r;
        this.ponderationEx = p;
    }

    public Examen(String n)
    {
        this.nomEx = n;
    }

    public Examen(double p)
    {
        this.ponderationEx = p;
    }

    public Examen(String n, double p)
    {
        this.nomEx = n;
        this.ponderationEx = p;
    }


    public Examen(String n, double p, double r)
    {
        this.nomEx = n;
        this.resultatEx = r;
        this.ponderationEx =p;
    }

    public Examen(String n, double p, double r, String c)
    {
        this.nomEx = n;
        this.resultatEx = r;
        this.ponderationEx =p;
        this.categorie =c;
    }

    public Examen(String n, double p, String c)
    {
        this.nomEx = n;
        this.ponderationEx =p;
        this.categorie =c;
    }

    public double getResultat() {
        return resultatEx;
    }
    public String getNomCours(){return nomEx;}
    public  double getPonderation(){return this.ponderationEx;}
    public String getCategorie(){return this.categorie;}


    public void setResultat(double resultat) {
        this.resultatEx = resultat;
    }
    public void setNom(String n){this.nomEx = n;}
    public  void setPonderation(double p){this.ponderationEx = p;}
    public void setCategorie(String c){this.categorie = c;}
}
