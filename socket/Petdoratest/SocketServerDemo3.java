
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
 
public class SocketServerDemo3 {
 
    private static int serverport = 20000;
    private static ServerSocket serverSocket;
    private static ArrayList<Socket> players=new ArrayList<Socket>();
 
    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(serverport);
            System.out.println("Server is start.");
 
            while (!serverSocket.isClosed()) {
                System.out.println("Wait new clinet connect");
                waitNewPlayer();
            }
 
        } catch (IOException e) {
	    e.printStackTrace();
	    System.out.println(e);
            System.out.println("Server Socket ERROR");
        }
 
    }
 
    public static void waitNewPlayer() {
        try {
            Socket socket = serverSocket.accept();
	    System.out.println("clinet connect!"); 
            createNewPlayer(socket);
        } catch (IOException e) {
 
        }
 
    }
 
    public static void createNewPlayer(final Socket socket) {
	System.out.println(socket);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    players.add(socket);
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
 
                    while (socket.isConnected()) {
                        String msg= br.readLine();
			if(msg==null){
			       break;
			}	       
                        System.out.println(msg);
                        castMsg(msg);
                    }
 
                } catch (IOException e) {
                players.remove(socket);
	        System.out.println("remove!!");
                }
 
                players.remove(socket);
    		System.out.println("remove!!");		
            }
        });
 
        t.start();
    }
 
    public static void castMsg(String Msg){
        Socket[] ps=new Socket[players.size()]; 
        players.toArray(ps);
 
        for (Socket socket :ps ) {
            try {
                BufferedWriter bw;
                bw = new BufferedWriter( new OutputStreamWriter(socket.getOutputStream()));
                bw.write(Msg+"\n");
                bw.flush();
            } catch (IOException e) {
 
            }
        }
    }
}
