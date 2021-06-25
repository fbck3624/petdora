#!/usr/bin/python
# -*- coding: utf-8 -*-
import socket 
import sys
import random
from firebase import Firebase
import os
import jiagu
import threading
import readchar

def getipaddrs(hostname):#只是為了顯示IP,僅僅測試一下 
    result = socket.getaddrinfo(hostname, None, 0, socket.SOCK_STREAM) 
    return [x[4][0] for x in result] 

def job():
    print ("Press 'q' to quit...\n")
    while True:
        ch = readchar.readkey()
        if ch == 'q':
            print ("Quit\n")
            s.shutdown(socket.SHUT_RDWR)
            s.close()
            os._exit(0)


def emotion_dog(text):
    string=['哭','悲傷','嗚嗚','痛苦','哀傷','心碎','焦慮','難過','淚','憂鬱','自殺','跳樓','可憐','悲','哀','傷心']
    length=len(string)
    for i in range(0,length):
        if string[i].find(text)!=-1:
            return 'sad'
        elif text.find(string[i])!=-1:
            return 'sad'
    words = jiagu.seg(text) # 分词
  #  print(words)

    length2=len(words)
   # print(length)
    pos=0
    neg=0
    for i in range(0,length2):
        sentiment=jiagu.sentiment(words[i])
        if(sentiment[0]=='positive'):
            pos=pos+1
        else:
            neg=neg+1
    if(pos>neg):
        return 'happy'
    elif(neg>pos):
        return 'mad'
    else:
        return 'happy'
    

if __name__ == '__main__':    
    host = ''#為空代表為本地host 
    hostname = socket.gethostname() 
    hostip = getipaddrs(hostname) 
    print('host ip', hostip)#應該顯示為:127.0.1.1 
    port = 1200     # Arbitrary non-privileged port 
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM) 
    s.bind((host, port)) 
    s.listen(4) 
    thread=threading.Thread(target=job)
    thread.start()
    while True: 
        conn, addr = s.accept()
        print('Connected by', addr) 
        data = conn.recv(1024) 

        if not data: break
        text=data.decode()
        conn.send(emotion_dog(text)[0].encode())#把接收到資料原封不動的傳送回去 
        print('Received', data.decode()) 
        print('SEND',emotion_dog(text))
        conn.close()
        

