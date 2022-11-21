package application.Pane;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class ChessBoard {
    private Button quit = new Button("Quit");
    private Text win = new Text();
    private HBox nameHBox = new HBox();
    private HBox linkHbox = new HBox();
    private VBox nameVBox = new VBox();
    private Button link = new Button("Link");
    private Button match = new Button("Match");
    private Text linkInfo = new Text();
    private Text matchInfo = new Text();
    private Pane pane = new Pane();
    private double width = 501 ;
    private Canvas canvas = new Canvas(550, 750);
    private GraphicsContext gc = canvas.getGraphicsContext2D();
    private String[][] board = new String[3][3];


    public void drawChessBoard(){
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3);
        gc.strokeRect(25,100,width,width);
        for (int i = 0; i < 3; i++) {
            gc.strokeLine(25,100+i*167,25+width,100+i*167);
        }
        for (int i = 0; i < 3; i++) {
            gc.strokeLine(25+i*167,100,25+i*167,100+width);
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = "0";
            }

        }
        linkInfo.setText("Server disconnect");
        nameHBox.getChildren().addAll(win,quit);
        linkHbox.getChildren().addAll(link,linkInfo,match,matchInfo);
        nameVBox.getChildren().addAll(nameHBox,linkHbox);
        pane.getChildren().addAll(canvas,nameVBox);
    }


    public boolean drawChess(int x, int y, String name){
        if (x>3 || x<0 || y>3 || y<0){
            return false;
        }
        double radius= 125;
        if (name.equals("2")){//圆
            gc.setLineWidth(2);
            gc.strokeOval(50 + x * 167, 125 + y * 167, radius, radius);
            board[x][y] = name;
            System.out.println(board[x][y]);
        }
        else if (name.equals("1")){//×
            gc.setLineWidth(2);
            gc.strokeLine(50+x*167,125+y*167,167+x*167,242+y*167);
            gc.strokeLine(167+x*167,125+y*167,50+x*167,242+y*167);
            board[x][y] = name;
            System.out.println(board[x][y]);
        }
        return true;
    }

    public void gameOverWin(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("GameOver!!!");
        alert.setContentText("you win");
        alert.show();
    }

    public void gameOverLose(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("GameOver!!!");
        alert.setContentText("you lose");
        alert.show();
    }

    public Pane getPane() {
        return pane;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public GraphicsContext getGc() {
        return gc;
    }

    public Button getLink() {
        return link;
    }

    public Button getMatch() {
        return match;
    }

    public Button getQuit(){return quit;}

    public Text getLinkInfo() {
        return linkInfo;
    }

    public Text getMatchInfo() {
        return matchInfo;
    }

    public Text getWin() {
        return win;
    }

    public String[][] getBoard(){return board;}



}
