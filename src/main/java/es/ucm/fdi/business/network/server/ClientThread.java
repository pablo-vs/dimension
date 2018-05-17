package es.ucm.fdi.business.network.server;

import es.ucm.fdi.business.network.server.codes.ClientMessages;
import es.ucm.fdi.business.exceptions.network.UnrecognizablePackageException;
import es.ucm.fdi.business.network.server.codes.ServerMessages;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;


 public  class ClientThread implements Runnable {

    protected final Socket clientSocket;
    private final int id;

    private final ObjectInputStream  streamInput;
    private final PrintWriter streamOutput;
    private final Server server;
    
    ClientThread(int id, Socket clientSocket, Server server) throws IOException {
        this.server = server;
        this.id = id;
        this.clientSocket = clientSocket;
        streamInput = new ObjectInputStream(clientSocket.getInputStream());
        streamOutput = new PrintWriter(clientSocket.getOutputStream());
    }

    @Override
    public void run() {
        // sd
        
    }

    public boolean welcomeClient() throws UnrecognizablePackageException {
        // Sends request code log-in
       streamOutput.print(ServerMessages.WELCOME);
       // Receive client notification
        ClientMessages msg  = null;
         try {
           msg = (ClientMessages) streamInput.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new UnrecognizablePackageException(e);
        } 
       switch(msg.getType()){
           case ClientMessages.LOG_IN:{
               
               break;
           }
           case ClientMessages.REGISTER:{
               
               break;
           }
           default:{
               return false;
           }
       }
       
        return true;
    }

    private String waitMessage() throws UnrecognizablePackageException{
        
        
        return null;
    }
    
    public void close() throws IOException {
        // 
    //    streamOutput.print(new ServerMessages(ServerMessages.LOG_OUT));
        streamInput.close();
        streamOutput.close();
        clientSocket.close();
    }

    public int getID() {
        return id;
    }
}