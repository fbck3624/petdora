
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MyServer {

    // 定義保存所有Socket的集合
    public static ArrayList<Socket> socketList = new ArrayList<Socket>();

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(20000);
        System.out.println("服務器創建成功！");
        System.out.println("等待客戶端的連接。。。");
        while (true) {
            // 此行代碼會阻塞，等待用戶的連接
            Socket socket = ss.accept();
            System.out.println("有客戶端連接進來！");
            socketList.add(socket);
            // 每當客戶端連接後啟動一條ServerThread線程為該客戶端服務
            new Thread(new ServerThread(socket)).start();
        }
    }

}
