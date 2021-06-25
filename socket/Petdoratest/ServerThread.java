
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class ServerThread implements Runnable {
    // 定義當前線程所處理的Socket
    private Socket socket = null;
    // 該線程所處理的Socket所對應的輸入流
    BufferedReader br = null;

    public ServerThread(Socket socket) throws IOException {
        this.socket = socket;
        // 初始化該Socket對應的輸入流
        br = new BufferedReader(new InputStreamReader(socket.getInputStream(),
                "utf-8"));
    }

    @Override
    public void run() {
        try {
            String content = null;
            // 采用循環不斷從Socket中讀取客戶端發送過來的數據
            while ((content = readFromClient()) != null) {
                // 遍歷socketList中的每個Socket，將讀到的內容向每個Socket發送一次
                for (Socket s : MyServer.socketList) {
                    OutputStream os = s.getOutputStream();
                    os.write((content + "\n").getBytes("utf-8"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 定義讀取客戶端數據的方法
     *
     * @return
     */
    private String readFromClient() {
        try {
            return br.readLine();
        }
        // 如果捕捉到異常，表明該Socket對應的客戶端已經關閉
        catch (Exception e) {
            // 刪除該Socket
            MyServer.socketList.remove(socket);
            e.printStackTrace();
        }
        return null;
    }
}
