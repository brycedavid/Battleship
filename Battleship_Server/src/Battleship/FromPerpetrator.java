package Battleship;

import java.io.*;
import java.net.*;

//*********************************************************************************************************************
//Author: Bryce Landry
//This class receives input from the perpetrator.
//*********************************************************************************************************************
public class FromPerpetrator implements Runnable {

    Socket sock;
    BufferedReader in = null;

    //This object is used to forward the input to the victim.
    ToVictim tovic;

    //constructor
    public FromPerpetrator(Socket sock, ToVictim tovic) throws Exception {
        this.sock = sock;
        in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        this.tovic = tovic;
    }

    //Receive and display input from perpetrator
    @Override
    public void run() {
        String input;
        try {
            //Receive input
            while ((input = in.readLine()) != null) {
                //Display input
                System.out.println("Server received: " + input);
                //Write input to victim
                tovic.write(input + "\n");
            }

        //Catch errors
        } catch (Exception e) {
            System.out.println("Exception: " + e);

        //Cleanup
        } finally {
            try {
                in.close();
            } catch (Exception e) {
                System.out.println("Perpetrator closed");
                e.printStackTrace();
            }
        }
    }
}
