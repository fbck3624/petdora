package com.example.myapplication;

public class groups {
    private String name,say,time,image,unread,talkroom;
    private int count;

    public  groups(String name,String say,String image,String time,int count,String unread,String talkroom){
        this.name=name;
        this.say=say;
        this.image=image;
        this.time=time;
        this.count=count;
        this.unread=unread;
        this.talkroom=talkroom;
    }

    public String getName(){
        return name;
    }
    public String getSay(){
        return say;
    }
    public String getImage(){
        return image;
    }
    public String getTime(){
        return time;
    }
    public int getCount(){
        return count;
    }
    public String getUnread(){
        return unread;
    }
    public String getTalkroom(){
        return talkroom;
    }
}
