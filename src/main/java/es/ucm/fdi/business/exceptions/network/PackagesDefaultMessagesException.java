/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ucm.fdi.business.exceptions.network;

/**
 * This exception is thrown when a ServerMessage with a default message is built
 * but no default package message file is available.
 *
 * @author Arturo Acuaviva Huertos
 */
public class PackagesDefaultMessagesException extends Exception {

    /**
     * Simple constructor. It calls the exception constructor indicating in the
     * message that the exception has been thrown.
     */
    public PackagesDefaultMessagesException() {
        super("Error while loading default packages messages!");
    }

    /**
     * Constructor with a context message. It calls the exception constructor
     * with the given message.
     *
     * @param message
     */
    public PackagesDefaultMessagesException(String message) {
        super(message);
    }

    /**
     * Constructor with cause. It calls the exception constructor indicating in
     * the message that the exception has been thrown and the cause.
     *
     * @param cause
     */
    public PackagesDefaultMessagesException(Throwable cause) {
        super("Error while loading default packages messages!", cause);
    }

    /**
     * Constructor with a context message and a cause. It calls the exception
     * constructor with the given arguments.
     *
     * @param message
     * @param cause
     */
    public PackagesDefaultMessagesException(String message, Throwable cause) {
        super(message, cause);
    }

}
