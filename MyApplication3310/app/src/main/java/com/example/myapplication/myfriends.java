package com.example.myapplication;

public class myfriends {
    private String name,image,friendid,nowroom;

    public  myfriends(String name,String image,String friendid,String nowroom){
        this.name=name;
        this.image=image;
        this.friendid=friendid;
        this.nowroom=nowroom;
    }


    public String getName(){
        return name;
    }
    public String getImage(){
        return image;
    }
    public String getFriendid(){
        return friendid;
    }
    public String getNowroom(){
        return nowroom;
    }
}
