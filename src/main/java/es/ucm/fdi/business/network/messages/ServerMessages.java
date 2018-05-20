/*
  This file is part of Dimension.
  Dimension is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.
  Dimension is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
  You should have received a copy of the GNU General Public License
  along with Dimension.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.ucm.fdi.business.network.messages;

import es.ucm.fdi.business.exceptions.network.PackagesDefaultMessagesException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

/**
 * 
 * @author Arturo Acuaviva 
 */
public class ServerMessages {
    
    public static final int WELCOME = 0;
    public static final int LOG_IN = 1;
    public static final int LOG_OUT = 2;
    public static final int REGISTER = 3;
    
    private final int type;
    private final String msg;
    private Properties properties = new Properties();

    public ServerMessages(int type) throws PackagesDefaultMessagesException{
        try {
            URL resource = ServerMessages.class.getClassLoader()
                    .getResource("properties/en.servermessages.properties");     
            File file = new File(resource.toURI());
            FileInputStream fileInput = new FileInputStream(file);
            properties.load(fileInput);
            fileInput.close();
        } catch (IOException | URISyntaxException ex) {
            throw new PackagesDefaultMessagesException(
                    "Error while loading server message type:" + type ,ex);
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
