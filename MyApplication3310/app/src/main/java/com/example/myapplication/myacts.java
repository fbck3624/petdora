package com.example.myapplication;

public class myacts {
    private String name,image,name2,image2,id,id2,document1,document2;

    public  myacts(String name,String image,String id,String document1,String name2,String image2,String id2,String document2){
        this.name=name;
        this.image=image;
        this.name2=name2;
        this.image2=image2;
        this.id=id;
        this.id2=id2;
        this.document1=document1;
        this.document2=document2;
    }


    public String getName(){
        return name;
    }
    public String getImage(){
        return image;
    }
    public String getName2(){
        return name2;
    }
    public String getImage2(){
        return image2;
    }
    public String getId(){
        return id;
    }
    public String getId2(){
        return id2;
    }
    public String getDocument1(){
        return  document1;
    }
    public String getDocument2(){
        return document2;
    }
}
