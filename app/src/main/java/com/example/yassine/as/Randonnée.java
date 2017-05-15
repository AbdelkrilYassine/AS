package com.example.yassine.as;

import java.util.Date;
import java.util.Timer;

/**
 * Created by yassine on 03/02/2017.
 */

public class Randonnée {
    private String Lieu;
    private Date date;
    private Timer Heure_départ;
    private String Lieu_départ;
    private Timer Heure_arrivée;
    private Double Distance_parcourir;
    private Timer Heure_retour;
    private int Nombre_place;

    public Randonnée() {
    }

    public String getLieu() {
        return Lieu;
    }

    public void setLieu(String lieu) {
        Lieu = lieu;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Timer getHeure_départ() {
        return Heure_départ;
    }

    public void setHeure_départ(Timer heure_départ) {
        Heure_départ = heure_départ;
    }

    public String getLieu_départ() {
        return Lieu_départ;
    }

    public void setLieu_départ(String lieu_départ) {
        Lieu_départ = lieu_départ;
    }

    public Timer getHeure_arrivée() {
        return Heure_arrivée;
    }

    public void setHeure_arrivée(Timer heure_arrivée) {
        Heure_arrivée = heure_arrivée;
    }

    public Double getDistance_parcourir() {
        return Distance_parcourir;
    }

    public void setDistance_parcourir(Double distance_parcourir) {
        Distance_parcourir = distance_parcourir;
    }

    public Timer getHeure_retour() {
        return Heure_retour;
    }

    public void setHeure_retour(Timer heure_retour) {
        Heure_retour = heure_retour;
    }

    public int getNombre_place() {
        return Nombre_place;
    }

    public void setNombre_place(int nombre_place) {
        Nombre_place = nombre_place;
    }

    public Randonnée(String lieu, Date date, Timer heure_départ, String lieu_départ, Timer heure_arrivée, Double distance_parcourir, Timer heure_retour, int nombre_place) {
        Lieu = lieu;
        this.date = date;
        Heure_départ = heure_départ;
        Lieu_départ = lieu_départ;
        Heure_arrivée = heure_arrivée;
        Distance_parcourir = distance_parcourir;
        Heure_retour = heure_retour;
        Nombre_place = nombre_place;
    }
}
