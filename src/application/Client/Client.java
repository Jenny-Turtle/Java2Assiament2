package application.Client;

import application.Pane.ChessBoard;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    private Socket socket = null;
    private DataInputStream ds;
    private PrintStream ps;
    private Listener listener;
    private ChessBoard chessBoard = new ChessBoard();
    public Client(){
        chessBoard.drawChessBoard();
        chessBoard.getLink().setOnMouseClicked(e->linkButton());
        chessBoard.getMatch().setOnMouseClicked(e->matchButton());
        chessBoard.getQuit().setOnMouseClicked(e->quitButton());
    }


    public void linkButton()  {
        if (socket == null){
            try {
                socket = new Socket(InetAddress.getLocalHost(),1234);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (socket != null){
                try {
                    ps = new PrintStream(socket.getOutputStream());
                    listener = new Listener(socket,this);
                    listener.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public void matchButton(){
        if (socket != null){
            ps.println("match");
            chessBoard.getLinkInfo().setText("wait");
            ps.flush();
        }
    }

    public void quitButton(){
        if (socket != null){
            ps.println("quit");
            ps.flush();
            chessBoard.getLinkInfo().setText("Server disconnect");
        }
    }

    public ChessBoard getChess() {
        return chessBoard;
    }
}






