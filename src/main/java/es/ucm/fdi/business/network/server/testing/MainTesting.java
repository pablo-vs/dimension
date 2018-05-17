package es.ucm.fdi.business.network.server.testing;

import es.ucm.fdi.business.network.server.Server;


public class MainTesting {

    public static void main(String[] args) {
        Server server = new Server(900);
        new Thread(server).start();
     //   server.stop();
    }
    
}
