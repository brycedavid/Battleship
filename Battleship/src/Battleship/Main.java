package Battleship;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//*********************************************************************************************************************
//Author: Bryce Landry
//This program acts as a client to the server program, titled "Battleship_Server". This program connects to the server
//using sockets on port 8088. The game is to be played either on one laptop or from two laptops on the same network,
//as per usual with using ports. Once the server program is up and running, two copies of this program can be run and
//will connect to the server (server hosts 2 of these at a time). Upon both connecting, the game will start.
//*********************************************************************************************************************


//This class sets up the UI and launches the game for the client that runs it. It also initializes the Controller class.
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        //Load sample.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));

        //Initialize Controller class
        loader.setController(new Controller());
        Parent root = loader.load();

        //Set the stage
        primaryStage.setTitle("Battleship!");
        primaryStage.setScene(new Scene(root, 750, 700));
        primaryStage.show();

    }

    //Start the game once UI is loaded
    public static void main(String[] args) {
        launch(args);
    }
}
