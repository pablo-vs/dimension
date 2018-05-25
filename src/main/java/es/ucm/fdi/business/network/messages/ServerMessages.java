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
import java.io.Serializable;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

/**
 * This class defines a package send by the server to the clients. A package has
 * its own type (an integer id). A Server message also contains a message with
 * the information requested.
 *
 * @author Arturo Acuaviva
 */
public class ServerMessages implements Serializable {

    /**
     * Message type for welcoming
     */
    public static final int WELCOME = 0;
    /**
     * Message type for logging-in
     */
    public static final int LOG_IN = 1;
    /**
     * Message type for logging-out
     */
    public static final int LOG_OUT = 2;
    /**
     * Message type for registering
     */
    public static final int REGISTER = 3;
    /**
     * Type of message package
     */
    private final int type;
    /**
     * Message sent from the server
     */
    private final String msg;
    /**
     * Properties to manage default text from the server
     */
    private Properties properties = new Properties();

    /**
     * Class constructor specifying the type of package message which is
     * created.
     *
     * @param type
     * @throws PackagesDefaultMessagesException
     */
    public ServerMessages(int type) throws PackagesDefaultMessagesException {
        try {
            URL resource = ServerMessages.class.getClassLoader()
                    .getResource("properties/en.servermessages.properties");
            File file = new File(resource.toURI());
            FileInputStream fileInput = new FileInputStream(file);
            properties.load(fileInput);
            fileInput.close();
        } catch (IOException | URISyntaxException ex) {
            throw new PackagesDefaultMessagesException(
                    "Error while loading server message type:" + type, ex);
        }
        this.type = type;
        switch (type) {
            case WELCOME: {
                this.msg = properties.getProperty("welcome");
                break;
            }
            case LOG_IN: {
                this.msg = properties.getProperty("log_in");
                break;
            }
            case LOG_OUT: {
                this.msg = properties.getProperty("log_out");
                break;
            }
            case REGISTER: {
                this.msg = properties.getProperty("register");
                break;
            }
            default:
                this.msg = "Null";
                break;
        }
    }

    /**
     * Class constructor specifying the package type and the message.
     *
     * @param type
     * @param msg
     */
    public ServerMessages(int type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    /**
     * Getter method.
     *
     * @return the type of the package message
     */
    public int getType() {
        return type;
    }

    /**
     * Getter method.
     *
     * @return the message in the package
     */
    public String getMessage() {
        return msg;
    }

    /**
     * Describes a package.
     *
     * @return a package string description
     */
    @Override
    public String toString() {
        return "[" + type + "][" + msg + "]";
    }
}
