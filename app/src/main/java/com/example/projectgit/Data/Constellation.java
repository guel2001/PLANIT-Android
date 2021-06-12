package com.example.projectgit.Data;

public class Constellation {
   // public String text;
    // public String img_url;
    int image;
    String title;
    String content;
    int cnum;
    int[] sx;
    int[] sy;
    int scount;

    String Cons_code;

    public String getCons_code() {
        return Cons_code;
    }

    public void setCons_code(String cons_code) {
        Cons_code = cons_code;
    }

    public Constellation(int image, String title, String content,String cons_code) {
        this.image = image;
        this.title = title;
        this.content = content;
        this.Cons_code = cons_code;
    }


    public Constellation(int cnum, int[] sx, int[] sy, int scount) {
        this.cnum = cnum;
        this.sx = sx;
        this.sy = sy;
        this.scount = scount;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        if (content.equals("")) content = "";
        this.content = content;
    }

    public int getCnum() {
        return cnum;
    }

    public void setCnum(int cnum) {
        this.cnum = cnum;
    }

    public int[] getSx() {
        return sx;
    }

    public void setSx(int[] sx) {
        this.sx = sx;
    }

    public int[] getSy() {
        return sy;
    }

    public void setSy(int[] sy) {
        this.sy = sy;
    }

    public int getScount() {
        return scount;
    }

    public void setScount(int scount) {
        this.scount = scount;
    }



}
