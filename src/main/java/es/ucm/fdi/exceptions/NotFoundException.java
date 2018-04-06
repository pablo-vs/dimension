package es.ucm.fdi.exceptions;

/**
 * Controls the existence of an item.
 * @author Inmaculada Pérez
 */
public class NotFoundException extends RuntimeException {
        /**
         * Serial version
         */
	static final long serialVersionUID = 1000L;
	
       /***
         * Class constructor specifying error message and exception.
         * @param message Error message
         * @param e Exception
         */
	public NotFoundException(String message, Throwable e) {
		super(message, e);
	}

        /**
         * Class constructor specifying error message.
         * @param message Error message
         */
	public NotFoundException(String message) {
		super(message);
	}
}
