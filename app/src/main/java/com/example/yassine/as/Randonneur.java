package com.example.yassine.as;

import java.util.Date;

/**
 * Created by yassine on 03/02/2017.
 */

public class Randonneur {
    private String code;
    private String nompart;
    private String emp;
    private String distance;


    public Randonneur(){}

    public Randonneur(String code, String nompart, String emp,String distance) {
        this.code = code;
        this.nompart = nompart;
        this.emp = emp;
        this.distance = distance;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNompart() {
        return nompart;
    }

    public void setNompart(String nompart) {
        this.nompart = nompart;
    }

    public String getEmp() {
        return emp;
    }

    public void setEmp(String emp) {
        this.emp = emp;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
