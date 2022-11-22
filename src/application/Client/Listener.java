package application.Client;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.StringTokenizer;

public class Listener extends Thread{
  private Socket socket;
  private Client client;
  private DataInputStream ds;
  private PrintStream ps;
  private String  name ;
  private String rival;
  private boolean gameOver = false;

    private boolean heartBeat = true;


    public Listener(Socket s, Client client){
        this.socket = s;
        this.client = client;
        if (socket != null){
            try {
                ds = new DataInputStream(socket.getInputStream());
                ps = new PrintStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        String line = null;
        while (heartBeat){
            try {
                line = ds.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("这里");
                heartBeat = false;
                System.out.println("服务器断开");
                client.getChess().getLinkInfo().setText("Server disconnect");
            }
            StringTokenizer st = new StringTokenizer(line, "|");
            String key = st.nextToken();
            String msg;
            if (key.equals("link")){
                name = st.nextToken();
                msg = st.nextToken();
                if (msg.equals("Server connect")){
                    client.getChess().getLinkInfo().setText(msg);
                }
            } else if (key.equals("match")){
                String valueName = st.nextToken();
                rival = valueName;
                client.getChess().getGc().strokeText(name, 50, 75);
                client.getChess().getLinkInfo().setText("Game start");
                if (name.equals("1")){
                    client.getChess().getMatchInfo().setText("you are X,go firstly");
                    chessMove();
                }
                else if (name.equals("2")){
                    client.getChess().getMatchInfo().setText("you are O");

                }
            } else if (key.equals("move")){
                msg = st.nextToken();
                readMotion(msg);
                if (name.equals("1")){
                   if (judgeWinner(client.getChess().getBoard(), "2")){ //对方赢了
                        client.getChess().getWin().setText("lose");
   //                     client.getChess().gameOverLose();
                        gameOver = true;
                    }else {
                       int count = 0;
                       for (int i = 0; i < 3; i++) {
                           for (int j = 0; j < 3; j++) {
                               if (client.getChess().getBoard()[j][i].equals("0")) count++;
                           }
                       }

                       if (count == 0) {
                           client.getChess().getWin().setText("Tie");
                           gameOver = true;
                       }
                   }
                } else if (name.equals("2")){
                    if (judgeWinner(client.getChess().getBoard(), "1")){ //对方赢了
                        client.getChess().getWin().setText("lose");
  //                      client.getChess().gameOverLose();
                        gameOver = true;
                    }else {
                        int count = 0;
                        for (int i = 0; i < 3; i++) {
                            for (int j = 0; j < 3; j++) {
                                if (client.getChess().getBoard()[j][i].equals("0")) count++;
                            }
                        }

                        if (count == 0) {
                            client.getChess().getWin().setText("Tie");
                            gameOver = true;
                        }

                    }
                }
                chessMove();
            }else if (key.equals("wrong")){
                heartBeat = false;
                client.getChess().getLinkInfo().setText("Opponent offline");
            }
        }
    }

  public void chessMove(){
      if(!gameOver) {
          client.getChess().getCanvas().setOnMouseClicked(e -> {
                double a, b;
                int m, n;
                boolean flag = false;

                a = e.getX();
                b = e.getY();

                m = (int) (a - 25) / 167;
                n = (int) (b - 100) / 167;
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        System.out.print(client.getChess().getBoard()[j][i] + " ");
                    }
                    System.out.println();
                }
                System.out.println(name);
                if (client.getChess().getBoard()[m][n].equals("0")) {
                    flag = client.getChess().drawChess(m, n, name);
                }

                client.getChess().getMatchInfo().setText("opponent turn");

                if (flag) {
                    send("move|" + rival + "|" + m + " " + n);
                    if (name.equals("1")) {
                        if (judgeWinner(client.getChess().getBoard(), "1")) { //赢了
                            client.getChess().getWin().setText("win");
     //                       client.getChess().gameOverWin();
                            gameOver = true;
                        }else {
                            int count = 0;
                            for (int i = 0; i < 3; i++) {
                                for (int j = 0; j < 3; j++) {
                                    if (client.getChess().getBoard()[j][i].equals("0")) count++;
                                }
                            }

                            if (count == 0) {
                                client.getChess().getWin().setText("Tie");
                                gameOver = true;
                            }
                        }
                    } else {
                        if (judgeWinner(client.getChess().getBoard(), "2")) { //赢了
                            client.getChess().getWin().setText("win");
     //                       client.getChess().gameOverWin();
                            gameOver = true;
                        }else {
                            int count = 0;
                            for (int i = 0; i < 3; i++) {
                                for (int j = 0; j < 3; j++) {
                                    if (client.getChess().getBoard()[j][i].equals("0")) count++;
                                }
                            }

                            if (count == 0) {
                                client.getChess().getWin().setText("Tie");
                                gameOver = true;
                            }
                        }
                    }
                    client.getChess().getCanvas().setOnMouseClicked(null);
                }

            });
        }
    }

    public void readMotion(String value){
        StringTokenizer s = new StringTokenizer(value, " ");
        int x = Integer.parseInt(s.nextToken());
        int y = Integer.parseInt(s.nextToken());
        if (name.equals("1")) {
            client.getChess().drawChess(x,y,"2");
        }
        else {
            client.getChess().drawChess(x,y,"1");
        }
        client.getChess().getMatchInfo().setText("my turn");
    }

    public boolean judgeWinner(String[][] chessboard,String name){
        return row(chessboard, name) || cur(chessboard, name) || bias(chessboard, name);
    }

    private boolean row(String[][] chessBoard,String player){
        int j;
        int count = 0;
        for (int i = 0; i < 3; i++) {
            for ( j = 0; j < 3; j++) {
                if (chessBoard[j][i].equals(player)) count++;
                if (count == 3) return true;
            }
            count = 0;
        }
        return false;
    }

    private boolean cur(String[][] chessBoard, String player){
        int j;
        int count = 0;
        for (int i = 0; i < 3; i++) {
            for ( j = 0; j < 3; j++) {
                if (chessBoard[i][j].equals(player)) count++;
                if (count == 3) return true;
            }
            count = 0;
        }
        return false;
    }

    private boolean bias(String[][] chessBoard, String player){
        int i;
        int j;

        for (i = 0; i < 3; i++) {
            if (!chessBoard[i][i].equals(player)) break;
        }
        for (j = 0; j < 3; j++) {
            if (!chessBoard[j][2-j].equals(player)) break;
        }

        return i == 3 || j == 3;
    }

    public void send(String msg){
        ps.println(msg);
        ps.flush();
    }
}

//class ClientHeartbeat implements Runnable{
//    private Listener listener;
//    private Socket socket;
//    private DataInputStream ds;
//    private PrintStream ps;
//
//    public ClientHeartbeat(Listener listener, Socket socket){
//        this.listener = listener;
//        this.socket = socket;
//    }
//    @Override
//    public void run() {
//
//    }
//}
