package application.Server;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import java.util.StringTokenizer;
import java.util.Vector;

public class Server {
    private ServerSocket server;
    private Socket socket;
    private int port;
    private List<Player> players;
    private Link link;
    private Separate separate;

    public void init() {
        port = 1234;
        players = new Vector<>();   //线程安全
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        link = new Link();
        link.start();
        separate = new Separate();
        separate.start();
    }

    public synchronized void disconnect(Player player) throws IOException {
        player.send("quit|"+player.name);
        players.remove(player);
    }


    class Player extends Thread{
        Socket s;
        String name;
        InetAddress ip;
        PrintStream ps;
        DataInputStream ds;
        int status = 0;
        boolean heartBeat = true;

        public Player(Socket s) throws IOException {
            this.s = s;
            ps = new PrintStream(s.getOutputStream());
            ds = new DataInputStream(s.getInputStream());
            this.ip = s.getLocalAddress();
        }

        @Override
        public void run() {
            while (heartBeat){
                String s = null;
                try {
                    s = ds.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("这里");
                    heartBeat = false;
                    for (Player player : players) {
                        System.out.println("find"+player.name);
                        player.send("wrong");
                    }

                }
                if (s != null){
                    StringTokenizer st = new StringTokenizer(s,"|");
                    String key = st.nextToken();
                    String valueName;
                    String msg;
                    System.out.println(s);
                    if (key.equals("match")){
                        status = 1;
                    }else if (key.equals("move")){
                        System.out.println("move");
                        System.out.println(players.size());
                        valueName = st.nextToken();
                        msg = st.nextToken();
                        for (Player player : players) {
                            if (valueName.equals(player.name)) {
                                System.out.println("find"+player.name);
                                player.send("move|"+msg);}
                        }
                    }else if (key.equals("quit")){
                        System.out.println(this.name);
                        try {
                            disconnect(this);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                }

            }
        }

        public void send(String msg){
            ps.println(msg);
            ps.flush();
        }

        public int getStatus() {
            return status;
        }
        public void setStatus(int status) {
            this.status = status;
        }

    }

    class Link extends Thread {
        boolean heartBeat = true;
        @Override
        public void run() {
            int i = 1;
            while (heartBeat){
                if (players.size() < 2){
                    Player player = null;
                    try {
                        socket = server.accept();
                        System.out.println(i);
                        player = new Player(socket);
                        player.name = String.valueOf(i);
                        i++;
                    } catch (IOException e) {
                        e.printStackTrace();
                        heartBeat = false;
                    }

                    synchronized (new Link()){
                        players.add(player);

                    }

                    player.send("link|"+player.name+"|Server connect");
                    player.start();
                }

            }
        }
    }

    class Separate extends Thread{
        @Override
        public void run() {
            while (true){
                Player player1 = null;
                Player player2 = null;
                synchronized (new Separate()){
                    for (Player player : players) {
                        if (player.getStatus() == 1) {
                            player1 = player;
                        }
                    }
                    for (Player player : players) {
                        if (player.getStatus() == 1 && !player.equals(player1)) {
                            player2 = player;
                        }
                    }

                }
                if (player1 != null && player2 != null){
                    player1.setStatus(2);
                    player2.setStatus(2);
                    player1.send("match|"+player2.name);
                    player2.send("match|"+player1.name);
                }
            }
        }
    }
}



