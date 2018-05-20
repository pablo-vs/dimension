package es.ucm.fdi.business.network.testing;

import es.ucm.fdi.business.exceptions.network.PackagesDefaultMessagesException;
import es.ucm.fdi.business.network.server.Server;
import es.ucm.fdi.business.network.messages.ServerMessages;


public class MainTesting {

    public static void main(String[] args) throws PackagesDefaultMessagesException {
        ServerMessages message = new ServerMessages(ServerMessages.WELCOME);
        System.out.println(message.getMessage());
     //    Server server = new Server(900);
     //     new Thread(server).start();
     //   server.stop();
    }
    
}
