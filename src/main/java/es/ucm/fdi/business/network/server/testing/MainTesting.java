package es.ucm.fdi.business.network.server.testing;

import es.ucm.fdi.business.network.server.Server;
import es.ucm.fdi.business.network.server.codes.ServerMessages;


public class MainTesting {

    public static void main(String[] args) {
        ServerMessages message = new ServerMessages(ServerMessages.WELCOME);
        System.out.println(message.getMessage());
     //    Server server = new Server(900);
     //     new Thread(server).start();
     //   server.stop();
    }
    
}
