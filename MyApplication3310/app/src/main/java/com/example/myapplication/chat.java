package com.example.myapplication;

public class chat {
    private String name,say,time,unread;

    public  chat(String name,String say,String time,String unread){
        this.name=name;
        this.say=say;
        this.time=time;
        this.unread=unread;
    }

    public String getName(){
        return name;
    }
    public String getSay(){
        return say;
    }
    public String getTime(){
        return time;
    }
    public String getUnread(){
        return unread;
    }
}
