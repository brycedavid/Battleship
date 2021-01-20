package Battleship;

import java.io.*;
import java.net.*;

//*********************************************************************************************************************
//Author: Bryce Landry
//This class Forwards information to the perpetrator from the victim. Created/called by FromVictim class.
//*********************************************************************************************************************
public class ToPerpetrator{

    Socket sock;
    DataOutputStream out = null;

    //constructor
    public ToPerpetrator(Socket sock)  throws Exception {
        this.sock = sock;
        out = new DataOutputStream(sock.getOutputStream());
    }

    //Forward message from victim to the perpetrator.
    public void write(String message){
        try{
            out.writeBytes(message + "\n");
            out.flush();
            System.out.println("Perpetrator send: " + message);

        } catch (Exception e){
            System.out.println("Exception: " + e);
        }
    }

}
