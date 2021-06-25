package com.example.myapplication;

public class note{
    private String notice;
    private String id;
    private String image;
    private String name;
    private String photoid;
    private boolean read;
    public note(String notice,String id,String image,String name,String photoid,boolean read){
        this.notice=notice;
        this.id=id;
        this.image=image;
        this.name=name;
        this.photoid=photoid;
        this.read=read;

    }

    public String getnotice(){
        return notice;
    }

    public String getId() {
        return id;
    }
    public String getImage(){
        return image;
    }
    public String getName(){
        return name;
    }
    public String getPhoto(){
        return photoid;
    }
    public boolean getread(){
        return read;
    }
}