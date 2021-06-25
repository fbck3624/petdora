package com.example.myapplication;

public class notis {
    private String name,reply,read,commentid,id;

    public  notis(String name,String reply,String read,String commentid,String id){
        this.name=name;
        this.reply=reply;
        this.read=read;
        this.commentid=commentid;
        this.id=id;
    }

    public String getName(){
        return name;
    }
    public String getReply(){
        return reply;
    }
    public String getRead(){
        return read;
    }
    public String getCommentid(){
        return commentid;
    }
    public String getId(){
        return id;
    }
}