package Battleship;

import java.io.*;
import java.net.*;

public class ToVictim{

    Socket sock;
    DataOutputStream out = null;

    public ToVictim(Socket sock) throws Exception {
        this.sock = sock;
        out = new DataOutputStream(sock.getOutputStream());
    }

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
