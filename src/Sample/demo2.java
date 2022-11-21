package Sample;

import javax.script.ScriptContext;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;

public class demo2 {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(InetAddress.getLocalHost(),1234);
        OutputStream os = socket.getOutputStream();
        Scanner in = new Scanner(System.in);
        System.out.println("you want to say: ");
        String s = in.next();
        while (!Objects.equals(s, "out")){
            byte[] msg = s.getBytes(StandardCharsets.UTF_8);
            os.write(msg);
            s = in.next();
        }
        System.out.println("sent");
        os.close();
    }
}

