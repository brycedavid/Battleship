package Battleship;

import java.io.*;
import java.net.*;

public class ToPerpetrator{

    Socket sock;
    DataOutputStream out = null;

    public ToPerpetrator(Socket sock)  throws Exception {
        this.sock = sock;
        out = new DataOutputStream(sock.getOutputStream());
    }

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
