package com.example.yassine.as;

/**
 * Created by yassine on 09/05/2017.
 */

public class Photoo {
    String nom;
    String path;
    String pos;
    String usr;

    public Photoo(){}
    public Photoo(String nom, String path, String pos, String usr) {
        this.nom = nom;
        this.path = path;
        this.pos = pos;
        this.usr = usr;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getUsr() {
        return usr;
    }

    public void setUsr(String usr) {
        this.usr = usr;
    }
}
