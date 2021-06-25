import socket 
import numpy as np
from numpy import fft
from scipy.io import wavfile
import matplotlib.pyplot as plt
from scipy.signal import get_window
from pydub import AudioSegment
import sys
import random
from firebase import Firebase
import os
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

def myfft(file):
#	output="14.wav"
    while(os.path.isfile(file)==False):
      #  output="14.wav"

        #input="QQ.wav"
        config={
            "apiKey": "AIzaSyDpZdTr7AikMhuLbbm0EnvN9QnT3GSABzA",
            "authDomain": "lalala-c7bcf.firebaseapp.com",
            "databaseURL": "https://lalala-c7bcf.firebaseio.com",
            "projectId": "lalala-c7bcf",
            "storageBucket": "lalala-c7bcf.appspot.com",
            "messagingSenderId": "142519307372",
            "appId": "1:142519307372:web:952d311e1823b444ff1c49",
            "measurementId": "G-G0002R1S35"
        }
        firebase = Firebase(config)
        storage=firebase.storage()
        path_on_cloud="audio/"+file
        storage.child(path_on_cloud).download(file)
    output="14.wav"
    sound = AudioSegment.from_file(file).set_channels(2).set_frame_rate(44100)#通道數#採樣率
    sound.export(output, format="wav") 
    # recording of me whistling at ~1.2 and 1khz
    fs_rate, signal = wavfile.read(output)

    # convert stereo to mono

    signal = signal.mean(axis = 1)  #單聲道

    # generate time in seconds
    t = np.arange(signal.shape[0]) / fs_rate 

    # create some plots
    five_hundred=one_thousand=two_hundred=one_hundred=ten=one=0
    count=0
    mad=sad=happy=0

    for i in range(0,int(t[-1])):
        # pull out sample for this second
        ss = signal[i:i + fs_rate]
        # generate FFT and frequencies

        sp = fft.fft(ss)
        freq = fft.fftfreq(len(ss), 1 / fs_rate)

        # plot the first few components
        if(np.abs(sp.real[:2000]).any()):
            sp2=np.abs(sp.real[:2000])
        maxvalue=sp2[np.argmax(sp2)]
        maxplus=sp2[np.argmax(sp2)+300]
        maxminus=sp2[np.argmax(sp2)-300]
        plus=maxvalue/maxplus
        minus=maxvalue/maxminus
        if(plus>100):
            count=count+1
        if(minus>100): 
            count=count+1
        if(count>int(t[-1])/2):
            sad=sad+10
            return "sad"
           

      #  ax.plot(freq[:2000], np.abs(sp.real[:2000]));#real()只取實部


        array=sorted(np.sort(sp2),reverse=True)

        one_thousand+=sum(array[0:1000])
        five_hundred+=sum(array[0:500])
        two_hundred+=sum(array[0:200])
        one_hundred+=sum(array[0:100])
        ten+=sum(array[0:10])
        one+=sum(array[0:1])
        #print(five_hundred)
        #print(np.argmax(sp2))#index
        #print(np.abs(sp.real[np.argmax(sp2)]))

        #sheet1.write((int)(i/fs_rate),0,np.mean(array[0:500]))
    one_thousand=one_thousand/(int(t[-1])*1000)
    five_hundred=five_hundred/(int(t[-1])*500)
    two_hundred=two_hundred/(int(t[-1])*200)
    one_hundred=one_hundred/(int(t[-1])*100)
    ten=ten/(int(t[-1])*10)
    one=one/(int(t[-1])*1)
    print(int(one/one_thousand))
    if(int(one/one_thousand)>13):
        return "mad"
    elif(int(one/one_thousand)<13 and  int(one/one_thousand)>7):
        return "happy"
    elif(int(one/one_thousand)<7):
        return "mad"


if __name__ == '__main__':    
    host = ''#為空代表為本地host 
    hostname = socket.gethostname() 
    hostip = getipaddrs(hostname) 
    print('host ip', hostip)#應該顯示為:127.0.1.1 
    port = 1234     # Arbitrary non-privileged port 
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
        x=data.decode()
        conn.send(myfft(x).encode())#把接收到資料原封不動的傳送回去 
        print('Received', data.decode()) 
        print('SEND',myfft(x))
        conn.close()
        


