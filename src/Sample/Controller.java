package Sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private static final int PLAY_1 = 1;
    private static final int PLAY_2 = 2;
    private static final int EMPTY = 0;
    private static final int BOUND = 90;
    private static final int OFFSET = 15;
    private Socket socket = null;
    private PrintStream ps;

    @FXML
    private Pane base_square;

    @FXML
    private Rectangle game_panel;

    private static boolean TURN = false;

    private static final int[][] chessBoard = new int[3][3];
    private static final boolean[][] flag = new boolean[3][3];

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        game_panel.setOnMouseClicked(event -> {
            int x = (int) (event.getX() / BOUND);
            int y = (int) (event.getY() / BOUND);
            if (refreshBoard(x, y)) {
                TURN = !TURN;
            }
        });
    }

    private boolean refreshBoard (int x, int y) {
        if (chessBoard[x][y] == EMPTY) {
            chessBoard[x][y] = TURN ? PLAY_2 : PLAY_1;
            drawChess();
            gameOver(chessBoard[x][y]);
            return true;
        }
        return false;
    }

    private void drawChess () {
        for (int i = 0; i < chessBoard.length; i++) {
            for (int j = 0; j < chessBoard[0].length; j++) {
                if (flag[i][j]) {
                    // This square has been drawing, ignore.
                    continue;
                }
                switch (chessBoard[i][j]) {
                    case PLAY_2:
                        drawCircle(i, j);
                        break;
                    case PLAY_1:
                        drawLine(i, j);
                        break;
                    case EMPTY:
                        // do nothing
                        break;
                    default:
                        System.err.println("Invalid value!");
                }
            }
        }
    }

    private void drawCircle (int i, int j) {
        Circle circle = new Circle();
        base_square.getChildren().add(circle);
        circle.setCenterX(i * BOUND + BOUND / 2.0 + OFFSET);
        circle.setCenterY(j * BOUND + BOUND / 2.0 + OFFSET);
        circle.setRadius(BOUND / 2.0 - OFFSET / 2.0);
        circle.setStroke(Color.RED);
        circle.setFill(Color.TRANSPARENT);
        flag[i][j] = true;
    }

    private void drawLine (int i, int j) {
        Line line_a = new Line();
        Line line_b = new Line();
        base_square.getChildren().add(line_a);
        base_square.getChildren().add(line_b);
        line_a.setStartX(i * BOUND + OFFSET * 1.5);
        line_a.setStartY(j * BOUND + OFFSET * 1.5);
        line_a.setEndX((i + 1) * BOUND + OFFSET * 0.5);
        line_a.setEndY((j + 1) * BOUND + OFFSET * 0.5);
        line_a.setStroke(Color.BLUE);

        line_b.setStartX((i + 1) * BOUND + OFFSET * 0.5);
        line_b.setStartY(j * BOUND + OFFSET * 1.5);
        line_b.setEndX(i * BOUND + OFFSET * 1.5);
        line_b.setEndY((j + 1) * BOUND + OFFSET * 0.5);
        line_b.setStroke(Color.BLUE);
        flag[i][j] = true;
    }

    private void gameOver(int player){
        if (row(player) || cur(player) || bias(player)  ) {
            System.out.println("ture");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("GameOver!!!");
            alert.setContentText("player"+player+" win");
            alert.show();
        }

    }

    private boolean row(int player){
        int j;
        int count = 0;
        for (int i = 0; i < 3; i++) {
            for ( j = 0; j < 3; j++) {
                if (chessBoard[j][i] == player) count++;
                if (count == 3) return true;
            }
            count = 0;
        }
        return false;
    }

    private boolean cur(int player){
        int j;
        int count = 0;
        for (int i = 0; i < 3; i++) {
            for ( j = 0; j < 3; j++) {
                if (chessBoard[i][j] == player) count++;
                if (count == 3) return true;
            }
            count = 0;
        }
        return false;
    }

    private boolean bias(int player){
        int i;
        int j;

        for (i = 0; i < 3; i++) {
            if (chessBoard[i][i] != player) break;
        }
        for (j = 0; j < 3; j++) {
            if (chessBoard[j][2-j] != player) break;
        }

        return i == 3 || j == 3;
    }

    public void onLink(ActionEvent actionEvent) throws IOException {


    }

//    name = chess.getNameField().getText();
//        if (socket == null){
//        try {
//            socket = new Socket(InetAddress.getLocalHost(), 8888);
//            if (socket != null){
//                ps = new PrintStream(socket.getOutputStream());
//                ps.println("LINK:"+name);
//                ps.flush();
//                //启动监听线程
//                lisener = new Lisener(socket, name, this);
//                lisener.start();
//            }
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public void onMatch(ActionEvent actionEvent) {
    }
}

