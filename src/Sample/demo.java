package Sample;

import javax.script.ScriptContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;

public class demo {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(InetAddress.getLocalHost(),1234);
        OutputStream os = socket.getOutputStream();

        boolean turn = false;
        int line;
        InputStream inputStream = socket.getInputStream();
        byte[] buf = new byte[1024];
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
                    os.write(msg);
                    s = in.next();
                    if (s.equals("out")) break Label;
                }
                turn = true;
            }
        }
        System.out.println("sent");
        os.close();
    }
}
