package application.test;

import application.Client.Client;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Client2 extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage)  {
        Client client = new Client();
        Scene scene = new Scene(client.getChess().getPane(), 550, 750);
        stage.setScene(scene);
        stage.setTitle("connect");
        stage.show();
    }
}



