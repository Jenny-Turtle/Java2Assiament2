package application.Pane;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Demo extends Application {

    public void start(Stage stage) throws Exception {
        ChessBoard chess = new ChessBoard();
        chess.drawChessBoard();
        chess.drawChess(0, 2, "2");
        chess.drawChess(0, 1, "1");
        Scene scene = new Scene(chess.getPane(), 550, 750);
        stage.setScene(scene);
        stage.setTitle("井字棋");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
