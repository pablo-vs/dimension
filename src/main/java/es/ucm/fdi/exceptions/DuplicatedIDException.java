package es.ucm.fdi.exceptions;

/**
 * Controls the uniqueness of the identifiers.
 * @author Inmaculada Pérez
 */
public class DuplicatedIDException extends RuntimeException {
        /**
         * Serial version
         */
	static final long serialVersionUID = 1001L;
	
        /***
         * Class constructor specifying error message and exception.
         * @param message Error message
         * @param e Exception
         */
	public DuplicatedIDException(String message, Throwable e) {
		super(message, e);
	}

        /**
         * Class constructor specifying error message.
         * @param message Error message
         */
	public DuplicatedIDException(String message) {
		super(message);
	}
}
