package Battleship;

import java.io.*;
import java.net.*;

public class FromPerpetrator implements Runnable {
    Socket sock;
    BufferedReader in = null;
    ToVictim tovic;

    public FromPerpetrator(Socket sock, ToVictim tovic) throws Exception {
        this.sock = sock;
        in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        this.tovic = tovic;
    }

    @Override
    public void run() {
        String input;
        try {
            while ((input = in.readLine()) != null) {
                System.out.println("Server received: " + input);
                tovic.write(input + "\n");
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e);
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