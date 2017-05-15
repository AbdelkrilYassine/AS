package com.example.yassine.as;

/**
 * Created by yassine on 19/04/2017.
 */

public class Here {
    String name;
    String p;

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    String img;

    public Here() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Here(String name,String p, String img) {

        this.name = name;
        this.p=p;
        this.img = img;
    }
}
