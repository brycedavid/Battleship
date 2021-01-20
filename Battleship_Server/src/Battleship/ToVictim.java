package Battleship;

import java.io.*;
import java.net.*;

//*********************************************************************************************************************
//Author: Bryce Landry
//This class Forwards information to the victim from the perpetrator. Created/called by FromPerpetrator class.
//*********************************************************************************************************************
public class ToVictim{

    Socket sock;
    DataOutputStream out = null;

    //constructor
    public ToVictim(Socket sock) throws Exception {
        this.sock = sock;
        out = new DataOutputStream(sock.getOutputStream());
    }

    //Send message from the perpetrator to the victim.
    public void write(String message){
        try{
            out.writeBytes(message + "\n");
            out.flush();
            System.out.println("Victim send: " + message);
        } catch (Exception e){
            System.out.println("Exception: " + e);
        }
    }
}
