// Bryce Landry
// Finish Date : 3/19/2020
// This program acts as a client, which connects to the server (on port 8088) that hosts another client. Once
// two clients have connected, a game of salvo starts between them.
//**For Server details, see server program**//

package Battleship;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


//This class sets up the UI and launches the game for the client that runs it. It initializes the Controller class.
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        loader.setController(new Controller());
        Parent root = loader.load();

        primaryStage.setTitle("Salvo!");
        primaryStage.setScene(new Scene(root, 750, 700));
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
