package Battleship;

//*********************************************************************************************************************
// Bryce Landry
// Finish Date: 3/19/2020
// This program acts as a server to the two corresponding client programs in order to host a game of salvo (battleship).
// One client is referenced as the perpetrator, and one as the victim. This program handles all communications between
// the two clients, acting as a middleman. It utilizes Java sockets on port 8088 to do this.
//*********************************************************************************************************************


import java.net.*;

public class Main {

    public static void main(String[] args) throws Exception {

        //setup sockets to use port 8088
        ServerSocket serverSock = new ServerSocket(8088);
        System.out.println("Server listening on local port 8088");

        //infinite loop that continuously listens on port 8088
        while(true){

            //the first client to connect will be the perpetrator; establish sockets.
            Socket sock1 = serverSock.accept();
            System.out.println("perp has connected to the socket\n");

            //the second client to connect will be the victim; establish sockets.
            Socket sock2 = serverSock.accept();
            System.out.println("vic has connected to the socket\n");

            //Create ToVictim and ToPerpetrator objects (from other class files) using associated sockets.
            //These handle sending output data from this server to each client, respectfully.
            ToVictim tovic = new ToVictim(sock2);
            ToPerpetrator toperp = new ToPerpetrator(sock1);

            //Create FromPerpetrator and FromVictim objects using associated sockets.
            //These handle receiving input data from each client to this server, respectfully.
            FromPerpetrator fromperp = new FromPerpetrator(sock1, tovic);
            FromVictim fromvic = new FromVictim(sock2, toperp);

            //Begin threads. Threads used to implement simultaneous communications.
            new Thread(fromperp).start();
            new Thread(fromvic).start();
        }
    }

}
