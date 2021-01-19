package Battleship;

import java.io.*;
import java.net.*;

public class FromVictim implements Runnable {
    Socket sock;
    BufferedReader in = null;
    ToPerpetrator toperp;

    public FromVictim(Socket sock, ToPerpetrator toperp) throws Exception {
        this.sock = sock;
        in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        this.toperp = toperp;
    }

    @Override
    public void run() {
        String input;
        try {
            while ((input = in.readLine()) != null) {
                System.out.println("Server received: " + input);
                toperp.write(input + "\n");
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e);
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

