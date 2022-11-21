package Sample;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.SQLOutput;
import java.util.Scanner;

public class ser {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);

        System.out.println("Waiting for client.....");
        Socket socket = serverSocket.accept();
        System.out.println("connect");

        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        byte[] buf = new byte[1024];
        int line = 0;
        boolean turn = true;
        Label: while (true){
            if (turn) {
                while ((line = inputStream.read(buf))!=-1) {
                    String s = new String(buf, 0, line);
                    System.out.println(s);
                    if (s.equals("finish")) break;
                    if (s.equals("out")) break Label;
                }
                turn = false;
            }else {
                System.out.println("what you want to say: ");
                Scanner in = new Scanner(System.in);
                String s = in.next();
                while (!s.equals("finish")){
                    byte[] msg = s.getBytes(StandardCharsets.UTF_8);
                    outputStream.write(msg);
                    s = in.next();
                    if (s.equals("out")) break Label;
                }
                turn = true;
            }
        }
        inputStream.close();
        serverSocket.close();
    }
}
