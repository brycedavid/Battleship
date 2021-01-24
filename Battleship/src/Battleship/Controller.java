package Battleship;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

//This is the main controller class that controls all of the interactions, rules, etc. between the user and the game.
//This class will execute upon running Main.
public class Controller implements Initializable, Runnable {

    //All of the display elements are initialized
    @FXML
    Label hitDisplay;

    @FXML
    Button  startButton;

    @FXML
    Label display;

    @FXML
    GridPane enemyGrid;

    @FXML
    GridPane playerGrid;

    @FXML
    Button  playerButton10;

    @FXML
    Button  playerButton11;

    @FXML
    Button  playerButton12;

    @FXML
    Button  playerButton13;

    @FXML
    Button  playerButton14;

    @FXML
    Button  playerButton00;

    @FXML
    Button  playerButton01;

    @FXML
    Button  playerButton02;

    @FXML
    Button  playerButton03;

    @FXML
    Button  playerButton04;

    @FXML
    Button  playerButton20;

    @FXML
    Button  playerButton21;

    @FXML
    Button  playerButton22;

    @FXML
    Button  playerButton23;

    @FXML
    Button  playerButton24;

    @FXML
    Button  playerButton30;

    @FXML
    Button  playerButton31;

    @FXML
    Button  playerButton32;

    @FXML
    Button  playerButton33;

    @FXML
    Button  playerButton34;

    @FXML
    Button  playerButton40;

    @FXML
    Button  playerButton41;

    @FXML
    Button  playerButton42;

    @FXML
    Button  playerButton43;

    @FXML
    Button  playerButton44;

    @FXML
    Button  enemyButton00;

    @FXML
    Button  enemyButton01;

    @FXML
    Button  enemyButton02;

    @FXML
    Button  enemyButton03;

    @FXML
    Button  enemyButton04;

    @FXML
    Button  enemyButton10;

    @FXML
    Button  enemyButton11;

    @FXML
    Button  enemyButton12;

    @FXML
    Button  enemyButton13;

    @FXML
    Button  enemyButton14;

    @FXML
    Button  enemyButton20;

    @FXML
    Button  enemyButton21;

    @FXML
    Button  enemyButton22;

    @FXML
    Button  enemyButton23;

    @FXML
    Button  enemyButton24;

    @FXML
    Button  enemyButton30;

    @FXML
    Button  enemyButton31;

    @FXML
    Button  enemyButton32;

    @FXML
    Button  enemyButton33;

    @FXML
    Button  enemyButton34;

    @FXML
    Button  enemyButton40;

    @FXML
    Button  enemyButton41;

    @FXML
    Button  enemyButton42;

    @FXML
    Button  enemyButton43;

    @FXML
    Button  enemyButton44;

    @FXML
    CheckBox verticalCheckBox;

    @FXML
    Button confirmShipPlacementButton;

    @FXML
    Label turnIndicator;

    @FXML
    Label hitOrMiss;


    //All of the variables that keep track of the game state and important data are initialized.
    boolean playersConnected = false;
    boolean gameStarted = false;
    boolean vertical = false;
    boolean myTurn = false;
    boolean otherPlayerFirst = false;
    boolean imFirst = false;
    boolean imSecond = false;
    boolean placementSet = false;
    boolean playersReady = false;
    boolean enemyMoved = false;
    boolean IMoved = false;
    boolean enemyReceivedAttack = false;
    boolean nextTurnReady = false;
    boolean lost = false;
    boolean win = false;
    int health = 5;

    String input = null;
    String lastInput = null;

    //Create instances of class Board to represent player/enemy boards
    Board playerBoard;
    Board enemyBoard;

    //2D Button arrays; these represent the 5x5 grids present in-game
    Button[][] playerButtonArray = new Button[5][5];
    Button[][] enemyButtonArray = new Button[5][5];

    //This event handler handles user mouse clicks that represent moves in-game. It collects moves from the player
    //upon a mouse click and updates the UI.
    EventHandler<MouseEvent> attack = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            //These nested loops check the entire button arrays in order to find exactly what tile the player clicked.
            for(int i = 0; i < 5; i++){
                for(int j = 0; j < 5; j++){
                    //When tile is found....
                    if(event.getSource() == enemyButtonArray[i][j]){
                        try{
                            Thread.sleep(250);
                        } catch(Exception e){}

                        //Reference this button as the source of the move.
                        Button x = (Button) event.getSource();
                        //Turn the clicked tile RED to represent that the tile has been clicked/played.
                        x.setBackground((new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY))));
                        try{
                            Thread.sleep(250);
                        } catch(Exception e){}
                        sendText(Integer.toString(i) + Integer.toString(j));
                        IMoved = true;

                        while(enemyReceivedAttack == false && lost == false){
                            boolean done = false;
                            if(done == false) {
                                x.setBackground((new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY))));
                                done = true;
                            }
                        }

                        enemyReceivedAttack = false;

                        while(nextTurnReady == false && lost == false){
                            boolean done = false;
                            if(done == false) {
                                x.setBackground((new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY))));
                                done = true;
                            }
                        }


                        nextTurnReady = false;
                        myTurn = false;
                    }
                }
            }
        }
    };


    //timeline that handles the gameplay once started
    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent event){
            if(win == true){
                display.setText("You have destroyed all enemy ships.. you've won!");
                try{
                    Thread.sleep(250);
                } catch(Exception e){}


            }
            if(health == 0){
                display.setText("The enemy has destroyed all of your ships.. you've lost!");
                try{
                    Thread.sleep(250);
                } catch(Exception e){}

                myTurn = false;
                lost = true;
                sendText("Win");

                for(int i = 0; i < 5; i++){
                    for(int j = 0; j < 5; j++){
                        enemyButtonArray[i][j].setVisible(false);
                    }
                }
            }

            if(lost == true && win == false){
                display.setText("Your ships have been destroyed.. you've lost!");

                try{
                    Thread.sleep(250);
                } catch(Exception e){}

                for(int i = 0; i < 5; i++){
                    for(int j = 0; j < 5; j++){
                        enemyButtonArray[i][j].setVisible(false);
                    }
                }

            }

            if(myTurn == true && lost == false){
                for(int i = 0; i < 5; i++){
                    for(int j = 0; j < 5; j++){
                        enemyButtonArray[i][j].setVisible(true);
                        enemyButtonArray[i][j].setOnMouseClicked(attack);
                    }
                }
            }
            else if(myTurn == false && lost == false){
                try{
                    Thread.sleep(250);
                } catch(Exception e){}
                try{
                    Thread.sleep(250);
                } catch(Exception e){}

                for(int i = 0; i < 5; i++){
                    for(int j = 0; j < 5; j++){
                        enemyButtonArray[i][j].setVisible(false);
                    }
                }


                while(enemyMoved == false){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                enemyMoved = false;

                myTurn = true;
            }
        }
    }));


    //setup server and client ports to communicate
    Socket sock = null;

    DataOutputStream out;
    BufferedReader in = null;


    {
        try{
            sock = new Socket("localhost", 8088);
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new DataOutputStream(sock.getOutputStream());
        } catch (Exception e){
            System.out.println("Victim");
            e.printStackTrace();
        }
        new Thread(this).start();
    }

    //This method handles all communications between the players and the server by sending messages. These messages are received from the other end.
    public void run(){
        String message;
        try{
            while((message = in.readLine()) != null){
                System.out.println("vic received: " + message);
                lastInput = input;
                input = message;
                if(input.equals("GameReady")){
                    playersConnected = true;
                }
                if(input.equals("ImFirst") && placementSet == false){
                    imSecond = true;
                    placementSet = true;
                }
                if(input.equals("ReadyToPlay")){
                    gameStarted = true;
                }
                if(input.equals("ImStarting")){
                    playersReady = true;
                }

                if(input.equals("AttackReceivedHit")){
                    Platform.runLater(()-> hitOrMiss.setText("You've been Hit!"));

                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){}

                    nextTurnReady = true;
                }

                if(input.equals("AttackReceivedAlreadyHit")){

                    Platform.runLater(()-> hitOrMiss.setText("They Missed!"));

                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){}

                    nextTurnReady = true;
                }

                if(input.equals("AttackReceivedMiss")){
                    Platform.runLater(()-> hitOrMiss.setText("They Missed!"));

                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){}

                    nextTurnReady = true;
                }

                if(input.equals("Win")){
                    myTurn = false;
                    lost = true;
                    win = true;
                    Platform.runLater(()->display.setText("You have destroyed all enemy ships.. you've won!"));
                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){}
                    for(int i = 0; i < 5; i++){
                        for(int j = 0; j < 5; j++){
                            enemyButtonArray[i][j].setVisible(false);
                        }
                    }
                    sendText("Lost");
                    Platform.runLater(()->display.setText("You have destroyed all enemy ships.. you've won!"));
                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){}
                }

                if(input.equals("Lost")){
                    myTurn = false;
                    lost = true;
                    Platform.runLater(()->display.setText("Your ships have been destroyed.. you've lost!"));
                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){}
                    for(int i = 0; i < 5; i++){
                        for(int j = 0; j < 5; j++){
                            enemyButtonArray[i][j].setVisible(false);
                        }
                    }
                    Platform.runLater(()->display.setText("Your ships have been destroyed.. you've lost!"));
                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){}

                }

                if(input.equals("directHit")){
                    Platform.runLater(()-> hitDisplay.setText("Your last shot Hit them!"));

                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){}

                    sendText("AttackReceivedHit");
                    enemyReceivedAttack = true;
                }

                if(input.equals("alreadyHit")){
                    Platform.runLater(()-> hitDisplay.setText("You've Already Hit that space!"));

                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){}
                    sendText("AttackReceivedAlreadyHit");
                    enemyReceivedAttack = true;
                }

                if(input.equals("Miss")){
                    Platform.runLater(()-> hitDisplay.setText("Your last shot Missed!"));

                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){}
                    sendText("AttackReceivedMiss");
                    enemyReceivedAttack = true;
                }

                //These if's associated with numbers handle the moves of the other player and make the changes pertaining to that move. The numbers are
                //representative of the grid tiles (00 is row zero column zero, 01 is row zero column one, etc.)
                if(input.equals("00")){
                    Platform.runLater(() ->playerButton00.setBackground((new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)))));

                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){}
                    
                    if(playerBoard.boardArray[0][0] == 8){
                        health--;
                        playerBoard.boardArray[0][0] = -1;
                        sendText("directHit");
                        enemyMoved = true;
                    }
                    else if(playerBoard.boardArray[0][0] == -1){
                        sendText("alreadyHit");
                        enemyMoved = true;
                    }
                    else{
                        sendText("Miss");
                        enemyMoved = true;
                    }
                }

                if(input.equals("01")){
                    Platform.runLater(() ->playerButton01.setBackground((new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)))));

                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){}

                    if(playerBoard.boardArray[0][1] == 8){
                        health--;
                        playerBoard.boardArray[0][1] = -1;
                        sendText("directHit");
                        enemyMoved = true;
                    }
                    else if(playerBoard.boardArray[0][1] == -1){
                        sendText("alreadyHit");
                        enemyMoved = true;
                    }
                    else{
                        sendText("Miss");
                        enemyMoved = true;
                    }
                }

                if(input.equals("02")){
                    Platform.runLater(() ->playerButton02.setBackground((new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)))));

                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){}

                    if(playerBoard.boardArray[0][2] == 8){
                        health--;
                        playerBoard.boardArray[0][2] = -1;
                        sendText("directHit");
                        enemyMoved = true;
                    }
                    else if(playerBoard.boardArray[0][2] == -1){
                        sendText("alreadyHit");
                        enemyMoved = true;
                    }
                    else{
                        sendText("Miss");
                        enemyMoved = true;
                    }
                }

                if(input.equals("03")){
                    Platform.runLater(() ->playerButton03.setBackground((new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)))));

                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){}

                    if(playerBoard.boardArray[0][3] == 8){
                        health--;
                        playerBoard.boardArray[0][3] = -1;
                        sendText("directHit");
                        enemyMoved = true;
                    }
                    else if(playerBoard.boardArray[0][3] == -1){
                        sendText("alreadyHit");
                        enemyMoved = true;
                    }
                    else{
                        sendText("Miss");
                        enemyMoved = true;
                    }
                }

                if(input.equals("04")){
                    Platform.runLater(() ->playerButton04.setBackground((new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)))));

                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){}

                    if(playerBoard.boardArray[0][4] == 8){
                        health--;
                        playerBoard.boardArray[0][4] = -1;
                        sendText("directHit");
                        enemyMoved = true;
                    }
                    else if(playerBoard.boardArray[0][4] == -1){
                        sendText("alreadyHit");
                        enemyMoved = true;
                    }
                    else{
                        sendText("Miss");
                        enemyMoved = true;
                    }
                }

                if(input.equals("10")){
                    Platform.runLater(() ->playerButton10.setBackground((new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)))));

                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){}

                    if(playerBoard.boardArray[1][0] == 8){
                        health--;
                        playerBoard.boardArray[1][0] = -1;
                        sendText("directHit");
                        enemyMoved = true;
                    }
                    else if(playerBoard.boardArray[1][0] == -1){
                        sendText("alreadyHit");
                        enemyMoved = true;
                    }
                    else{
                        sendText("Miss");
                        enemyMoved = true;
                    }
                }

                if(input.equals("11")){
                    Platform.runLater(() ->playerButton11.setBackground((new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)))));

                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){}

                    if(playerBoard.boardArray[1][1] == 8){
                        health--;
                        playerBoard.boardArray[1][1] = -1;
                        sendText("directHit");
                        enemyMoved = true;
                    }
                    else if(playerBoard.boardArray[1][1] == -1){
                        sendText("alreadyHit");
                        enemyMoved = true;
                    }
                    else{
                        sendText("Miss");
                        enemyMoved = true;
                    }
                }

                if(input.equals("12")){
                    Platform.runLater(() ->playerButton12.setBackground((new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)))));

                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){}

                    if(playerBoard.boardArray[1][2] == 8){
                        health--;
                        playerBoard.boardArray[1][2] = -1;
                        sendText("directHit");
                        enemyMoved = true;
                    }
                    else if(playerBoard.boardArray[1][2] == -1){
                        sendText("alreadyHit");
                        enemyMoved = true;
                    }
                    else{
                        sendText("Miss");
                        enemyMoved = true;
                    }
                }

                if(input.equals("13")){
                    Platform.runLater(() ->playerButton13.setBackground((new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)))));

                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){}
                    //playerButton13.setBackground((new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY))));
                    //enemyButton13.setBackground((new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY))));
                    if(playerBoard.boardArray[1][3] == 8){
                        health--;
                        playerBoard.boardArray[1][3] = -1;
                        sendText("directHit");
                        enemyMoved = true;
                    }
                    else if(playerBoard.boardArray[1][3] == -1){
                        sendText("alreadyHit");
                        enemyMoved = true;
                    }
                    else{
                        sendText("Miss");
                        enemyMoved = true;
                    }
                }

                if(input.equals("14")){
                    Platform.runLater(() ->playerButton14.setBackground((new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)))));

                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){}

                    if(playerBoard.boardArray[1][4] == 8){
                        health--;
                        playerBoard.boardArray[1][4] = -1;
                        sendText("directHit");
                        enemyMoved = true;
                    }
                    else if(playerBoard.boardArray[1][4] == -1){
                        sendText("alreadyHit");
                        enemyMoved = true;
                    }
                    else{
                        sendText("Miss");
                        enemyMoved = true;
                    }
                }

                if(input.equals("20")){
                    Platform.runLater(() ->playerButton20.setBackground((new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)))));

                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){}

                    if(playerBoard.boardArray[2][0] == 8){
                        health--;
                        playerBoard.boardArray[2][0] = -1;
                        sendText("directHit");
                        enemyMoved = true;
                    }
                    else if(playerBoard.boardArray[2][0] == -1){
                        sendText("alreadyHit");
                        enemyMoved = true;
                    }
                    else{
                        sendText("Miss");
                        enemyMoved = true;
                    }
                }

                if(input.equals("21")){
                    Platform.runLater(() ->playerButton21.setBackground((new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)))));

                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){}

                    if(playerBoard.boardArray[2][1] == 8){
                        health--;
                        playerBoard.boardArray[2][1] = -1;
                        sendText("directHit");
                        enemyMoved = true;
                    }
                    else if(playerBoard.boardArray[2][1] == -1){
                        sendText("alreadyHit");
                        enemyMoved = true;
                    }
                    else{
                        sendText("Miss");
                        enemyMoved = true;
                    }
                }

                if(input.equals("22")){
                    Platform.runLater(() ->playerButton22.setBackground((new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)))));

                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){}

                    if(playerBoard.boardArray[2][2] == 8){
                        health--;
                        playerBoard.boardArray[2][2] = -1;
                        sendText("directHit");
                        enemyMoved = true;
                    }
                    else if(playerBoard.boardArray[2][2] == -1){
                        sendText("alreadyHit");
                        enemyMoved = true;
                    }
                    else{
                        sendText("Miss");
                        enemyMoved = true;
                    }
                }

                if(input.equals("23")){
                    Platform.runLater(() ->playerButton23.setBackground((new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)))));

                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){}

                    if(playerBoard.boardArray[2][3] == 8){
                        health--;
                        playerBoard.boardArray[2][3] = -1;
                        sendText("directHit");
                        enemyMoved = true;
                    }
                    else if(playerBoard.boardArray[2][3] == -1){
                        sendText("alreadyHit");
                        enemyMoved = true;
                    }
                    else{
                        sendText("Miss");
                        enemyMoved = true;
                    }
                }

                if(input.equals("24")){
                    Platform.runLater(() ->playerButton24.setBackground((new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)))));

                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){}

                    if(playerBoard.boardArray[2][4] == 8){
                        health--;
                        playerBoard.boardArray[2][4] = -1;
                        sendText("directHit");
                        enemyMoved = true;
                    }
                    else if(playerBoard.boardArray[2][4] == -1){
                        sendText("alreadyHit");
                        enemyMoved = true;
                    }
                    else{
                        sendText("Miss");
                        enemyMoved = true;
                    }
                }

                if(input.equals("30")){
                    Platform.runLater(() ->playerButton30.setBackground((new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)))));

                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){}

                    if(playerBoard.boardArray[3][0] == 8){
                        health--;
                        playerBoard.boardArray[3][0] = -1;
                        sendText("directHit");
                        enemyMoved = true;
                    }
                    else if(playerBoard.boardArray[3][0] == -1){
                        sendText("alreadyHit");
                        enemyMoved = true;
                    }
                    else{
                        sendText("Miss");
                        enemyMoved = true;
                    }
                }

                if(input.equals("31")){
                    Platform.runLater(() ->playerButton31.setBackground((new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)))));

                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){}

                    if(playerBoard.boardArray[3][1] == 8){
                        health--;
                        playerBoard.boardArray[3][1] = -1;
                        sendText("directHit");
                        enemyMoved = true;
                    }
                    else if(playerBoard.boardArray[3][1] == -1){
                        sendText("alreadyHit");
                        enemyMoved = true;
                    }
                    else{
                        sendText("Miss");
                        enemyMoved = true;
                    }
                }

                if(input.equals("32")){
                    Platform.runLater(() ->playerButton32.setBackground((new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)))));

                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){}

                    if(playerBoard.boardArray[3][2] == 8){
                        health--;
                        playerBoard.boardArray[3][2] = -1;
                        sendText("directHit");
                        enemyMoved = true;
                    }
                    else if(playerBoard.boardArray[3][2] == -1){
                        sendText("alreadyHit");
                        enemyMoved = true;
                    }
                    else{
                        sendText("Miss");
                        enemyMoved = true;
                    }
                }

                if(input.equals("33")){
                    Platform.runLater(() ->playerButton33.setBackground((new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)))));

                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){}

                    if(playerBoard.boardArray[3][3] == 8){
                        health--;
                        playerBoard.boardArray[3][3] = -1;
                        sendText("directHit");
                        enemyMoved = true;
                    }
                    else if(playerBoard.boardArray[3][3] == -1){
                        sendText("alreadyHit");
                        enemyMoved = true;
                    }
                    else{
                        sendText("Miss");
                        enemyMoved = true;
                    }
                }

                if(input.equals("34")){
                    Platform.runLater(() ->playerButton34.setBackground((new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)))));

                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){}

                    if(playerBoard.boardArray[3][4] == 8){
                        health--;
                        playerBoard.boardArray[3][4] = -1;
                        sendText("directHit");
                        enemyMoved = true;
                    }
                    else if(playerBoard.boardArray[3][4] == -1){
                        sendText("alreadyHit");
                        enemyMoved = true;
                    }
                    else{
                        sendText("Miss");
                        enemyMoved = true;
                    }
                }

                if(input.equals("40")){
                    Platform.runLater(() ->playerButton40.setBackground((new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)))));

                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){}

                    if(playerBoard.boardArray[4][0] == 8){
                        health--;
                        playerBoard.boardArray[4][0] = -1;
                        sendText("directHit");
                        enemyMoved = true;
                    }
                    else if(playerBoard.boardArray[4][0] == -1){
                        sendText("alreadyHit");
                        enemyMoved = true;
                    }
                    else{
                        sendText("Miss");
                        enemyMoved = true;
                    }
                }

                if(input.equals("41")){
                    Platform.runLater(() ->playerButton41.setBackground((new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)))));

                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){}

                    if(playerBoard.boardArray[4][1] == 8){
                        health--;
                        playerBoard.boardArray[4][1] = -1;
                        sendText("directHit");
                        enemyMoved = true;
                    }
                    else if(playerBoard.boardArray[4][1] == -1){
                        sendText("alreadyHit");
                        enemyMoved = true;
                    }
                    else{
                        sendText("Miss");
                        enemyMoved = true;
                    }
                }

                if(input.equals("42")){
                    Platform.runLater(() ->playerButton42.setBackground((new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)))));

                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){}

                    if(playerBoard.boardArray[4][2] == 8){
                        health--;
                        playerBoard.boardArray[4][2] = -1;
                        sendText("directHit");
                        enemyMoved = true;
                    }
                    else if(playerBoard.boardArray[4][2] == -1){
                        sendText("alreadyHit");
                        enemyMoved = true;
                    }
                    else{
                        sendText("Miss");
                        enemyMoved = true;
                    }
                }

                if(input.equals("43")){
                    Platform.runLater(() ->playerButton43.setBackground((new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)))));

                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){}

                    if(playerBoard.boardArray[4][3] == 8){
                        health--;
                        playerBoard.boardArray[4][3] = -1;
                        sendText("directHit");
                        enemyMoved = true;
                    }
                    else if(playerBoard.boardArray[4][3] == -1){
                        sendText("alreadyHit");
                        enemyMoved = true;
                    }
                    else{
                        sendText("Miss");
                        enemyMoved = true;
                    }
                }

                if(input.equals("44")){
                    Platform.runLater(() ->playerButton44.setBackground((new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)))));

                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){}

                    if(playerBoard.boardArray[4][4] == 8){
                        health--;
                        playerBoard.boardArray[4][4] = -1;
                        sendText("directHit");
                        enemyMoved = true;
                    }
                    else if(playerBoard.boardArray[4][4] == -1){
                        sendText("alreadyHit");
                        enemyMoved = true;
                    }
                    else{
                        sendText("Miss");
                        enemyMoved = true;
                    }
                }
            }
        } catch(Exception e){
            System.out.println("Victim read");
        }
    }

    //This method sends a message to the server which is relayed to the other player. The messages sent are interpreted by the list of if statements
    //above.
    public void sendText(String message){
        try{
            out.writeBytes(message + "\n");
            out.flush();
            System.out.println("vic to perp: " + message);
        } catch (Exception e){
            System.out.println("victim send");
            e.printStackTrace();
        }
    }

    //This method initializes the player/enemy board with initial values. The button arrays are directly associated with the buttons
    //on the board to allow for easy access.
    public void setUp(){
        playerButtonArray[0][0] = playerButton00;
        playerButtonArray[0][1] = playerButton01;
        playerButtonArray[0][2] = playerButton02;
        playerButtonArray[0][3] = playerButton03;
        playerButtonArray[0][4] = playerButton04;
        playerButtonArray[1][0] = playerButton10;
        playerButtonArray[1][1] = playerButton11;
        playerButtonArray[1][2] = playerButton12;
        playerButtonArray[1][3] = playerButton13;
        playerButtonArray[1][4] = playerButton14;
        playerButtonArray[2][0] = playerButton20;
        playerButtonArray[2][1] = playerButton21;
        playerButtonArray[2][2] = playerButton22;
        playerButtonArray[2][3] = playerButton23;
        playerButtonArray[2][4] = playerButton24;
        playerButtonArray[3][0] = playerButton30;
        playerButtonArray[3][1] = playerButton31;
        playerButtonArray[3][2] = playerButton32;
        playerButtonArray[3][3] = playerButton33;
        playerButtonArray[3][4] = playerButton34;
        playerButtonArray[4][0] = playerButton40;
        playerButtonArray[4][1] = playerButton41;
        playerButtonArray[4][2] = playerButton42;
        playerButtonArray[4][3] = playerButton43;
        playerButtonArray[4][4] = playerButton44;

        enemyButtonArray[0][0] = enemyButton00;
        enemyButtonArray[0][1] = enemyButton01;
        enemyButtonArray[0][2] = enemyButton02;
        enemyButtonArray[0][3] = enemyButton03;
        enemyButtonArray[0][4] = enemyButton04;
        enemyButtonArray[1][0] = enemyButton10;
        enemyButtonArray[1][1] = enemyButton11;
        enemyButtonArray[1][2] = enemyButton12;
        enemyButtonArray[1][3] = enemyButton13;
        enemyButtonArray[1][4] = enemyButton14;
        enemyButtonArray[2][0] = enemyButton20;
        enemyButtonArray[2][1] = enemyButton21;
        enemyButtonArray[2][2] = enemyButton22;
        enemyButtonArray[2][3] = enemyButton23;
        enemyButtonArray[2][4] = enemyButton24;
        enemyButtonArray[3][0] = enemyButton30;
        enemyButtonArray[3][1] = enemyButton31;
        enemyButtonArray[3][2] = enemyButton32;
        enemyButtonArray[3][3] = enemyButton33;
        enemyButtonArray[3][4] = enemyButton34;
        enemyButtonArray[4][0] = enemyButton40;
        enemyButtonArray[4][1] = enemyButton41;
        enemyButtonArray[4][2] = enemyButton42;
        enemyButtonArray[4][3] = enemyButton43;
        enemyButtonArray[4][4] = enemyButton44;

        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                playerButtonArray[i][j].setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
                enemyButtonArray[i][j].setVisible(false);
            }
        }
    }

    //This class represents each ship, both player and enemy. It keeps track of data directly related to these ships.
    public static class Ship {

        public int shipType;
        private int health;


        public Ship(int type){
            this.shipType = type;
            health = type;
        }

        public void isHit(){
            health--;
        }

        public boolean isAlive(){
            return health > 0;
        }


        public int getShipType() {
            return this.shipType;
        }

        public void set(Ship ship){

        }
    }

    //This class represents the two game boards, both player and enemy. It keeps track of data associated with each board.
    public static class Board {
        public int ships = 2;
        public int[][] boardArray = new int[5][5];
        private boolean enemy = false;
        public boolean shipsPlaced = false;

        //constructor
        public Board(boolean enemy){
            this.enemy = enemy;

            //Initialize array representing board to 0
            for(int i = 0; i < 5; i++){
                for(int j = 0; j < 5; j++){
                    boardArray[i][j] = 0;
                }
            }


        }

        //this method handles the placing of ships.
        public boolean placeShip(Ship ship, int x, int y){
            if(boardArray[x][y] == 0) {
                boardArray[x][y] = 5;
            }

            return false;
        }

        public void shoot(int x, int y){

        }
    }

    //This method drives the way ships are handled during the game, as well as handles the changes to the boards upon placement.
    public void placeShips() {
        AtomicInteger shipsPlaced = new AtomicInteger(0);
        AtomicBoolean shipPlacementComplete = new AtomicBoolean(false);

        Ship ship1 = new Ship(2);
        Ship ship2 = new Ship(3);

        AtomicBoolean vertical = new AtomicBoolean(false);
        AtomicBoolean mouseMoved = new AtomicBoolean(false);

        AtomicReference<Ship> currentShip = new AtomicReference<>(new Ship(2));

        display.setText("Place your ships by clicking your board! Press the check box to switch between vertical " +
                "and horizontal placement.");

        //handles the vertical placement check box that allows vertical ship placement.
        verticalCheckBox.setOnMouseClicked(event20 -> {
            if (vertical.get() == false) vertical.set(true);
            else {
                vertical.set(false);
            }
        });

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        //All mouse events below handle the board state during the placement of ships.
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        playerButton00.setOnMouseMoved(event3 -> {
            if (mouseMoved.get() == true && shipsPlaced.get() != 2) {
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        if (playerBoard.boardArray[i][j] != 8) {
                            playerButtonArray[i][j].setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
                        }
                    }
                }
                mouseMoved.set(false);
            }
            if (!vertical.get() && playerBoard.boardArray[0][0] != 8 && playerBoard.boardArray[0][1] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton00.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton01.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[0][0] != 8 && playerBoard.boardArray[1][0] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton00.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton10.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (!vertical.get() && playerBoard.boardArray[0][0] != 8 && playerBoard.boardArray[0][1] != 8 && playerBoard.boardArray[0][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton00.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton01.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton02.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[0][0] != 8 && playerBoard.boardArray[1][0] != 8 && playerBoard.boardArray[2][0] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton00.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton10.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton20.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
        });

        playerButton00.setOnMouseClicked(event8 -> {
            if (!vertical.get() && playerBoard.boardArray[0][0] != 8 && playerBoard.boardArray[0][1] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton00.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton01.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[0][0] = 8;
                playerBoard.boardArray[0][1] = 8;
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[0][0] != 8 && playerBoard.boardArray[1][0] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton00.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton10.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[0][0] = 8;
                playerBoard.boardArray[1][0] = 8;
                mouseMoved.set(true);
            }
            if (!vertical.get() && playerBoard.boardArray[0][0] != 8 && playerBoard.boardArray[0][1] != 8 && playerBoard.boardArray[0][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton00.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton01.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton02.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[0][0] = 8;
                playerBoard.boardArray[0][1] = 8;
                playerBoard.boardArray[0][2] = 8;
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[0][0] != 8 && playerBoard.boardArray[1][0] != 8 && playerBoard.boardArray[2][0] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton00.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton10.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton20.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[0][0] = 8;
                playerBoard.boardArray[1][0] = 8;
                playerBoard.boardArray[2][0] = 8;
                mouseMoved.set(true);
            }

            shipsPlaced.getAndIncrement();
            if (shipsPlaced.get() == 1) {
                currentShip.set(new Ship(3));
            }
            if (shipsPlaced.get() == 2) {
                shipPlacementComplete.set(true); playerBoard.shipsPlaced = true;
            }
        });

        playerButton01.setOnMouseMoved(event3 -> {
            if (mouseMoved.get() == true && shipsPlaced.get() != 2) {
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        if (playerBoard.boardArray[i][j] != 8) {
                            playerButtonArray[i][j].setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
                        }
                    }
                }
                mouseMoved.set(false);
            }
            if (!vertical.get() && playerBoard.boardArray[0][1] != 8 && playerBoard.boardArray[0][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton01.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton02.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[0][1] != 8 && playerBoard.boardArray[1][1] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton01.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton11.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (!vertical.get() && playerBoard.boardArray[0][1] != 8 && playerBoard.boardArray[0][2] != 8 && playerBoard.boardArray[0][3] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton01.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton02.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton03.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[0][1] != 8 && playerBoard.boardArray[1][1] != 8 && playerBoard.boardArray[2][1] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton01.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton11.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton21.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
        });

        playerButton01.setOnMouseClicked(event8 -> {
            if (!vertical.get() && playerBoard.boardArray[0][1] != 8 && playerBoard.boardArray[0][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton01.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton02.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[0][1] = 8;
                playerBoard.boardArray[0][2] = 8;
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[0][1] != 8 && playerBoard.boardArray[1][1] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton01.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton11.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[0][1] = 8;
                playerBoard.boardArray[1][1] = 8;
                mouseMoved.set(true);
            }
            if (!vertical.get() && playerBoard.boardArray[0][1] != 8 && playerBoard.boardArray[0][2] != 8 && playerBoard.boardArray[0][3] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton01.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton02.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton03.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[0][1] = 8;
                playerBoard.boardArray[0][2] = 8;
                playerBoard.boardArray[0][3] = 8;
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[0][1] != 8 && playerBoard.boardArray[1][1] != 8 && playerBoard.boardArray[2][1] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton01.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton11.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton21.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[0][1] = 8;
                playerBoard.boardArray[1][1] = 8;
                playerBoard.boardArray[2][1] = 8;
                mouseMoved.set(true);
            }
            shipsPlaced.getAndIncrement();
            if (shipsPlaced.get() == 1) {
                currentShip.set(new Ship(3));
            }
            if (shipsPlaced.get() == 2) {
                shipPlacementComplete.set(true); playerBoard.shipsPlaced = true;
            }
        });

        playerButton02.setOnMouseMoved(event4 -> {
            if (mouseMoved.get() == true && shipsPlaced.get() != 2) {
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        if (playerBoard.boardArray[i][j] != 8) {
                            playerButtonArray[i][j].setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
                        }
                    }
                }
                mouseMoved.set(false);
            }
            if (!vertical.get() && playerBoard.boardArray[0][2] != 8 && playerBoard.boardArray[0][3] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton02.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton03.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[0][2] != 8 && playerBoard.boardArray[1][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton02.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton12.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (!vertical.get() && playerBoard.boardArray[0][2] != 8 && playerBoard.boardArray[0][3] != 8 && playerBoard.boardArray[0][4] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton02.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton03.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton04.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[0][2] != 8 && playerBoard.boardArray[1][2] != 8 && playerBoard.boardArray[2][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton02.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton12.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton22.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
        });

        playerButton02.setOnMouseClicked(event8 -> {
            if (!vertical.get() && playerBoard.boardArray[0][2] != 8 && playerBoard.boardArray[0][3] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton02.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton03.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[0][2] = 8;
                playerBoard.boardArray[0][3] = 8;
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[0][2] != 8 && playerBoard.boardArray[1][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton02.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton12.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[0][2] = 8;
                playerBoard.boardArray[1][2] = 8;
                mouseMoved.set(true);
            }
            if (!vertical.get() && playerBoard.boardArray[0][2] != 8 && playerBoard.boardArray[0][3] != 8 && playerBoard.boardArray[0][4] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton02.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton03.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton04.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[0][2] = 8;
                playerBoard.boardArray[0][3] = 8;
                playerBoard.boardArray[0][4] = 8;
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[0][2] != 8 && playerBoard.boardArray[1][2] != 8 && playerBoard.boardArray[2][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton02.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton12.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton22.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[0][2] = 8;
                playerBoard.boardArray[1][2] = 8;
                playerBoard.boardArray[2][2] = 8;
                mouseMoved.set(true);
            }
            shipsPlaced.getAndIncrement();
            if (shipsPlaced.get() == 1) {
                currentShip.set(new Ship(3));
            }
            if (shipsPlaced.get() == 2) {
                shipPlacementComplete.set(true); playerBoard.shipsPlaced = true;
            }
        });

        playerButton03.setOnMouseMoved(event5 -> {
            if (mouseMoved.get() == true && shipsPlaced.get() != 2) {
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        if (playerBoard.boardArray[i][j] != 8) {
                            playerButtonArray[i][j].setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
                        }
                    }
                }
                mouseMoved.set(false);
            }
            if (!vertical.get() && playerBoard.boardArray[0][3] != 8 && playerBoard.boardArray[0][4] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton03.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton04.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[0][3] != 8 && playerBoard.boardArray[1][3] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton03.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton13.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (!vertical.get() && playerBoard.boardArray[0][3] != 8 && playerBoard.boardArray[0][4] != 8 && playerBoard.boardArray[0][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton03.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton04.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton02.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[0][3] != 8 && playerBoard.boardArray[1][3] != 8 && playerBoard.boardArray[2][3] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton03.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton13.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton23.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
        });

        playerButton03.setOnMouseClicked(event8 -> {
            if (!vertical.get() && playerBoard.boardArray[0][3] != 8 && playerBoard.boardArray[0][4] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton03.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton04.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[0][3] = 8;
                playerBoard.boardArray[0][4] = 8;
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[0][3] != 8 && playerBoard.boardArray[1][3] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton03.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton13.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[0][3] = 8;
                playerBoard.boardArray[1][3] = 8;
                mouseMoved.set(true);
            }
            if (!vertical.get() && playerBoard.boardArray[0][3] != 8 && playerBoard.boardArray[0][4] != 8 && playerBoard.boardArray[0][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton03.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton04.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton02.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[0][3] = 8;
                playerBoard.boardArray[0][4] = 8;
                playerBoard.boardArray[0][2] = 8;
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[0][3] != 8 && playerBoard.boardArray[1][3] != 8 && playerBoard.boardArray[2][3] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton03.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton13.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton23.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[0][3] = 8;
                playerBoard.boardArray[1][3] = 8;
                playerBoard.boardArray[2][3] = 8;
                mouseMoved.set(true);
            }
            shipsPlaced.getAndIncrement();
            if (shipsPlaced.get() == 1) {
                currentShip.set(new Ship(3));
            }
            if (shipsPlaced.get() == 2) {
                shipPlacementComplete.set(true); playerBoard.shipsPlaced = true;
            }
        });

        playerButton04.setOnMouseMoved(event6 -> {
            if (mouseMoved.get() == true && shipsPlaced.get() != 2) {
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        if (playerBoard.boardArray[i][j] != 8) {
                            playerButtonArray[i][j].setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
                        }
                    }
                }
                mouseMoved.set(false);
            }
            if (!vertical.get() && playerBoard.boardArray[0][4] != 8 && playerBoard.boardArray[0][3] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton04.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton03.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[0][4] != 8 && playerBoard.boardArray[1][4] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton04.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton14.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (!vertical.get() && playerBoard.boardArray[0][4] != 8 && playerBoard.boardArray[0][3] != 8 && playerBoard.boardArray[0][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton04.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton03.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton02.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[0][4] != 8 && playerBoard.boardArray[1][4] != 8 && playerBoard.boardArray[2][4] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton04.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton14.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton24.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
        });

        playerButton04.setOnMouseClicked(event8 -> {
            if (!vertical.get() && playerBoard.boardArray[0][4] != 8 && playerBoard.boardArray[0][3] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton04.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton03.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[0][4] = 8;
                playerBoard.boardArray[0][3] = 8;
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[0][4] != 8 && playerBoard.boardArray[1][4] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton04.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton14.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[0][4] = 8;
                playerBoard.boardArray[1][4] = 8;
                mouseMoved.set(true);
            }
            if (!vertical.get() && playerBoard.boardArray[0][4] != 8 && playerBoard.boardArray[0][3] != 8 && playerBoard.boardArray[0][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton04.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton03.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton02.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[0][4] = 8;
                playerBoard.boardArray[0][3] = 8;
                playerBoard.boardArray[0][2] = 8;
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[0][4] != 8 && playerBoard.boardArray[1][4] != 8 && playerBoard.boardArray[2][4] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton04.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton14.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton24.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[0][4] = 8;
                playerBoard.boardArray[1][4] = 8;
                playerBoard.boardArray[2][4] = 8;
                mouseMoved.set(true);
            }
            shipsPlaced.getAndIncrement();
            if (shipsPlaced.get() == 1) {
                currentShip.set(new Ship(3));
            }
            if (shipsPlaced.get() == 2) {
                shipPlacementComplete.set(true); playerBoard.shipsPlaced = true;
            }
        });

        playerButton10.setOnMouseMoved(event7 -> {
            if (mouseMoved.get() == true && shipsPlaced.get() != 2) {
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        if (playerBoard.boardArray[i][j] != 8) {
                            playerButtonArray[i][j].setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
                        }
                    }
                }
                mouseMoved.set(false);
            }
            if (!vertical.get() && playerBoard.boardArray[1][0] != 8 && playerBoard.boardArray[1][1] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton10.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton11.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[1][0] != 8 && playerBoard.boardArray[2][0] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton10.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton20.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (!vertical.get() && playerBoard.boardArray[1][0] != 8 && playerBoard.boardArray[1][1] != 8 && playerBoard.boardArray[1][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton10.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton11.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton12.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[1][0] != 8 && playerBoard.boardArray[2][0] != 8 && playerBoard.boardArray[3][0] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton10.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton20.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton30.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
        });

        playerButton10.setOnMouseClicked(event8 -> {
            if (!vertical.get() && playerBoard.boardArray[1][0] != 8 && playerBoard.boardArray[1][1] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton10.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton11.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[1][0] = 8;
                playerBoard.boardArray[1][1] = 8;
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[1][0] != 8 && playerBoard.boardArray[2][0] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton10.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton20.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[1][0] = 8;
                playerBoard.boardArray[2][0] = 8;
                mouseMoved.set(true);
            }
            if (!vertical.get() && playerBoard.boardArray[1][0] != 8 && playerBoard.boardArray[1][1] != 8 && playerBoard.boardArray[1][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton10.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton11.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton12.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[1][0] = 8;
                playerBoard.boardArray[1][1] = 8;
                playerBoard.boardArray[1][2] = 8;
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[1][0] != 8 && playerBoard.boardArray[2][0] != 8 && playerBoard.boardArray[3][0] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton10.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton20.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton30.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[1][0] = 8;
                playerBoard.boardArray[2][0] = 8;
                playerBoard.boardArray[3][0] = 8;
                mouseMoved.set(true);
            }
            shipsPlaced.getAndIncrement();
            if (shipsPlaced.get() == 1) {
                currentShip.set(new Ship(3));
            }
            if (shipsPlaced.get() == 2) {
                shipPlacementComplete.set(true); playerBoard.shipsPlaced = true;
            }
        });

        playerButton11.setOnMouseMoved(event9 -> {
            if (mouseMoved.get() == true && shipsPlaced.get() != 2) {
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        if (playerBoard.boardArray[i][j] != 8) {
                            playerButtonArray[i][j].setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
                        }
                    }
                }
                mouseMoved.set(false);
            }
            if (!vertical.get() && playerBoard.boardArray[1][1] != 8 && playerBoard.boardArray[1][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton11.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton12.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[1][1] != 8 && playerBoard.boardArray[2][1] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton11.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton21.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (!vertical.get() && playerBoard.boardArray[1][1] != 8 && playerBoard.boardArray[1][2] != 8 && playerBoard.boardArray[1][3] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton11.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton12.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton13.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[1][1] != 8 && playerBoard.boardArray[2][1] != 8 && playerBoard.boardArray[3][1] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton11.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton21.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton31.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
        });

        playerButton11.setOnMouseClicked(event8 -> {
            if (!vertical.get() && playerBoard.boardArray[1][1] != 8 && playerBoard.boardArray[1][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton11.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton12.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[1][1] = 8;
                playerBoard.boardArray[1][2] = 8;
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[1][1] != 8 && playerBoard.boardArray[2][1] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton11.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton21.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[1][1] = 8;
                playerBoard.boardArray[2][1] = 8;
                mouseMoved.set(true);
            }
            if (!vertical.get() && playerBoard.boardArray[1][1] != 8 && playerBoard.boardArray[1][2] != 8 && playerBoard.boardArray[1][3] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton11.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton12.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton13.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[1][1] = 8;
                playerBoard.boardArray[1][2] = 8;
                playerBoard.boardArray[1][3] = 8;
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[1][1] != 8 && playerBoard.boardArray[2][1] != 8 && playerBoard.boardArray[3][1] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton11.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton21.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton31.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[1][1] = 8;
                playerBoard.boardArray[2][1] = 8;
                playerBoard.boardArray[3][1] = 8;
                mouseMoved.set(true);
            }
            shipsPlaced.getAndIncrement();
            if (shipsPlaced.get() == 1) {
                currentShip.set(new Ship(3));
            }
            if (shipsPlaced.get() == 2) {
                shipPlacementComplete.set(true); playerBoard.shipsPlaced = true;
            }
        });

        playerButton13.setOnMouseMoved(event8 -> {
            if (mouseMoved.get() == true && shipsPlaced.get() != 2) {
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        if (playerBoard.boardArray[i][j] != 8) {
                            playerButtonArray[i][j].setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
                        }
                    }
                }
                mouseMoved.set(false);
            }
            if (!vertical.get() && playerBoard.boardArray[1][3] != 8 && playerBoard.boardArray[1][4] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton13.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton14.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[1][3] != 8 && playerBoard.boardArray[2][3] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton13.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton23.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (!vertical.get() && playerBoard.boardArray[1][3] != 8 && playerBoard.boardArray[1][4] != 8 && playerBoard.boardArray[1][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton12.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton13.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton14.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[1][3] != 8 && playerBoard.boardArray[2][3] != 8 && playerBoard.boardArray[3][3] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton13.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton23.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton33.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
        });

        playerButton13.setOnMouseClicked(event20 -> {
            if (!vertical.get() && playerBoard.boardArray[1][3] != 8 && playerBoard.boardArray[1][4] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton13.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton14.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[1][2] = 8;
                playerBoard.boardArray[1][3] = 8;
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[1][3] != 8 && playerBoard.boardArray[2][3] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton13.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton23.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[1][3] = 8;
                playerBoard.boardArray[2][3] = 8;
                mouseMoved.set(true);
            }
            if (!vertical.get() && playerBoard.boardArray[1][3] != 8 && playerBoard.boardArray[1][4] != 8 && playerBoard.boardArray[1][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton14.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton12.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton13.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[1][2] = 8;
                playerBoard.boardArray[1][3] = 8;
                playerBoard.boardArray[1][4] = 8;
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[1][3] != 8 && playerBoard.boardArray[2][3] != 8 && playerBoard.boardArray[3][3] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton13.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton23.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton33.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[1][3] = 8;
                playerBoard.boardArray[2][3] = 8;
                playerBoard.boardArray[3][3] = 8;
                mouseMoved.set(true);
            }
            shipsPlaced.getAndIncrement();
            if (shipsPlaced.get() == 1) {
                currentShip.set(new Ship(3));
            }
            if (shipsPlaced.get() == 2) {
                shipPlacementComplete.set(true); playerBoard.shipsPlaced = true;
            }
        });

        playerButton12.setOnMouseMoved(event8 -> {
            if (mouseMoved.get() == true && shipsPlaced.get() != 2) {
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        if (playerBoard.boardArray[i][j] != 8) {
                            playerButtonArray[i][j].setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
                        }
                    }
                }
                mouseMoved.set(false);
            }
            if (!vertical.get() && playerBoard.boardArray[1][2] != 8 && playerBoard.boardArray[1][3] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton12.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton13.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[1][2] != 8 && playerBoard.boardArray[2][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton12.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton22.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (!vertical.get() && playerBoard.boardArray[1][2] != 8 && playerBoard.boardArray[1][3] != 8 && playerBoard.boardArray[1][4] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton12.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton13.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton14.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[1][2] != 8 && playerBoard.boardArray[2][2] != 8 && playerBoard.boardArray[3][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton12.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton22.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton32.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
        });

        playerButton12.setOnMouseClicked(event20 -> {
            if (!vertical.get() && playerBoard.boardArray[1][2] != 8 && playerBoard.boardArray[1][3] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton12.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton13.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[1][2] = 8;
                playerBoard.boardArray[1][3] = 8;
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[1][2] != 8 && playerBoard.boardArray[2][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton12.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton22.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[1][2] = 8;
                playerBoard.boardArray[2][2] = 8;
                mouseMoved.set(true);
            }
            if (!vertical.get() && playerBoard.boardArray[1][2] != 8 && playerBoard.boardArray[1][3] != 8 && playerBoard.boardArray[1][4] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton14.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton12.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton13.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[1][2] = 8;
                playerBoard.boardArray[1][3] = 8;
                playerBoard.boardArray[1][4] = 8;
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[1][2] != 8 && playerBoard.boardArray[2][2] != 8 && playerBoard.boardArray[3][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton12.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton22.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton32.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[1][2] = 8;
                playerBoard.boardArray[2][2] = 8;
                playerBoard.boardArray[3][2] = 8;
                mouseMoved.set(true);
            }
            shipsPlaced.getAndIncrement();
            if (shipsPlaced.get() == 1) {
                currentShip.set(new Ship(3));
            }
            if (shipsPlaced.get() == 2) {
                shipPlacementComplete.set(true); playerBoard.shipsPlaced = true;
            }
        });

        playerButton14.setOnMouseMoved(event8 -> {
            if (mouseMoved.get() == true && shipsPlaced.get() != 2) {
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        if (playerBoard.boardArray[i][j] != 8) {
                            playerButtonArray[i][j].setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
                        }
                    }
                }
                mouseMoved.set(false);
            }
            if (!vertical.get() && playerBoard.boardArray[1][4] != 8 && playerBoard.boardArray[1][3] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton14.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton13.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[1][4] != 8 && playerBoard.boardArray[2][4] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton14.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton24.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (!vertical.get() && playerBoard.boardArray[1][4] != 8 && playerBoard.boardArray[1][3] != 8 && playerBoard.boardArray[1][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton12.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton13.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton14.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[1][4] != 8 && playerBoard.boardArray[2][4] != 8 && playerBoard.boardArray[3][4] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton14.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton24.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton34.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
        });

        playerButton14.setOnMouseClicked(event20 -> {
            if (!vertical.get() && playerBoard.boardArray[1][4] != 8 && playerBoard.boardArray[1][3] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton14.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton13.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[1][4] = 8;
                playerBoard.boardArray[1][3] = 8;
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[1][4] != 8 && playerBoard.boardArray[2][4] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton14.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton24.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[1][4] = 8;
                playerBoard.boardArray[2][4] = 8;
                mouseMoved.set(true);
            }
            if (!vertical.get() && playerBoard.boardArray[1][4] != 8 && playerBoard.boardArray[1][3] != 8 && playerBoard.boardArray[1][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton14.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton12.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton13.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[1][2] = 8;
                playerBoard.boardArray[1][3] = 8;
                playerBoard.boardArray[1][4] = 8;
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[1][4] != 8 && playerBoard.boardArray[2][4] != 8 && playerBoard.boardArray[3][4] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton14.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton24.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton34.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[1][4] = 8;
                playerBoard.boardArray[2][4] = 8;
                playerBoard.boardArray[3][4] = 8;
                mouseMoved.set(true);
            }
            shipsPlaced.getAndIncrement();
            if (shipsPlaced.get() == 1) {
                currentShip.set(new Ship(3));
            }
            if (shipsPlaced.get() == 2) {
                shipPlacementComplete.set(true); playerBoard.shipsPlaced = true;
            }
        });

        playerButton21.setOnMouseMoved(event8 -> {
            if (mouseMoved.get() == true && shipsPlaced.get() != 2) {
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        if (playerBoard.boardArray[i][j] != 8) {
                            playerButtonArray[i][j].setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
                        }
                    }
                }
                mouseMoved.set(false);
            }
            if (!vertical.get() && playerBoard.boardArray[2][1] != 8 && playerBoard.boardArray[2][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton21.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton22.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[2][1] != 8 && playerBoard.boardArray[3][1] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton21.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton31.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (!vertical.get() && playerBoard.boardArray[2][1] != 8 && playerBoard.boardArray[2][2] != 8 && playerBoard.boardArray[2][3] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton21.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton22.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton23.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[2][1] != 8 && playerBoard.boardArray[3][1] != 8 && playerBoard.boardArray[4][1] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton21.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton31.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton34.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
        });

        playerButton21.setOnMouseClicked(event20 -> {
            if (!vertical.get() && playerBoard.boardArray[2][1] != 8 && playerBoard.boardArray[2][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton21.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton22.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[2][1] = 8;
                playerBoard.boardArray[2][2] = 8;
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[2][1] != 8 && playerBoard.boardArray[3][1] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton21.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton31.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[2][1] = 8;
                playerBoard.boardArray[3][1] = 8;
                mouseMoved.set(true);
            }
            if (!vertical.get() && playerBoard.boardArray[2][1] != 8 && playerBoard.boardArray[2][2] != 8 && playerBoard.boardArray[2][3] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton21.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton22.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton23.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[2][1] = 8;
                playerBoard.boardArray[2][2] = 8;
                playerBoard.boardArray[2][3] = 8;
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[2][1] != 8 && playerBoard.boardArray[3][1] != 8 && playerBoard.boardArray[4][1] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton21.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton31.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton41.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[2][1] = 8;
                playerBoard.boardArray[3][1] = 8;
                playerBoard.boardArray[4][1] = 8;
                mouseMoved.set(true);
            }
            shipsPlaced.getAndIncrement();
            if (shipsPlaced.get() == 1) {
                currentShip.set(new Ship(3));
            }
            if (shipsPlaced.get() == 2) {
                shipPlacementComplete.set(true); playerBoard.shipsPlaced = true;
            }
        });

        playerButton22.setOnMouseMoved(event8 -> {
            if (mouseMoved.get() == true && shipsPlaced.get() != 2) {
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        if (playerBoard.boardArray[i][j] != 8) {
                            playerButtonArray[i][j].setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
                        }
                    }
                }
                mouseMoved.set(false);
            }
            if (!vertical.get() && playerBoard.boardArray[2][2] != 8 && playerBoard.boardArray[2][3] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton22.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton23.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[2][2] != 8 && playerBoard.boardArray[3][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton22.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton32.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (!vertical.get() && playerBoard.boardArray[2][2] != 8 && playerBoard.boardArray[2][3] != 8 && playerBoard.boardArray[2][4] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton22.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton23.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton24.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[2][2] != 8 && playerBoard.boardArray[3][2] != 8 && playerBoard.boardArray[4][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton22.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton32.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton42.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
        });

        playerButton22.setOnMouseClicked(event20 -> {
            if (!vertical.get() && playerBoard.boardArray[2][2] != 8 && playerBoard.boardArray[2][3] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton22.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton23.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[2][2] = 8;
                playerBoard.boardArray[2][3] = 8;
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[2][2] != 8 && playerBoard.boardArray[3][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton22.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton32.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[2][2] = 8;
                playerBoard.boardArray[3][2] = 8;
                mouseMoved.set(true);
            }
            if (!vertical.get() && playerBoard.boardArray[2][2] != 8 && playerBoard.boardArray[2][3] != 8 && playerBoard.boardArray[2][4] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton24.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton22.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton23.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[2][2] = 8;
                playerBoard.boardArray[2][3] = 8;
                playerBoard.boardArray[2][4] = 8;
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[2][2] != 8 && playerBoard.boardArray[3][2] != 8 && playerBoard.boardArray[4][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton22.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton32.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton42.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[2][2] = 8;
                playerBoard.boardArray[4][2] = 8;
                playerBoard.boardArray[3][2] = 8;
                mouseMoved.set(true);
            }
            shipsPlaced.getAndIncrement();
            if (shipsPlaced.get() == 1) {
                currentShip.set(new Ship(3));
            }
            if (shipsPlaced.get() == 2) {
                shipPlacementComplete.set(true); playerBoard.shipsPlaced = true;
            }
        });

        playerButton20.setOnMouseMoved(event8 -> {
            if (mouseMoved.get() == true && shipsPlaced.get() != 2) {
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        if (playerBoard.boardArray[i][j] != 8) {
                            playerButtonArray[i][j].setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
                        }
                    }
                }
                mouseMoved.set(false);
            }
            if (!vertical.get() && playerBoard.boardArray[2][0] != 8 && playerBoard.boardArray[2][1] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton20.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton21.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[2][0] != 8 && playerBoard.boardArray[3][0] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton20.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton30.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (!vertical.get() && playerBoard.boardArray[2][0] != 8 && playerBoard.boardArray[2][1] != 8 && playerBoard.boardArray[2][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton20.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton21.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton22.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[2][0] != 8 && playerBoard.boardArray[3][0] != 8 && playerBoard.boardArray[4][0] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton20.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton30.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton40.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
        });

        playerButton20.setOnMouseClicked(event20 -> {
            if (!vertical.get() && playerBoard.boardArray[2][0] != 8 && playerBoard.boardArray[2][1] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton20.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton21.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[2][0] = 8;
                playerBoard.boardArray[2][1] = 8;
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[2][0] != 8 && playerBoard.boardArray[3][0] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton20.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton30.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[2][0] = 8;
                playerBoard.boardArray[3][0] = 8;
                mouseMoved.set(true);
            }
            if (!vertical.get() && playerBoard.boardArray[2][0] != 8 && playerBoard.boardArray[2][1] != 8 && playerBoard.boardArray[2][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton20.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton21.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton22.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[1][2] = 8;
                playerBoard.boardArray[1][3] = 8;
                playerBoard.boardArray[1][4] = 8;
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[2][0] != 8 && playerBoard.boardArray[3][0] != 8 && playerBoard.boardArray[4][0] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton20.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton30.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton40.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[2][0] = 8;
                playerBoard.boardArray[3][0] = 8;
                playerBoard.boardArray[4][0] = 8;
                mouseMoved.set(true);
            }
            shipsPlaced.getAndIncrement();
            if (shipsPlaced.get() == 1) {
                currentShip.set(new Ship(3));
            }
            if (shipsPlaced.get() == 2) {
                shipPlacementComplete.set(true); playerBoard.shipsPlaced = true;
            }
        });

        playerButton23.setOnMouseMoved(event8 -> {
            if (mouseMoved.get() == true && shipsPlaced.get() != 2) {
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        if (playerBoard.boardArray[i][j] != 8) {
                            playerButtonArray[i][j].setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
                        }
                    }
                }
                mouseMoved.set(false);
            }
            if (!vertical.get() && playerBoard.boardArray[2][3] != 8 && playerBoard.boardArray[2][4] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton23.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton24.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[2][3] != 8 && playerBoard.boardArray[3][3] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton23.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton33.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (!vertical.get() && playerBoard.boardArray[2][3] != 8 && playerBoard.boardArray[2][4] != 8 && playerBoard.boardArray[2][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton22.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton23.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton24.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[2][3] != 8 && playerBoard.boardArray[3][3] != 8 && playerBoard.boardArray[4][3] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton23.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton33.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton43.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
        });

        playerButton23.setOnMouseClicked(event20 -> {
            if (!vertical.get() && playerBoard.boardArray[2][3] != 8 && playerBoard.boardArray[2][4] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton23.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton24.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[2][3] = 8;
                playerBoard.boardArray[2][4] = 8;
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[2][3] != 8 && playerBoard.boardArray[3][3] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton23.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton33.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[2][3] = 8;
                playerBoard.boardArray[3][3] = 8;
                mouseMoved.set(true);
            }
            if (!vertical.get() && playerBoard.boardArray[2][3] != 8 && playerBoard.boardArray[2][4] != 8 && playerBoard.boardArray[2][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton23.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton24.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton22.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[2][2] = 8;
                playerBoard.boardArray[2][3] = 8;
                playerBoard.boardArray[2][4] = 8;
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[2][3] != 8 && playerBoard.boardArray[3][3] != 8 && playerBoard.boardArray[4][3] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton23.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton33.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton43.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[2][3] = 8;
                playerBoard.boardArray[3][3] = 8;
                playerBoard.boardArray[4][3] = 8;
                mouseMoved.set(true);
            }
            shipsPlaced.getAndIncrement();
            if (shipsPlaced.get() == 1) {
                currentShip.set(new Ship(3));
            }
            if (shipsPlaced.get() == 2) {
                shipPlacementComplete.set(true); playerBoard.shipsPlaced = true;
            }
        });

        playerButton24.setOnMouseMoved(event8 -> {
            if (mouseMoved.get() == true && shipsPlaced.get() != 2) {
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        if (playerBoard.boardArray[i][j] != 8) {
                            playerButtonArray[i][j].setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
                        }
                    }
                }
                mouseMoved.set(false);
            }
            if (!vertical.get() && playerBoard.boardArray[2][4] != 8 && playerBoard.boardArray[2][3] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton23.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton24.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[2][4] != 8 && playerBoard.boardArray[3][4] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton24.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton34.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (!vertical.get() && playerBoard.boardArray[2][4] != 8 && playerBoard.boardArray[2][3] != 8 && playerBoard.boardArray[2][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton24.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton23.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton22.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[2][4] != 8 && playerBoard.boardArray[3][4] != 8 && playerBoard.boardArray[4][4] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton24.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton34.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton44.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
        });

        playerButton24.setOnMouseClicked(event20 -> {
            if (!vertical.get() && playerBoard.boardArray[2][4] != 8 && playerBoard.boardArray[2][3] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton24.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton23.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[2][4] = 8;
                playerBoard.boardArray[2][3] = 8;
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[2][4] != 8 && playerBoard.boardArray[3][4] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton24.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton34.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[2][4] = 8;
                playerBoard.boardArray[3][4] = 8;
                mouseMoved.set(true);
            }
            if (!vertical.get() && playerBoard.boardArray[2][4] != 8 && playerBoard.boardArray[2][3] != 8 && playerBoard.boardArray[2][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton24.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton23.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton22.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[2][2] = 8;
                playerBoard.boardArray[2][3] = 8;
                playerBoard.boardArray[2][4] = 8;
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[2][4] != 8 && playerBoard.boardArray[3][4] != 8 && playerBoard.boardArray[4][4] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton24.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton34.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton44.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[2][4] = 8;
                playerBoard.boardArray[3][4] = 8;
                playerBoard.boardArray[4][4] = 8;
                mouseMoved.set(true);
            }
            shipsPlaced.getAndIncrement();
            if (shipsPlaced.get() == 1) {
                currentShip.set(new Ship(3));
            }
            if (shipsPlaced.get() == 2) {
                shipPlacementComplete.set(true); playerBoard.shipsPlaced = true;
            }
        });

        playerButton30.setOnMouseMoved(event8 -> {
            if (mouseMoved.get() == true && shipsPlaced.get() != 2) {
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        if (playerBoard.boardArray[i][j] != 8) {
                            playerButtonArray[i][j].setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
                        }
                    }
                }
                mouseMoved.set(false);
            }
            if (!vertical.get() && playerBoard.boardArray[3][0] != 8 && playerBoard.boardArray[3][1] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton30.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton31.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[3][0] != 8 && playerBoard.boardArray[4][0] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton30.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton40.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (!vertical.get() && playerBoard.boardArray[3][0] != 8 && playerBoard.boardArray[3][1] != 8 && playerBoard.boardArray[3][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton30.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton31.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton32.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[3][0] != 8 && playerBoard.boardArray[4][0] != 8 && playerBoard.boardArray[2][0] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton30.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton40.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton20.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
        });

        playerButton30.setOnMouseClicked(event20 -> {
            if (!vertical.get() && playerBoard.boardArray[3][0] != 8 && playerBoard.boardArray[3][1] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton30.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton31.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[3][0] = 8;
                playerBoard.boardArray[3][1] = 8;
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[3][0] != 8 && playerBoard.boardArray[4][0] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton30.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton40.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[3][0] = 8;
                playerBoard.boardArray[4][0] = 8;
                mouseMoved.set(true);
            }
            if (!vertical.get() && playerBoard.boardArray[3][0] != 8 && playerBoard.boardArray[3][1] != 8 && playerBoard.boardArray[3][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton30.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton31.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton32.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[3][0] = 8;
                playerBoard.boardArray[3][1] = 8;
                playerBoard.boardArray[3][2] = 8;
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[3][0] != 8 && playerBoard.boardArray[4][0] != 8 && playerBoard.boardArray[2][0] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton30.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton40.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton20.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[3][0] = 8;
                playerBoard.boardArray[4][0] = 8;
                playerBoard.boardArray[2][0] = 8;
                mouseMoved.set(true);
            }
            shipsPlaced.getAndIncrement();
            if (shipsPlaced.get() == 1) {
                currentShip.set(new Ship(3));
            }
            if (shipsPlaced.get() == 2) {
                shipPlacementComplete.set(true); playerBoard.shipsPlaced = true;
            }
        });

        playerButton31.setOnMouseMoved(event8 -> {
            if (mouseMoved.get() == true && shipsPlaced.get() != 2) {
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        if (playerBoard.boardArray[i][j] != 8) {
                            playerButtonArray[i][j].setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
                        }
                    }
                }
                mouseMoved.set(false);
            }
            if (!vertical.get() && playerBoard.boardArray[3][1] != 8 && playerBoard.boardArray[3][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton31.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton32.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[3][1] != 8 && playerBoard.boardArray[4][1] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton31.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton41.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (!vertical.get() && playerBoard.boardArray[3][1] != 8 && playerBoard.boardArray[3][2] != 8 && playerBoard.boardArray[3][3] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton31.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton32.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton33.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[3][1] != 8 && playerBoard.boardArray[4][1] != 8 && playerBoard.boardArray[2][1] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton31.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton41.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton21.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
        });

        playerButton31.setOnMouseClicked(event20 -> {
            if (!vertical.get() && playerBoard.boardArray[3][1] != 8 && playerBoard.boardArray[3][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton31.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton32.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[3][1] = 8;
                playerBoard.boardArray[3][2] = 8;
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[3][1] != 8 && playerBoard.boardArray[4][1] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton31.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton41.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[3][1] = 8;
                playerBoard.boardArray[4][1] = 8;
                mouseMoved.set(true);
            }
            if (!vertical.get() && playerBoard.boardArray[3][1] != 8 && playerBoard.boardArray[3][2] != 8 && playerBoard.boardArray[3][3] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton31.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton32.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton33.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[3][1] = 8;
                playerBoard.boardArray[3][2] = 8;
                playerBoard.boardArray[3][3] = 8;
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[3][1] != 8 && playerBoard.boardArray[4][1] != 8 && playerBoard.boardArray[2][1] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton31.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton41.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton21.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[3][1] = 8;
                playerBoard.boardArray[4][1] = 8;
                playerBoard.boardArray[2][1] = 8;
                mouseMoved.set(true);
            }
            shipsPlaced.getAndIncrement();
            if (shipsPlaced.get() == 1) {
                currentShip.set(new Ship(3));
            }
            if (shipsPlaced.get() == 2) {
                shipPlacementComplete.set(true); playerBoard.shipsPlaced = true;
            }
        });

        playerButton32.setOnMouseMoved(event8 -> {
            if (mouseMoved.get() == true && shipsPlaced.get() != 2) {
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        if (playerBoard.boardArray[i][j] != 8) {
                            playerButtonArray[i][j].setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
                        }
                    }
                }
                mouseMoved.set(false);
            }
            if (!vertical.get() && playerBoard.boardArray[3][2] != 8 && playerBoard.boardArray[3][3] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton32.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton33.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[3][2] != 8 && playerBoard.boardArray[4][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton32.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton42.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (!vertical.get() && playerBoard.boardArray[3][2] != 8 && playerBoard.boardArray[3][3] != 8 && playerBoard.boardArray[3][4] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton32.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton33.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton34.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[3][2] != 8 && playerBoard.boardArray[4][2] != 8 && playerBoard.boardArray[2][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton32.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton42.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton22.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
        });

        playerButton32.setOnMouseClicked(event20 -> {
            if (!vertical.get() && playerBoard.boardArray[3][2] != 8 && playerBoard.boardArray[3][3] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton32.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton33.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[3][2] = 8;
                playerBoard.boardArray[3][3] = 8;
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[3][2] != 8 && playerBoard.boardArray[4][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton32.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton42.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[3][2] = 8;
                playerBoard.boardArray[4][2] = 8;
                mouseMoved.set(true);
            }
            if (!vertical.get() && playerBoard.boardArray[3][2] != 8 && playerBoard.boardArray[3][3] != 8 && playerBoard.boardArray[3][4] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton34.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton32.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton33.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[3][2] = 8;
                playerBoard.boardArray[3][3] = 8;
                playerBoard.boardArray[3][4] = 8;
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[3][2] != 8 && playerBoard.boardArray[4][2] != 8 && playerBoard.boardArray[2][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton32.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton42.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton22.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[3][2] = 8;
                playerBoard.boardArray[4][2] = 8;
                playerBoard.boardArray[2][2] = 8;
                mouseMoved.set(true);
            }
            shipsPlaced.getAndIncrement();
            if (shipsPlaced.get() == 1) {
                currentShip.set(new Ship(3));
            }
            if (shipsPlaced.get() == 2) {
                shipPlacementComplete.set(true); playerBoard.shipsPlaced = true;
            }
        });

        playerButton33.setOnMouseMoved(event8 -> {
            if (mouseMoved.get() == true && shipsPlaced.get() != 2) {
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        if (playerBoard.boardArray[i][j] != 8) {
                            playerButtonArray[i][j].setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
                        }
                    }
                }
                mouseMoved.set(false);
            }
            if (!vertical.get() && playerBoard.boardArray[3][3] != 8 && playerBoard.boardArray[3][4] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton33.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton34.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[3][3] != 8 && playerBoard.boardArray[4][3] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton33.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton43.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (!vertical.get() && playerBoard.boardArray[3][3] != 8 && playerBoard.boardArray[3][4] != 8 && playerBoard.boardArray[3][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton33.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton34.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton32.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[3][3] != 8 && playerBoard.boardArray[4][3] != 8 && playerBoard.boardArray[2][3] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton33.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton43.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton23.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
        });

        playerButton33.setOnMouseClicked(event20 -> {
            if (!vertical.get() && playerBoard.boardArray[3][3] != 8 && playerBoard.boardArray[3][4] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton33.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton34.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[3][3] = 8;
                playerBoard.boardArray[3][4] = 8;
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[3][3] != 8 && playerBoard.boardArray[4][3] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton33.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton43.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[3][3] = 8;
                playerBoard.boardArray[4][3] = 8;
                mouseMoved.set(true);
            }
            if (!vertical.get() && playerBoard.boardArray[3][3] != 8 && playerBoard.boardArray[3][4] != 8 && playerBoard.boardArray[3][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton33.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton34.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton32.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[3][2] = 8;
                playerBoard.boardArray[3][3] = 8;
                playerBoard.boardArray[3][4] = 8;
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[3][3] != 8 && playerBoard.boardArray[4][3] != 8 && playerBoard.boardArray[2][3] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton33.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton43.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton23.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[3][3] = 8;
                playerBoard.boardArray[4][3] = 8;
                playerBoard.boardArray[2][3] = 8;
                mouseMoved.set(true);
            }
            shipsPlaced.getAndIncrement();
            if (shipsPlaced.get() == 1) {
                currentShip.set(new Ship(3));
            }
            if (shipsPlaced.get() == 2) {
                shipPlacementComplete.set(true); playerBoard.shipsPlaced = true;
            }
        });

        playerButton34.setOnMouseMoved(event8 -> {
            if (mouseMoved.get() == true && shipsPlaced.get() != 2) {
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        if (playerBoard.boardArray[i][j] != 8) {
                            playerButtonArray[i][j].setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
                        }
                    }
                }
                mouseMoved.set(false);
            }
            if (!vertical.get() && playerBoard.boardArray[3][4] != 8 && playerBoard.boardArray[3][3] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton33.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton34.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[3][4] != 8 && playerBoard.boardArray[4][4] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton34.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton44.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (!vertical.get() && playerBoard.boardArray[3][4] != 8 && playerBoard.boardArray[3][3] != 8 && playerBoard.boardArray[3][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton32.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton33.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton34.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[3][4] != 8 && playerBoard.boardArray[4][4] != 8 && playerBoard.boardArray[2][4] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton34.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton44.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton24.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
        });

        playerButton34.setOnMouseClicked(event20 -> {
            if (!vertical.get() && playerBoard.boardArray[3][4] != 8 && playerBoard.boardArray[3][3] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton33.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton34.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[3][3] = 8;
                playerBoard.boardArray[3][4] = 8;
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[3][4] != 8 && playerBoard.boardArray[4][4] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton34.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton44.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[3][4] = 8;
                playerBoard.boardArray[4][4] = 8;
                mouseMoved.set(true);
            }
            if (!vertical.get() && playerBoard.boardArray[3][4] != 8 && playerBoard.boardArray[3][3] != 8 && playerBoard.boardArray[3][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton34.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton33.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton32.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[3][4] = 8;
                playerBoard.boardArray[3][3] = 8;
                playerBoard.boardArray[3][2] = 8;
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[3][4] != 8 && playerBoard.boardArray[4][4] != 8 && playerBoard.boardArray[2][4] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton34.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton44.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton24.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[3][4] = 8;
                playerBoard.boardArray[2][4] = 8;
                playerBoard.boardArray[4][4] = 8;
                mouseMoved.set(true);
            }
            shipsPlaced.getAndIncrement();
            if (shipsPlaced.get() == 1) {
                currentShip.set(new Ship(3));
            }
            if (shipsPlaced.get() == 2) {
                shipPlacementComplete.set(true); playerBoard.shipsPlaced = true;
            }
        });

        playerButton40.setOnMouseMoved(event8 -> {
            if (mouseMoved.get() == true && shipsPlaced.get() != 2) {
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        if (playerBoard.boardArray[i][j] != 8) {
                            playerButtonArray[i][j].setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
                        }
                    }
                }
                mouseMoved.set(false);
            }
            if (!vertical.get() && playerBoard.boardArray[4][0] != 8 && playerBoard.boardArray[4][1] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton40.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton41.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[4][0] != 8 && playerBoard.boardArray[3][0] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton30.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton40.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (!vertical.get() && playerBoard.boardArray[4][0] != 8 && playerBoard.boardArray[4][1] != 8 && playerBoard.boardArray[4][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton40.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton41.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton42.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[4][0] != 8 && playerBoard.boardArray[3][0] != 8 && playerBoard.boardArray[2][0] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton40.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton30.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton20.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
        });

        playerButton40.setOnMouseClicked(event20 -> {
            if (!vertical.get() && playerBoard.boardArray[4][0] != 8 && playerBoard.boardArray[4][1] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton40.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton41.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[4][0] = 8;
                playerBoard.boardArray[4][1] = 8;
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[4][0] != 8 && playerBoard.boardArray[3][0] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton30.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton40.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[3][0] = 8;
                playerBoard.boardArray[4][0] = 8;
                mouseMoved.set(true);
            }
            if (!vertical.get() && playerBoard.boardArray[4][0] != 8 && playerBoard.boardArray[4][1] != 8 && playerBoard.boardArray[4][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton40.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton41.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton42.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[4][0] = 8;
                playerBoard.boardArray[4][1] = 8;
                playerBoard.boardArray[4][2] = 8;
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[4][0] != 8 && playerBoard.boardArray[3][0] != 8 && playerBoard.boardArray[2][0] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton40.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton30.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton20.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[4][0] = 8;
                playerBoard.boardArray[3][0] = 8;
                playerBoard.boardArray[2][0] = 8;
                mouseMoved.set(true);
            }
            shipsPlaced.getAndIncrement();
            if (shipsPlaced.get() == 1) {
                currentShip.set(new Ship(3));
            }
            if (shipsPlaced.get() == 2) {
                shipPlacementComplete.set(true); playerBoard.shipsPlaced = true;
            }
        });

        playerButton41.setOnMouseMoved(event8 -> {
            if (mouseMoved.get() == true && shipsPlaced.get() != 2) {
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        if (playerBoard.boardArray[i][j] != 8) {
                            playerButtonArray[i][j].setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
                        }
                    }
                }
                mouseMoved.set(false);
            }
            if (!vertical.get() && playerBoard.boardArray[4][1] != 8 && playerBoard.boardArray[4][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton41.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton42.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[4][1] != 8 && playerBoard.boardArray[3][1] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton31.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton41.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (!vertical.get() && playerBoard.boardArray[4][1] != 8 && playerBoard.boardArray[4][2] != 8 && playerBoard.boardArray[4][3] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton41.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton42.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton43.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[4][1] != 8 && playerBoard.boardArray[3][1] != 8 && playerBoard.boardArray[2][1] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton41.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton31.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton21.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
        });

        playerButton41.setOnMouseClicked(event20 -> {
            if (!vertical.get() && playerBoard.boardArray[4][1] != 8 && playerBoard.boardArray[4][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton41.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton42.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[4][1] = 8;
                playerBoard.boardArray[4][2] = 8;
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[4][1] != 8 && playerBoard.boardArray[3][1] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton31.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton41.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[3][1] = 8;
                playerBoard.boardArray[4][1] = 8;
                mouseMoved.set(true);
            }
            if (!vertical.get() && playerBoard.boardArray[4][1] != 8 && playerBoard.boardArray[4][2] != 8 && playerBoard.boardArray[4][3] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton41.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton42.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton43.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[4][1] = 8;
                playerBoard.boardArray[4][2] = 8;
                playerBoard.boardArray[4][3] = 8;
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[4][1] != 8 && playerBoard.boardArray[2][1] != 8 && playerBoard.boardArray[3][1] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton41.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton31.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton21.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[2][1] = 8;
                playerBoard.boardArray[3][1] = 8;
                playerBoard.boardArray[4][1] = 8;
                mouseMoved.set(true);
            }
            shipsPlaced.getAndIncrement();
            if (shipsPlaced.get() == 1) {
                currentShip.set(new Ship(3));
            }
            if (shipsPlaced.get() == 2) {
                shipPlacementComplete.set(true); playerBoard.shipsPlaced = true;
            }
        });

        playerButton42.setOnMouseMoved(event8 -> {
            if (mouseMoved.get() == true && shipsPlaced.get() != 2) {
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        if (playerBoard.boardArray[i][j] != 8) {
                            playerButtonArray[i][j].setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
                        }
                    }
                }
                mouseMoved.set(false);
            }
            if (!vertical.get() && playerBoard.boardArray[4][2] != 8 && playerBoard.boardArray[4][3] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton42.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton43.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[4][2] != 8 && playerBoard.boardArray[3][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton42.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton32.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (!vertical.get() && playerBoard.boardArray[4][2] != 8 && playerBoard.boardArray[4][3] != 8 && playerBoard.boardArray[4][4] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton42.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton43.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton44.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[4][2] != 8 && playerBoard.boardArray[3][2] != 8 && playerBoard.boardArray[2][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton42.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton32.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton22.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
        });

        playerButton42.setOnMouseClicked(event20 -> {
            if (!vertical.get() && playerBoard.boardArray[4][2] != 8 && playerBoard.boardArray[4][3] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton42.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton43.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[4][2] = 8;
                playerBoard.boardArray[4][3] = 8;
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[4][2] != 8 && playerBoard.boardArray[3][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton42.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton32.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[4][2] = 8;
                playerBoard.boardArray[3][2] = 8;
                mouseMoved.set(true);
            }
            if (!vertical.get() && playerBoard.boardArray[4][2] != 8 && playerBoard.boardArray[4][3] != 8 && playerBoard.boardArray[4][4] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton42.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton43.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton44.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[4][2] = 8;
                playerBoard.boardArray[4][3] = 8;
                playerBoard.boardArray[4][4] = 8;
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[4][2] != 8 && playerBoard.boardArray[2][2] != 8 && playerBoard.boardArray[3][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton42.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton32.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton22.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[4][2] = 8;
                playerBoard.boardArray[2][2] = 8;
                playerBoard.boardArray[3][2] = 8;
                mouseMoved.set(true);
            }
            shipsPlaced.getAndIncrement();
            if (shipsPlaced.get() == 1) {
                currentShip.set(new Ship(3));
            }
            if (shipsPlaced.get() == 2) {
                shipPlacementComplete.set(true); playerBoard.shipsPlaced = true;
            }
        });

        playerButton43.setOnMouseMoved(event8 -> {
            if (mouseMoved.get() == true && shipsPlaced.get() != 2) {
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        if (playerBoard.boardArray[i][j] != 8) {
                            playerButtonArray[i][j].setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
                        }
                    }
                }
                mouseMoved.set(false);
            }
            if (!vertical.get() && playerBoard.boardArray[4][3] != 8 && playerBoard.boardArray[4][4] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton43.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton44.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[4][3] != 8 && playerBoard.boardArray[3][3] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton43.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton33.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (!vertical.get() && playerBoard.boardArray[4][3] != 8 && playerBoard.boardArray[4][4] != 8 && playerBoard.boardArray[4][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton43.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton44.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton42.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[4][3] != 8 && playerBoard.boardArray[3][3] != 8 && playerBoard.boardArray[2][3] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton43.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton33.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton23.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
        });

        playerButton43.setOnMouseClicked(event20 -> {
            if (!vertical.get() && playerBoard.boardArray[4][3] != 8 && playerBoard.boardArray[4][4] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton43.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton44.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[4][3] = 8;
                playerBoard.boardArray[4][4] = 8;
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[4][3] != 8 && playerBoard.boardArray[3][3] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton43.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton33.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[4][3] = 8;
                playerBoard.boardArray[3][3] = 8;
                mouseMoved.set(true);
            }
            if (!vertical.get() && playerBoard.boardArray[4][3] != 8 && playerBoard.boardArray[4][4] != 8 && playerBoard.boardArray[4][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton43.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton44.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton42.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[4][3] = 8;
                playerBoard.boardArray[4][4] = 8;
                playerBoard.boardArray[4][2] = 8;
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[4][3] != 8 && playerBoard.boardArray[3][3] != 8 && playerBoard.boardArray[2][3] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton43.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton33.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton23.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[4][3] = 8;
                playerBoard.boardArray[3][3] = 8;
                playerBoard.boardArray[2][3] = 8;
                mouseMoved.set(true);
            }
            shipsPlaced.getAndIncrement();
            if (shipsPlaced.get() == 1) {
                currentShip.set(new Ship(3));
            }
            if (shipsPlaced.get() == 2) {
                shipPlacementComplete.set(true); playerBoard.shipsPlaced = true;
            }
        });

        playerButton44.setOnMouseMoved(event8 -> {
            if (mouseMoved.get() == true && shipsPlaced.get() != 2) {
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        if (playerBoard.boardArray[i][j] != 8) {
                            playerButtonArray[i][j].setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
                        }
                    }
                }
                mouseMoved.set(false);
            }
            if (!vertical.get() && playerBoard.boardArray[4][4] != 8 && playerBoard.boardArray[4][3] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton44.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton43.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[4][4] != 8 && playerBoard.boardArray[3][4] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton44.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton34.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (!vertical.get() && playerBoard.boardArray[4][4] != 8 && playerBoard.boardArray[4][3] != 8 && playerBoard.boardArray[4][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton44.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton43.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton42.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[4][4] != 8 && playerBoard.boardArray[3][4] != 8 && playerBoard.boardArray[2][4] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton44.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton34.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton24.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
                mouseMoved.set(true);
            }
        });

        playerButton44.setOnMouseClicked(event20 -> {
            if (!vertical.get() && playerBoard.boardArray[4][4] != 8 && playerBoard.boardArray[4][3] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton44.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton43.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[4][4] = 8;
                playerBoard.boardArray[4][3] = 8;
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[4][4] != 8 && playerBoard.boardArray[3][4] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 2) {
                playerButton44.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton34.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[4][4] = 8;
                playerBoard.boardArray[3][4] = 8;
                mouseMoved.set(true);
            }
            if (!vertical.get() && playerBoard.boardArray[4][4] != 8 && playerBoard.boardArray[4][3] != 8 && playerBoard.boardArray[4][2] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton44.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton43.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton42.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[4][2] = 8;
                playerBoard.boardArray[4][3] = 8;
                playerBoard.boardArray[4][4] = 8;
                mouseMoved.set(true);
            }
            if (vertical.get() && playerBoard.boardArray[4][4] != 8 && playerBoard.boardArray[2][4] != 8 && playerBoard.boardArray[3][4] != 8 && shipPlacementComplete.get() == false && currentShip.get().getShipType() == 3) {
                playerButton44.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton34.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerButton24.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                playerBoard.boardArray[4][4] = 8;
                playerBoard.boardArray[3][4] = 8;
                playerBoard.boardArray[2][4] = 8;
                mouseMoved.set(true);
            }
            shipsPlaced.getAndIncrement();
            if (shipsPlaced.get() == 1) {
                currentShip.set(new Ship(3));
            }
            if (shipsPlaced.get() == 2) {
                shipPlacementComplete.set(true); playerBoard.shipsPlaced = true;
            }
        });

    }
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //End mouse events based on ship placement
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    //This method is invoked upon the start of the game. It drives the setup and ship placement, as well as keeps
    //track of information related to the game setup, such as who goes first.
    public void startGame(){
        
        //Call setUp method to initialize values
        setUp();

        //Create board objects
        playerBoard = new Board(false);
        enemyBoard = new Board(true);
    
        //Call placeShips to handle the placement of ships
        placeShips();

        //When the confirm ship placement button is clicked, check if
        //both ships are placed, and if so, start.
        confirmShipPlacementButton.setOnMouseClicked(event10 ->{
            if(playerBoard.shipsPlaced == false){
                display.setText("Finish placing your ships!");
            }
            else if(playerBoard.shipsPlaced == true) {
                confirmShipPlacementButton.setVisible(false);

                sendText("ImFirst");

                if(imSecond == false){
                    imFirst = true;
                    placementSet = true;
                }

                startButton.setVisible(false);
                display.setText("Ship placement done! Beginning battle...");


                System.out.println("lastInput: " + lastInput);

                if(imSecond == true){
                    myTurn = false;
                    otherPlayerFirst = true;
                }
                else{
                    myTurn = true;
                    otherPlayerFirst = false;
                }

                sendText("ReadyToPlay");

                while(gameStarted == false){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                sendText("ImStarting");

                while(playersReady == false){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


                display.setText("The battle has started!");

                //Start the timeline that handles the gameplay
                timeline.play();

            }
        });
    }


    //This is the first method from Controller that is invoked upon running Main (by default).
    //It forces both players to be connected before commencing the game setup.
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        startButton.setOnMouseClicked((new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                display.setText("Searching for opponent...");
                sendText("GameReady");

                while(playersConnected == false){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


                timeline.setCycleCount(Timeline.INDEFINITE);

                display.setText("Opponent found! Starting game...");

                //call startGame to begin setUp and ship placement once both players are connected.
                startGame();
                startButton.setVisible(false);
            }
        }));
    }
}
