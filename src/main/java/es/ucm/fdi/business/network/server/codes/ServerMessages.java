package es.ucm.fdi.business.network.server.codes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerMessages {
    
    
    public static final int WELCOME = 0;
    public static final int LOG_IN = 1;
    public static final int LOG_OUT = 2;
    public static final int REGISTER = 3;
    
    private final int type;
    private final String msg;
    private Properties properties = new Properties();

    public ServerMessages(int type){
        try {
            FileInputStream fileInput;
            fileInput = new FileInputStream
                             (new File("src/main/resources/properties/en.servermessages.properties"));
            properties.load(fileInput);
            fileInput.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ServerMessages.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerMessages.class.getName()).log(Level.SEVERE, null, ex);
        }     
        this.type = type;
        switch(type){
            case WELCOME:{
                this.msg = properties.getProperty("welcome");
                  break;
            }
            case LOG_IN:{
                this.msg = properties.getProperty("log_in");
                 break;
            }
            case LOG_OUT:{
                  this.msg = properties.getProperty("log_out");
                  break;
            }
            case REGISTER:{
                this.msg = properties.getProperty("register");
                break;
            }
            default:
                this.msg = "Null";
                break;
        }
    }
    
    public ServerMessages(int type, String msg){
        this.type = type;
        this.msg = msg;
    }
    
    public int getType(){
        return type;
    }
    
    public String getMessage(){
        return msg;
    }
    
    @Override
    public String toString(){
        return "[" + type + "][" + msg+"]";
    }
}
